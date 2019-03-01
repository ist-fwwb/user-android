package com.huangtao.user.network.api;

import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.MeetingNote;
import com.huangtao.user.model.MeetingNoteWrapper;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.QueueNode;
import com.huangtao.user.model.TimeSlice;
import com.huangtao.user.model.User;
import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.model.meta.Size;
import com.huangtao.user.model.meta.Status;
import com.huangtao.user.model.meta.Type;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("testMongoFindUser")
    Call<Boolean> test(@Query("key") String test);

    // user
    @POST("user/login")
    Call<User> login(@Query("phone") String phone, @Query("password") String password, @Query("deviceId") String deviceId);

    @POST("user/")
    Call<User> register(@Query("enterpriseId") String enterpriseId, @Query("phone") String phone,
                        @Query("password") String password, @Query("name") String name, @Query
                                ("faceFile") String faceFile, @Query("featureFile") String
                                featureFile);

    @GET("user/")
    Call<List<User>> queryUser(@Query("type") Type type, @Query("ids") List<String> ids);

    @GET("user/{id}")
    Call<User> queryUserById(@Path("id") String id);

    @GET("user/{id}/meeting")
    Call<List<Meeting>> queryMeetingByUid(@Path("id") String id, @Query("date") String date, @Query("status") Status status);

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

    @POST("meeting/intelligent")
    Call<Meeting> appointMeetingroomIntelligent(@Query("utils") Set<MeetingRoomUtils> utils, @Query("size") Size size, @Body Meeting origin);

    @GET("meeting/{id}/attendant")
    Call<List<User>> queryAttendantsFromMeeting(@Path("id") String id);

    @POST("meeting/{attendantNum}/attendant")
    Call<Meeting> joinMeeting(@Path("attendantNum") String attendantNum, @Query("userId") String uid);

    @DELETE("meeting/{id}/attendants/{userId}")
    Call<String> exitMeeting(@Path("id") String id, @Path("userId") String userId);

    @PUT("meeting/{id}")
    Call<Meeting> modifyMeeting(@Body Meeting meeting, @Path("id") String id);

    @POST("meeting/{id}/attendants")
    Call<Meeting> addAttendants(@Path("id") String id, @Body List<String> uids);


    // queue
    @POST("QueueNode/")
    Call<QueueNode> addQueueNode(@Body QueueNode queueNode);

    @GET("QueueNode/{id}")
    Call<QueueNode> getQueueNodeById(@Path("id") String id);

    @GET("QueueNode/")
    Call<List<QueueNode>> getQueueNodes(@Query("userId") String userId);

    @DELETE("QueueNode/{id}")
    Call<String> deleteQueueNodeById(@Path("id") String id);

    // meeting note
    @GET("meetingNote/")
    Call<List<MeetingNoteWrapper>> getMeetingNotes(@Query("userId") String userId, @Query("meetingId") String meetingId, @Query("ownerId") String ownerId);

    @POST("meetingNote/")
    Call<List<MeetingNote>> addMeetingNote(@Query("fileName") String fileName, @Body MeetingNote meetingNote);

    @GET("meetingNote/{id}")
    Call<MeetingNote> getMeetingNoteById(@Path("id") String id);

    @DELETE("meetingNote/{id}/collectors")
    Call<MeetingNote> removeFavoriteNote(@Path("id") String id, @Body String userId);

    @POST("meetingNote/{id}/collectors")
    Call<MeetingNote> addFavoriteNote(@Path("id") String id, @Body String userId);

}
