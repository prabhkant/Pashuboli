package biding.animal.com.animalbiding.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.adapter.DetailAnimalCategoryAdapter;
import biding.animal.com.animalbiding.fragment.LargeImageDialogFragment;
import biding.animal.com.animalbiding.model.CattleModel;
import biding.animal.com.animalbiding.model.DoctorEobDetailModel;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class ViewDoctorDetailActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private static final int GET_ANIMAL_CATEGORY_TAG = 2;
    private List<CattleModel> animalCategoryList;
    private List<DoctorEobDetailModel> postModelList;
    private static final int GET_REQUEST_TAG = 1;
    private static int requestId;
    private ProgressBar mProgressBar;
    private LinearLayout noBankLayout;
    private EditText mDDCheckNo, mBankName, mNoOfDays;
    private TextView mAddLocation, mViewMap, doctorType, address, referenceBy, phoneNo, facebookId, panCard, adharCard;
    private TextView mVerify, mUpdate;
    private RecyclerView categoryListView;
    private Spinner mPaymentSpinner;
    private ImageView userImage;
    private String[] paymentModeList = {"DD", "Check", "Pending", "Cash"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_detail);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey(ConstantMsg.REQUEST_ID)) {
                    requestId = getIntent().getIntExtra(ConstantMsg.REQUEST_ID, 0);
                }
            }
        }

        initiateViews();
        setPaymentAdapter();
    }

    //method to initiate views...
    private void initiateViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        userImage = (ImageView) findViewById(R.id.userImage);
        noBankLayout = (LinearLayout) findViewById(R.id.noBankLay);
        mBankName = (EditText) findViewById(R.id.bankName);
        mNoOfDays = (EditText) findViewById(R.id.noOfDays);
        mDDCheckNo = (EditText) findViewById(R.id.ddCheckNo);
        mPaymentSpinner = (Spinner) findViewById(R.id.payment_spinner);
        mAddLocation = (TextView) findViewById(R.id.addLocation);
        mViewMap = (TextView) findViewById(R.id.viewMap);
        doctorType = (TextView) findViewById(R.id.doctorType);
        address = (TextView) findViewById(R.id.address);
        referenceBy = (TextView) findViewById(R.id.referenceBy);
        phoneNo = (TextView) findViewById(R.id.phoneNo);
        facebookId = (TextView) findViewById(R.id.facebookId);
        panCard = (TextView) findViewById(R.id.panCard);
        adharCard = (TextView) findViewById(R.id.adharCard);
        mVerify = (TextView) findViewById(R.id.verify);
        mUpdate = (TextView) findViewById(R.id.update);
        categoryListView = (RecyclerView) findViewById(R.id.categoryListView);

        //...
        mVerify.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAddLocation.setOnClickListener(this);
        mViewMap.setOnClickListener(this);
        panCard.setOnClickListener(this);
        adharCard.setOnClickListener(this);

        mPaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String paymentMode = mPaymentSpinner.getSelectedItem().toString();
                if (paymentMode.equalsIgnoreCase("DD")) {
                    noBankLayout.setVisibility(View.VISIBLE);
                    mNoOfDays.setVisibility(View.GONE);
                    mDDCheckNo.setHint("DD No.");
                }//
                else if (paymentMode.equalsIgnoreCase("Check")) {
                    noBankLayout.setVisibility(View.VISIBLE);
                    mNoOfDays.setVisibility(View.GONE);
                    mDDCheckNo.setHint("Check No.");
                }//
                else if (paymentMode.equalsIgnoreCase("Pending")) {
                    noBankLayout.setVisibility(View.GONE);
                    mNoOfDays.setVisibility(View.VISIBLE);

                }//
                else {
                    noBankLayout.setVisibility(View.GONE);
                    mNoOfDays.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //...
    private void setPaymentAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewDoctorDetailActivity.this, android.R.layout.simple_spinner_item, paymentModeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mPaymentSpinner.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //...
        getDoctorRequestDetail(requestId);
        //...
        getAnimalCategory();
    }

    //...
    private void populateViews(List<DoctorEobDetailModel> postModelList) {
        doctorType.setText(postModelList.get(0).getDoctorType());
        address.setText(postModelList.get(0).getClinicAddress());
        referenceBy.setText(postModelList.get(0).getRefBy());
        phoneNo.setText(postModelList.get(0).getAltContactNumaber());
        facebookId.setText(postModelList.get(0).getFaceBookId());
        //set User image...
        if (postModelList != null && postModelList.size() > 0) {
            if (postModelList.get(0).getFileName() != null && !(postModelList.get(0).getFileName().isEmpty())) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.cow)
                        .showImageOnFail(R.drawable.cow)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                imageLoader.displayImage(postModelList.get(0).getFileName(), userImage, options);
            }
        }
    }

    //Method to set recycler adapter...
    private void setCategoryAdapter(List<CattleModel> categoryList) {
        GridLayoutManager layoutManager = new GridLayoutManager(ViewDoctorDetailActivity.this, 2);
        categoryListView.setLayoutManager(layoutManager);
        DetailAnimalCategoryAdapter categoryAdapter = new DetailAnimalCategoryAdapter(ViewDoctorDetailActivity.this, categoryList);
        categoryListView.setAdapter(categoryAdapter);
    }

    //method to get every user detail type
    private void getDoctorRequestDetail(int requestId) {
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_DOCTOR_REQUEST_DETAIL + "?requestid=" + requestId, null, GET_REQUEST_TAG);
    }

    //method to get animal category
    private void getAnimalCategory() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ANIMAL_CATEGORY, null, GET_ANIMAL_CATEGORY_TAG);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_REQUEST_TAG:
                try {
                    if (object != null) {
                        if (object.has("lstRequestData") && object.getJSONArray("lstRequestData") != null) {
                            JSONArray detailArray = object.getJSONArray("lstRequestData");
                            if (detailArray.length() > 0) {
                                postModelList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    postModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), DoctorEobDetailModel.class));
                                }
                                //TODO
                                populateViews(postModelList);
                            } else {
                                Toast.makeText(ViewDoctorDetailActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    mProgressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressBar.setVisibility(View.GONE);
                break;

            case GET_ANIMAL_CATEGORY_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object.has("lstAnimalCategoryModel")) {
                        JSONArray jsonArray = object.getJSONArray("lstAnimalCategoryModel");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            animalCategoryList = new ArrayList<>();
                            Gson gson = new GsonBuilder().create();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                animalCategoryList.add(gson.fromJson(detailObj.toString(), CattleModel.class));
                            }
                            setCategoryAdapter(animalCategoryList);
                        } else {
                            Toast.makeText(ViewDoctorDetailActivity.this, R.string.no_category_available, Toast.LENGTH_LONG).show();
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
        switch (tag) {
            case GET_REQUEST_TAG:
                Toast.makeText(ViewDoctorDetailActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verify:
                break;
            case R.id.update:
                break;
            case R.id.addLocation:
                break;
            case R.id.viewMap:
                break;
            case R.id.adharCard:
                if (postModelList != null && postModelList.size() > 0) {
                    if (postModelList.get(0).getAdharCard() != null && !(postModelList.get(0).getAdharCard().isEmpty())) {
                        openLargeImage(postModelList.get(0).getAdharCard());
                    }
                }
                break;
            case R.id.panCard:
//                if (postModelList != null && postModelList.size() > 0) {
//                    if (postModelList.get(0).getPanCard() != null && !(postModelList.get(0).getPanCard().isEmpty())) {
//                        openLargeImage(postModelList.get(0).getPanCard());
//                    }
//                }
                String url = "https://www.google.co.in/search?q=sample+images&tbm=isch&source=iu&ictx=1&fir=" +
                        "ogIr6Tx9fZfJAM%253A%252CN6CmunP9eW6aiM%252C_&usg=__Q9kbemijzQCx3LfDQOnr9scyOWo%3D&sa=X&ved=0ahUKEwiJ2LjCguXaAhVCqY8KHca0ATAQ9QEIKjAA" +
                        "#imgrc=ogIr6Tx9fZfJAM:";
                openLargeImage(url);
                break;
        }
    }

    //...
    public void openLargeImage(String imageUrl) {
        FragmentManager fragmentManager = getFragmentManager();
        LargeImageDialogFragment largeImageDialogFragment = new LargeImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantMsg.IMAGE_URL, imageUrl);
        largeImageDialogFragment.setArguments(bundle);
        largeImageDialogFragment.show(fragmentManager, "");
    }

}
