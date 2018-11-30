package ru.nsuorg.shiftorg.network.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsuorg.shiftorg.entity_models.NotificationInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

public interface NotificationApi {

    @GET("users/{id}/notifications")
    Call<ResponseWrapper<List<NotificationInfo>>> getNotifications(@Path("id") String id, @Query("limit") int limit, @Query("offset") int offset);

    @DELETE("notifications/{id}")
    Call<ResponseWrapper> deleteNotification(@Path("id") String notificationID, @Query("userID") String userID);

}
