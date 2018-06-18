package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.adapter.BiddingHistoryAdapter;
import biding.animal.com.animalbiding.model.BiddingHistoryModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

/**
 * Created by Prabhakant.Agnihotri on 06-03-2018.
 */

public class BiddingHistroyActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private static final int GET_BIDDING_HISTROY = 1;
    private BiddingHistoryAdapter biddingHistoryAdapter;
    private ApplicationClass mInstance;
    private ImageView mCancel;
    private TextView mHeading;
    private RecyclerView mBiddingHistoryListView;
    private ProgressBar mProgressBar;
    private List<BiddingHistoryModel> biddingHistoryModelList;

//    private DialogInterface.OnDismissListener onDismissListener;
//
//    public void setOnBiddingHistoryDismissListener(DialogInterface.OnDismissListener onDismissListener) {
//        this.onDismissListener = onDismissListener;
//    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        if (onDismissListener != null) {
//            onDismissListener.onDismiss(dialog);
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getDialog() != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            getDialog().getWindow().setLayout(width, height);
//        }
//    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomOTPDialog;
//        return dialog;
//    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.bidding_histroy_post_layout, container, false);
//
//        initiateViews(view);
//        return view;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bidding_histroy_post_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initiateViews();
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

    //method to initiate views
    private void initiateViews() {
        mInstance = ApplicationClass.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mCancel = (ImageView) findViewById(R.id.cancel);
        mHeading = (TextView) findViewById(R.id.heading);
        mBiddingHistoryListView = (RecyclerView) findViewById(R.id.history_postListView);
        mHeading.setText(R.string.bidding_history);
        mCancel.setOnClickListener(this);

        //...
        getBiddingHistory();
    }

    //....
    private void setBiddingHistoryAdapter(List<BiddingHistoryModel> biddingHistoryModelList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(BiddingHistroyActivity.this, LinearLayoutManager.VERTICAL, false);
        biddingHistoryAdapter = new BiddingHistoryAdapter(BiddingHistroyActivity.this, biddingHistoryModelList);
        mBiddingHistoryListView.setLayoutManager(layoutManager);
        mBiddingHistoryListView.setAdapter(biddingHistoryAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
//                dismiss();
                break;
        }
    }

    //...
    private void getBiddingHistory() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(),
                VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_BIDDING_HISTORY + "?userid=" + SharedPrefernceManger.getUserId(),
                null, GET_BIDDING_HISTROY);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_BIDDING_HISTROY:
                if (object != null) {
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        if (object.has("lstBidingModel")) {
                            Gson gson = new GsonBuilder().create();
                            JSONArray jsonArray = object.getJSONArray("lstBidingModel");
                            biddingHistoryModelList = new ArrayList<>();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    biddingHistoryModelList.add(gson.fromJson(jsonObject.toString(), BiddingHistoryModel.class));
                                }
                                setBiddingHistoryAdapter(biddingHistoryModelList);
                            } else {
                                Toast.makeText(BiddingHistroyActivity.this, "No History found", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception e) {
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
        Toast.makeText(BiddingHistroyActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
    }


}
