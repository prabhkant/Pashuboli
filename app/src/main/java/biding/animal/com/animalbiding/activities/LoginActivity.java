package biding.animal.com.animalbiding.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.UtilityClass;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseInterface {

    private static final int LOGIN_TAG = 1;
    private static final int FORGOT_PASS_CHECK_TAG = 2;
    private static final int VERIFY_TAG = 3;
    private static final int RESEND_TAG = 4;
    private static final int FORGOT_CHANGE_PASS = 5;
    private EditText mUserName, mPassword;
    private TextView mSignIn, mForgotPwd, mCreateAccount;
    private ProgressBar mProgressBar;
    private ApplicationClass mInstance;
    private Dialog forgotPasswordDialog, mOTPDialog, forgotChangePassDialog;
    private TextView mTimerDialog, mResendTvDialog;
    private EditText mFirstDigitDialog;
    private LinearLayout mResendOTPLayout;
    private CountDownTimer timer;
    private String forgotMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        mInstance = ApplicationClass.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mUserName = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_pwd);
        mSignIn = (TextView) findViewById(R.id.login_sign_in);
        mForgotPwd = (TextView) findViewById(R.id.login_forgot_pwd);
        mCreateAccount = (TextView) findViewById(R.id.login_create_accnt);

        mSignIn.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);
        mForgotPwd.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_sign_in:
                checkValidation();
                break;
            case R.id.login_create_accnt:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.login_forgot_pwd:
                openForgotPasswordDialog();
                break;
        }

    }

    //method to check validation on user input
    private void checkValidation() {
        String userName = mUserName.getText().toString();
        String userPass = mPassword.getText().toString();

        if (userName.isEmpty()) {
            mUserName.setError(getString(R.string.please_enter_username));
            mUserName.requestFocus();
        } else if (userPass.isEmpty()) {
            mPassword.setError(getString(R.string.please_enter_username));
            mPassword.requestFocus();
        } else {
            if (!UtilityClass.isConnected(LoginActivity.this)) {
                doLogin(userName, userPass);
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    //service to login
    private void doLogin(String username, String password) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
        volleyClient.hitWithOutTokenService(Constants.GET_LOGIN + "UserName=" + username + "&Password=" + password, null, LOGIN_TAG);
//        volleyClient.hitWithOutTokenService(Constants.GET_LOGIN , getRequest(username,password), LOGIN_TAG);
    }

    private JSONObject getRequest(String username, String password) {
        JSONObject object = new JSONObject();
        try {
            object.put("UserName", username);
            object.put("Password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    // Open forgot password dialog
    private void openForgotPasswordDialog() {
        forgotPasswordDialog = new Dialog(LoginActivity.this);
        forgotPasswordDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        forgotPasswordDialog.setContentView(R.layout.dialog_forgot_pwd_layout);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        forgotPasswordDialog.getWindow().setLayout(width, height);
        forgotPasswordDialog.getWindow().setWindowAnimations(R.style.CustomOTPDialog);
        forgotPasswordDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final AutoCompleteTextView mPhoneNumber = (AutoCompleteTextView) forgotPasswordDialog.findViewById(R.id.mobile_no);
        final TextInputLayout mobileLayout = (TextInputLayout) forgotPasswordDialog.findViewById(R.id.noLayout);
        ImageView cancel = (ImageView) forgotPasswordDialog.findViewById(R.id.cancel);
        TextView submit = (TextView) forgotPasswordDialog.findViewById(R.id.save);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotMobile = mPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(forgotMobile) || forgotMobile.length() < 10) {
                    mobileLayout.setError(getString(R.string.enter_valid_mob_no));
                } else {
                    mobileLayout.setErrorEnabled(false);
                    if (!UtilityClass.isConnected(LoginActivity.this)) {
                        forgotPassCheck(forgotMobile);
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        forgotPasswordDialog.setCanceledOnTouchOutside(false);
        forgotPasswordDialog.show();
    }

    // Open forgot password dialog
    private void openForgotChangePasswordDialog() {
        forgotChangePassDialog = new Dialog(LoginActivity.this);
        forgotChangePassDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        forgotChangePassDialog.setContentView(R.layout.forgot_change_pass_layout);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        forgotChangePassDialog.getWindow().setLayout(width, height);
        forgotChangePassDialog.getWindow().setWindowAnimations(R.style.CustomOTPDialog);
        forgotChangePassDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final AutoCompleteTextView newPassword = (AutoCompleteTextView) forgotChangePassDialog.findViewById(R.id.newPassword);
        final TextInputLayout passLayout = (TextInputLayout) forgotChangePassDialog.findViewById(R.id.passLayout);
        ImageView cancel = (ImageView) forgotChangePassDialog.findViewById(R.id.cancel);
        TextView submit = (TextView) forgotChangePassDialog.findViewById(R.id.save);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotChangePassDialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = newPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    passLayout.setError(getString(R.string.enter_valid_mob_no));
                } else {
                    passLayout.setErrorEnabled(false);
                    if (!UtilityClass.isConnected(LoginActivity.this)) {
                        forgotPassChange(password, forgotMobile);
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        forgotChangePassDialog.setCanceledOnTouchOutside(false);
        forgotChangePassDialog.show();
    }

    //service to login
    private void forgotPassChange(String passwrd, String phone) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(),
                VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.FORGOT_CHANGE_PASS, getForgotPassChangeReq(passwrd, phone), FORGOT_CHANGE_PASS);
    }

    //...
    private JSONObject getForgotPassChangeReq(String password, String phone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Password", password);
            jsonObject.put("Phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //service to login
    private void forgotPassCheck(String phone) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(),
                VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.FORGOT_PASS_CHECK, getForgotPassReq(phone), FORGOT_PASS_CHECK_TAG);
    }

    //...
    private JSONObject getForgotPassReq(String phone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //...
    private void openOtpDailog() {
        mOTPDialog = new Dialog(LoginActivity.this);
        mOTPDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mOTPDialog.setContentView(R.layout.otp_pop_layout);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mOTPDialog.getWindow().setLayout(width, height);
        mOTPDialog.getWindow().setWindowAnimations(R.style.CustomOTPDialog);

        mFirstDigitDialog = (EditText) mOTPDialog.findViewById(R.id.first_digit);
        TextView verifyOTP = (TextView) mOTPDialog.findViewById(R.id.verifyOTP);
        ImageView cancel = (ImageView) mOTPDialog.findViewById(R.id.cancel);
        mTimerDialog = (TextView) mOTPDialog.findViewById(R.id.time_count);
        mResendOTPLayout = (LinearLayout) mOTPDialog.findViewById(R.id.resend_otp);
        mResendTvDialog = (TextView) mOTPDialog.findViewById(R.id.resend_text);

        mResendOTPLayout.setEnabled(false);
        mResendTvDialog.setTextColor(LoginActivity.this.getResources().getColor(R.color.grey));
        setTimer();

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking for OTP entered and calling for valid OTP service...
                if (checkValidOTP(mFirstDigitDialog.getText().toString().trim())) {
                    String OTP = mFirstDigitDialog.getText().toString();
                    verifyOtp(OTP);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOTPDialog.dismiss();
            }
        });

        mResendOTPLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstDigitDialog.setText("");
                mFirstDigitDialog.requestFocus();
                mResendOTPLayout.setEnabled(false);
                mResendTvDialog.setTextColor(LoginActivity.this.getResources().getColor(R.color.grey));
                setTimer();
                // calling for resend OTP using text service...
                resendOtp();
            }
        });

        mOTPDialog.setCanceledOnTouchOutside(false);
        mOTPDialog.show();
    }

    //...
    private boolean checkValidOTP(String firstDigit) {
        if (TextUtils.isEmpty(firstDigit)) {
            Toast.makeText(LoginActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //...
    private void setTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                String timeToShow;
                long time = millisUntilFinished / 1000;
                if (time < 10) {
                    timeToShow = "0" + time;
                } else {
                    timeToShow = String.valueOf(time);
                }
                mTimerDialog.setText("00:" + timeToShow);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTimerDialog.setText("00:30");
                mResendOTPLayout.setEnabled(true);
                mResendTvDialog.setTextColor(LoginActivity.this.getResources().getColor(R.color.black));

            }
        };
        timer.start();
    }

    //...
    private void verifyOtp(String otp) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.VERIFY_OTP, getOtpRequest(otp), VERIFY_TAG);
    }

    //...
    private void resendOtp() {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.RESEND_OTP, getOtpRequest(""), RESEND_TAG);
    }

    //...
    //...
    // For getting OTP SMS
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null)
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    String message = intent.getStringExtra("message");
                    final String sender = intent.getStringExtra("sender");
                    message = message.replace(" ", "");
                    if (!TextUtils.isEmpty(message) && message.contains("PASHUB")) {
                        autoSubmitOTP(message);
                    }
                }
        }
    };

    //...
    // Auto submitting OTP
    private void autoSubmitOTP(String message) {
        String otp = message.substring(message.length() - 5); // Extracting OTP
        if (!TextUtils.isEmpty(otp)) {
            mFirstDigitDialog.setText(String.valueOf(otp));
            verifyOtp(otp);
        }
    }

    //...
    private JSONObject getOtpRequest(String otp) {
        JSONObject object = new JSONObject();
        try {
            object.put("UserId", SharedPrefernceManger.getUserId());
            if (!otp.equalsIgnoreCase("")) {
                object.put("OTP", otp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case LOGIN_TAG:
                try {
                    mProgressBar.setVisibility(View.GONE);
                    if (object != null) {
                        SharedPrefernceManger.setToken(object.getString("TokenName"));
                        if (object.get("Status").equals("valid")) {
                            if (object.has("UserDetails")) {
                                JSONObject userObject = object.getJSONObject("UserDetails");
                                SharedPrefernceManger.setUserId(userObject.getString("UserId"));
                                SharedPrefernceManger.setUserName(userObject.getString("FirstName"));
                                SharedPrefernceManger.setUserType(userObject.getString("UserTypeName"));
                                SharedPrefernceManger.setUserTypId(userObject.getString("UserTypeId"));
                            }
                            gotoHomeScreen();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.invalid_credentials, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case FORGOT_PASS_CHECK_TAG:
                try {
                    mProgressBar.setVisibility(View.GONE);
                    if (object != null) {
                        String msg = object.getString("Message");
                        if (object.getString("Status").equals("1")) {
                            SharedPrefernceManger.setUserId(object.getString("UserId"));
                            if (forgotPasswordDialog != null && forgotPasswordDialog.isShowing()) {
                                forgotPasswordDialog.dismiss();
                            }
                            openOtpDailog();
                        }
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RESEND_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        String message = object.getString("Message");
                        if (object.getString("Status").equalsIgnoreCase("1")) { // successful

                        } else { // failure
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case VERIFY_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        SharedPrefernceManger.setToken(object.getString("TokenName"));
                        if (object.get("Status").equals("valid")) { // successfully verified
                            if (object.has("UserDetails")) {
                                JSONObject userObject = object.getJSONObject("UserDetails");
                                SharedPrefernceManger.setUserId(userObject.getString("UserId"));
                                SharedPrefernceManger.setUserName(userObject.getString("FirstName"));
                                SharedPrefernceManger.setUserType(userObject.getString("UserTypeName"));
                                SharedPrefernceManger.setUserTypId(userObject.getString("UserTypeId"));
                            }
                            openForgotChangePasswordDialog();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.invalid_credentials, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case FORGOT_CHANGE_PASS:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        String msg = object.getString("Message");
                        if (object.get("Status").equals("1")) { // successfully change pass
                            gotoHomeScreen();
                            forgotChangePassDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //go to home screen
    private void gotoHomeScreen() {
        Intent homeIntent;
        if (SharedPrefernceManger.getUserTypeId().equalsIgnoreCase("2")) // Associate
            homeIntent = new Intent(LoginActivity.this, AssociateHomeActivity.class);
        else if (SharedPrefernceManger.getUserTypeId().equalsIgnoreCase("4")) // Doctor
            homeIntent = new Intent(LoginActivity.this, DoctorHomeActivity.class);
        else
            homeIntent = new Intent(LoginActivity.this, BiddingBaseActivity.class); // guest user and normal user
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
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
