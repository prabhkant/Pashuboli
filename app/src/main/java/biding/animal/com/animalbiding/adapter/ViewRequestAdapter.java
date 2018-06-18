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
import biding.animal.com.animalbiding.activities.EOBDetailActivity;
import biding.animal.com.animalbiding.activities.ViewDoctorDetailActivity;
import biding.animal.com.animalbiding.model.RequestModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;
import biding.animal.com.animalbiding.utilities.ConstantMsg;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class ViewRequestAdapter extends RecyclerView.Adapter<ViewRequestAdapter.RequestVH> {

    private LayoutInflater mInflater;
    private List<RequestModel> mRequestModelList;
    private ApplicationClass mInsatance;
    private Context mContext;
    private boolean mFromDoctor;

    public ViewRequestAdapter(Context context, List<RequestModel> requestModel, boolean fromDoctor) {
        mContext = context;
        mRequestModelList = requestModel;
        mFromDoctor = fromDoctor;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public RequestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_eob_request, parent, false);
        return new RequestVH(view);
    }

    @Override
    public void onBindViewHolder(final RequestVH holder, final int position) {
        //set animation for itemview
        mInsatance.setScaleAnimation(holder.itemView);
        holder.address.setText(mRequestModelList.get(position).getAddress());
        holder.name.setText(mRequestModelList.get(position).getFirstName());
        holder.phone.setText(mRequestModelList.get(position).getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFromDoctor) {
                    openDoctorDetailActivity(mRequestModelList.get(position).getRequestId());
                } else {

                }
            }
        });
    }

    //...
    private void openDoctorDetailActivity(int itemId) {
        Intent cattleDetailIntent = new Intent(mContext, ViewDoctorDetailActivity.class);
        cattleDetailIntent.putExtra(ConstantMsg.REQUEST_ID, itemId);
        mContext.startActivity(cattleDetailIntent);
    }

    @Override
    public int getItemCount() {
        return mRequestModelList.size();
    }

    //class to hold the views
    class RequestVH extends RecyclerView.ViewHolder {
        TextView name, phone, address;

        public RequestVH(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

}
