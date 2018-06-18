package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class DoctorSearchActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private final int DOCTOR_SEARCH_TAG = 1;
    private static final int GET_ANIMAL_CATEGORY_TAG = 2;
    private List<String> mCategoryNameArray;
    private List<String> mCategoryIdArray;
    private String catId = "ALL";
    private ProgressBar mProgressBar;
    private Spinner mCategorySpinner;
    private EditText mComments;
    private TextView mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);
        mComments = (EditText) findViewById(R.id.comments);
        mSubmit = (TextView) findViewById(R.id.submit);

        mSubmit.setOnClickListener(this);
        //onItemSelected listener
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(mCategoryIdArray.get(i).equals("ALL"))) {
                    catId = mCategoryIdArray.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //...
        getAnimalCategory();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //method to get notification
    private void searchDoctor(String comments, String catId) {  // status 1 for approve, 2 for reject
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.DOCTOR_SEARCH, getSearchRequest(comments, catId), DOCTOR_SEARCH_TAG);
    }

    //method to get animal category
    private void getAnimalCategory() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ANIMAL_CATEGORY, null, GET_ANIMAL_CATEGORY_TAG);
    }

    //...
    //method to set category spinner adapter
    private void setCategorySpinner(List<String> categoryArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DoctorSearchActivity.this, android.R.layout.simple_spinner_item, categoryArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mCategorySpinner.setAdapter(arrayAdapter);
    }

    //...
    private JSONObject getSearchRequest(String comments, String catId) {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", SharedPrefernceManger.getUserId());
            object.put("Comments", comments);
            object.put("Fk_CategoryId", catId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (catId.equals("ALL")) {
                    Toast.makeText(DoctorSearchActivity.this, R.string.select_category, Toast.LENGTH_LONG).show();
                } //
                else {
                    String comments = mComments.getText().toString();
                    searchDoctor(comments,catId);
                }
                break;
        }
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case DOCTOR_SEARCH_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        String msg = object.getString("Message");
                        if (object.getString("Status").equals("1")) {
                            finish();
                        }
                        Toast.makeText(DoctorSearchActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GET_ANIMAL_CATEGORY_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstAnimalCategoryModel")) {
                        mCategoryNameArray = new ArrayList<>();
                        mCategoryIdArray = new ArrayList<>();
                        mCategoryIdArray.add("ALL");
                        mCategoryNameArray.add(getString(R.string.select_category));
                        JSONArray jsonArray = object.getJSONArray("lstAnimalCategoryModel");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mCategoryIdArray.add(detailObj.getString("AnimalId"));
                                mCategoryNameArray.add(detailObj.getString("AnimalName"));
                            }
                        } else {
                            Toast.makeText(DoctorSearchActivity.this, R.string.no_category_available, Toast.LENGTH_LONG).show();
                        }
                        setCategorySpinner(mCategoryNameArray);
                    }
                } catch (Exception e) {
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

    }

}
