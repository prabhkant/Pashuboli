package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.TotalRequestActivity;
import biding.animal.com.animalbiding.model.TotalRequestModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class TotalRequestAdapter extends RecyclerView.Adapter<TotalRequestAdapter.CategoryVH> {

    private LayoutInflater mInflater;
    private List<TotalRequestModel> mTotalRequestList;
    private ApplicationClass mInsatance;
    private TotalRequestActivity mActivity;

    public TotalRequestAdapter(TotalRequestActivity activity, List<TotalRequestModel> totalRequestList) {
        mActivity = activity;
        mTotalRequestList = totalRequestList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CategoryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_total_request_layout, parent, false);
        return new CategoryVH(view);
    }

    @Override
    public void onBindViewHolder(final CategoryVH holder, final int position) {
        holder.totalReqTxt.setText(mTotalRequestList.get(position).getComments());
        holder.status.setText(mTotalRequestList.get(position).getStatus());
        holder.categoryName.setText(mTotalRequestList.get(position).getCatName());
        //set animation for itemview
        mInsatance.setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return mTotalRequestList.size();
    }

    //class to hold the views
    class CategoryVH extends RecyclerView.ViewHolder {
        TextView totalReqTxt, categoryName, status, action;

        public CategoryVH(View itemView) {
            super(itemView);
            totalReqTxt = (TextView) itemView.findViewById(R.id.requaetDes);
            categoryName = (TextView) itemView.findViewById(R.id.catName);
            status = (TextView) itemView.findViewById(R.id.status);
            action = (TextView) itemView.findViewById(R.id.action);
        }
    }

}
