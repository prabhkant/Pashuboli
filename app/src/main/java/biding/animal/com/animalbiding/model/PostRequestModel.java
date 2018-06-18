package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 02-04-2018.
 */

public class PostRequestModel {
    private String UserId;
    private String ImageName;
    private String AnimalId;
    private String ItemId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getAnimalId() {
        return AnimalId;
    }

    public void setAnimalId(String animalId) {
        AnimalId = animalId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }
}
