package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 24-03-2018.
 */

public class TotalRequestModel {

    private String Status;

    private String Fk_CategoryId;

    private String Comments;

    private String CatName;

    private String UpdatedOn;

    private String CreatedOn;

    private String StatusName;

    private String Id;

    private String UserId;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFk_CategoryId() {
        return Fk_CategoryId;
    }

    public void setFk_CategoryId(String fk_CategoryId) {
        Fk_CategoryId = fk_CategoryId;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }

    public String getUpdatedOn() {
        return UpdatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        UpdatedOn = updatedOn;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
