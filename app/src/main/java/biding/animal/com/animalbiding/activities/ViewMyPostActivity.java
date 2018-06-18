package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import biding.animal.com.animalbiding.adapter.ViewPostAdapter;
import biding.animal.com.animalbiding.model.ViewPostModel;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class ViewMyPostActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private static final int VIEW_POST_TAG = 1;
    private static final int UPDATE_POST_TAG = 2;
    private ProgressBar mProgressBar;
    private RecyclerView mViewPostListView;
    private ImageView mBackToHome;
    private LinearLayout mRejectLay, mApproveLay, mSelectLay, mUnSelectLay,mActionLay;
    private ViewPostAdapter viewPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_post);

        initiateViews();
    }

    //...
    private void initiateViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mViewPostListView = (RecyclerView) findViewById(R.id.specificationListView);
        mRejectLay = (LinearLayout) findViewById(R.id.rejectLay);
        mApproveLay = (LinearLayout) findViewById(R.id.approveLay);
        mSelectLay = (LinearLayout) findViewById(R.id.selectLay);
        mUnSelectLay = (LinearLayout) findViewById(R.id.unselectLay);
        mBackToHome = (ImageView) findViewById(R.id.detail_back_arrow);
        mActionLay = (LinearLayout) findViewById(R.id.action_lay);

        if (getIntent() != null) {
            String postId = getIntent().getStringExtra(ConstantMsg.POST_ID);
            getViewPostDetail(postId);
        }

        //click listener
        mRejectLay.setOnClickListener(this);
        mApproveLay.setOnClickListener(this);
        mSelectLay.setOnClickListener(this);
        mUnSelectLay.setOnClickListener(this);
        mBackToHome.setOnClickListener(this);
    }

    //...
    //method to get every user detail type
    private void getViewPostDetail(String postId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(ViewMyPostActivity.this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.VIEW_POST_DETAIL + "?postid=" + postId, null, VIEW_POST_TAG);
    }

    //method to get every user detail type
    private void updateBiddingStatus(String bidderIds, String status) {  // status 1 for approve, 2 for reject
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(ViewMyPostActivity.this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.UPDATE_BIDDING_STATUS, getUpdateRequest(bidderIds, status), UPDATE_POST_TAG);
    }

    //...
    private JSONObject getUpdateRequest(String bidderIds, String status) {
        JSONObject object = new JSONObject();
        try {
            object.put("bidderids", bidderIds);
            object.put("status", status); // status 1 for approve, 2 for reject
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    //Method to set recycler adapter...
    private void setViewPostAdapter(List<ViewPostModel> specificationList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mViewPostListView.setLayoutManager(layoutManager);
        viewPostAdapter = new ViewPostAdapter(ViewMyPostActivity.this, specificationList);
        mViewPostListView.setAdapter(viewPostAdapter);
    }

    //...
    private void checkValidation(String status) {
        if (viewPostAdapter.getSelectedBidder().isEmpty()) {
            Toast.makeText(ViewMyPostActivity.this, R.string.select_bidder_alert, Toast.LENGTH_LONG).show();
        } else {
            String bidderId = viewPostAdapter.getSelectedBidder();
            updateBiddingStatus(bidderId, status);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rejectLay:
                checkValidation("0"); // 0 for reject
                break;
            case R.id.approveLay:
                checkValidation("1"); // 1 for approve
                break;
            case R.id.selectLay:
                viewPostAdapter.selectAllBid();
                mSelectLay.setVisibility(View.GONE);
                mUnSelectLay.setVisibility(View.VISIBLE);
                break;
            case R.id.unselectLay:
                viewPostAdapter.unselectAllBid();
                mSelectLay.setVisibility(View.VISIBLE);
                mUnSelectLay.setVisibility(View.GONE);
                break;
            case R.id.detail_back_arrow:
                finish();
                break;
        }
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case VIEW_POST_TAG:
                try {
                    mProgressBar.setVisibility(View.GONE);
                    if (object != null) {
                        if (object.has("lstAnimalBidding") && object.getJSONArray("lstAnimalBidding") != null) {
                            JSONArray detailArray = object.getJSONArray("lstAnimalBidding");
                            if (detailArray.length() > 0) {
                                List<ViewPostModel> postModelList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    postModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), ViewPostModel.class));
                                }
                                setViewPostAdapter(postModelList);
                                mActionLay.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(ViewMyPostActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
                                mActionLay.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case UPDATE_POST_TAG:
                mProgressBar.setVisibility(View.GONE);
                if (object != null) {
                    try {
                        String msg = object.getString("Message");
                        if (object.getString("Status").equals("1")) {
                            finish();
                        }
                        Toast.makeText(ViewMyPostActivity.this ,msg, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(ViewMyPostActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
    }

}
