package com.mobilefintech09.lookwides.network;

import com.mobilefintech09.lookwides.entities.AccessToken;
import com.mobilefintech09.lookwides.entities.ImageResponse;
import com.mobilefintech09.lookwides.entities.OrderResponse;
import com.mobilefintech09.lookwides.entities.OrderResponses;
import com.mobilefintech09.lookwides.entities.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String mail, @Field("password") String password, @Field("phone") String phone, @Field("address") String address);

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);

    @POST("logout")
    Call<AccessToken> logout();

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> reAuthenticateClient(@Field("refresh_token") String refreshToken);

    @POST("reset")
    @FormUrlEncoded
    Call<AccessToken> resetPassword(@Field("password") String password, @Field("newPassword") String confirm_password);

    @GET("user")
    Call<UserResponse> getUser();

    @GET("order")
    Call<List<OrderResponses>> getOrder();

    @GET("completed")
    Call<List<OrderResponses>> getCompletedOrders();

    @POST("user/avatar")
    @Multipart
    Call<ImageResponse> uploadAvatar(@Part MultipartBody.Part avatar);

    @POST("new_order")
    @FormUrlEncoded
    Call<OrderResponse> order(
            @Field("description") String description,
            @Field("location") String location,
            @Field("destination") String destination,
            @Field("reciever_name") String reciever_name,
            @Field("reciever_address") String reciever_address,
            @Field("reciever_phone") String reciever_phone,
            @Field("num_of_items") String num_of_items,
            @Field("weight") String weight,
            @Field("avatar") String avatar,
            @Field("price") String price,
            @Field("user_id") String user_id);

    @POST("{id}/update_order_payment")
    @FormUrlEncoded
    Call<OrderResponse> paidOrder(@Field("tranx_id") String tranx_id, @Path("id") String id );

    @POST("{id}/complete_order")
    @FormUrlEncoded
    Call<OrderResponse> completeOrder(@Path("id") String order_id, @Field("id") String id );

    @POST("{id}/cancel_order")
    @FormUrlEncoded
    Call<OrderResponse> cancelOrder( @Path("id") String order_id,  @Field("id") String id );
}
