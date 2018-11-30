package ru.nsuorg.shiftorg.network.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.UserInfo;

public interface MyPageApi {

    @GET("users/{id}")
    Call<ResponseWrapper<UserInfo>> getData(@Path("id") String id);

    @POST("users/token")
    Call<ResponseWrapper> sendNotificationToken(@Query("token") String token);
}
