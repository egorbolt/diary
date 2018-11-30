package ru.nsuorg.shiftorg;

import android.app.Application;
import android.content.Context;

import ru.nsuorg.shiftorg.network.RetrofitProvider;

public class EventorApp extends Application {

    private RetrofitProvider retrofitProvider;

    public static RetrofitProvider getRetrofitProvider(Context context) {

        return getApp(context).retrofitProvider;
    }

    private static EventorApp getApp(Context context) {
        return (EventorApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        retrofitProvider = new RetrofitProvider(getApplicationContext());
    }

}
