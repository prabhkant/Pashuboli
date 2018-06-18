package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 21-04-2018.
 */

public class ViewPostCattleRequestModel {
    private String ItemId;
    private String UserId;
    private String PostDate;
    private String AnimalName;
    private String SubCatName;
    private String ItemCode;

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public String getAnimalName() {
        return AnimalName;
    }

    public void setAnimalName(String animalName) {
        AnimalName = animalName;
    }

    public String getSubCatName() {
        return SubCatName;
    }

    public void setSubCatName(String subCatName) {
        SubCatName = subCatName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }
}
