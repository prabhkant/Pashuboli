package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
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
import biding.animal.com.animalbiding.adapter.TotalRequestAdapter;
import biding.animal.com.animalbiding.model.TotalRequestModel;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class TotalRequestActivity extends AppCompatActivity implements VolleyResponseInterface {

    private final int GET_REQUEST_TAG = 1;
    private RecyclerView mRequestListView;
    private ProgressBar mProgressBar;
    private TotalRequestAdapter mTotalRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRequestListView = (RecyclerView) findViewById(R.id.totalReqListView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //...
        getTotalRequest();
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

    //Method to set recycler adapter...
    private void setTotalRequestAdapter(List<TotalRequestModel> totalRequestList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRequestListView.setLayoutManager(layoutManager);
        mTotalRequestAdapter = new TotalRequestAdapter(TotalRequestActivity.this, totalRequestList);
        mRequestListView.setAdapter(mTotalRequestAdapter);
    }

    //method to get notification
    private void getTotalRequest() {  // status 1 for approve, 2 for reject
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_TOTAL_REQUEST + "?UserId=" + SharedPrefernceManger.getUserId(), null, GET_REQUEST_TAG);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_REQUEST_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        if (object.has("lstDoctorSearchModels") && object.getJSONArray("lstDoctorSearchModels") != null) {
                            JSONArray detailArray = object.getJSONArray("lstDoctorSearchModels");
                            if (detailArray.length() > 0) {
                                List<TotalRequestModel> postModelList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    postModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), TotalRequestModel.class));
                                }
                                setTotalRequestAdapter(postModelList);
                            } else {
                                Toast.makeText(TotalRequestActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
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

    }
}
