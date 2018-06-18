package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import biding.animal.com.animalbiding.model.UserDetailModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

/**
 * Created by Prabhakant.Agnihotri on 06-03-2018.
 */

public class ProfileUpdateActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseInterface {

    private ApplicationClass mInstance;
    private ImageView mCancel;
    private Spinner mStateSpinner, mDistrictSpinner, mAreaSpinner;
    private LinearLayout mStateLay, mDistrictLay, mAreaLay;
    private ProgressBar mProgressBar;
    private EditText mFirstName, mMiddleName, mLastName, mAddress, mZipCode, mMobileNo, mFatherName, mEmail;
    private TextView mUpdate;

    private static final int UPDATE_PROFILE_TAG = 1;
    private static final int GET_STATE_TAG = 3;
    private List<String> mStateNameArray;
    private List<String> mStateIdArray;
    private String stateId = "ALL";
    private static final int GET_DISTRICT_TAG = 4;
    private List<String> mDistrictNameArray;
    private List<String> mDistrictIdArray;
    private String districtId = "ALL";
    private static final int GET_AREA_TAG = 5;
    private List<String> mAreaNameArray;
    private List<String> mAreaIdArray;
    private String areaId = "ALL";

//    private DialogInterface.OnDismissListener onDismissListener;
//
//    public void setOnUpdateDismissListener(DialogInterface.OnDismissListener onDismissListener) {
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
//
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
//        View view = inflater.inflate(R.layout.profile_update, container, false);
//
//        initiateViews(view);
//        return view;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_update);

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
    //...
    private void initiateViews() {
        mInstance = ApplicationClass.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mCancel = (ImageView) findViewById(R.id.cancel);
        mStateSpinner = (Spinner) findViewById(R.id.state_spinner);
        mDistrictSpinner = (Spinner) findViewById(R.id.city_spinner);
        mAreaSpinner = (Spinner) findViewById(R.id.area_spinner);
        mStateLay = (LinearLayout) findViewById(R.id.stateLayout);
        mDistrictLay = (LinearLayout) findViewById(R.id.districtLayout);
        mAreaLay = (LinearLayout) findViewById(R.id.areaLayout);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mMiddleName = (EditText) findViewById(R.id.middle_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mAddress = (EditText) findViewById(R.id.address);
        mZipCode = (EditText) findViewById(R.id.zipcode);
        mMobileNo = (EditText) findViewById(R.id.alternate_contact_no);
        mFatherName = (EditText) findViewById(R.id.father_name);
        mEmail = (EditText) findViewById(R.id.email_id);
        mUpdate = (TextView) findViewById(R.id.update);

        mCancel.setOnClickListener(this);
        mUpdate.setOnClickListener(this);

        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!(mStateIdArray.get(position).equals("ALL"))) {
                    mDistrictLay.setVisibility(View.VISIBLE);
                    stateId = mStateIdArray.get(position);
                    getDistrictByState(stateId);
                } else {
                    mDistrictLay.setVisibility(View.GONE);
                }

                if (ApplicationClass.getInstance().getUserDetailModelList() != null && ApplicationClass.getInstance().getUserDetailModelList().size()>0) {
                    UserDetailModel userDetailModel = ApplicationClass.getInstance().getUserDetailModelList().get(0);
                    String stateId = userDetailModel.getFK_StateId();
                    if (mStateIdArray != null && mStateIdArray.size()>0) {
                        for (int i=0; i<mStateIdArray.size(); i++) {
                            if (stateId.equals(mStateIdArray.get(i))) {
                                mStateSpinner.setSelection(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (!(mDistrictIdArray.get(position).equals("ALL"))) {
                    mAreaLay.setVisibility(View.VISIBLE);
                    districtId = mDistrictIdArray.get(position);
                    getAreaByDistrict(districtId);
                } else {
                    mAreaLay.setVisibility(View.GONE);
                }
                if (ApplicationClass.getInstance().getUserDetailModelList() != null && ApplicationClass.getInstance().getUserDetailModelList().size()>0) {
                    UserDetailModel userDetailModel = ApplicationClass.getInstance().getUserDetailModelList().get(0);
                    String districtId = userDetailModel.getFK_CityId();
                    if (mDistrictIdArray != null && mDistrictIdArray.size()>0) {
                        for (int i=0; i<mDistrictIdArray.size(); i++) {
                            if (districtId.equals(mDistrictIdArray.get(i))) {
                                mDistrictSpinner.setSelection(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                areaId = mAreaIdArray.get(position);
                if (ApplicationClass.getInstance().getUserDetailModelList() != null && ApplicationClass.getInstance().getUserDetailModelList().size()>0) {
                    UserDetailModel userDetailModel = ApplicationClass.getInstance().getUserDetailModelList().get(0);
                    String areaId = userDetailModel.getFK_AreaId();
                    if (mAreaIdArray != null && mAreaIdArray.size()>0) {
                        for (int i=0; i<mAreaIdArray.size(); i++) {
                            if (areaId.equals(mAreaIdArray.get(i))) {
                                mAreaSpinner.setSelection(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //...
        getAllState();

        //...
        setProfileValues();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
//                dismiss();
                break;
            case R.id.update:
                checkValidation();
                break;
        }
    }

    //...
    private void checkValidation() {
        String strFirstName, strMiddleName, strLastName, strAddress, strZipCode, strFatherName, strEmail;
        strFirstName = mFirstName.getText().toString().trim();
        strMiddleName = mMiddleName.getText().toString().trim();
        strLastName = mLastName.getText().toString().trim();
        strAddress = mAddress.getText().toString().trim();
        strZipCode = mZipCode.getText().toString().trim();
        strFatherName = mFatherName.getText().toString().trim();
        strEmail = mEmail.getText().toString().trim();

        if (strFirstName.isEmpty()) {
            mFirstName.setError(getString(R.string.enter_first_name));
            mFirstName.requestFocus();
        } else if (stateId.equalsIgnoreCase("ALL")) {
            Toast.makeText(ProfileUpdateActivity.this, getString(R.string.select_state), Toast.LENGTH_LONG).show();
        } else if (districtId.equalsIgnoreCase("ALL")) {
            Toast.makeText(ProfileUpdateActivity.this, getString(R.string.select_district), Toast.LENGTH_LONG).show();
        } else if (areaId.equalsIgnoreCase("ALL")) {
            Toast.makeText(ProfileUpdateActivity.this, getString(R.string.select_area), Toast.LENGTH_LONG).show();
        } else if (strFatherName.isEmpty()) {
            mFatherName.setError(getString(R.string.enter_father_name));
            mFatherName.requestFocus();
        } else {
            updateProfile(strFirstName, strMiddleName, strLastName, stateId, districtId, areaId, strAddress, strZipCode, strFatherName, strEmail);
        }

    }

    //method to call update profile
    private void updateProfile(String firstName, String middleName, String lastName, String stateId, String cityId, String areaId, String address,
                               String zipCode, String fatherName, String email) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.UPDATE_PROFILE,
                getUpdateRequest(firstName, middleName, lastName, stateId, cityId, areaId, address, zipCode, fatherName, email), UPDATE_PROFILE_TAG);
    }

    //...
    private JSONObject getUpdateRequest(String firstName, String middleName, String lastName, String stateId, String cityId, String areaId, String address,
                                        String zipCode, String fatherName, String email) {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", SharedPrefernceManger.getUserId());
            object.put("FirstName", firstName);
            object.put("MiddleName", middleName);
            object.put("LastName", lastName);
            object.put("FK_StateId", stateId);
            object.put("FK_CityId", cityId);
            object.put("FK_AreaId", areaId);
            object.put("Address", address);
            object.put("Pincode", zipCode);
            object.put("FatherName", fatherName);
            object.put("EmailId", email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    //method to get state category
    private void getAllState() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ALL_STATE, null, GET_STATE_TAG);
    }

    //method to get district category
    private void getDistrictByState(String stateId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ALL_CITY_BY_STATE_ID + "stateid=" + stateId, null, GET_DISTRICT_TAG);
    }

    //method to get area category
    private void getAreaByDistrict(String districtId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_AREA_BY_CITY_ID + "cityid=" + districtId, null, GET_AREA_TAG);
    }

    //method to set category spinner adapter
    private void setStateSpinner(List<String> stateArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileUpdateActivity.this, android.R.layout.simple_spinner_item, stateArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mStateSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setDistrictSpinner(List<String> districtArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileUpdateActivity.this, android.R.layout.simple_spinner_item, districtArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mDistrictSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setAreaSpinner(List<String> areaArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileUpdateActivity.this, android.R.layout.simple_spinner_item, areaArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mAreaSpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_STATE_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstStateModelsModels")) {
                        mStateIdArray = new ArrayList<>();
                        mStateNameArray = new ArrayList<>();
                        mStateNameArray.add(getString(R.string.select_state));
                        mStateIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstStateModelsModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mStateIdArray.add(detailObj.getString("StateId"));
                                mStateNameArray.add(detailObj.getString("StateName"));
                            }
                        } else {
                            Toast.makeText(ProfileUpdateActivity.this, R.string.no_state_available, Toast.LENGTH_LONG).show();
                        }
                        setStateSpinner(mStateNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GET_DISTRICT_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstCityModelsModels")) {
                        mDistrictIdArray = new ArrayList<>();
                        mDistrictNameArray = new ArrayList<>();
                        mDistrictNameArray.add(getString(R.string.select_district));
                        mDistrictIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstCityModelsModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mDistrictIdArray.add(detailObj.getString("CityId"));
                                mDistrictNameArray.add(detailObj.getString("CityName"));
                            }
                        } else {
                            Toast.makeText(ProfileUpdateActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
                        }
                        setDistrictSpinner(mDistrictNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GET_AREA_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstAreaModels")) {
                        mAreaIdArray = new ArrayList<>();
                        mAreaNameArray = new ArrayList<>();
                        mAreaNameArray.add(getString(R.string.select_area));
                        mAreaIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstAreaModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mAreaIdArray.add(detailObj.getString("AreaId"));
                                mAreaNameArray.add(detailObj.getString("AreaName"));
                            }
                        } else {
                            Toast.makeText(ProfileUpdateActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
                        }
                        setAreaSpinner(mAreaNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case UPDATE_PROFILE_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        String msg = object.getString("Message");
                        if (object.getString("Status").equals("1")) {
//                            dismiss();
                            finish();
                        }
                        Toast.makeText(ProfileUpdateActivity.this, msg, Toast.LENGTH_LONG).show();
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

    //method to get user
    private void setProfileValues () {
        if (ApplicationClass.getInstance().getUserDetailModelList() != null && ApplicationClass.getInstance().getUserDetailModelList().size()>0) {
            UserDetailModel userDetailModel = ApplicationClass.getInstance().getUserDetailModelList().get(0);

            mFirstName.setText(userDetailModel.getFirstName());
            mMiddleName.setText(userDetailModel.getMiddleName());
            mLastName.setText(userDetailModel.getLastName());
            mAddress.setText(userDetailModel.getAddress());
            mZipCode.setText(userDetailModel.getPincode());
            mMobileNo.setText(userDetailModel.getPhone());
            mFatherName.setText(userDetailModel.getFatherName());
            mEmail.setText(userDetailModel.getEmailId());
        }
    }

}
