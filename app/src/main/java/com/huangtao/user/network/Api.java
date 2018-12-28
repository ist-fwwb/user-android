package com.huangtao.user.network;

import com.huangtao.user.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("testMongoFindUser")
    Call<Boolean> test(@Query("key") String test);

    @POST("user/login")
    Call<User> login(@Body User user);

}
