package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 22-02-2018.
 */

public class AnimalSpecificationDetailModel {
    private String AnimalSpecId;
    private String AnimalSpecification;
    private String Status;
    private String UserId;

    public String getAnimalSpecId() {
        return AnimalSpecId;
    }

    public void setAnimalSpecId(String animalSpecId) {
        AnimalSpecId = animalSpecId;
    }

    public String getAnimalSpecification() {
        return AnimalSpecification;
    }

    public void setAnimalSpecification(String animalSpecification) {
        AnimalSpecification = animalSpecification;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAnimalSpecificationValue() {

        return AnimalSpecificationValue;
    }

    public void setAnimalSpecificationValue(String animalSpecificationValue) {
        AnimalSpecificationValue = animalSpecificationValue;
    }

    private String AnimalSpecificationValue;
}
