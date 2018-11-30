package ru.nsuorg.shiftorg.network.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.nsuorg.shiftorg.entity_models.NotificationRequest;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

public interface ServiceSearchApi {

    @POST("notifications/sendJob")
    Call<ResponseWrapper> sendInvitations(@Body NotificationRequest request);
}
