package biding.animal.com.animalbiding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import biding.animal.com.animalbiding.R;

/**
 * Created by Prabhakant.Agnihotri on 06-03-2018.
 */

public class ProfileUpgradeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mCancel;
    private RadioButton mAssociateBtn, mDoctorBtn, mMerchantBtn, mEobBtn;

//    private DialogInterface.OnDismissListener onDismissListener;
//
//    public void setOnUpgradeDismissListener(DialogInterface.OnDismissListener onDismissListener) {
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
//        View view = inflater.inflate(R.layout.profile_upgrade, container, false);
//
//        initiateViews(view);
//        return view;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_upgrade);
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
        mCancel = (ImageView) findViewById(R.id.cancel);
        mAssociateBtn = (RadioButton) findViewById(R.id.associate);
        mDoctorBtn = (RadioButton) findViewById(R.id.doctor);
        mMerchantBtn = (RadioButton) findViewById(R.id.merchant);
        mEobBtn = (RadioButton) findViewById(R.id.eob);

        mCancel.setOnClickListener(this);
        mAssociateBtn.setOnClickListener(this);
        mDoctorBtn.setOnClickListener(this);
        mMerchantBtn.setOnClickListener(this);
        mEobBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
//                dismiss();
                break;
            case R.id.associate:
                openAssociateProfile();
                break;
            case R.id.doctor:
                openDoctorProfile();
                break;
            case R.id.merchant:
//                dismiss();
                break;
            case R.id.eob:
//                dismiss();
                break;
        }
    }

    //...
    private void openDoctorProfile() {
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        DoctorProfileActivity doctorProfileFragment = new DoctorProfileActivity();
//        doctorProfileFragment.show(manager, "doctorProfileFragment");
//        dismiss();
        Intent intent = new Intent(ProfileUpgradeActivity.this, DoctorProfileActivity.class);
        startActivity(intent);
    }

    //...
    private void openAssociateProfile() {
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        AssociateActivity associateFragment = new AssociateActivity();
//        associateFragment.show(manager, "AssociateActivity");
//        dismiss();
        Intent intent = new Intent(ProfileUpgradeActivity.this, AssociateActivity.class);
        startActivity(intent);
    }

}
