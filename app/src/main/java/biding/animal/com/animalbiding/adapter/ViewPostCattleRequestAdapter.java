package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.ViewPostCattleDetailRequestActivity;
import biding.animal.com.animalbiding.model.ViewPostCattleRequestModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class ViewPostCattleRequestAdapter extends RecyclerView.Adapter<ViewPostCattleRequestAdapter.CattleRequestVH> {

    private LayoutInflater mInflater;
    private List<ViewPostCattleRequestModel> mRequestModelList;
    private ApplicationClass mInsatance;
    private Context mContext;

    public ViewPostCattleRequestAdapter(Context context, List<ViewPostCattleRequestModel> requestModel) {
        mContext = context;
        mRequestModelList = requestModel;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CattleRequestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_post_cattle_request, parent, false);
        return new CattleRequestVH(view);
    }

    @Override
    public void onBindViewHolder(final CattleRequestVH holder, final int position) {
        //set animation for itemview
        mInsatance.setScaleAnimation(holder.itemView);

        holder.animalCode.setText(mRequestModelList.get(position).getItemCode());
        holder.animalName.setText(mRequestModelList.get(position).getAnimalName());
        holder.breedName.setText(mRequestModelList.get(position).getSubCatName());
        holder.date.setText(mRequestModelList.get(position).getPostDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPostCattleDetailRequestActivity(Integer.parseInt(mRequestModelList.get(position).getItemId()));
            }
        });
    }

    //...
    private void openPostCattleDetailRequestActivity(int itemId) {
        Intent cattleDetailIntent = new Intent(mContext, ViewPostCattleDetailRequestActivity.class);
        cattleDetailIntent.putExtra(ConstantMsg.ITEM_ID, itemId);
        mContext.startActivity(cattleDetailIntent);
    }

    @Override
    public int getItemCount() {
        return mRequestModelList.size();
    }

    //class to hold the views
    class CattleRequestVH extends RecyclerView.ViewHolder {
        TextView animalCode, animalName, breedName, date;

        public CattleRequestVH(View itemView) {
            super(itemView);
            animalCode = (TextView) itemView.findViewById(R.id.animal_code);
            animalName = (TextView) itemView.findViewById(R.id.animal_name);
            breedName = (TextView) itemView.findViewById(R.id.breed_name);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }

}
