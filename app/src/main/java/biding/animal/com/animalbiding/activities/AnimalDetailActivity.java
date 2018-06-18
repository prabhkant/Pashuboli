package biding.animal.com.animalbiding.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import biding.animal.com.animalbiding.adapter.ImageAdapter;
import biding.animal.com.animalbiding.adapter.SpecificationAdapter;
import biding.animal.com.animalbiding.model.AnimalDetailImages;
import biding.animal.com.animalbiding.model.AnimalDetailModel;
import biding.animal.com.animalbiding.model.AnimalSpecificationDetailModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class AnimalDetailActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private static final int POST_DETAIL_TAG = 1;
    private static final int ADD_BID_TAG = 2;
    private LinearLayout mPhotoLay, mBidNowLay, mContactLay;
    private TextView mAnimalName, mPostTime, mItemCode, mPrice, mDescription, mPhotoCount, mBiiderName, mBidderNumber;
    private ImageView mBackToHome;
    private RecyclerView mSpecificationListView;
    private ProgressBar mProgressBar;
    private ApplicationClass mInstance;
    private List<AnimalDetailModel> mDetailModelList;
    private List<AnimalSpecificationDetailModel> mSpecificationModelList;
    private SpecificationAdapter mSpecificationAdapter;
    private List<AnimalDetailImages> mImageModelList;
    private ImageAdapter mImageAdapter;
    private Dialog bidnowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        //...
        initiateViews();

    }

    //...
    private void initiateViews() {
        mInstance = ApplicationClass.getInstance();
        mSpecificationListView = (RecyclerView) findViewById(R.id.specificationListView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPhotoLay = (LinearLayout) findViewById(R.id.photo_lay);
        mAnimalName = (TextView) findViewById(R.id.detail_animal_name);
        mPostTime = (TextView) findViewById(R.id.detail_cattle_time);
        mItemCode = (TextView) findViewById(R.id.detail_itemcode);
        mPrice = (TextView) findViewById(R.id.detail_price);
        mDescription = (TextView) findViewById(R.id.detail_description);
        mPhotoCount = (TextView) findViewById(R.id.detail_photono);
        mBidNowLay = (LinearLayout) findViewById(R.id.detail_bidnow);
        mBackToHome = (ImageView) findViewById(R.id.detail_back_arrow);
        mContactLay = (LinearLayout) findViewById(R.id.contactLayout);
        mBidderNumber = (TextView) findViewById(R.id.contactPersonNumber);
        mBiiderName = (TextView) findViewById(R.id.contactPersonName);

        if (getIntent() != null) {
            String postId = getIntent().getStringExtra(ConstantMsg.POST_ID);
            //...
            getPostDetail(postId);
        }

        mPhotoLay.setOnClickListener(this);
        mBidNowLay.setOnClickListener(this);
        mBackToHome.setOnClickListener(this);
    }

    //...
    private void updateDetailViews(List<AnimalDetailModel> list) {
        mAnimalName.setText(list.get(0).getAnimalName());
        mItemCode.setText(getString(R.string.item_code) + ": " + list.get(0).getItemCode());
        mPrice.setText(list.get(0).getPrice());
        mDescription.setText(list.get(0).getItemDescription());

        if (list.get(0).getStatus().equals("1")) {
            mContactLay.setVisibility(View.VISIBLE);
            mBiiderName.setText(list.get(0).getUserName());
            mBidderNumber.setText(list.get(0).getPhoneNumber());
        } else {
            mContactLay.setVisibility(View.GONE);
        }

    }

    //...
    //Method to set recycler adapter...
    private void setSpecificationAdapter(List<AnimalSpecificationDetailModel> specificationList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSpecificationListView.setLayoutManager(layoutManager);
        mSpecificationAdapter = new SpecificationAdapter(AnimalDetailActivity.this, specificationList);
        mSpecificationListView.setAdapter(mSpecificationAdapter);
    }

    //...
    //Method to set recycler adapter...
    private void setImageAdapter(List<AnimalDetailImages> imageList, RecyclerView imageListView) {
        GridLayoutManager layoutManager = new GridLayoutManager(AnimalDetailActivity.this, 2);
        imageListView.setLayoutManager(layoutManager);
        mImageAdapter = new ImageAdapter(AnimalDetailActivity.this, imageList);
        imageListView.setAdapter(mImageAdapter);
    }

    //...
    private void openImageDialog(List<AnimalDetailImages> imageList) {
        final Dialog dialog = new Dialog(AnimalDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_layout);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setWindowAnimations(R.style.CustomOTPDialog);

        RecyclerView imageListView = (RecyclerView) dialog.findViewById(R.id.imageListView);
        setImageAdapter(imageList, imageListView);

        dialog.show();

    }

    //...
    //method to get every user detail type
    private void getPostDetail(String postId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_POST_DEATAIL_BY_POST_ID + "postid=" + postId + "&userid=" + SharedPrefernceManger.getUserId()
                , null, POST_DETAIL_TAG);
    }

    //...
    private void addBidPrice(String price, String postId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.ADD_BID_PRICE
                , getRequest(price, postId), ADD_BID_TAG);
    }

    //...
    private JSONObject getRequest(String price, String postId) {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", SharedPrefernceManger.getUserId());
            object.put("PostId", postId);
            object.put("Price", price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void openBidNowDialog(final String postId) {
        bidnowDialog = new Dialog(AnimalDetailActivity.this);
        bidnowDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bidnowDialog.setContentView(R.layout.bid_now_price_lay);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bidnowDialog.getWindow().setLayout(width, height);
        bidnowDialog.getWindow().setWindowAnimations(R.style.CustomOTPDialog);

        final AutoCompleteTextView mPrice = (AutoCompleteTextView) bidnowDialog.findViewById(R.id.bid_price);
        final TextInputLayout mPriceLay = (TextInputLayout) bidnowDialog.findViewById(R.id.bid_lay);
        ImageView cancel = (ImageView) bidnowDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bidnowDialog.dismiss();
            }
        });
        TextView btnPrice = (TextView) bidnowDialog.findViewById(R.id.btn_bid);
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mPrice.getText().toString().trim())) {
                    mPriceLay.setError("Please enter price");
                } else {
                    String price = mPrice.getText().toString().trim();
                    addBidPrice(price, postId);
                }
            }
        });
        bidnowDialog.setCancelable(false);
        bidnowDialog.setCanceledOnTouchOutside(false);
        try {
            bidnowDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo_lay:
                try {
                    if (mImageModelList != null && mImageModelList.size() > 0) {
                        openImageDialog(mImageModelList);
                    } else {
                        Toast.makeText(AnimalDetailActivity.this, "Working on it wait...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.detail_bidnow:
                try {
                    if (mDetailModelList != null && mDetailModelList.size() > 0) {
                        openBidNowDialog(mDetailModelList.get(0).getItemId());
                    } else {
                        Toast.makeText(AnimalDetailActivity.this, "Working on it wait...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.detail_back_arrow:
                finish();
                break;
        }
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case POST_DETAIL_TAG:
                try {
                    mProgressBar.setVisibility(View.GONE);
                    if (object != null) {
                        if (object.has("lstAnimalItemModel") && object.getJSONArray("lstAnimalItemModel") != null) {
                            JSONArray detailArray = object.getJSONArray("lstAnimalItemModel");
                            if (detailArray.length() > 0) {
                                mDetailModelList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    mDetailModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), AnimalDetailModel.class));
                                }
                                updateDetailViews(mDetailModelList);
                            } else {
                                Toast.makeText(AnimalDetailActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
                            }
                        }
                        if (object.has("lstSpecificationDetailModel") && object.getJSONArray("lstSpecificationDetailModel") != null) {
                            JSONArray specificationArray = object.getJSONArray("lstSpecificationDetailModel");
                            if (specificationArray.length() > 0) {
                                mSpecificationModelList = new ArrayList<>();
                                for (int i = 0; i < specificationArray.length(); i++) {
                                    JSONObject jsonObject = specificationArray.getJSONObject(i);
                                    mSpecificationModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), AnimalSpecificationDetailModel.class));
                                }
                                setSpecificationAdapter(mSpecificationModelList);
                            } else {
                                Toast.makeText(AnimalDetailActivity.this, R.string.no_specification, Toast.LENGTH_LONG).show();
                            }
                        }
                        if (object.has("lstUserImageMapingModel") && object.getJSONArray("lstUserImageMapingModel") != null) {
                            JSONArray imageArray = object.getJSONArray("lstUserImageMapingModel");
                            if (imageArray.length() > 0) {
                                mImageModelList = new ArrayList<>();
                                for (int i = 0; i < imageArray.length(); i++) {
                                    JSONObject jsonObject = imageArray.getJSONObject(i);
                                    mImageModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), AnimalDetailImages.class));
                                }
                                mPhotoCount.setText(String.valueOf(mImageModelList.size()) + " Photo");
                            } else {
                                Toast.makeText(AnimalDetailActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ADD_BID_TAG:
                try {
                    mProgressBar.setVisibility(View.GONE);
                    if (object != null) {
                        String msg = object.getString("Message");
                        Toast.makeText(AnimalDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                        if (bidnowDialog != null && bidnowDialog.isShowing()) {
                            bidnowDialog.dismiss();
                        }

//                        if (object.getString("Status").equalsIgnoreCase("1")) {
//
//                        } else {
//
//                        }
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
        Toast.makeText(AnimalDetailActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
    }

}
