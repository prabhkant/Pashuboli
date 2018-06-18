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
import biding.animal.com.animalbiding.adapter.NotificationAdapter;
import biding.animal.com.animalbiding.model.NotificationModel;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class NotificationActivity extends AppCompatActivity implements VolleyResponseInterface {

    private final int GET_NOTIFICATION_TAG = 1;
    private RecyclerView mNotificationListView;
    private ProgressBar mProgressBar;
    private NotificationAdapter mNotificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNotificationListView = (RecyclerView) findViewById(R.id.notificationListView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //...
        getNotifications();
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
    private void setNotificationAdapter(List<NotificationModel> specificationList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mNotificationListView.setLayoutManager(layoutManager);
        mNotificationAdapter = new NotificationAdapter(NotificationActivity.this, specificationList);
        mNotificationListView.setAdapter(mNotificationAdapter);
    }

    //method to get notification
    private void getNotifications() {  // status 1 for approve, 2 for reject
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ALL_NOTIFICATION + "?UserId=" + SharedPrefernceManger.getUserId(), null, GET_NOTIFICATION_TAG);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_NOTIFICATION_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        if (object.has("lstNotificationModel") && object.getJSONArray("lstNotificationModel") != null) {
                            JSONArray detailArray = object.getJSONArray("lstNotificationModel");
                            if (detailArray.length() > 0) {
                                List<NotificationModel> postModelList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    postModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), NotificationModel.class));
                                }
                                setNotificationAdapter(postModelList);
                            } else {
                                Toast.makeText(NotificationActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
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
