package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.AnimalDetailActivity;
import biding.animal.com.animalbiding.model.AnimalFeatureModel;
import biding.animal.com.animalbiding.model.AnimalFeatureValuesModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 03-02-2018.
 */

public class BreedFeaturesAdapter extends RecyclerView.Adapter<BreedFeaturesAdapter.FeatureVH> {

    private LayoutInflater mInflater;
    private ApplicationClass mInsatance;
    private List<AnimalFeatureModel> mAnimalFeatureList;
    private Context mContext;
    public String mFeatureNameValues = "" ;
    public String mSpecificationNameValues = "";
    private Set<String> mFeatureSet;

    public BreedFeaturesAdapter(Context context, List<AnimalFeatureModel> animalFeatureModels) {
        mContext = context;
        mAnimalFeatureList = animalFeatureModels;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
        mFeatureSet = new HashSet<>();
    }

    @Override
    public FeatureVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_breed_feature_layout, parent, false);
        return new FeatureVH(view);
    }

    @Override
    public void onBindViewHolder(final FeatureVH holder, final int position) {
        //set adapter for feature
        setFeatureSpinner(holder, position);

        holder.specificationName.setText(mAnimalFeatureList.get(position).getAnimalSpecification());

        mSpecificationNameValues = mSpecificationNameValues + "~" +mAnimalFeatureList.get(position).getAnimalSpecId();

        holder.calvingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mFeatureSet.add(holder.calvingSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimalFeatureList.size();
    }

    //method to set category spinner adapter
    private void setFeatureSpinner(FeatureVH holder, int position) {
        try {
            List<String> featureArray = new ArrayList<>();
            List<AnimalFeatureValuesModel> valuesModelList;

            valuesModelList = mAnimalFeatureList.get(position).getAnimalFeatureValuesModels();
            for (int feature = 0; feature<valuesModelList.size(); feature++) {
                String animalFeature = valuesModelList.get(feature).getFeaturesName();
//                String featureValue = valuesModelList.get(feature).getFeaturesId();
                featureArray.add(animalFeature/* + " " + featureValue*/);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, featureArray);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            holder.calvingSpinner.setAdapter(arrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method to get selected feature name
    public String getmFeatureNameValues () {
        if (mFeatureSet != null && mFeatureSet.size()>0) {
            Iterator iterator = mFeatureSet.iterator();
            mFeatureNameValues = "";
            while (iterator.hasNext()) {
                mFeatureNameValues = mFeatureNameValues + "~" + iterator.next();
            }
            return mFeatureNameValues.substring(1,mFeatureNameValues.length());
        }
        return "";
    }

    //method to get specification name
    public String getmSpecificationNameValues () {
        if (mSpecificationNameValues != null && mSpecificationNameValues.length()>0) {
            return mSpecificationNameValues.substring(1,mSpecificationNameValues.length());
        }
        return "";
    }
    //class to hold view
    static class FeatureVH extends RecyclerView.ViewHolder {
        Spinner calvingSpinner;
        TextView specificationName;
        public FeatureVH(View itemView) {
            super(itemView);
            calvingSpinner = (Spinner) itemView.findViewById(R.id.calving_spinner);
            specificationName = (TextView) itemView.findViewById(R.id.specificationName);
        }
    }
}
