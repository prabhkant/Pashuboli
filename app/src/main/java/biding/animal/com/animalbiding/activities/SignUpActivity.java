package biding.animal.com.animalbiding.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseInterface {

    private static final int SIGN_UP_TAG = 1;
    private static final int VERIFY_TAG = 2;
    private static final int RESEND_TAG = 3;
    private EditText mUserName, mPassword, mConfirmPassword, mMobileNumber;
    private TextView mSignUp, mSignIn;
    private ProgressBar mProgressBar;
    private ApplicationClass mInstance;
    private EditText mFirstDigitDialog;
    private Dialog mOTPDialog;
    private TextView mTimerDialog, mResendTvDialog;
    private LinearLayout mResendOTPLayout;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
    }

    private void initViews() {
        mInstance = ApplicationClass.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mUserName = (EditText) findViewById(R.id.sign_name);
        mPassword = (EditText) findViewById(R.id.sign_password);
        mConfirmPassword = (EditText) findViewById(R.id.sign_confirm_pwd);
        mMobileNumber = (EditText) findViewById(R.id.sign_mobileno);
        mSignIn = (TextView) findViewById(R.id.sign_login);
        mSignUp = (TextView) findViewById(R.id.sign_register);

        mSignUp.setOnClickListener(this);
        mSignIn.setOnClickListener(this);

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
            case R.id.sign_register:
                checkValidation();
                break;
            case R.id.sign_login:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    //service to login
    private void signUp(String username, String password, String phone) {
        mProgressBar.setVisibility(View.VISIBLE);
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.ADD_USER, getRequest(username, password, phone), SIGN_UP_TAG);
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
    private void openOtpDailog() {
        mOTPDialog = new Dialog(SignUpActivity.this);
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
        mResendTvDialog.setTextColor(SignUpActivity.this.getResources().getColor(R.color.grey));
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
                mResendTvDialog.setTextColor(SignUpActivity.this.getResources().getColor(R.color.grey));
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
            Toast.makeText(SignUpActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
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
                mResendTvDialog.setTextColor(SignUpActivity.this.getResources().getColor(R.color.black));

            }
        };
        timer.start();
    }

    //...
    private JSONObject getRequest(String username, String password, String phone) {
        JSONObject object = new JSONObject();
        try {
            object.put("Phone", phone);
            object.put("Password", password);
            object.put("FirstName", username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
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

    //go to home screen
    private void gotoHomeScreen() {
        Intent homeIntent = new Intent(SignUpActivity.this, BiddingBaseActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
    }

    //method to check validation on user input
    private void checkValidation() {
        String userName = mUserName.getText().toString();
        String userPass = mPassword.getText().toString();
        String userCnfPass = mConfirmPassword.getText().toString();
        String userMobile = mMobileNumber.getText().toString();

        if (userName.isEmpty()) {
            mUserName.setError(getString(R.string.please_enter_username));
            mUserName.requestFocus();
        } else if (userMobile.isEmpty()) {
            mMobileNumber.setError(getString(R.string.enter_mobile_no));
            mMobileNumber.requestFocus();
        } else if (userMobile.length() < 10) {
            mMobileNumber.setError(getString(R.string.enter_valid_mob_no));
            mMobileNumber.requestFocus();
        } else if (userPass.isEmpty()) {
            mPassword.setError(getString(R.string.enter_pass));
            mPassword.requestFocus();
        } else if (userCnfPass.isEmpty()) {
            mConfirmPassword.setError(getString(R.string.enter_cnf_pass));
            mConfirmPassword.requestFocus();
        } else if (!userCnfPass.equals(userPass)) {
            mConfirmPassword.setError(getString(R.string.pass_does_nt_match));
            mConfirmPassword.requestFocus();
        } else {
            signUp(userName, userPass, userMobile);
        }
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case SIGN_UP_TAG:
                mProgressBar.setVisibility(View.GONE);
                try {
                    if (object != null) {
                        String message = object.getString("Message");
                        if (object.getString("Status").equalsIgnoreCase("1")) { // first time successful
                            String userId = object.getString("UserId");
                            SharedPrefernceManger.setUserId(userId);
                            openOtpDailog();
                        } else if (object.getString("Status").equalsIgnoreCase("2")) { // already registered
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                        } else if (object.getString("Status").equalsIgnoreCase("3")) { // registered but not verified
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                            String userId = object.getString("UserId");
                            SharedPrefernceManger.setUserId(userId);
                            resendOtp();
                        } else {
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
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
                            openOtpDailog();
                        } else { // failure
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
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
                            gotoHomeScreen();
                        } else {
                            Toast.makeText(SignUpActivity.this, R.string.invalid_credentials, Toast.LENGTH_LONG).show();
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
        mProgressBar.setVisibility(View.GONE);
    }

}
