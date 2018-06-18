package biding.animal.com.animalbiding.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.ConstantMsg;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;
import biding.animal.com.animalbiding.utilities.UtilityClass;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;
import biding.animal.com.animalbiding.utilities.VolleyResponseInterface;

/**
 * Created by Prabhakant.Agnihotri on 03-05-2018.
 */

public class UplaodGaushalaPhotoQueryDialog extends DialogFragment implements View.OnClickListener, VolleyResponseInterface {

    private static final int SAVE_GAUSHALA_QUERY = 1;
    private ImageView dismiss, gaushalaImage;
    private TextView uploadPhoto, submitBtn;
    private EditText comments;
    private Dialog mPhotoOptionsDialog;
    private String gaushalaByteCode;

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnUploadPhotoDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_gaushala_photo_query, container, false);

        initiateViews(view);
        return view;
    }

    //....
    private void initiateViews(View view) {
        dismiss = (ImageView) view.findViewById(R.id.dismiss);
        gaushalaImage = (ImageView) view.findViewById(R.id.gaushalaImage);
        uploadPhoto = (TextView) view.findViewById(R.id.uploadPhoto);
        submitBtn = (TextView) view.findViewById(R.id.submitBtn);
        comments = (EditText) view.findViewById(R.id.comments);

        submitBtn.setOnClickListener(this);
        uploadPhoto.setOnClickListener(this);
        dismiss.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
//        if (dialog != null) {
//            getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
//
//            WindowManager.LayoutParams param = getDialog().getWindow().getAttributes();
//            param.y = 200;
//            getDialog().getWindow().setAttributes(param);
//        }
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    //...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == getActivity().RESULT_OK) {
                Bitmap bitmap = null;
                if (requestCode == ConstantMsg.REQUEST_CAMERA) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = UtilityClass.getCroppedBitmap(bitmap);
                } else if (requestCode == ConstantMsg.SELECT_GALLERY_FILE) {
                    ParcelFileDescriptor pfd;
                    try {
                        pfd = getActivity().getContentResolver().openFileDescriptor(data.getData(), "r");
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //...
    private void setImage(Bitmap bitmap) {
        gaushalaImage.setImageBitmap(bitmap);
        gaushalaImage.setVisibility(View.VISIBLE);
        gaushalaByteCode = UtilityClass.convetBitmapToByteArray(bitmap);
        mPhotoOptionsDialog.dismiss();
    }


    //logic for image uploading
    private void showDialogGalleryCamera() {
        mPhotoOptionsDialog = new Dialog(getActivity());
        mPhotoOptionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadPhoto:
                showDialogGalleryCamera();
                break;
            case R.id.submitBtn:
                if (gaushalaImage.getVisibility() == View.VISIBLE)
                    saveQueryPhoto(gaushalaByteCode, comments.getText().toString().trim());
                else
                    Toast.makeText(getActivity(), "Please upload photo", Toast.LENGTH_LONG).show();
                break;
            case R.id.dismiss:
                dismiss();
                break;
        }
    }

    //...
    private void saveQueryPhoto(String imageData, String comment) {
        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(getActivity(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.POST);
        volleyClient.hitWithOutTokenService(Constants.SAVE_GAUSHALA_QUERY, getGaushalaRequest(imageData, comment), SAVE_GAUSHALA_QUERY);
    }

    //...
    private JSONObject getGaushalaRequest(String imageData, String comment) {
        JSONObject object = new JSONObject();
        try {
            object.put("Comments", comment);
            object.put("ImageData", imageData);
            object.put("UserId", SharedPrefernceManger.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void onJsonResponse(JSONObject object, int tag) {
        switch (tag) {
            case SAVE_GAUSHALA_QUERY:
                if (object != null) {
                    try {
                        if (object.getString("Status").equalsIgnoreCase("1")) {
                            dismiss();
                        }
                        String msg = object.getString("Message");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismiss();
                        Toast.makeText(getActivity(), R.string.some_problem_occurred, Toast.LENGTH_LONG).show();
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
