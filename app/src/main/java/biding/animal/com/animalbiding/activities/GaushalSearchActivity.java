package biding.animal.com.animalbiding.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.adapter.GaushalaSearchAdapter;
import biding.animal.com.animalbiding.model.GaushalaSearchModel;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class GaushalSearchActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private static final int GET_GAUSHALA_TAG = 1;
    private ProgressBar mProgressBar;
    private ImageView mBack;
    private RecyclerView mListView;
    private Spinner mStateSpinner, mDistrictSpinner, mAreaSpinner;
    private LinearLayout mDistrictLay, mAreaLay;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaushal_search);

        initiateViews();
    }

    private void initiateViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mBack = (ImageView) findViewById(R.id.back_arrow);
        mListView = (RecyclerView) findViewById(R.id.gaushalaListView);
        mStateSpinner = (Spinner) findViewById(R.id.state_spinner);
        mDistrictSpinner = (Spinner) findViewById(R.id.district_spinner);
        mAreaSpinner = (Spinner) findViewById(R.id.area_spinner);
        mDistrictLay = (LinearLayout) findViewById(R.id.districtLayout);
        mAreaLay = (LinearLayout) findViewById(R.id.areaLayout);

        mBack.setOnClickListener(this);
        ///....
        getAllState();

        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(mStateIdArray.get(i).equals("ALL"))) {
                    mDistrictLay.setVisibility(View.VISIBLE);
                    stateId = mStateIdArray.get(i);
                    getDistrictByState(stateId);
                } else {
                    mDistrictLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(mDistrictIdArray.get(i).equals("ALL"))) {
                    mAreaLay.setVisibility(View.VISIBLE);
                    districtId = mDistrictIdArray.get(i);
                    getAreaByDistrict(districtId);
                } else {
                    mAreaLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaId = mAreaIdArray.get(i);
                getGaushalaData(stateId, districtId, areaId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //method to set category spinner adapter
    private void setStateSpinner(List<String> stateArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GaushalSearchActivity.this, android.R.layout.simple_spinner_item, stateArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mStateSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setDistrictSpinner(List<String> districtArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GaushalSearchActivity.this, android.R.layout.simple_spinner_item, districtArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mDistrictSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setAreaSpinner(List<String> areaArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GaushalSearchActivity.this, android.R.layout.simple_spinner_item, areaArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mAreaSpinner.setAdapter(arrayAdapter);
    }

    //method to get state category
    private void getAllState() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ALL_STATE, null, GET_STATE_TAG);
    }

    //method to get district category
    private void getDistrictByState(String stateId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ALL_CITY_BY_STATE_ID + "stateid=" + stateId, null, GET_DISTRICT_TAG);
    }

    //method to get area category
    private void getAreaByDistrict(String districtId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_AREA_BY_CITY_ID + "cityid=" + districtId, null, GET_AREA_TAG);
    }

    //method to get gaushala data
    private void getGaushalaData(String stateId, String districtId, String areaId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ALL_GAUSHALA_DATA + "?stateid=" + stateId
                + "&cityid=" + districtId + "&areaid=" + areaId, null, GET_GAUSHALA_TAG);
    }

    //Method to set recycler adapter...
    private void setGaushalaSearchAdapter(List<GaushalaSearchModel> gaushalaList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(GaushalSearchActivity.this, LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(layoutManager);
        GaushalaSearchAdapter searchAdapter = new GaushalaSearchAdapter(GaushalSearchActivity.this, gaushalaList);
        mListView.setAdapter(searchAdapter);
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
                            Toast.makeText(GaushalSearchActivity.this, R.string.no_state_available, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(GaushalSearchActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(GaushalSearchActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
                        }
                        setAreaSpinner(mAreaNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GET_GAUSHALA_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstGaushalaModels")) {
                        JSONArray jsonArray = object.getJSONArray("lstGaushalaModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            List<GaushalaSearchModel> searchModelList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                searchModelList.add(new GsonBuilder().create().fromJson(detailObj.toString(), GaushalaSearchModel.class));
                            }
                            setGaushalaSearchAdapter(searchModelList);
                        } else {
                            Toast.makeText(GaushalSearchActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
                        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_arrow:
                finish();
                break;
        }
    }
}
