package biding.animal.com.animalbiding.model;

import java.util.List;

/**
 * Created by Prabhakant.Agnihotri on 21-03-2018.
 */

public class ViewPostModel {

    private String UserId;
    private String PostId;
    private String Status;
    private String Bidding;
    private String Price;
    private String BidCount;
    private String LastPrice;
    private String UserResponce;
    private List<BidderBidModel> lstAnimalBiddingPricesForAPIS;

    public List<BidderBidModel> getLstAnimalBiddingPricesForAPIS() {
        return lstAnimalBiddingPricesForAPIS;
    }

    public void setLstAnimalBiddingPricesForAPIS(List<BidderBidModel> lstAnimalBiddingPricesForAPIS) {
        this.lstAnimalBiddingPricesForAPIS = lstAnimalBiddingPricesForAPIS;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBidding() {
        return Bidding;
    }

    public void setBidding(String bidding) {
        Bidding = bidding;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getBidCount() {
        return BidCount;
    }

    public void setBidCount(String bidCount) {
        BidCount = bidCount;
    }

    public String getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(String lastPrice) {
        LastPrice = lastPrice;
    }

    public String getUserResponce() {
        return UserResponce;
    }

    public void setUserResponce(String userResponce) {
        UserResponce = userResponce;
    }
}
