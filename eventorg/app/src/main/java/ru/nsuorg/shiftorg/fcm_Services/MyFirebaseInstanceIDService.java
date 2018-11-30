package ru.nsuorg.shiftorg.fcm_Services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.EventorApp;
import ru.nsuorg.shiftorg.LoginActivity;
import ru.nsuorg.shiftorg.MainActivity;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d("TAG", "Token: " + FirebaseInstanceId.getInstance().getToken());
        EventorApp.getRetrofitProvider(this).getMyPageApi().sendNotificationToken(FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {

            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {

            }
        });
    }

    /**
     * Persist token to third-party servers.
     *
     *
     * @param token The new token.
     */


    private void sendRegistrationToServer(String token) {

    }
}