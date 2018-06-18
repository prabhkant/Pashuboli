package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import biding.animal.com.animalbiding.adapter.AutoScrollAnimalViewPager;
import biding.animal.com.animalbiding.model.PostRequestModel;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class ViewPostCattleDetailRequestActivity extends AppCompatActivity implements View.OnClickListener,
        VolleyResponseInterface {

    private final int GET_REASON_TAG = 1;
    private final int GET_POST_IMAGE_ID = 2;
    private final int UPDATE_POST_ID = 3;
    private ImageView mBack, mCattleImage;
    private TextView mApprove, mReject, mNext, mPrevious;
    private Spinner mRejectReasonSpinner;
    private ProgressBar mProgressBar;
    private List<String> mReasonIdArray;
    private List<String> mReasonNameArray;
    private String reasonId = "0";
    private int itemId;
    private AutoScrollViewPager mAutoScrollPager;
    private AutoScrollAnimalViewPager mAnimalImageViewPager;
    private ArrayList<String> imageList;
    private List<PostRequestModel> postModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_detail_cattle_request);

        mBack = (ImageView) findViewById(R.id.back_arrow);
        mCattleImage = (ImageView) findViewById(R.id.cattle_img);
        mApprove = (TextView) findViewById(R.id.approve_request);
        mReject = (TextView) findViewById(R.id.reject_request);
        mRejectReasonSpinner = (Spinner) findViewById(R.id.reason_spinner);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAutoScrollPager = (AutoScrollViewPager) findViewById(R.id.image_viewpager);
        mNext = (TextView) findViewById(R.id.next);
        mPrevious = (TextView) findViewById(R.id.previous);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() > 1)
                    mAutoScrollPager.setCurrentItem(mAutoScrollPager.getCurrentItem() + 1);
            }
        });
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageList.size() > 1)
                    mAutoScrollPager.setCurrentItem(mAutoScrollPager.getCurrentItem() - 1);
            }
        });

        mApprove.setOnClickListener(this);
        mReject.setOnClickListener(this);
        mBack.setOnClickListener(this);
        //...
        mAutoScrollPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mPrevious.setVisibility(View.INVISIBLE);
                } else {
                    mPrevious.setVisibility(View.VISIBLE);
                }
                if (position == (imageList.size() - 1)) {
                    mNext.setVisibility(View.INVISIBLE);
                } else {
                    mNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getIntent() != null) {
            if (getIntent().getExtras().containsKey(ConstantMsg.ITEM_ID))
                itemId = getIntent().getIntExtra(ConstantMsg.ITEM_ID, 0);
            getPostImageId(itemId);
        }
        //...
        getReasonList();

        //onItemSelected listener
        mRejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(mReasonIdArray.get(i).equals("0"))) {
                    reasonId = mReasonIdArray.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //...
    private void setAutoScrollImageView(ArrayList<String> imageList) {
        mAnimalImageViewPager = new AutoScrollAnimalViewPager(ViewPostCattleDetailRequestActivity.this, imageList, true);
        mAutoScrollPager.setAdapter(mAnimalImageViewPager);
        mAutoScrollPager.startAutoScroll();
        mAutoScrollPager.setInterval(5000);
    }

    //method to get every user detail type
    private void getPostImageId(int itemId) {
        showProgressBar();
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_POST_IMAGE_ID + "?ItemId=" + itemId, null, GET_POST_IMAGE_ID);
    }

    //method to get every user detail type
    private void getReasonList() {
        showProgressBar();
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_REASON_LIST + "?usertypeid=" + SharedPrefernceManger.getUserTypeId(), null, GET_REASON_TAG);
    }

    //method to get every user detail type
    private void updateStatus(int itemId, int statusId, int reasonId) {
        showProgressBar();
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.UPDATE_USER_REQUEST_STATUS, getUpdateReq(itemId, statusId, reasonId), UPDATE_POST_ID);
    }

    //method to set category spinner adapter
    private void setReasonSpinner(List<String> reasonArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewPostCattleDetailRequestActivity.this, android.R.layout.simple_spinner_item, reasonArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mRejectReasonSpinner.setAdapter(arrayAdapter);
    }

    private void showProgressBar() {
        if (!isFinishing()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        if (mProgressBar != null && mProgressBar.getVisibility() == View.GONE)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (!isFinishing()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE)
            mProgressBar.setVisibility(View.GONE);
    }

    //...
    private JSONObject getUpdateReq(int itemId, int statusId, int reasonId) {
        JSONObject object = new JSONObject();
        try {
            object.put("ItemId", itemId);
            object.put("StatusId", statusId);
            object.put("ReasonId", reasonId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.approve_request:
                updateStatus(Integer.parseInt(postModelList.get(0).getItemId()), 1, Integer.parseInt(reasonId));
                break;
            case R.id.reject_request:
                if (reasonId.equalsIgnoreCase("0")) {
                    Toast.makeText(ViewPostCattleDetailRequestActivity.this, R.string.select_reason, Toast.LENGTH_LONG).show();
                } else {
                    updateStatus(Integer.parseInt(postModelList.get(0).getItemId()), 0, Integer.parseInt(reasonId));
                }
                break;
            case R.id.back_arrow:
                finish();
                break;
        }
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_POST_IMAGE_ID:
                try {
                    if (object != null) {
                        if (object.has("lstUserImageMaping") && object.getJSONArray("lstUserImageMaping") != null) {
                            JSONArray detailArray = object.getJSONArray("lstUserImageMaping");
                            if (detailArray.length() > 0) {
                                postModelList = new ArrayList<>();
                                imageList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    postModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), PostRequestModel.class));
                                }
                                //...
                                for (int j = 0; j < postModelList.size(); j++) {
                                    imageList.add(postModelList.get(j).getImageName());
                                }
                                //...
                                if (imageList.size() > 0) {
                                    setAutoScrollImageView(imageList);
                                } else {
                                    Toast.makeText(this, "No Image Available", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(ViewPostCattleDetailRequestActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressBar();
                break;

            case GET_REASON_TAG:
                try {
                    if (object.has("lslRejectionModel")) {
                        mReasonNameArray = new ArrayList<>();
                        mReasonIdArray = new ArrayList<>();
                        mReasonIdArray.add("0");
                        mReasonNameArray.add(getString(R.string.select_reason));
                        JSONArray jsonArray = object.getJSONArray("lslRejectionModel");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mReasonIdArray.add(detailObj.getString("RejectionId"));
                                mReasonNameArray.add(detailObj.getString("RejectionName"));
                            }
                        } else {
                            Toast.makeText(ViewPostCattleDetailRequestActivity.this, R.string.no_reason_available, Toast.LENGTH_LONG).show();
                        }
                        setReasonSpinner(mReasonNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hideProgressBar();
                break;
            case UPDATE_POST_ID:
                try {
                    if (object != null) {
                        String msg = object.getString("Message");
                        if (object.getString("Status").equalsIgnoreCase("1")) {
                            finish();
                        }
                        Toast.makeText(ViewPostCattleDetailRequestActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
//        switch (tag) {
//            case GET_POST_IMAGE_ID:
//                Toast.makeText(ViewPostCattleDetailRequestActivity.this,R.string.some_problem_occurred,Toast.LENGTH_LONG).show();
//                break;
//        }
        Toast.makeText(ViewPostCattleDetailRequestActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
    }

}
