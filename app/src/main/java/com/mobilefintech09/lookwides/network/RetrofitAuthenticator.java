package com.mobilefintech09.lookwides.network;

import com.mobilefintech09.lookwides.entities.AccessToken;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class RetrofitAuthenticator implements Authenticator {

    private  TokenManager mTokenManager;
    private static RetrofitAuthenticator mInstance;

    private RetrofitAuthenticator(TokenManager tokenManager){
        this.mTokenManager = tokenManager;
    }
    public static synchronized RetrofitAuthenticator getInstance(TokenManager tokenManager){
        if(mInstance == null) {
            mInstance = new RetrofitAuthenticator(tokenManager);
        }
        return mInstance;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {

        if(responseCount(response) >= 3){
            return null;
        }
        AccessToken token =  mTokenManager.getToken();
        ApiService apiService = RetrofitBuilder.createService(ApiService.class);
        Call<AccessToken> call = apiService.reAuthenticateClient(token.getRefreshToken());
        retrofit2.Response<AccessToken> res = call.execute();

        if(res.isSuccessful()){
            AccessToken newToken =  res.body();
            mTokenManager.saveToken(newToken);

            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + res.body().getAccessToken())
                    .build();
        }else {
            return null;
        }
    }
    private int responseCount(Response response){
        int result = 1;
        while((response = response.priorResponse()) != null){
            result++;
        }
        return result;
    }

}
