package ru.nsuorg.shiftorg.network.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.nsuorg.shiftorg.entity_models.UserInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.LoginRequest;

public interface LoginApi {

    @POST("users/login")
    Call <ResponseWrapper<UserInfo>> go(@Body LoginRequest object);

    @GET("users/professions")
    Call<ResponseWrapper<List<String>>> getProfessions();
}
