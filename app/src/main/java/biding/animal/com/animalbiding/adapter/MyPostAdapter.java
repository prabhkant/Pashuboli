package biding.animal.com.animalbiding.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.AnimalDetailActivity;
import biding.animal.com.animalbiding.activities.BiddingBaseActivity;
import biding.animal.com.animalbiding.activities.MyPostActivity;
import biding.animal.com.animalbiding.activities.ViewMyPostActivity;
import biding.animal.com.animalbiding.model.BiddingHistoryModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.CircularImageClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;

/**
 * Created by Prabhakant.Agnihotri on 03-02-2018.
 */

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.CattleVH> {

    private LayoutInflater mInflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ApplicationClass mInsatance;
    private Activity mActivity;
    private List<BiddingHistoryModel> biddingHistoryModelList;

    public MyPostAdapter(Activity activity,List<BiddingHistoryModel> biddingHistoryModelList) {
        mActivity = activity;
        this.biddingHistoryModelList = biddingHistoryModelList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        mInsatance = ApplicationClass.getInstance();
    }
    @Override
    public CattleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_bidding_histroy, parent, false);
        return new CattleVH(view);
    }

    @Override
    public void onBindViewHolder(CattleVH holder, final int position) {
        holder.lastbid.setVisibility(View.GONE);
        holder.animalName.setText(biddingHistoryModelList.get(position).getAnimalName());
        holder.postDate.setText("Post address: "+ biddingHistoryModelList.get(position).getPostDate());
        holder.animalCode.setText("Animal Code: " + biddingHistoryModelList.get(position).getItemCode());
        holder.status.setText("Status: "+ biddingHistoryModelList.get(position).getStatusName());
        holder.price.setText("Price: " + biddingHistoryModelList.get(position).getPrice());

        if (biddingHistoryModelList.get(position).getImage() != null && !biddingHistoryModelList.get(position).getImage().isEmpty()) {
            //load image
            imageLoader.displayImage(biddingHistoryModelList.get(position).getImage(),holder.animalImage,mInsatance.displayImageOptions());
        } else {
            holder.animalImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.cow));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewPostPage(biddingHistoryModelList.get(position).getAnimalId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return biddingHistoryModelList.size();
    }

    //method to open animal detail page
    public void openViewPostPage(String postId) {
        Intent intent = new Intent((MyPostActivity) mActivity, ViewMyPostActivity.class);
        intent.putExtra(ConstantMsg.POST_ID, postId);
        ((MyPostActivity) mActivity).startActivity(intent);
    }

    static class CattleVH extends RecyclerView.ViewHolder {
        TextView animalName, viewDetails, animalCode, postDate, status, price, lastbid;
        CircularImageClass animalImage;

        public CattleVH(View itemView) {
            super(itemView);
            animalName = (TextView) itemView.findViewById(R.id.animal_name);
            viewDetails = (TextView) itemView.findViewById(R.id.view_details);
            animalCode = (TextView) itemView.findViewById(R.id.animal_code);
            postDate = (TextView) itemView.findViewById(R.id.post_date);
            status = (TextView) itemView.findViewById(R.id.status);
            price = (TextView) itemView.findViewById(R.id.price);
            lastbid = (TextView) itemView.findViewById(R.id.last_bid);
            animalImage = (CircularImageClass) itemView.findViewById(R.id.img_cat);
        }
    }
}
