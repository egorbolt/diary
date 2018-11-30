package ru.nsuorg.shiftorg.network.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.nsuorg.shiftorg.entity_models.RegisterInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.UserInfo;

public interface RegisterApi {
    @POST("users/register")
    Call<ResponseWrapper<UserInfo>> register(@Body UserInfo userInfo);

}
