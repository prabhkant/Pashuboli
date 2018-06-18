package biding.animal.com.animalbiding.adapter;

import android.content.Context;
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
import biding.animal.com.animalbiding.activities.AnimalDetailActivity;
import biding.animal.com.animalbiding.model.AnimalDetailImages;
import biding.animal.com.animalbiding.model.AnimalSpecificationDetailModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 03-02-2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.CattleVH> {

    private LayoutInflater mInflater;
    private ApplicationClass mInsatance;
    private List<AnimalDetailImages> mImageModelList;
    private AnimalDetailActivity mActivity;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ImageAdapter(AnimalDetailActivity activity, List<AnimalDetailImages> imageModelList) {
        mActivity = activity;
        mImageModelList = imageModelList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CattleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_image_lay, parent, false);
        return new CattleVH(view);
    }

    @Override
    public void onBindViewHolder(CattleVH holder, final int position) {
        if (mImageModelList.get(position).getImageName() != null && !mImageModelList.get(position).getImageName().isEmpty()) {
            imageLoader.displayImage(mImageModelList.get(position).getImageName(), holder.cattleImg, mInsatance.displayImageOptions());
        }

        holder.dialogDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageModelList.size();
    }

    static class CattleVH extends RecyclerView.ViewHolder {
        ImageView cattleImg,dialogDismiss;

        public CattleVH(View itemView) {
            super(itemView);
            cattleImg = (ImageView) itemView.findViewById(R.id.img_card);
            dialogDismiss = (ImageView) itemView.findViewById(R.id.img_cancel_iv);

        }
    }
}
