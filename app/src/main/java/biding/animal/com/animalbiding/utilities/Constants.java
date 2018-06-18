package biding.animal.com.animalbiding.utilities;

/**
 * Created by Prabhakant.Agnihotri on 04-12-2017.
 */

public class Constants {

    //Live server
//    public static final String BASE_URL = "http://newanimal.cltsolutions.in/api/";

    //Test server
//    public static final String BASE_URL = "http://testnew.pashuboli.com/api/";

    //Development server
    public static final String BASE_URL = "http://dev.pashuboli.com/api/";

    public static final String GET_USER_DETAIL = BASE_URL + "homeAPI/GetUserDetailByUserid";
    public static final String GET_ANIMAL_CATEGORY = BASE_URL + "homeAPI/GetHomeCategoryDetail";
    public static final String GET_ALL_USER_TYPE_DETAIL = BASE_URL + "homeAPI/GetAllUserTypeDetail";
    public static final String GET_ALL_STATE = BASE_URL + "homeAPI/GetAllState";
    public static final String GET_ALL_CITY_BY_STATE_ID = BASE_URL + "homeAPI/GetAllCityByStateId?";
    public static final String GET_AREA_BY_CITY_ID = BASE_URL + "homeAPI/GetAreaByCityId?";
    public static final String GET_BREED_DETAIL = BASE_URL + "homeAPI/GetBreedDetail?";
    public static final String SEARCH_ANIMAL_DATA = BASE_URL + "homeAPI/GetSerachAnimalData?";
    public static final String GET_LOGIN = BASE_URL + "account/Getlogin?";
    public static final String ADD_USER = BASE_URL + "homeAPI/AddUsersDetail";
    public static final String GET_POST_DEATAIL_BY_POST_ID = BASE_URL + "homeapi/GetPostDetailsByPostId?";
    public static final String VERIFY_OTP = BASE_URL + "homeapi/VerifyRegistrationOtp";
    public static final String RESEND_OTP = BASE_URL + "homeAPI/ResendRegistrationOTP";
    public static final String ADD_BID_PRICE = BASE_URL + "homeAPI/AddBiddingPrice";
    public static final String FORGOT_PASS_CHECK = BASE_URL + "homeAPI/forgetPasswordCheck";
    public static final String FORGOT_CHANGE_PASS = BASE_URL + "homeAPI/ChangePasswords";
    public static final String UPDATE_PROFILE = BASE_URL + "homeAPI/UpdateUserDetail";
    public static final String UPDATE_PICTURE = BASE_URL + "homeAPI/UpdateUserPicture";
    public static final String GET_DOCTOR_DETAIL = BASE_URL + "homeAPI/GetDoctorDetails";
    public static final String UPGRADE_DOCTOR_PROFILE = BASE_URL + "homeAPI/UpGradeDoctorProfile";
    public static final String UPGRADE_ASSOCIATE_PROFILE = BASE_URL + "homeAPI/UpGradeAssociateProfile";
    public static final String GET_BIDDING_HISTORY = BASE_URL + "homeAPI/GetAllBiddinghistory";
    public static final String GET_MY_POST = BASE_URL + "homeAPI/GetAllPostByUserId";
    public static final String SAVE_USER_POST = BASE_URL + "homeAPI/SaveUserPost";
    public static final String VIEW_POST_DETAIL = BASE_URL + "homeAPI/GetAllBidderListByPostId";
    public static final String UPDATE_BIDDING_STATUS = BASE_URL + "homeAPI/UpdateBiddingStatus";
    public static final String GET_ALL_NOTIFICATION = BASE_URL + "homeAPI/GetAllNotificationByUserId";
    public static final String GET_TOTAL_REQUEST = BASE_URL + "homeAPI/GetAllUserRequest";
    public static final String DOCTOR_SEARCH = BASE_URL + "homeAPI/Doctorserch";
    public static final String GET_REASON_LIST = BASE_URL + "homeAPI/GetAllRejectionModelList";
    public static final String GET_ALL_ASSOCIATE_POST_REQUEST = BASE_URL + "homeAPI/GetAllAssociatePostRequest";
    public static final String UPDATE_USER_REQUEST_STATUS = BASE_URL + "homeAPI/UpdateAssociatePostRequestStatus";
    public static final String GET_POST_IMAGE_ID = BASE_URL + "homeAPI/GetAllPostrequestImageItemByID";
    public static final String GET_ASSOCIATE_FEATURE = BASE_URL + "homeAPI/GetAllAssociateCountDetail";
    public static final String GET_ALL_ASSOCIATE_EOB_REQUEST = BASE_URL + "homeAPI/GetAllAssociateEOBRequest";
    public static final String GET_ALL_ASSOCIATE_DOCTOR_REQUEST = BASE_URL + "homeAPI/GetAssociateDoctorRequest";
    public static final String GET_DOCTOR_REQUEST_DETAIL = BASE_URL + "homeAPI/GetAssociateDoctorRequestById";
    public static final String GET_ALL_GAUSHALA_DATA = BASE_URL + "homeAPI/GetAllGaushalaStateCityArea";
    public static final String SAVE_GAUSHALA_QUERY = BASE_URL + "homeAPI/SaveGaushalaQuery";
    public static final String GET_PRODUCT_LIST = BASE_URL + "homeAPI/GetAllProduct";
    public static final String SEARCH_EOB_DETAIL = BASE_URL + "homeAPI/SearchEobDetail";
}
