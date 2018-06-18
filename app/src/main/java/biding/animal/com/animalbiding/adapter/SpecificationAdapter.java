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
import biding.animal.com.animalbiding.activities.CattleActivity;
import biding.animal.com.animalbiding.model.AnimalSpecificationDetailModel;
import biding.animal.com.animalbiding.model.CattleModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 03-02-2018.
 */

public class SpecificationAdapter extends RecyclerView.Adapter<SpecificationAdapter.CattleVH> {

    private LayoutInflater mInflater;
    private ApplicationClass mInsatance;
    private List<AnimalSpecificationDetailModel> mSpecificationModelList;
    private AnimalDetailActivity mActivity;

    public SpecificationAdapter(AnimalDetailActivity activity, List<AnimalSpecificationDetailModel> specificationModelList) {
        mActivity = activity;
        mSpecificationModelList = specificationModelList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CattleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_specification, parent, false);
        return new CattleVH(view);
    }

    @Override
    public void onBindViewHolder(CattleVH holder, final int position) {
        holder.animalSpecification.setText(mSpecificationModelList.get(position).getAnimalSpecification());
        holder.animalSpecificationValue.setText(mSpecificationModelList.get(position).getAnimalSpecificationValue());
    }

    @Override
    public int getItemCount() {
        return mSpecificationModelList.size();
    }

    static class CattleVH extends RecyclerView.ViewHolder {
        TextView animalSpecification, animalSpecificationValue;

        public CattleVH(View itemView) {
            super(itemView);
            animalSpecification = (TextView) itemView.findViewById(R.id.animal_specification);
            animalSpecificationValue = (TextView) itemView.findViewById(R.id.animal_specification_value);

        }
    }
}
