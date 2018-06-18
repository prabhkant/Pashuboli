package biding.animal.com.animalbiding.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.adapter.BreedFeaturesAdapter;
import biding.animal.com.animalbiding.adapter.PostCattleImageAdapter;
import biding.animal.com.animalbiding.model.AnimalFeatureModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.UtilityClass;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class PostCattleActivity extends AppCompatActivity implements VolleyResponseInterface, View.OnClickListener {

    private static final int SAVE_USER_POST_TAG = 6;
    private ProgressBar mProgressBar;
    private TextView mUploadPhotoBtn;
    private Spinner mCategorySpinner, mBreedSpinner, mStateSpinner, mDistrictSpinner, mAreaSpinner;
    private EditText mPrice, mAddress;
    private CheckBox mNegotiable;
    private LinearLayout mBreedLay, mDistrictLay, mAreaLay, mPostCattleLay;
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
    //-------------------------------------------------------------------
    //Specifications
    private LinearLayout mSpecificationLayout;
    private RecyclerView mFeatureListView;
    private BreedFeaturesAdapter mBreedFeaturesAdapter;

    private Dialog mPhotoOptionsDialog;
    private ArrayList<Bitmap> mImageList;
    private RecyclerView mPhotoListView;
    private PostCattleImageAdapter mImageAdapter;

//    private DialogInterface.OnDismissListener onDismissListener;
//
//    public void setOnPostCattleDismissListener(DialogInterface.OnDismissListener onDismissListener) {
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
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.post_cattle, container, false);
//
//        mInstance = ApplicationClass.getInstance();
//        initiateViews(view);
//        return view;
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_cattle);

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
        mPostCattleLay = (LinearLayout) findViewById(R.id.post_cattle_lay);
        mAddress = (EditText) findViewById(R.id.address);
        mPrice = (EditText) findViewById(R.id.price);
        mNegotiable = (CheckBox) findViewById(R.id.negotiable);
        mSpecificationLayout = (LinearLayout) findViewById(R.id.specificationLay);
        mFeatureListView = (RecyclerView) findViewById(R.id.breedFeatureListView);

        mImageList = new ArrayList<>();
        mUploadPhotoBtn = (TextView) findViewById(R.id.upload_btn);
        mPhotoListView = (RecyclerView) findViewById(R.id.img_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(PostCattleActivity.this, 2);  // 2 is no. of columns..
        mPhotoListView.setLayoutManager(gridLayoutManager);

        mUploadPhotoBtn.setOnClickListener(this);
        mPostCattleLay.setOnClickListener(this);
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PostCattleActivity.this, android.R.layout.simple_spinner_item, categoryArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mCategorySpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setBreedSpinner(List<String> breedArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PostCattleActivity.this, android.R.layout.simple_spinner_item, breedArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mBreedSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setStateSpinner(List<String> stateArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PostCattleActivity.this, android.R.layout.simple_spinner_item, stateArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mStateSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setDistrictSpinner(List<String> districtArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PostCattleActivity.this, android.R.layout.simple_spinner_item, districtArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mDistrictSpinner.setAdapter(arrayAdapter);
    }

    //method to set category spinner adapter
    private void setAreaSpinner(List<String> areaArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PostCattleActivity.this, android.R.layout.simple_spinner_item, areaArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mAreaSpinner.setAdapter(arrayAdapter);
    }

    //Method to set recycler adapter for specifications...
    private void setSpecificationFeatureAdapter(List<AnimalFeatureModel> featureModels) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(PostCattleActivity.this, LinearLayoutManager.VERTICAL, false);
        mFeatureListView.setLayoutManager(layoutManager);
        mBreedFeaturesAdapter = new BreedFeaturesAdapter(PostCattleActivity.this, featureModels);
        mFeatureListView.setAdapter(mBreedFeaturesAdapter);
    }

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
                            Toast.makeText(PostCattleActivity.this, R.string.no_category_available, Toast.LENGTH_LONG).show();
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
                        mBreedIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstAnimalSubCategoryModel");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mBreedIdArray.add(detailObj.getString("AnimalSubCatId"));
                                mBreedNameArray.add(detailObj.getString("SubCatName"));
                            }
                        } else {
                            Toast.makeText(PostCattleActivity.this, R.string.no_breed_available, Toast.LENGTH_LONG).show();
                        }
                        setBreedSpinner(mBreedNameArray);
                    }
                    //logic to get breed specifications
                    try {
                        if (object.has("lstAnimalSpecificationModel")) {
                            JSONArray jsonArray = object.getJSONArray("lstAnimalSpecificationModel");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                List<AnimalFeatureModel> animalFeatureModelList = new ArrayList<>();
                                Gson gson = new GsonBuilder().create();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject detailObj = jsonArray.getJSONObject(i);
                                    animalFeatureModelList.add(gson.fromJson(detailObj.toString(), AnimalFeatureModel.class));
                                }
                                mSpecificationLayout.setVisibility(View.VISIBLE);
                                setSpecificationFeatureAdapter(animalFeatureModelList);
                            } else {
                                mSpecificationLayout.setVisibility(View.GONE);
                            }
                        } else {
                            mSpecificationLayout.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        mSpecificationLayout.setVisibility(View.GONE);
                        e.printStackTrace();
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
                        mStateIdArray.add("ALL");
                        JSONArray jsonArray = object.getJSONArray("lstStateModelsModels");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                mStateIdArray.add(detailObj.getString("StateId"));
                                mStateNameArray.add(detailObj.getString("StateName"));
                            }
                        } else {
                            Toast.makeText(PostCattleActivity.this, R.string.no_state_available, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(PostCattleActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(PostCattleActivity.this, R.string.no_district_available, Toast.LENGTH_LONG).show();
                        }
                        setAreaSpinner(mAreaNameArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SAVE_USER_POST_TAG:
                mProgressBar.setVisibility(View.GONE);
                if (object != null) {
                    try {
                        String msg = object.getString("Message");
                        if (object.getString("Status").equals("1")) {
//                            dismiss();
                            finish();
                        }
                      Toast.makeText(PostCattleActivity.this,msg, Toast.LENGTH_LONG).show();
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
        Toast.makeText(PostCattleActivity.this,R.string.some_problem_occurred,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_cattle_lay:
                checkValidation();
                break;
            case R.id.cancel:
//                dismiss();
                break;
            case R.id.upload_btn:
                showDialogGalleryCamera();
                break;
        }
    }

    //...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == PostCattleActivity.this.RESULT_OK) {
                Bitmap bitmap = null;
                if (requestCode == ConstantMsg.REQUEST_CAMERA) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = UtilityClass.getCroppedBitmap(bitmap);
                } else if (requestCode == ConstantMsg.SELECT_GALLERY_FILE) {
                    ParcelFileDescriptor pfd;
                    try {
                        pfd = PostCattleActivity.this.getContentResolver().openFileDescriptor(data.getData(), "r");
                        FileDescriptor fd = pfd.getFileDescriptor();

                        bitmap = BitmapFactory.decodeFileDescriptor(fd);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 120, true);//aange added
                        bitmap = UtilityClass.getCroppedBitmap(bitmap);

                        pfd.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //...
                if (bitmap != null) {
                    setImage(bitmap);
                }
                super.onActivityResult(requestCode, resultCode, data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //--------------------------------------------------------------------------------------------------------------------
    //logic for image uploading
    private void showDialogGalleryCamera() {
        mPhotoOptionsDialog = new Dialog(PostCattleActivity.this);
        mPhotoOptionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) PostCattleActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_photo_options_popup, null);
        mPhotoOptionsDialog.setContentView(dialogView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mPhotoOptionsDialog.show();
        ImageView cameraBtn = (ImageView) mPhotoOptionsDialog.findViewById(R.id.camerabtnaan);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectImageforCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView galleryBtn = (ImageView) mPhotoOptionsDialog.findViewById(R.id.gallerybtnaan);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectImageForGallery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void selectImageforCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ConstantMsg.REQUEST_CAMERA);
    }

    void selectImageForGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    ConstantMsg.SELECT_GALLERY_FILE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    ConstantMsg.SELECT_GALLERY_FILE);
        }
    }

    //...
    public void setImage(Bitmap bitmap) {
        if (bitmap != null) {
            mImageList.add(bitmap);
            if (mImageAdapter == null) {
                mImageAdapter = new PostCattleImageAdapter(PostCattleActivity.this, mImageList, this);
                mPhotoListView.setAdapter(mImageAdapter);
            } else {
                mImageAdapter.notifyDataSetChanged();
            }
            if (mImageList.size() == 4) {
                mUploadPhotoBtn.setVisibility(View.GONE);
            }
        }
        mPhotoOptionsDialog.dismiss();
    }

    public void imageCancelClick() {
        if (mImageList != null && mImageList.size() < 4) {
            mUploadPhotoBtn.setVisibility(View.VISIBLE);
        }
    }

    //method to check validationn for posting cattle
    private void checkValidation() {
        String address, price;
        address = mAddress.getText().toString().trim();
        price = mPrice.getText().toString().trim();
        if (catId.equals("ALL")) {
            Toast.makeText(PostCattleActivity.this, R.string.select_category, Toast.LENGTH_LONG).show();
        } //
        else if (breedId.equals("ALL")) {
            Toast.makeText(PostCattleActivity.this, R.string.select_breed, Toast.LENGTH_LONG).show();
        } //
        else if (stateId.equals("ALL")) {
            Toast.makeText(PostCattleActivity.this, R.string.select_state, Toast.LENGTH_LONG).show();
        } //
        else if (districtId.equals("ALL")) {
            Toast.makeText(PostCattleActivity.this, R.string.select_district, Toast.LENGTH_LONG).show();
        } //
        else if (areaId.equals("ALL")) {
            Toast.makeText(PostCattleActivity.this, R.string.select_area, Toast.LENGTH_LONG).show();
        } //
        else if (address.isEmpty()) {
            mAddress.setError(getString(R.string.enter_address));
            mAddress.requestFocus();
        } //
        else if (price.isEmpty()) {
            mPrice.setError(getString(R.string.enter_price));
            mPrice.requestFocus();
        } //
        else if (mImageList != null && mImageList.size() <1) {
            Toast.makeText(PostCattleActivity.this, R.string.upload_photo_alert, Toast.LENGTH_LONG).show();
        } //
        else {
//            Toast.makeText(PostCattleActivity.this, "validation done", Toast.LENGTH_LONG).show();
            postCattleService(price);
        }

    }

    //method to get district category
    private void postCattleService(String price) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.SAVE_USER_POST, saveUserPostRequest(price), SAVE_USER_POST_TAG);
    }

    //...
    private JSONObject saveUserPostRequest (String price) {
        JSONObject object = new JSONObject();
        try {
            object.put("Fk_AnimalId",catId);
            object.put("Fk_StateId",stateId);
            object.put("Fk_CityId",districtId);
            object.put("Fk_AreaId",areaId);
            object.put("Fk_BreedId",breedId);
            object.put("Features",mBreedFeaturesAdapter.getmFeatureNameValues());
            object.put("Price",price);
//            object.put("ItemName","ItemName");
//            object.put("ItemDescription","ItemDescription");
            object.put("UserId", SharedPrefernceManger.getUserId());
            object.put("SpecificationIds",mBreedFeaturesAdapter.getmSpecificationNameValues());
            object.put("Images1",UtilityClass.convetBitmapToByteArray(mImageList.get(0)));
            try {
                object.put("Images2",UtilityClass.convetBitmapToByteArray(mImageList.get(1)));
                object.put("Images3",UtilityClass.convetBitmapToByteArray(mImageList.get(2)));
                object.put("Images4",UtilityClass.convetBitmapToByteArray(mImageList.get(3)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;

    }

}
