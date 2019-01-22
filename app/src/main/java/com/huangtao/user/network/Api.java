package com.huangtao.user.network;

import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.TimeSlice;
import com.huangtao.user.model.User;
import com.huangtao.user.model.meta.Size;
import com.huangtao.user.model.meta.Type;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("testMongoFindUser")
    Call<Boolean> test(@Query("key") String test);

    // user
    @POST("user/login")
    Call<User> login(@Query("phone") String phone, @Query("password") String password);

    @POST("user/")
    Call<User> register(@Query("enterpriseId") String enterpriseId, @Query("phone") String phone,
                        @Query("password") String password, @Query("name") String name, @Query
                                ("faceFile") String faceFile, @Query("featureFile") String
                                featureFile);

    @GET("user/")
    Call<List<User>> queryUser(@Query("type") Type type, @Query("ids") List<String> ids);

    @GET("user/{id}")
    Call<User> queryUserById(@Path("id") String id);

    @GET("user/{id}/meeting/{date}")
    Call<List<Meeting>> queryMeetingByUid(@Path("id") String id, @Path("date") String date);

    // meeting room
    @GET("meetingroom/")
    Call<List<MeetingRoom>> queryMeetingroom(@Query("utils") String utils, @Query("size") Size size);

    @GET("meetingroom/{id}")
    Call<MeetingRoom> queryMeetingroomById(@Path("id") String id);

    // time slice
    @GET("timeSlice/")
    Call<List<TimeSlice>> queryTimeSlice(@Query("date") String date, @Query("roomId") String roomId);


    // meeting
    @GET("meeting/")
    Call<List<Meeting>> queryMeeting(@Query("date") String date, @Query("roomId") String roomId, @Query("time") int time);
    @GET("meeting/")
    Call<List<Meeting>> queryMeeting(@Query("date") String date, @Query("roomId") String roomId);

    @GET("meeting/{id}")
    Call<Meeting> queryMeetingById(@Path("id") String id);

    @POST("meeting/")
    Call<Meeting> appointMeetingroom(@Body Meeting meeting);

    @GET("meeting/{id}/attendants")
    Call<List<User>> queryAttendantsFromMeeting(@Path("id") String id);

    @POST("meeting/{attendantNum}/attendants")
    Call<Meeting> joinMeeting(@Path("attendantNum") String attendantNum, @Query("userId") String uid);

    @DELETE("meeting/{id}/attendants/{userId}")
    Call<String> exitMeeting(@Path("id") String id, @Path("userId") String userId);



}
