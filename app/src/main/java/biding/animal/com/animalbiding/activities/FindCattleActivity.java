package biding.animal.com.animalbiding.activities;

import android.content.Intent;
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
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.UtilityClass;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class FindCattleActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private ProgressBar mProgressBar;
    private Spinner mCategorySpinner, mBreedSpinner, mStateSpinner, mDistrictSpinner, mAreaSpinner;
    private EditText minPrice, maxPrice;
    private LinearLayout mBreedLay, mDistrictLay, mAreaLay, mFindCattleLay;
    private ImageView cancelDialog;
    private String[] categoryArray = {"Select Category", "Cow", "Dog"};
    private ApplicationClass mInstance;

    private static final int GET_ANIMAL_CATEGORY_TAG = 1;
    private List<String> mCategoryNameArray;
    private List<String> mCategoryIdArray;
    private String catId = "ALL";
    private static final int GET_ANIMAL_BREED_TAG = 2;
    private List<String> mBreedNameArray;
    private List<String> mBreedIdArray;
    private String breedId = "ALL";
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

    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.find_cattle, container, false);
//
//        mInstance = ApplicationClass.getInstance();
//        initiateViews(view);
//        return view;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_cattle);

        mInstance = ApplicationClass.getInstance();
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
        cancelDialog = (ImageView) findViewById(R.id.cancel);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);
        mBreedSpinner = (Spinner) findViewById(R.id.breed_spinner);
        mStateSpinner = (Spinner) findViewById(R.id.state_spinner);
        mDistrictSpinner = (Spinner) findViewById(R.id.district_spinner);
        mAreaSpinner = (Spinner) findViewById(R.id.area_spinner);
        mBreedLay = (LinearLayout) findViewById(R.id.breedLayout);
        mDistrictLay = (LinearLayout) findViewById(R.id.districtLayout);
        mAreaLay = (LinearLayout) findViewById(R.id.areaLayout);
        mFindCattleLay = (LinearLayout) findViewById(R.id.find_cattle_lay);
        minPrice = (EditText) findViewById(R.id.minPrice);
        maxPrice = (EditText) findViewById(R.id.maxPrice);

        mFindCattleLay.setOnClickListener(this);
        cancelDialog.setOnClickListener(this);
        //call service to get data
        getAnimalCategory();
        getAllState();

        //onItemSelected listener
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(mCategoryIdArray.get(i).equals("ALL"))) {
                    mBreedLay.setVisibility(View.VISIBLE);
                    catId = mCategoryIdArray.get(i);
                    getBreedCategory(catId);
                } else {
                    mBreedLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mBreedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                breedId = mBreedIdArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(mStateIdArray.get(i).equals("-1") || mStateIdArray.get(i).equals("ALL"))) {
                    mDistrictLay.setVisibility(View.VISIBLE);
                    stateId = mStateIdArray.get(i);
                    getDistrictByState(stateId);
                } else {
                    mDistrictLay.setVisibility(View.GONE);
                    stateId = mStateIdArray.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(mDistrictIdArray.get(i).equals("-1") || mDistrictIdArray.get(i).equals("ALL"))) {
                    mAreaLay.setVisibility(View.VISIBLE);
                    districtId = mDistrictIdArray.get(i);
                    getAreaByDistrict(districtId);
                } else {
                    mAreaLay.setVisibility(View.GONE);
                    districtId = mDistrictIdArray.get(i);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //method to get animal category
    private void getAnimalCategory() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ANIMAL_CATEGORY, null, GET_ANIMAL_CATEGORY_TAG);
    }

    //method to get breed category
    private void getBreedCategory(String animalId) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_BREED_DETAIL + "animalid=" + animalId, null, GET_ANIMAL_BREED_TAG);
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
    private void setCategorySpinner(List<String> categoryArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FindCattleActivity.this, android.R.layout.simple_spinner_item, categoryArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mCategorySpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setBreedSpinner(List<String> breedArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FindCattleActivity.this, android.R.layout.simple_spinner_item, breedArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mBreedSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setStateSpinner(List<String> stateArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FindCattleActivity.this, android.R.layout.simple_spinner_item, stateArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mStateSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setDistrictSpinner(List<String> districtArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FindCattleActivity.this, android.R.layout.simple_spinner_item, districtArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mDistrictSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setAreaSpinner(List<String> areaArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FindCattleActivity.this, android.R.layout.simple_spinner_item, areaArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mAreaSpinner.setAdapter(arrayAdapter);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getDialog() != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
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

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
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
                            Toast.makeText(FindCattleActivity.this, R.string.no_category_available, Toast.LENGTH_LONG).show();
                        }
                        setCategorySpinner(mCategoryNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case GET_ANIMAL_BREED_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstAnimalSubCategoryModel")) {
                        mBreedIdArray = new ArrayList<>();
                        mBreedNameArray = new ArrayList<>();
                        mBreedNameArray.add(getString(R.string.select_breed));
                        mBreedIdArray.add("-1");
                        mBreedNameArray.add(getString(R.string.all));
                        mBreedIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstAnimalSubCategoryModel");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mBreedIdArray.add(detailObj.getString("AnimalSubCatId"));
                                mBreedNameArray.add(detailObj.getString("SubCatName"));
                            }
                        } else {
                            Toast.makeText(FindCattleActivity.this, R.string.no_breed_available, Toast.LENGTH_LONG).show();
                        }
                        setBreedSpinner(mBreedNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GET_STATE_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstStateModelsModels")) {
                        mStateIdArray = new ArrayList<>();
                        mStateNameArray = new ArrayList<>();
                        mStateNameArray.add(getString(R.string.select_state));
                        mStateIdArray.add("-1");
                        mStateNameArray.add(getString(R.string.all));
                        mStateIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstStateModelsModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mStateIdArray.add(detailObj.getString("StateId"));
                                mStateNameArray.add(detailObj.getString("StateName"));
                            }
                        } else {
                            Toast.makeText(FindCattleActivity.this, R.string.no_state_available, Toast.LENGTH_LONG).show();
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
                        mDistrictIdArray.add("-1");
                        mDistrictNameArray.add(getString(R.string.all));
                        mDistrictIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstCityModelsModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mDistrictIdArray.add(detailObj.getString("CityId"));
                                mDistrictNameArray.add(detailObj.getString("CityName"));
                            }
                        } else {
                            Toast.makeText(FindCattleActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
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
                        mAreaIdArray.add("-1");
                        mAreaNameArray.add(getString(R.string.all));
                        mAreaIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstAreaModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mAreaIdArray.add(detailObj.getString("AreaId"));
                                mAreaNameArray.add(detailObj.getString("AreaName"));
                            }
                        } else {
                            Toast.makeText(FindCattleActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
                        }
                        setAreaSpinner(mAreaNameArray);
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
            case R.id.find_cattle_lay:
                if (catId.equals("ALL")) {
                    Toast.makeText(FindCattleActivity.this, R.string.select_category, Toast.LENGTH_LONG).show();
                } //
                else if (breedId.equals("-1")) {
                    Toast.makeText(FindCattleActivity.this, R.string.select_breed, Toast.LENGTH_LONG).show();
                } //
                else if (stateId.equals("-1")) {
                    Toast.makeText(FindCattleActivity.this, R.string.select_state, Toast.LENGTH_LONG).show();
                }//
                else if (districtId.equals("-1")) {
                    Toast.makeText(FindCattleActivity.this, R.string.select_district, Toast.LENGTH_LONG).show();
                } //
                else if (areaId.equals("-1")) {
                    Toast.makeText(FindCattleActivity.this, R.string.select_area, Toast.LENGTH_LONG).show();
                } //
                else {
                    if (!UtilityClass.isConnected(FindCattleActivity.this)) {
                        sendDataIntent();
                        finish();
                    } else {
                        Toast.makeText(FindCattleActivity.this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.cancel:
//                dismiss();
                break;
        }
    }

    //...
    private void sendDataIntent() {
        String minimumPrice = minPrice.getText().toString().trim();
        String maximumPrice = maxPrice.getText().toString().trim();
        String animalName = mCategorySpinner.getSelectedItem().toString();

        Intent intent = new Intent(FindCattleActivity.this, CattleActivity.class);
        intent.putExtra(ConstantMsg.ANIMAL_ID, catId);
        intent.putExtra(ConstantMsg.BREED_ID, breedId);
        intent.putExtra(ConstantMsg.STATE_ID, stateId);
        intent.putExtra(ConstantMsg.CITY_ID, districtId);
        intent.putExtra(ConstantMsg.AREA_ID, areaId);
        intent.putExtra(ConstantMsg.MIN_PRICE, minimumPrice);
        intent.putExtra(ConstantMsg.MAX_PRICE, maximumPrice);
        intent.putExtra(ConstantMsg.ANIMAL_NAME, animalName);
        FindCattleActivity.this.startActivity(intent);
    }

}
