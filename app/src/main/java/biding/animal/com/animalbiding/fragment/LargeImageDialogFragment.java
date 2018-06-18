package biding.animal.com.animalbiding.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.ConstantMsg;


/**
 * Created by Prabhakant.Agnihotri on 08-01-2018.
 */

public class LargeImageDialogFragment extends DialogFragment implements View.OnClickListener {

    private Bitmap imageBitmap;
    private ImageView mImage;
    private LinearLayout mViewProfile;
    private byte[] imageByteArray;
    private String astroId;
    private String imageUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_large_image, container, false);
        try {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initiateViews(view);
        return view;
    }

    private void initiateViews(View view) {
        mImage = (ImageView) view.findViewById(R.id.large_astro_image);
        mViewProfile = (LinearLayout) view.findViewById(R.id.view_profile_lay);

        if (getArguments() != null) {
            imageUrl = getArguments().getString(ConstantMsg.IMAGE_URL);
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.cow)
                    .showImageOnFail(R.drawable.cow)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            imageLoader.displayImage(imageUrl, mImage, options);
        }

        mViewProfile.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

            WindowManager.LayoutParams param = getDialog().getWindow().getAttributes();
            param.y = 200;
            getDialog().getWindow().setAttributes(param);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.view_profile_lay:
//                openAstroProfile(imageUrl);
//                dismiss();
//                break;
        }
    }

    //...
    public void openAstroProfile(String imgUrl) {
//        try {
//            Intent astroProfileIntent = new Intent(getActivity(), AstroRateReviewDetailActivity.class);
//            astroProfileIntent.putExtra("astroid", String.valueOf(astroId));
//            astroProfileIntent.putExtra(Constants.BITMAP_BYTE_ARRAY, imageByteArray);
//            astroProfileIntent.putExtra(Constants.ASTRO_IMAGE_URL,imgUrl);
//            startActivityForResult(astroProfileIntent, 1);
//            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        } catch (Exception e) {
//            Intent astroProfileIntent = new Intent(getActivity(), AstroRateReviewDetailActivity.class);
//            astroProfileIntent.putExtra("astroid", String.valueOf(astroId));
//            astroProfileIntent.putExtra(Constants.ASTRO_IMAGE_URL,imgUrl);
//            startActivityForResult(astroProfileIntent, 1);
//            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            e.printStackTrace();
//        }
    }

}
