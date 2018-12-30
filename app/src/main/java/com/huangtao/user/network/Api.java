package com.huangtao.user.network;

import com.huangtao.user.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @GET("testMongoFindUser")
    Call<Boolean> test(@Query("key") String test);

    @POST("user/login")
    Call<User> login(@Query("phone") String phone, @Query("password") String password);

    @POST("user/register")
    @Multipart
    Call<User> register(@Part("enterpriseId") String enterpriseId, @Part("phone") String phone,
                        @Part("password") String password, @Part("name") String name, @Part() List<MultipartBody.Part> parts);

}
