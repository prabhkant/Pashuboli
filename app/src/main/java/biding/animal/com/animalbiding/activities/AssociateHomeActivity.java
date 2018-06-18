package biding.animal.com.animalbiding.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.adapter.AssociatePermissionAdapter;
import biding.animal.com.animalbiding.model.AssociateFeatureModel;
import biding.animal.com.animalbiding.model.UserDetailModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.CircularImageClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.UtilityClass;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class AssociateHomeActivity extends AppCompatActivity implements VolleyResponseInterface, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int UPDATE_PICTURE_TAG = 102;
    private final int GET_ASSOCIATE_REQUEST_TAG = 1;
    private NavigationView navigationView;
    private TextView mName, mUserType;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private LinearLayout mContentLayout;
    private MenuItem mSignOut, mSignIn, mProfileUpdate, mProfileUpgrade, mMyPost, mBiddingHistory,
            mDoctor, mTotalRequest, mUserRequest, mGaushalaRequest, mFindCattle, mPostCattle;
    private ImageView mEditImage;
    private CircularImageClass mUserImg;
    private LinearLayout mUserImageEditLay;
    private Dialog mPhotoOptionsDialog;
    private static ProgressDialog mProgressDialog;

    //Home Screen Views-------------------------------------------------------------------------------------------------------------------
    private static final int GET_USER_DETAIL = 3;
    private RecyclerView associatePermissionList;
    AssociatePermissionAdapter permissionAdapter;
    private TextView associateName;
    private TextView associateDescription;
    private ApplicationClass mInstance;
    private List<UserDetailModel> userDetailModelList;
    private ProgressBar mProgressBar;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    ///-----------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_home);
        imageLoader.init(ImageLoaderConfiguration.createDefault(AssociateHomeActivity.this));
        initView();

    }

    //method to initiate views
    private void initView() {
        mInstance = ApplicationClass.getInstance();
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        mUserType = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_address);
        mEditImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.edit_image);
        mUserImageEditLay = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.userImageEditLay);
        mUserImg = (CircularImageClass) navigationView.getHeaderView(0).findViewById(R.id.user_imageView);
        mSignIn = (MenuItem) navigationView.getMenu().findItem(R.id.sign_in);
        mSignOut = (MenuItem) navigationView.getMenu().findItem(R.id.sign_out);
        mProfileUpgrade = (MenuItem) navigationView.getMenu().findItem(R.id.profile_upgrade);
        mProfileUpdate = (MenuItem) navigationView.getMenu().findItem(R.id.profile_update);
        mMyPost = (MenuItem) navigationView.getMenu().findItem(R.id.my_post);
        mBiddingHistory = (MenuItem) navigationView.getMenu().findItem(R.id.bidding_history);
        mDoctor = (MenuItem) navigationView.getMenu().findItem(R.id.doctor);
        mTotalRequest = (MenuItem) navigationView.getMenu().findItem(R.id.total_request);
        mUserRequest = (MenuItem) navigationView.getMenu().findItem(R.id.user_request);
        mGaushalaRequest = (MenuItem) navigationView.getMenu().findItem(R.id.gaushala);
        mFindCattle = (MenuItem) navigationView.getMenu().findItem(R.id.find_cattle);
        mPostCattle = (MenuItem) navigationView.getMenu().findItem(R.id.post_cattle);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //...
                showMenuItem();
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mUserImageEditLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefernceManger.getUserId().equals("")) {
                    goToLoginScreen();
                    Toast.makeText(AssociateHomeActivity.this, R.string.please_login_first, Toast.LENGTH_LONG).show();
                } else {
                    switch (view.getId()) {
                        case R.id.userImageEditLay:
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            showDialogGalleryCamera();
                            break;

                    }
                }
            }
        });

        //...
        homeScreenInitiateViews();

    }

    //Initiate views
    private void homeScreenInitiateViews() {
        mInstance = ApplicationClass.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        associatePermissionList = (RecyclerView) findViewById(R.id.associatePermissionList);
        associateName = (TextView) findViewById(R.id.associateName);
        associateDescription = (TextView) findViewById(R.id.associateDescription);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInstance.setCurrentActivity(this);
        if (!SharedPrefernceManger.getUserId().equalsIgnoreCase("")) {
            getUserDetail();
        }
        getAssociateRequest();
    }

    //method to get every user detail type
    private void getUserDetail() {
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_USER_DETAIL + "?userid=" + Integer.parseInt(SharedPrefernceManger.getUserId()), null, GET_USER_DETAIL);
    }

    //method to get notification
    private void getAssociateRequest() {  // status 1 for approve, 2 for reject
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(this, VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_ASSOCIATE_FEATURE + "?userid=" + SharedPrefernceManger.getUserId(), null, GET_ASSOCIATE_REQUEST_TAG);
    }

    //Method to set recycler adapter...
    private void setAssociateFeatureAdapter(List<AssociateFeatureModel> featureList) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        associatePermissionList.setLayoutManager(layoutManager);
        permissionAdapter = new AssociatePermissionAdapter(AssociateHomeActivity.this, featureList);
        associatePermissionList.setAdapter(permissionAdapter);
    }

    //hide menu items
    private void showUserMenu() {
        mMyPost.setVisible(false);
        mBiddingHistory.setVisible(false);
        mDoctor.setVisible(false);
        mTotalRequest.setVisible(false);
        mUserRequest.setVisible(true);
        mGaushalaRequest.setVisible(false);
        mFindCattle.setVisible(false);
        mPostCattle.setVisible(false);
        mProfileUpgrade.setVisible(false);
        mTotalRequest.setVisible(false);
    }

    //show menu items
    private void showMenuItem() {
        setUserNameImage();
        if (SharedPrefernceManger.getUserTypeId().equalsIgnoreCase("2")) { // associate
            showUserMenu();
        }
    }

    //method to set view
    public void setView(int layoutId) {
        View view = getLayoutInflater().inflate(layoutId, mContentLayout, false);
        mContentLayout.addView(view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                showDailogForSignout();
                break;
            case R.id.sign_in:
                Intent intent = new Intent(AssociateHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.cattle_query:
                break;
            case R.id.doctor:
                Intent doctorSearchIntent = new Intent(AssociateHomeActivity.this, DoctorSearchActivity.class);
                startActivity(doctorSearchIntent);
                break;
            case R.id.total_request:
                Intent requestIntent = new Intent(AssociateHomeActivity.this, TotalRequestActivity.class);
                startActivity(requestIntent);
                break;
            case R.id.gaushala:
                Intent gaushalaIntent = new Intent(AssociateHomeActivity.this, GaushalSearchActivity.class);
                startActivity(gaushalaIntent);
                break;
            case R.id.notifications:
                Intent notificationIntent = new Intent(AssociateHomeActivity.this, NotificationActivity.class);
                startActivity(notificationIntent);
                break;
            case R.id.offers:
                break;
            case R.id.share:
                break;
            case R.id.user_request:
                Intent userRequestIntent = new Intent(AssociateHomeActivity.this, UserRequestActivity.class);
                startActivity(userRequestIntent);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    //--------------------------------------------------------------------------------------------------------------------
    //logic for image uploading
    private void showDialogGalleryCamera() {
        mPhotoOptionsDialog = new Dialog(AssociateHomeActivity.this);
        mPhotoOptionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                if (mPhotoOptionsDialog != null && mPhotoOptionsDialog.isShowing()) {
                    mPhotoOptionsDialog.dismiss();
                }
                if (requestCode == ConstantMsg.REQUEST_CAMERA) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = UtilityClass.getCroppedBitmap(bitmap);
                    mUserImg.setImageBitmap(bitmap);
                } else if (requestCode == ConstantMsg.SELECT_GALLERY_FILE) {
                    ParcelFileDescriptor pfd;
                    try {
                        pfd = getContentResolver().openFileDescriptor(data.getData(), "r");
                        FileDescriptor fd = pfd.getFileDescriptor();

                        bitmap = BitmapFactory.decodeFileDescriptor(fd);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 120, true);//aange added
                        bitmap = UtilityClass.getCroppedBitmap(bitmap);
                        pfd.close();
                        mUserImg.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //call service to upload picture...
                if (bitmap != null) {
                    String byteCode = UtilityClass.convetBitmapToByteArray(bitmap);
                    uplaodPicture(byteCode);
                } else {
//                    Toast.makeText(AssociateHomeActivity.this, R.string.image_select_again, Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method to get state category
    private void uplaodPicture(String byteCode) {
        showProgressDialog();
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.UPDATE_PICTURE, getRequest(byteCode), UPDATE_PICTURE_TAG);
    }

    //...
    private JSONObject getRequest(String byteCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", SharedPrefernceManger.getUserId());
            object.put("Images", byteCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    //...
    private void showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(AssociateHomeActivity.this);
        mProgressDialog.setMessage("uploading image...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    //...
    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case GET_ASSOCIATE_REQUEST_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        if (object.has("lstAssociateCountForAPI") && object.getJSONArray("lstAssociateCountForAPI") != null) {
                            JSONArray detailArray = object.getJSONArray("lstAssociateCountForAPI");
                            if (detailArray.length() > 0) {
                                List<AssociateFeatureModel> postModelList = new ArrayList<>();
                                for (int i = 0; i < detailArray.length(); i++) {
                                    JSONObject jsonObject = detailArray.getJSONObject(i);
                                    postModelList.add(new GsonBuilder().create().fromJson(jsonObject.toString(), AssociateFeatureModel.class));
                                }
                                setAssociateFeatureAdapter(postModelList);
                            } else {
                                Toast.makeText(AssociateHomeActivity.this, getString(R.string.no_detail_found), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case UPDATE_PICTURE_TAG:
                dismissDialog();
                try {
                    if (object != null) {
                        String message = object.getString("Message");
                        String[] messageList = message.split(",");
                        Toast.makeText(AssociateHomeActivity.this, messageList[0], Toast.LENGTH_LONG).show();
                        getUserDetail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GET_USER_DETAIL:
                try {
                    Gson gson = new GsonBuilder().create();
                    if (object.has("lstUsersModel")) {
                        JSONArray jsonArray = object.getJSONArray("lstUsersModel");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            userDetailModelList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject detailObj = jsonArray.getJSONObject(i);
                                userDetailModelList.add(gson.fromJson(detailObj.toString(), UserDetailModel.class));
                            }
                            //setting user detail to application class
                            ApplicationClass.getInstance().setUserDetailModelList(userDetailModelList);
                            SharedPrefernceManger.setUserId(userDetailModelList.get(0).getUserId());
                            SharedPrefernceManger.setUserName(userDetailModelList.get(0).getUserName());
                            SharedPrefernceManger.setUserType(userDetailModelList.get(0).getUserTypeName());
                            SharedPrefernceManger.setUserTypId(userDetailModelList.get(0).getUserTypeId());
                        } else {
                            Toast.makeText(AssociateHomeActivity.this, R.string.no_user_detail, Toast.LENGTH_LONG).show();
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
        dismissDialog();
        Toast.makeText(AssociateHomeActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mProgressDialog = null;
    }

    //...
    public void showDailogForSignout() {
        // custom dialog
        final Dialog dialog = new Dialog(AssociateHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_for_signout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("");
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        TextView textViewDialog = (TextView) dialog.findViewById(R.id.editTextTipsEditDescription);
        TextView title = (TextView) dialog.findViewById(R.id.textViewDialogTitle);
        title.setText(R.string.app_name);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        textViewDialog.setText(R.string.sign_out_alert);
        TextView okButton = (TextView) dialog.findViewById(R.id.buttonOk);
        TextView cancelButton = (TextView) dialog.findViewById(R.id.buttonCancel);
        okButton.setText(R.string.yes);
        cancelButton.setText(R.string.cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SharedPrefernceManger.clearSharedPreference();
                setUserGuset();
            }
        });
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //...
    private void openUserDetailDialog(String userTypeName, String userTypeDesc) {
        final Dialog dialog = new Dialog(AssociateHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_type_deatil);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setWindowAnimations(R.style.CustomOTPDialog);

        TextView userType = (TextView) dialog.findViewById(R.id.user_type_name);
        TextView userDesc = (TextView) dialog.findViewById(R.id.user_type_desc);
        final TextView userOk = (TextView) dialog.findViewById(R.id.ok);
        userType.setText(userTypeName);
        userDesc.setText(userTypeDesc);

        userOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //----------------------------------------------------------------------------------------
    //...
    public void goToLoginScreen() {
        Intent intent = new Intent(AssociateHomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //--------------------------------------------------------------------------------------

    //method to set user detail
    private void setUserNameImage() {
        String userName = SharedPrefernceManger.getUserName();
        String userType = SharedPrefernceManger.getUserType();
        if (userName.equalsIgnoreCase("Guest")) {
            setUserGuset();
        } else {
            if (ApplicationClass.getInstance().getUserDetailModelList().get(0).getImages() != null
                    && !ApplicationClass.getInstance().getUserDetailModelList().get(0).getImages().isEmpty()) {
                //load image
                imageLoader.displayImage(ApplicationClass.getInstance().getUserDetailModelList().get(0).getImages(), mUserImg,
                        ApplicationClass.getInstance().displayImageOptions());
            } else {
                mUserImg.setImageDrawable(getResources().getDrawable(R.drawable.user));
            }
            mUserType.setText(userType);
            mName.setText(userName);
            mSignOut.setVisible(true);
            mSignIn.setVisible(false);
        }
    }

    private void setUserGuset() {
        mUserType.setText("UserType");
        mName.setText("Guest");
        mSignOut.setVisible(false);
        mSignIn.setVisible(true);
    }
}
