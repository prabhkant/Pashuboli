package biding.animal.com.animalbiding.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.BiddingBaseActivity;
import biding.animal.com.animalbiding.activities.DoctorHomeActivity;
import biding.animal.com.animalbiding.model.AnimalCategoryModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.CircularImageClass;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class AnimalCategoryAdapter extends RecyclerView.Adapter<AnimalCategoryAdapter.CategoryVH> {

    private LayoutInflater mInflater;
    private List<AnimalCategoryModel> mCategoryList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ApplicationClass mInsatance;
    private Activity mActivity;

    public AnimalCategoryAdapter(Activity activity, List<AnimalCategoryModel> modelList) {
        mActivity = activity;
        mCategoryList = modelList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CategoryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_category, parent, false);
        return new CategoryVH(view);
    }

    @Override
    public void onBindViewHolder(final CategoryVH holder, final int position) {
        holder.categoryText.setText(mCategoryList.get(position).getAnimalName());
        if (mCategoryList.get(position).getAnimalImage() != null && !mCategoryList.get(position).getAnimalImage().isEmpty()) {
            //load image
            imageLoader.displayImage(mCategoryList.get(position).getAnimalImage(), holder.categoryImage, mInsatance.displayImageOptions());
        } else {
            holder.categoryImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.cow));
        }
        //set animation for itemview
        mInsatance.setScaleAnimation(holder.itemView);

        //click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApplicationClass.getInstance().getCurrentActivity() instanceof DoctorHomeActivity) {
                    ((DoctorHomeActivity) mActivity).openAnimalDetailPage(mCategoryList.get(position).getAnimalName(), mCategoryList.get(position).getAnimalId());
                }//
                else if (ApplicationClass.getInstance().getCurrentActivity() instanceof DoctorHomeActivity) {
                    ((BiddingBaseActivity) mActivity).openAnimalDetailPage(mCategoryList.get(position).getAnimalName(), mCategoryList.get(position).getAnimalId());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    //class to hold the views
    class CategoryVH extends RecyclerView.ViewHolder {
        CircularImageClass categoryImage;
        TextView categoryText;

        public CategoryVH(View itemView) {
            super(itemView);
            categoryImage = (CircularImageClass) itemView.findViewById(R.id.img_cat);
            categoryText = (TextView) itemView.findViewById(R.id.txt_cat);
        }
    }

}
