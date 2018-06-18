package biding.animal.com.animalbiding.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Prabhakant.Agnihotri on 18-02-2018.
 */

public class SharedPrefernceManger {

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(ApplicationClass.getInstance().getContext());
    }

    //--------------------------------------------------------------------------------------------------------------
    public static void setUserType(String usertype) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ConstantMsg.PREFERENCE_USERTYPE, usertype);
        editor.commit();
    }
    //...
    public static String getUserType() {
        SharedPreferences pref = getSharedPreferences();
        String userTypes = pref.getString(ConstantMsg.PREFERENCE_USERTYPE, "UserType");
        return userTypes;
    }
    //...
    public static void setUserTypId(String usertype) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ConstantMsg.PREFERENCE_USERTYPE_ID, usertype);
        editor.commit();
    }
    //...
    public static String getUserTypeId() {
        SharedPreferences pref = getSharedPreferences();
        String userTypes = pref.getString(ConstantMsg.PREFERENCE_USERTYPE_ID, "");
        return userTypes;
    }

    public static void setToken(String token) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ConstantMsg.PREFERENCE_TOKEN, token);
        editor.commit();
    }
    public static String getToken() {
        SharedPreferences pref = getSharedPreferences();
        String userid = pref.getString(ConstantMsg.PREFERENCE_TOKEN, "");
        return userid;
    }
    //--------------------------------------------------------------------------------------------------------------

    //...
    public static void setUserId(String userId) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ConstantMsg.PREFERENCE_USER_ID, userId);
        editor.commit();
    }
    //...
    public static String getUserId () {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userId = sharedPreferences.getString(ConstantMsg.PREFERENCE_USER_ID,"");
        return userId;
    }

    //...
    public static void setUserName(String userName) {
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ConstantMsg.PREFERENCE_USER_NAME, userName);
        editor.commit();
    }
    //...
    public static String getUserName () {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String userId = sharedPreferences.getString(ConstantMsg.PREFERENCE_USER_NAME,"Guest");
        return userId;
    }

    //method to clear shared preference
    public static void clearSharedPreference () {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }

}
