package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.model.CattleModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 03-02-2018.
 */

public class DetailAnimalCategoryAdapter extends RecyclerView.Adapter<DetailAnimalCategoryAdapter.CattleVH> {

    private List<CattleModel> mCattleList;
    private LayoutInflater mInflater;
    private ApplicationClass mInsatance;
    private Context mContext;
    private boolean checkSelected[];

    public DetailAnimalCategoryAdapter(Context context, List<CattleModel> cattleList) {
        mCattleList = cattleList;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
        checkSelected = new boolean[cattleList.size()];
        for (int i = 0; i < cattleList.size(); i++) {
            checkSelected[i] = false;
        }
    }

    @Override
    public CattleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_check_category, parent, false);
        return new CattleVH(view);
    }

    @Override
    public void onBindViewHolder(final CattleVH holder, final int position) {
        //...
        holder.catName.setText(mCattleList.get(position).getAnimalName());
        //method call to animate views
        mInsatance.setScaleAnimation(holder.itemView);

        holder.catCheck.setChecked(checkSelected[position]);
        //click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.catCheck.isChecked()) {
                    holder.catCheck.setChecked(false);
                    checkSelected[position] = false;
                } else {
                    holder.catCheck.setChecked(true);
                    checkSelected[position] = true;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCattleList.size();
    }

    static class CattleVH extends RecyclerView.ViewHolder {
        TextView catName;
        CheckBox catCheck;

        public CattleVH(View itemView) {
            super(itemView);
            catName = (TextView) itemView.findViewById(R.id.categoryName);
            catCheck = (CheckBox) itemView.findViewById(R.id.categoryChecked);
        }
    }
}
