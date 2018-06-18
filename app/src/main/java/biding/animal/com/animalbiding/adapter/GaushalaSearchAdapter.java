package biding.animal.com.animalbiding.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.GaushalSearchActivity;
import biding.animal.com.animalbiding.fragment.UplaodGaushalaPhotoQueryDialog;
import biding.animal.com.animalbiding.model.GaushalaSearchModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class GaushalaSearchAdapter extends RecyclerView.Adapter<GaushalaSearchAdapter.CattleRequestVH> {

    private LayoutInflater mInflater;
    private List<GaushalaSearchModel> mRequestModelList;
    private ApplicationClass mInsatance;
    private Context mContext;

    public GaushalaSearchAdapter(Context context, List<GaushalaSearchModel> requestModel) {
        mContext = context;
        mRequestModelList = requestModel;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CattleRequestVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_gaushala_request, parent, false);
        return new CattleRequestVH(view);
    }

    @Override
    public void onBindViewHolder(final CattleRequestVH holder, final int position) {
        //set animation for itemview
        mInsatance.setScaleAnimation(holder.itemView);

        holder.capacityCows.setText(mRequestModelList.get(position).getCapOfCow());
        holder.presentCows.setText(mRequestModelList.get(position).getAviCow());
        holder.emptySpaceCows.setText(mRequestModelList.get(position).getEmptySpace());
        holder.address.setText(mRequestModelList.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPostCattleDetailRequestActivity(Integer.parseInt(mRequestModelList.get(position).getGaushalaId()));
            }
        });
    }

    //...
    private void openPostCattleDetailRequestActivity(int itemId) {
        FragmentManager fragmentManager = ((GaushalSearchActivity) mContext).getFragmentManager();
        UplaodGaushalaPhotoQueryDialog largeImageDialogFragment = new UplaodGaushalaPhotoQueryDialog();
        Bundle bundle = new Bundle();
        largeImageDialogFragment.setArguments(bundle);
        largeImageDialogFragment.show(fragmentManager, "");
    }

    @Override
    public int getItemCount() {
        return mRequestModelList.size();
    }

    //class to hold the views
    class CattleRequestVH extends RecyclerView.ViewHolder {
        TextView capacityCows, presentCows, emptySpaceCows, address;

        public CattleRequestVH(View itemView) {
            super(itemView);
            capacityCows = (TextView) itemView.findViewById(R.id.capacity_cows);
            presentCows = (TextView) itemView.findViewById(R.id.present_cows);
            emptySpaceCows = (TextView) itemView.findViewById(R.id.empty_space_cows);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

}
