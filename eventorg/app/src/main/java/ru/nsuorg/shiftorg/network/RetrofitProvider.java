package ru.nsuorg.shiftorg.network;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsuorg.shiftorg.network.api.EventsApi;
import ru.nsuorg.shiftorg.network.api.LoginApi;
import ru.nsuorg.shiftorg.network.api.MyPageApi;
import ru.nsuorg.shiftorg.network.api.NotificationAcceptanceApi;
import ru.nsuorg.shiftorg.network.api.NotificationApi;
import ru.nsuorg.shiftorg.network.api.RegisterApi;
import ru.nsuorg.shiftorg.network.api.ServiceSearchApi;

public final class RetrofitProvider {

    private static final String BASE_URL = "http://192.168.43.7:8080/api/";//"http://alltoall98.000webhostapp.com/api/"; //"http://192.168.43.53:8080/api/v001/";

    private final Retrofit retrofit;

    private final MyPageApi myPageApi;
    private final LoginApi loginApi;
    private final RegisterApi registerApi;
    private final NotificationApi notificationApi;
    private final NotificationAcceptanceApi notificationAcceptanceApi;
    private final EventsApi eventsApi;
    private final ServiceSearchApi serviceSearchApi;

    public RetrofitProvider(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(createClient(context))
                .build();
        myPageApi = retrofit.create(MyPageApi.class);
        loginApi = retrofit.create(LoginApi.class);
        registerApi = retrofit.create(RegisterApi.class);
        notificationApi = retrofit.create(NotificationApi.class);
        notificationAcceptanceApi = retrofit.create(NotificationAcceptanceApi.class);
        eventsApi = retrofit.create(EventsApi.class);
        serviceSearchApi = retrofit.create(ServiceSearchApi.class);
    }

    private OkHttpClient createClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ReceivedCookiesInterceptor interceptor = new ReceivedCookiesInterceptor(context);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(logInterceptor);
        return builder.build();

    }

    public LoginApi getLoginApi() {
        return loginApi;
    }

    public RegisterApi getRegisterApi() {
        return registerApi;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public MyPageApi getMyPageApi() {
        return myPageApi;
    }

    public NotificationAcceptanceApi getNotificationAcceptanceApi() {
        return notificationAcceptanceApi;
    }

    public NotificationApi getNotificationApi() {
        return notificationApi;
    }

    public EventsApi getEventsApi() {
        return eventsApi;
    }

    public ServiceSearchApi getServiceSearchApi() {
        return serviceSearchApi;
    }
}