package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.adapter.ViewPostCattleRequestAdapter;
import biding.animal.com.animalbiding.model.ViewPostCattleRequestModel;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class ViewPostCattleRequestActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private static final int GET_REQUEST_TAG = 1;
    private ImageView mBack;
    private RecyclerView mRequestListView;
    private ProgressBar mProgressBar;
    private ViewPostCattleRequestAdapter viewPostCattleRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_cattle_request);

        mBack = (ImageView) findViewById(R.id.back_arrow);
        mRequestListView = (RecyclerView) findViewById(R.id.view_post_req_list_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mBack.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //...
        getPostCattleRequest();
    }

    //method to get every user detail type
    private void getPostCattleRequest() {
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ALL_ASSOCIATE_POST_REQUEST + "?userid=" + SharedPrefernceManger.getUserId(), null, GET_REQUEST_TAG);
    }

    //Method to set recycler adapter...
    private void setPostCattleRequestAdapter(List<ViewPostCattleRequestModel> featureList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRequestListView.setLayoutManager(layoutManager);
        viewPostCattleRequestAdapter = new ViewPostCattleRequestAdapter(ViewPostCattleRequestActivity.this, featureList);
        mRequestListView.setAdapter(viewPostCattleRequestAdapter);
    }


    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_REQUEST_TAG:
                try {
                    if (object != null) {
                        if (object.has("lstAnimalItemModel") && object.getJSONArray("lstAnimalItemModel") != null) {
                            JSONArray detailArray = object.getJSONArray("lstAnimalItemModel");
                            if (detailArray.length() > 0) {
                                List<ViewPostCattleRequestModel> postModelList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    postModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), ViewPostCattleRequestModel.class));
                                }
                                setPostCattleRequestAdapter(postModelList);
                            } else {
                                Toast.makeText(ViewPostCattleRequestActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    mProgressBar.setVisibility(View.GONE);
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
        switch (tag) {
            case GET_REQUEST_TAG:
                Toast.makeText(ViewPostCattleRequestActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow:
                finish();
                break;
        }
    }

}
