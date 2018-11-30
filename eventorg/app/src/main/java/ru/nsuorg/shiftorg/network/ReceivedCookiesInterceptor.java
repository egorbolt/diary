package ru.nsuorg.shiftorg.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;
import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

public class ReceivedCookiesInterceptor implements Interceptor {

    private SharedPreferences prefs;

    ReceivedCookiesInterceptor(Context context) {
        this.prefs = context.getSharedPreferences(context.getString(R.string.pref_key), Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request().newBuilder()
                .addHeader("Cookie", prefs.getString("cookie", "1")).build());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            String cookie = originalResponse.header("Set-Cookie");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("cookie", cookie);
            editor.commit();
        }
        return originalResponse;
    }
}