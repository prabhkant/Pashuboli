package biding.animal.com.animalbiding.model;

import java.util.List;

/**
 * Created by Prabhakant.Agnihotri on 18-03-2018.
 */

public class AnimalFeatureModel {
    private String AnimalSpecification;
    private String AnimalSpecId;
    private List<AnimalFeatureValuesModel> lstAnimalFeaturesModel;

    public String getAnimalSpecification() {
        return AnimalSpecification;
    }

    public void setAnimalSpecification(String animalSpecification) {
        AnimalSpecification = animalSpecification;
    }

    public String getAnimalSpecId() {
        return AnimalSpecId;
    }

    public void setAnimalSpecId(String animalSpecId) {
        AnimalSpecId = animalSpecId;
    }

    public List<AnimalFeatureValuesModel> getAnimalFeatureValuesModels() {
        return lstAnimalFeaturesModel;
    }

    public void setAnimalFeatureValuesModels(List<AnimalFeatureValuesModel> animalFeatureValuesModels) {
        this.lstAnimalFeaturesModel = animalFeatureValuesModels;
    }
}

