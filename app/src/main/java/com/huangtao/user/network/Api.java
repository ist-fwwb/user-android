package com.huangtao.user.network;

import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.User;
import com.huangtao.user.model.meta.Size;
import com.huangtao.user.network.model.ApiMeetingrooms;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("testMongoFindUser")
    Call<Boolean> test(@Query("key") String test);

    @POST("user/login")
    Call<User> login(@Query("phone") String phone, @Query("password") String password);

    @POST("user/")
    Call<User> register(@Query("enterpriseId") String enterpriseId, @Query("phone") String phone,
                        @Query("password") String password, @Query("name") String name, @Query
                                ("faceFile") String faceFile, @Query("featureFile") String
                                featureFile);

    @GET("meetingroom/")
    Call<ApiMeetingrooms> queryMeetingroom(@Query("pageNumber") int pageNumber, @Query("pageSize") int
            pageSize, @Query("utils") String utils, @Query("size") Size size);

    @GET("meetingroom/{id}")
    Call<MeetingRoom> queryMeetingroomById(@Path("id") String id);

}
