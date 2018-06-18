package biding.animal.com.animalbiding.model;

/**
 * Created by Prabhakant.Agnihotri on 21-04-2018.
 */

public class RequestModel {
    private String FirstName;
    private String Phone;
    private String Address;
    private int RequestId;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }
}
