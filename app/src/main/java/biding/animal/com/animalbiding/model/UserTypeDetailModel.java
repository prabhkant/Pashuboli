package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 01-02-2018.
 */

public class UserTypeDetailModel {
    private String UserTypeId;
    private String UserTypeName;
    private String Desc;

    public String getUserTypeId() {
        return UserTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        UserTypeId = userTypeId;
    }

    public String getUserTypeName() {
        return UserTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        UserTypeName = userTypeName;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

}
