package biding.animal.com.animalbiding.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

public class NewFilterFragment extends DialogFragment implements VolleyResponseInterface, View.OnClickListener {

    private static final int FILTER_TAG = 1;
    private RadioButton mLowToHigh, mHighToLow, mNewestFirst, mPopular;
    private LinearLayout mainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.filter_fragment_layout, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        mLowToHigh = (RadioButton) rootView.findViewById(R.id.low_to_high);
        mHighToLow = (RadioButton) rootView.findViewById(R.id.high_to_low);
        mNewestFirst = (RadioButton) rootView.findViewById(R.id.newest_first);
        mPopular = (RadioButton) rootView.findViewById(R.id.popular);
        mainLayout = (LinearLayout) rootView.findViewById(R.id.lay_filter);

        mainLayout.setOnClickListener(this);
        mLowToHigh.setOnClickListener(this);
        mHighToLow.setOnClickListener(this);
        mNewestFirst.setOnClickListener(this);
        mPopular.setOnClickListener(this);
    }

    private void getFilters() {
        VolleyInvokeWebService object = new VolleyInvokeWebService(getActivity(),
                VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case FILTER_TAG:
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_filter:
                dismiss();
                break;
            case R.id.low_to_high:
                dismiss();
                break;
            case R.id.high_to_low:
                dismiss();
                break;
            case R.id.newest_first:
                dismiss();
                break;
            case R.id.popular:
                dismiss();
                break;
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

}
