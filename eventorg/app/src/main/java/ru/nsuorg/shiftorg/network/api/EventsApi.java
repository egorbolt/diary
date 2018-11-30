package ru.nsuorg.shiftorg.network.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsuorg.shiftorg.EventorApp;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

public interface EventsApi {

    @GET("users/{id}/events")
    Call<ResponseWrapper<List<EventInfo>>> getEvents(@Path("id") String userID, @Query("limit") int limit, @Query("offset") int offset, @Query("role") String role);

    @POST("events")
    Call<ResponseWrapper<String>> createEvent(@Body EventInfo eventInfo);

    @POST("events/{eventID}")
    Call<ResponseWrapper<String>> deleteEvent(@Path("eventID") String eventID);

    @POST("events/change")
    Call<ResponseWrapper> editEvent(@Body EventInfo eventInfo);
}
