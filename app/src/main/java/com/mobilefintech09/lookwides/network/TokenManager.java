package com.mobilefintech09.lookwides.network;

import android.content.SharedPreferences;

import com.mobilefintech09.lookwides.entities.AccessToken;

public class TokenManager {
    private SharedPreferences prefs;
    private  SharedPreferences.Editor mEditor;
    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.mEditor =prefs.edit();
    }
    public static synchronized TokenManager getInstance(SharedPreferences prefs){
        if(INSTANCE == null ){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }
    public void saveToken(AccessToken token){
        mEditor.putString("ACCESS_TOKEN", token.getAccessToken()).commit();
        mEditor.putString("REFRESH_TOKEN", token.getRefreshToken()).commit();

    }
    public void deleteToken(){
        mEditor.remove("ACCESS_TOKEN").commit();
        mEditor.remove("REFRESH_TOKEN").commit();
    }
    public AccessToken getToken(){
        AccessToken token =new AccessToken();
        token.setAccessToken((prefs.getString("ACCESS_TOKEN", null)));
        token.setRefreshToken((prefs.getString("REFRESH_TOKEN", null)));
        return token;
    }
}
