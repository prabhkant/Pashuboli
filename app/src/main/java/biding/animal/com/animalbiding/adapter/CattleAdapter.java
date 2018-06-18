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
import biding.animal.com.animalbiding.activities.AnimalDetailActivity;
import biding.animal.com.animalbiding.activities.CattleActivity;
import biding.animal.com.animalbiding.model.CattleModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 03-02-2018.
 */

public class CattleAdapter extends RecyclerView.Adapter<CattleAdapter.CattleVH> {

    private List<CattleModel> mCattleList;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ApplicationClass mInsatance;
    private CattleActivity mActivity;

    public CattleAdapter(CattleActivity activity, List<CattleModel> cattleList) {
        mActivity = activity;
        mCattleList = cattleList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CattleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_cattle, parent, false);
        return new CattleVH(view);
    }

    @Override
    public void onBindViewHolder(CattleVH holder, final int position) {
        //...
        if (mCattleList.get(position).getImageName() != null && !mCattleList.get(position).getImageName().isEmpty()) {
            imageLoader.displayImage(mCattleList.get(position).getImageName(), holder.animalImage, mInsatance.displayImageOptions());
        }
        //...
        holder.breedName.setText(mCattleList.get(position).getBreedName());
        if (mCattleList.get(position).getPostDate() != null) {
            String[] dateTime = mCattleList.get(position).getPostDate().split(" ");
            holder.cattleDate.setText(dateTime[0]);
            holder.cattleTime.setText(dateTime[1].substring(0, dateTime[1].length() - 3));
        }
        holder.cattlePrice.setText(mCattleList.get(position).getPrice());

        //method call to animate views
        mInsatance.setScaleAnimation(holder.itemView);

        //click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.openAnimalDetailPage(mCattleList.get(position).getUserId(),mCattleList.get(position).getItemId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCattleList.size();
    }

    static class CattleVH extends RecyclerView.ViewHolder {
        TextView breedName, cattleDate, cattlePrice, cattleTime;
        ImageView animalImage;

        public CattleVH(View itemView) {
            super(itemView);
            breedName = (TextView) itemView.findViewById(R.id.breedName);
            cattleDate = (TextView) itemView.findViewById(R.id.cattleDate);
            cattlePrice = (TextView) itemView.findViewById(R.id.price);
            cattleTime = (TextView) itemView.findViewById(R.id.time);
            animalImage = (ImageView) itemView.findViewById(R.id.animalImage);
        }
    }
}
