package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 24-03-2018.
 */

public class NotificationModel {
    private String NotificationId;
    private String NotificationName;
    private String PageName;
    private String CreatedDate;
    private String ReadDate;
    private String IsRead;

    public String getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(String notificationId) {
        NotificationId = notificationId;
    }

    public String getNotificationName() {
        return NotificationName;
    }

    public void setNotificationName(String notificationName) {
        NotificationName = notificationName;
    }

    public String getPageName() {
        return PageName;
    }

    public void setPageName(String pageName) {
        PageName = pageName;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getReadDate() {
        return ReadDate;
    }

    public void setReadDate(String readDate) {
        ReadDate = readDate;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    private String UserId;

}
