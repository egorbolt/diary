package ru.nsuorg.shiftorg.network.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

public interface NotificationAcceptanceApi {
    @POST("notifications/{id}")
    Call<ResponseWrapper> sendResponse(@Path("id") String String, @Query("invitationStatus") String status, @Query("userID") String userID);
}
