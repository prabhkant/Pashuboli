package biding.animal.com.animalbiding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.adapter.CattleAdapter;
import biding.animal.com.animalbiding.fragment.NewFilterFragment;
import biding.animal.com.animalbiding.model.CattleModel;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class CattleActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseInterface {

    private ImageView mBackArrow, mFilter;
    private ProgressBar mProgressBar;
    private TextView mAnimalCategoryName;
    private RecyclerView mAnimalListView;
    private CattleAdapter mCattleAdapter;
    private String mAnimalId, mBreedId, mStateId, mCityId, mAreaId, minPrice, maxPrice, mAnimalName;
    private static final int ANIMAL_SEARCH_TAG = 1;
    private List<CattleModel> mCategoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle);

        initiateViews();
    }

    //...
    private void initiateViews() {
        mBackArrow = (ImageView) findViewById(R.id.back_arrow);
        mFilter = (ImageView) findViewById(R.id.filter);
        mAnimalCategoryName = (TextView) findViewById(R.id.animalName);
        mAnimalListView = (RecyclerView) findViewById(R.id.animalListView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mBackArrow.setOnClickListener(this);
        mFilter.setOnClickListener(this);

        getIntentValues();
    }

    //...
    private void getIntentValues() {
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                mAnimalId = bundle.getString(ConstantMsg.ANIMAL_ID);
                mBreedId = bundle.getString(ConstantMsg.BREED_ID);
                mStateId = bundle.getString(ConstantMsg.STATE_ID);
                mCityId = bundle.getString(ConstantMsg.CITY_ID);
                mAreaId = bundle.getString(ConstantMsg.AREA_ID);
                minPrice = bundle.getString(ConstantMsg.MIN_PRICE);
                maxPrice = bundle.getString(ConstantMsg.MAX_PRICE);

                mAnimalName = bundle.getString(ConstantMsg.ANIMAL_NAME);
                mAnimalCategoryName.setText(mAnimalName);

                getAnimalSearchData();
            }
        }
    }

    //Method to set recycler adapter...
    private void setCategoryAdapter(List<CattleModel> categoryList) {
        GridLayoutManager layoutManager = new GridLayoutManager(CattleActivity.this, 2);
        mAnimalListView.setLayoutManager(layoutManager);
        mCattleAdapter = new CattleAdapter(CattleActivity.this, categoryList);
        mAnimalListView.setAdapter(mCattleAdapter);
    }

    //...
    private void openFilterScreen() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            NewFilterFragment dialogFragment = new NewFilterFragment();
            dialogFragment.setCancelable(true);
            dialogFragment.show(fm, "");
        }
    }

    //...
    //method to get breed category
    private void getAnimalSearchData() {
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(CattleActivity.this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.SEARCH_ANIMAL_DATA
                        + "animalid=" + mAnimalId + "&breedid=" + mBreedId + "&stateid=" + mStateId + "&cityid=" + mCityId + "&areaid=" + mAreaId
                        + "&minprice=" + minPrice + "&maxprice=" + maxPrice,
                null, ANIMAL_SEARCH_TAG);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter:
                openFilterScreen();
                break;
            case R.id.back_arrow:
                finish();
                break;
        }
    }

    //method to open animal detail page
    public void openAnimalDetailPage(String userId, String postId) {
        Intent intent = new Intent(CattleActivity.this,AnimalDetailActivity.class);
        intent.putExtra(ConstantMsg.USER_ID, userId);
        intent.putExtra(ConstantMsg.POST_ID, postId);
        startActivity(intent);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        mProgressBar.setVisibility(View.GONE);
        switch (tag) {
            case ANIMAL_SEARCH_TAG:
                try {
                    if (object != null) {
                        Gson gson = new GsonBuilder().create();
                        if (object.has("lstAnimalItemModel")) {
                            JSONArray jsonArray = object.getJSONArray("lstAnimalItemModel");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                mCategoryArrayList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject detailObj = jsonArray.getJSONObject(i);
                                    mCategoryArrayList.add(gson.fromJson(detailObj.toString(), CattleModel.class));
                                }
                                setCategoryAdapter(mCategoryArrayList);
                            } else {
                                Toast.makeText(CattleActivity.this, R.string.no_category_available, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onJsonArrayResponse(JSONArray array, int tag) {

    }

    @Override
    public void onStringResponse(String string, int tag) {

    }

    @Override
    public void onStatusCodeResponse(int status, int tag) {

    }

    @Override
    public void onError(VolleyError error, int tag) {
        Toast.makeText(CattleActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
    }

}
