package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 14-03-2018.
 */

public class BiddingHistoryModel {
    private String AnimalId;
    private String Status;
    private String StatusName;
    private String LastPrice;
    private String Price;
    private String Image;
    private String AnimalName;
    private String ItemCode;
    private String PostDate;

    public String getAnimalId() {
        return AnimalId;
    }

    public void setAnimalId(String animalId) {
        AnimalId = animalId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(String lastPrice) {
        LastPrice = lastPrice;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAnimalName() {
        return AnimalName;
    }

    public void setAnimalName(String animalName) {
        AnimalName = animalName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }
}
