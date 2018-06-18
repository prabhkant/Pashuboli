package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 23-01-2018.
 */

public class AnimalCategoryModel {

    private String Status;

    private String AnimalImage;

    private String AnimalName;

    private String AnimalCode;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAnimalImage() {
        return AnimalImage;
    }

    public void setAnimalImage(String animalImage) {
        AnimalImage = animalImage;
    }

    public String getAnimalName() {
        return AnimalName;
    }

    public void setAnimalName(String animalName) {
        AnimalName = animalName;
    }

    public String getAnimalCode() {
        return AnimalCode;
    }

    public void setAnimalCode(String animalCode) {
        AnimalCode = animalCode;
    }

    public String getHomeActive() {
        return HomeActive;
    }

    public void setHomeActive(String homeActive) {
        HomeActive = homeActive;
    }

    public String getAnimalId() {
        return AnimalId;
    }

    public void setAnimalId(String animalId) {
        AnimalId = animalId;
    }

    private String HomeActive;

    private String AnimalId;

}
