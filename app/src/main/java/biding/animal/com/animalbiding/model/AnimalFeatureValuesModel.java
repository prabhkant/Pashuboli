package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 18-03-2018.
 */

public class AnimalFeatureValuesModel {
    private String FeaturesId;
    private String AnimalId;
    private String BreedId;
    private String Status;
    private String FeaturesName;

    public String getFeaturesId() {
        return FeaturesId;
    }

    public void setFeaturesId(String featuresId) {
        FeaturesId = featuresId;
    }

    public String getAnimalId() {
        return AnimalId;
    }

    public void setAnimalId(String animalId) {
        AnimalId = animalId;
    }

    public String getBreedId() {
        return BreedId;
    }

    public void setBreedId(String breedId) {
        BreedId = breedId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFeaturesName() {
        return FeaturesName;
    }

    public void setFeaturesName(String featuresName) {
        FeaturesName = featuresName;
    }
}
