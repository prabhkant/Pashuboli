package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.AssociateHomeActivity;
import biding.animal.com.animalbiding.activities.ViewDoctorRequestActivity;
import biding.animal.com.animalbiding.activities.ViewEOBRequestActivity;
import biding.animal.com.animalbiding.activities.ViewPostCattleRequestActivity;
import biding.animal.com.animalbiding.model.AssociateFeatureModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class AssociatePermissionAdapter extends RecyclerView.Adapter<AssociatePermissionAdapter.CategoryVH> {

    private LayoutInflater mInflater;
    private List<AssociateFeatureModel> mUserRequestList;
    private ApplicationClass mInstance;
    private AssociateHomeActivity mActivity;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public AssociatePermissionAdapter(AssociateHomeActivity activity, List<AssociateFeatureModel> userRequestList) {
        mActivity = activity;
        mUserRequestList = userRequestList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        mInstance = ApplicationClass.getInstance();
    }

    @Override
    public CategoryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_associate_permission_feature, parent, false);
        return new CategoryVH(view);
    }

    @Override
    public void onBindViewHolder(final CategoryVH holder, final int position) {
        //set animation for itemview
        mInstance.setScaleAnimation(holder.itemView);
        //...
        if (mUserRequestList.get(position).getImg() != null && !mUserRequestList.get(position).getImg().isEmpty()) {
            imageLoader.displayImage(mUserRequestList.get(position).getImg(), holder.permissionImage, mInstance.displayImageOptions());
        }
        holder.featureName.setText(mUserRequestList.get(position).getFeatureName());
        holder.requestCount.setText(" Request: " + mUserRequestList.get(position).getCount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserRequestList.get(position).getFeatureId().equals("7")) { // cattle request
                    openPostCattleRequestActivity();
                } else if (mUserRequestList.get(position).getFeatureId().equals("4")) { // doctor request
                    openDoctorRequestActivity();
                } else if (mUserRequestList.get(position).getFeatureId().equals("3")) { // eob request
                    openEOBRequestActivity();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserRequestList.size();
    }

    //...
    private void openPostCattleRequestActivity() {
        Intent cattleReqIntent = new Intent(mActivity, ViewPostCattleRequestActivity.class);
        mActivity.startActivity(cattleReqIntent);
    }

    //...
    private void openEOBRequestActivity() {
        Intent cattleReqIntent = new Intent(mActivity, ViewEOBRequestActivity.class);
        mActivity.startActivity(cattleReqIntent);
    }

    //...
    private void openDoctorRequestActivity() {
        Intent cattleReqIntent = new Intent(mActivity, ViewDoctorRequestActivity.class);
        mActivity.startActivity(cattleReqIntent);
    }

    //class to hold the views
    class CategoryVH extends RecyclerView.ViewHolder {
        TextView featureName, requestCount;
        ImageView permissionImage;

        public CategoryVH(View itemView) {
            super(itemView);
            featureName = (TextView) itemView.findViewById(R.id.featureName);
            requestCount = (TextView) itemView.findViewById(R.id.requestCount);
            permissionImage = (ImageView) itemView.findViewById(R.id.permissionImage);
        }
    }

}
