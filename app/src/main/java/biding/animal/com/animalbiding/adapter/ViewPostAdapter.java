package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.ViewMyPostActivity;
import biding.animal.com.animalbiding.model.BidderBidModel;
import biding.animal.com.animalbiding.model.ViewPostModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 03-02-2018.
 */

public class ViewPostAdapter extends RecyclerView.Adapter<ViewPostAdapter.ViewPostVH> {

    private LayoutInflater mInflater;
    private ApplicationClass mInsatance;
    private List<ViewPostModel> mViewPostModelList;
    private ViewMyPostActivity mActivity;
    private boolean selectBid[];

    public ViewPostAdapter(ViewMyPostActivity activity, List<ViewPostModel> viewPostModelList) {
        mActivity = activity;
        mViewPostModelList = viewPostModelList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
        selectBid = new boolean[mViewPostModelList.size()];
        for (int i = 0; i < mViewPostModelList.size(); i++) {
            selectBid[i] = false;
        }
    }

    @Override
    public ViewPostVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_bidder_bid_list_layout, parent, false);
        return new ViewPostVH(view);
    }

    @Override
    public void onBindViewHolder(ViewPostVH holder, final int position) {
        BidderBidModel bidderBidModel = mViewPostModelList.get(position).getLstAnimalBiddingPricesForAPIS().get(0);

        holder.bidCheck.setChecked(selectBid[position]);
        holder.bidCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    selectBid[position] = true;
                } else {
                    selectBid[position] = false;
                }
            }
        });
        setBidValues(holder, bidderBidModel);
    }

    //...
    private void hideAllBid(ViewPostVH holder) {
        holder.firstBidLay.setVisibility(View.GONE);
        holder.secondBidLay.setVisibility(View.GONE);
        holder.thirdBidLay.setVisibility(View.GONE);
        holder.fourthBidLay.setVisibility(View.GONE);
        holder.fifthBidLay.setVisibility(View.GONE);

        holder.line1.setVisibility(View.GONE);
        holder.line2.setVisibility(View.GONE);
        holder.line3.setVisibility(View.GONE);
        holder.line4.setVisibility(View.GONE);
        holder.line5.setVisibility(View.GONE);

    }

    //...
    private void setBidValues(ViewPostVH holder, BidderBidModel bidderBidModel) {
        //...
        hideAllBid(holder);

        if (!bidderBidModel.getPrice1().isEmpty()) {
            holder.firstBidLay.setVisibility(View.VISIBLE);
            holder.firstBidValue.setText(bidderBidModel.getPrice1());
            holder.line1.setVisibility(View.VISIBLE);
        } //
        if (!bidderBidModel.getPrice2().isEmpty()) {
            holder.firstBidLay.setVisibility(View.VISIBLE);
            holder.secondBidLay.setVisibility(View.VISIBLE);
            holder.firstBidValue.setText(bidderBidModel.getPrice1());
            holder.secondBidValue.setText(bidderBidModel.getPrice2());
            holder.line1.setVisibility(View.VISIBLE);
            holder.line2.setVisibility(View.VISIBLE);
        } //
        if (!bidderBidModel.getPrice3().isEmpty()) {
            holder.firstBidLay.setVisibility(View.VISIBLE);
            holder.secondBidLay.setVisibility(View.VISIBLE);
            holder.thirdBidLay.setVisibility(View.VISIBLE);
            holder.firstBidValue.setText(bidderBidModel.getPrice1());
            holder.secondBidValue.setText(bidderBidModel.getPrice2());
            holder.thirdBidValue.setText(bidderBidModel.getPrice3());
            holder.line1.setVisibility(View.VISIBLE);
            holder.line2.setVisibility(View.VISIBLE);
            holder.line3.setVisibility(View.VISIBLE);
        } //
        if (!bidderBidModel.getPrice4().isEmpty()) {
            holder.firstBidLay.setVisibility(View.VISIBLE);
            holder.secondBidLay.setVisibility(View.VISIBLE);
            holder.thirdBidLay.setVisibility(View.VISIBLE);
            holder.fourthBidLay.setVisibility(View.VISIBLE);
            holder.firstBidValue.setText(bidderBidModel.getPrice1());
            holder.secondBidValue.setText(bidderBidModel.getPrice2());
            holder.thirdBidValue.setText(bidderBidModel.getPrice3());
            holder.fourthBidValue.setText(bidderBidModel.getPrice4());
            holder.line1.setVisibility(View.VISIBLE);
            holder.line2.setVisibility(View.VISIBLE);
            holder.line3.setVisibility(View.VISIBLE);
            holder.line4.setVisibility(View.VISIBLE);
        } //
        if (!bidderBidModel.getPrice5().isEmpty()) {
            holder.firstBidLay.setVisibility(View.VISIBLE);
            holder.secondBidLay.setVisibility(View.VISIBLE);
            holder.thirdBidLay.setVisibility(View.VISIBLE);
            holder.fourthBidLay.setVisibility(View.VISIBLE);
            holder.fifthBidLay.setVisibility(View.VISIBLE);
            holder.firstBidValue.setText(bidderBidModel.getPrice1());
            holder.secondBidValue.setText(bidderBidModel.getPrice2());
            holder.thirdBidValue.setText(bidderBidModel.getPrice3());
            holder.fourthBidValue.setText(bidderBidModel.getPrice4());
            holder.fifthBidValue.setText(bidderBidModel.getPrice5());
            holder.line1.setVisibility(View.VISIBLE);
            holder.line2.setVisibility(View.VISIBLE);
            holder.line3.setVisibility(View.VISIBLE);
            holder.line4.setVisibility(View.VISIBLE);
            holder.line5.setVisibility(View.VISIBLE);
        }
    }

    //...
    public void selectAllBid() {
        for (int i = 0; i < mViewPostModelList.size(); i++) {
            selectBid[i] = true;
        }
        notifyDataSetChanged();
    }

    //...
    public void unselectAllBid() {
        for (int i = 0; i < mViewPostModelList.size(); i++) {
            selectBid[i] = false;
        }
        notifyDataSetChanged();
    }

    //...
    public String getSelectedBidder() {
        String bidders = "";
        for (int i = 0; i < mViewPostModelList.size(); i++) {
            if (selectBid[i]) {
                bidders = bidders + "," + mViewPostModelList.get(i).getUserId();
            }
        }
        if (bidders.isEmpty()) {
            return "";
        }
        return bidders.substring(1, bidders.length());
    }

    @Override
    public int getItemCount() {
        return mViewPostModelList.size();
    }

    //class to hold views
    static class ViewPostVH extends RecyclerView.ViewHolder {
        TextView firstBidValue, secondBidValue, thirdBidValue, fourthBidValue, fifthBidValue;
        LinearLayout firstBidLay, secondBidLay, thirdBidLay, fourthBidLay, fifthBidLay;
        View line1, line2, line3, line4, line5;
        ImageView approved, rejected;
        CheckBox bidCheck;

        public ViewPostVH(View itemView) {
            super(itemView);
            firstBidValue = (TextView) itemView.findViewById(R.id.first_bidValue);
            secondBidValue = (TextView) itemView.findViewById(R.id.second_bidValue);
            thirdBidValue = (TextView) itemView.findViewById(R.id.third_bidValue);
            fourthBidValue = (TextView) itemView.findViewById(R.id.fourth_bidValue);
            fifthBidValue = (TextView) itemView.findViewById(R.id.fifth_bidValue);

            firstBidLay = (LinearLayout) itemView.findViewById(R.id.first_bid_lay);
            secondBidLay = (LinearLayout) itemView.findViewById(R.id.second_bid_lay);
            thirdBidLay = (LinearLayout) itemView.findViewById(R.id.third_bid_lay);
            fourthBidLay = (LinearLayout) itemView.findViewById(R.id.fourth_bid_lay);
            fifthBidLay = (LinearLayout) itemView.findViewById(R.id.fifth_bid_lay);

            line1 = (View) itemView.findViewById(R.id.line1);
            line2 = (View) itemView.findViewById(R.id.line2);
            line3 = (View) itemView.findViewById(R.id.line3);
            line4 = (View) itemView.findViewById(R.id.line4);
            line5 = (View) itemView.findViewById(R.id.line5);

            approved = (ImageView) itemView.findViewById(R.id.approved);
            rejected = (ImageView) itemView.findViewById(R.id.rejected);
            bidCheck = (CheckBox) itemView.findViewById(R.id.checkBidder);
        }
    }
}
