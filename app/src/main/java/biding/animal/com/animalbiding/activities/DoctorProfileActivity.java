package biding.animal.com.animalbiding.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.UtilityClass;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

/**
 * Created by Prabhakant.Agnihotri on 06-03-2018.
 */

public class DoctorProfileActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseInterface {

    private static final int UPGRADE_DOCTOR_ACCOUNT = 1;
    private static final int GET_DOCTOR_DETAIL_TAG = 2;
    private ApplicationClass mInstance;
    private Dialog mPhotoOptionsDialog;
    private ImageView mCancel, mUserImg, mPanImg, mAdharImg;
    private TextView mUploadAdahar, mUploadPanCard, mUploadPhoto, mSubmit;
    private boolean adharUpload, panUplaod, photoUpload;
    private EditText mClinicalAddress, mAlternativeNo, mFacebookId;
    private Spinner mSelectTypeSpinner, mExperinceSpinner, mQualificationSpinner;
    private String adharByteCode, panByteCode, photoByteCode;
    private ProgressBar mProgressBar;
    private List<String> doctortypeList, qualificationList, experienceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_lay);

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
        mUserImg = (ImageView) findViewById(R.id.user_img);
        mPanImg = (ImageView) findViewById(R.id.pan_img);
        mAdharImg = (ImageView) findViewById(R.id.adhar_img);
        mUploadPanCard = (TextView) findViewById(R.id.upload_pan_card);
        mUploadAdahar = (TextView) findViewById(R.id.upload_aadhar_card);
        mUploadPhoto = (TextView) findViewById(R.id.upload_photo);
        mClinicalAddress = (EditText) findViewById(R.id.clinic_address);
        mAlternativeNo = (EditText) findViewById(R.id.alternate_contact_no);
        mFacebookId = (EditText) findViewById(R.id.facebook_id);
        mSelectTypeSpinner = (Spinner) findViewById(R.id.select_type_spinner);
        mExperinceSpinner = (Spinner) findViewById(R.id.experince_spinner);
        mQualificationSpinner = (Spinner) findViewById(R.id.qualification_spinner);
        mSubmit = (TextView) findViewById(R.id.submit);

        mUploadAdahar.setOnClickListener(this);
        mUploadPanCard.setOnClickListener(this);
        mUploadPhoto.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        //...
        getDoctorDetail();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
//                dismiss();
                break;
            case R.id.upload_aadhar_card:
                adharUpload = true;
                panUplaod = false;
                photoUpload = false;
                showDialogGalleryCamera();
                break;
            case R.id.upload_pan_card:
                panUplaod = true;
                adharUpload = false;
                photoUpload = false;
                showDialogGalleryCamera();
                break;
            case R.id.upload_photo:
                photoUpload = true;
                adharUpload = false;
                panUplaod = false;
                showDialogGalleryCamera();
                break;
            case R.id.submit:
                checkValidation();
                break;
        }
    }

    private void checkValidation() {
        String strClinicalAddress, strAlternativeNo, strFacebookId, strSelectTypeSpinner,
                strExperinceSpinner, strQualificationSpinner;
        strClinicalAddress = mClinicalAddress.getText().toString().trim();
        strAlternativeNo = mAlternativeNo.getText().toString().trim();
        strFacebookId = mFacebookId.getText().toString().trim();
        strSelectTypeSpinner = mSelectTypeSpinner.getSelectedItem().toString();
        strExperinceSpinner = mExperinceSpinner.getSelectedItem().toString();
        strQualificationSpinner = mQualificationSpinner.getSelectedItem().toString();

        if (strSelectTypeSpinner.equalsIgnoreCase("Select Type")) {
            Toast.makeText(DoctorProfileActivity.this, R.string.select_type_of_doc, Toast.LENGTH_LONG).show();
        } //
        else if (strExperinceSpinner.equalsIgnoreCase("Experience")) {
            Toast.makeText(DoctorProfileActivity.this, R.string.select_exp, Toast.LENGTH_LONG).show();
        } //
        else if (strQualificationSpinner.equalsIgnoreCase("Qualification")) {
            Toast.makeText(DoctorProfileActivity.this, R.string.select_qualification, Toast.LENGTH_LONG).show();
        } //
        else if (strClinicalAddress.isEmpty()) {
            mClinicalAddress.setError(getString(R.string.enter_clinical_address));
            mClinicalAddress.requestFocus();
        } //
        else if (mAdharImg.getVisibility() == View.GONE) {
            Toast.makeText(DoctorProfileActivity.this, R.string.upload_adhar_card, Toast.LENGTH_LONG).show();
        } //
        else if (mPanImg.getVisibility() == View.GONE) {
            Toast.makeText(DoctorProfileActivity.this, R.string.upload_pan_card, Toast.LENGTH_LONG).show();
        } //
        else if (mUserImg.getVisibility() == View.GONE) {
            Toast.makeText(DoctorProfileActivity.this, R.string.upload_photo, Toast.LENGTH_LONG).show();
        } //
        else {
            upgradeDoctorAccount(strFacebookId, strAlternativeNo, strQualificationSpinner, strExperinceSpinner, strSelectTypeSpinner
                    , strClinicalAddress, panByteCode, adharByteCode, photoByteCode);
        }
    }

    //call service to get detail of doctor
    private void upgradeDoctorAccount(String facebookId, String altContactNo, String qualification, String exp, String docType,
                                      String clinicAddress, String panPhoto, String adharPhoto, String photo) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(),
                VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.UPGRADE_DOCTOR_PROFILE, getRequest(facebookId, altContactNo, qualification,
                exp, docType, clinicAddress, panPhoto, adharPhoto, photo), UPGRADE_DOCTOR_ACCOUNT);
    }

    //...
    private JSONObject getRequest(String facebookId, String altContactNo, String qualification, String exp, String docType,
                                  String clinicAddress, String panPhoto, String adharPhoto, String photo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FaceBookId", facebookId);
            jsonObject.put("AltContactNumaber", altContactNo);
            jsonObject.put("Qualification", qualification);
            jsonObject.put("Experience", exp);
            jsonObject.put("DoctorType", docType);
            jsonObject.put("UserId", SharedPrefernceManger.getUserId());
            jsonObject.put("ClinicAddress", clinicAddress);
            jsonObject.put("PanCard", panPhoto);
            jsonObject.put("AdharCard", adharPhoto);
            jsonObject.put("FilePhoto", photo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    //...
    private void getDoctorDetail() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(),
                VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_DOCTOR_DETAIL, null, GET_DOCTOR_DETAIL_TAG);
    }

    //logic for image uploading
    private void showDialogGalleryCamera() {
        mPhotoOptionsDialog = new Dialog(DoctorProfileActivity.this);
        mPhotoOptionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) DoctorProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_photo_options_popup, null);
        mPhotoOptionsDialog.setContentView(dialogView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

    //....
    private void selectImageforCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ConstantMsg.REQUEST_CAMERA);
    }

    //...
    private void selectImageForGallery() {
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == DoctorProfileActivity.this.RESULT_OK) {
                Bitmap bitmap = null;
                if (requestCode == ConstantMsg.REQUEST_CAMERA) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = UtilityClass.getCroppedBitmap(bitmap);
                } else if (requestCode == ConstantMsg.SELECT_GALLERY_FILE) {
                    ParcelFileDescriptor pfd;
                    try {
                        pfd = DoctorProfileActivity.this.getContentResolver().openFileDescriptor(data.getData(), "r");
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
                //call service to upload picture...
                if (bitmap != null) {
                    setImage(bitmap);
                } else {
//                    Toast.makeText(DoctorProfileActivity.this, R.string.image_select_again, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //...
    private void setImage(Bitmap bitmap) {
        if (adharUpload) {
            mAdharImg.setImageBitmap(bitmap);
            mAdharImg.setVisibility(View.VISIBLE);
            adharByteCode = UtilityClass.convetBitmapToByteArray(bitmap);
        } else if (panUplaod) {
            mPanImg.setImageBitmap(bitmap);
            mPanImg.setVisibility(View.VISIBLE);
            panByteCode = UtilityClass.convetBitmapToByteArray(bitmap);
        } else if (photoUpload) {
            mUserImg.setImageBitmap(bitmap);
            mUserImg.setVisibility(View.VISIBLE);
            photoByteCode = UtilityClass.convetBitmapToByteArray(bitmap);
        }
        // set false to variables
        adharUpload = false;
        panUplaod = false;
        photoUpload = false;
        mPhotoOptionsDialog.dismiss();
    }

    //...
    private void setAdapters(List<String> doctortypeList, List<String> experienceList, List<String> qualificationList) {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(DoctorProfileActivity.this, android.R.layout.simple_spinner_item, doctortypeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSelectTypeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String> expAdapter = new ArrayAdapter<String>(DoctorProfileActivity.this, android.R.layout.simple_spinner_item, experienceList);
        expAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mExperinceSpinner.setAdapter(expAdapter);

        ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<String>(DoctorProfileActivity.this, android.R.layout.simple_spinner_item, qualificationList);
        qualificationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mQualificationSpinner.setAdapter(qualificationAdapter);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_DOCTOR_DETAIL_TAG:
                if (object != null) {
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        doctortypeList = new ArrayList<>();
                        doctortypeList.add("Select Type");
                        experienceList = new ArrayList<>();
                        experienceList.add("Experience");
                        qualificationList = new ArrayList<>();
                        qualificationList.add("Qualification");

                        if (object.has("lstSelectType")) {
                            JSONArray jsonArray = object.getJSONArray("lstSelectType");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    doctortypeList.add(jsonObject.getString("SelectTypeValue"));
                                }
                            } else {
                                Toast.makeText(DoctorProfileActivity.this, "no select type found", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (object.has("lstExperience")) {
                            if (object.has("lstExperience")) {
                                JSONArray jsonArray = object.getJSONArray("lstExperience");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        experienceList.add(jsonObject.getString("ExperienceName"));
                                    }
                                } else {
                                    Toast.makeText(DoctorProfileActivity.this, "no experience found", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        if (object.has("lstQualification")) {
                            if (object.has("lstQualification")) {
                                JSONArray jsonArray = object.getJSONArray("lstQualification");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        qualificationList.add(jsonObject.getString("QualificationName"));
                                    }
                                } else {
                                    Toast.makeText(DoctorProfileActivity.this, "no experience found", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        // method call to set adapters
                        setAdapters(doctortypeList, experienceList, qualificationList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case UPGRADE_DOCTOR_ACCOUNT:
                if (object != null) {
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        String msg = object.getString("Message");
                        if (object.getString("Status").equalsIgnoreCase("1")) {
//                            dismiss();
                            finish();
                        }
                        Toast.makeText(DoctorProfileActivity.this,msg,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(DoctorProfileActivity.this,R.string.some_problem_occurred,Toast.LENGTH_LONG).show();
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

    }

}
