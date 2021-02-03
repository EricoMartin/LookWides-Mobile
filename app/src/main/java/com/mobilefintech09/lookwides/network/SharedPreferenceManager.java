package com.mobilefintech09.lookwides.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobilefintech09.lookwides.entities.OrderResponse;
import com.mobilefintech09.lookwides.entities.Orders;
import com.mobilefintech09.lookwides.entities.OrderResponses;
import com.mobilefintech09.lookwides.entities.User;
import com.mobilefintech09.lookwides.entities.UserResponse;

public class SharedPreferenceManager {
    private static SharedPreferenceManager mInstance;
    private  SharedPreferences.Editor mEditor;
    private Context mContext;
    private SharedPreferences prefs;

    private static final String SHARED_PREF_NAME = "lookwidesuserdetails";

    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_NAME = "userfirstname";
    private static final String KEY_USER_PHONE = "userphone";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    private static final String KEY_USER_ADDRESS = "keyuseraddress";
    private static final String KEY_USER_AVATAR = "keyuseravatar";
    private static final String KEY_USER_STATUS = "keyuserstatus";
    private static final String KEY_USER_CREATED_AT = "keyusercreatedat";


    private static final String KEY_ORDER_ID = "keyorderid";
    private static final String KEY_ORDER_DESCRIPTION = "keyorderdescription";
    private static final String KEY_ORDER_LOCATION= "keyorderlocation";
    private static final String KEY_ORDER_DESTINATION = "keyorderdestination";
    private static final String KEY_ORDER_NUM_ITEMS = "keyorderitems";
    private static final String KEY_ORDER_AVATAR= "keyorderavatar";
    private static final String KEY_ORDER_WEIGHT= "keyorderweight";
    private static final String KEY_ORDER_RECEIVER_NAME = "orderreceivername";
    private static final String KEY_ORDER_RECEIVER_PHONE = "orderreceiverphone";
    private static final String KEY_ORDER_TRANX_ID = "ordertransactionid";
    private static final String KEY_ORDER_RECEIVER_ADDRESS = "orderreceiveraddress";
    private static final String KEY_ORDER_STATUS = "keyorderstatus";
    private static final String KEY_ORDER_PRICE = "keyorderprice";

    private SharedPreferenceManager(SharedPreferences prefs, Context mContext) {
        this.prefs = prefs;
        this.mEditor = prefs.edit();
        this.mContext= mContext;
    }

    public static synchronized SharedPreferenceManager getInstance(SharedPreferences prefs, Context mContext) {
        if (mInstance == null) {
            mInstance = new SharedPreferenceManager(prefs, mContext);
        }
        return mInstance;
    }

    public boolean userLogin(UserResponse user) {
        mEditor.putInt(KEY_USER_ID, user.getUser().getId());
        mEditor.putString(KEY_USER_NAME, user.getUser().getName());
        mEditor.putString(KEY_USER_PHONE, user.getUser().getPhone());
        mEditor.putString(KEY_USER_EMAIL, user.getUser().getEmail());
        mEditor.putString(KEY_USER_ADDRESS, user.getUser().getAddress());
        mEditor.putString(KEY_USER_AVATAR, user.getUser().getAvatar());
        mEditor.putInt(KEY_USER_STATUS, user.getUser().getStatus());
        mEditor.putString(KEY_USER_CREATED_AT, user.getUser().getCreatedAt());
        mEditor.apply();
        return true;
    }

    public boolean orderStatus(OrderResponse order) {
        mEditor.putInt(KEY_ORDER_ID, order.getOrder().getId());
        mEditor.putString(KEY_ORDER_DESCRIPTION, order.getOrder().getDescription());
        mEditor.putString(KEY_ORDER_LOCATION, order.getOrder().getLocation());
        mEditor.putString(KEY_ORDER_DESTINATION, order.getOrder().getDestination());
        mEditor.putInt(KEY_ORDER_NUM_ITEMS, order.getOrder().getNumOfItems());
        mEditor.putString(KEY_ORDER_WEIGHT, (String) order.getOrder().getWeight());
        mEditor.putString(KEY_ORDER_AVATAR, (String) order.getOrder().getAvatar());
        mEditor.putString(KEY_ORDER_RECEIVER_NAME, order.getOrder().getRecieverName());
        mEditor.putString(KEY_ORDER_RECEIVER_PHONE, order.getOrder().getRecieverPhone());
        mEditor.putString(KEY_ORDER_TRANX_ID, order.getOrder().getTranxId());
        mEditor.putString(KEY_ORDER_RECEIVER_ADDRESS, order.getOrder().getRecieverAddress());
        mEditor.putString(KEY_ORDER_STATUS, order.getOrder().getStatus());
        mEditor.putString(KEY_ORDER_PRICE, order.getOrder().getStatus());
        mEditor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL, null) != null)
            return true;
        return false;
    }

    public User getNewUser() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_USER_ID, 0),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_PHONE, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),
                sharedPreferences.getString(KEY_USER_ADDRESS, "0"),
                sharedPreferences.getString(KEY_USER_AVATAR, null),
                sharedPreferences.getInt(KEY_USER_STATUS, 0),
                sharedPreferences.getString(KEY_USER_CREATED_AT,null)
        );
    }

    public Orders getNewOrder() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Orders(
                sharedPreferences.getInt(KEY_ORDER_ID, 0),
                sharedPreferences.getString(KEY_ORDER_DESCRIPTION, null),
                sharedPreferences.getString(KEY_ORDER_LOCATION, null),
                sharedPreferences.getString(KEY_ORDER_DESTINATION, null),
                sharedPreferences.getInt(KEY_ORDER_NUM_ITEMS, 0),
                sharedPreferences.getString(KEY_ORDER_WEIGHT, null),
                sharedPreferences.getString(KEY_ORDER_AVATAR, null),
                sharedPreferences.getString(KEY_ORDER_RECEIVER_NAME, null),
                sharedPreferences.getString(KEY_ORDER_RECEIVER_PHONE,null),
                sharedPreferences.getString(KEY_ORDER_TRANX_ID, null),
                sharedPreferences.getString(KEY_ORDER_RECEIVER_ADDRESS, null),
                sharedPreferences.getString(KEY_ORDER_STATUS, null),
                sharedPreferences.getString(KEY_ORDER_PRICE, null)
        );
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
