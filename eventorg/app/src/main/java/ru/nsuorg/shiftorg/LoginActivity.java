package ru.nsuorg.shiftorg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.LoginRequest;
import ru.nsuorg.shiftorg.entity_models.UserInfo;

public class LoginActivity extends AppCompatActivity {

    private Button registerbutton;
    private Button gobutton;
    private EditText login;
    private EditText password;
    LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerbutton = findViewById(R.id.registerbutton);
        gobutton = findViewById(R.id.gobutton);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        loadingView = new LoadingView(this, (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0));

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.startAnimation();
                EventorApp.getRetrofitProvider(LoginActivity.this).getLoginApi().getProfessions().enqueue(new Callback<ResponseWrapper<List<String>>>() {
                    @Override
                    public void onResponse(Call<ResponseWrapper<List<String>>> call, Response<ResponseWrapper<List<String>>> response) {
                        ResponseWrapper<List<String>> responseWrapper = response.body();
                        if (responseWrapper != null && responseWrapper.getStatus().equals("OK")) {
                            loadingView.stopAnimation();
                            List<String> listProf = responseWrapper.getData();
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putStringArrayListExtra("professions", (ArrayList<String>) listProf);
                            LoginActivity.this.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWrapper<List<String>>> call, Throwable t) {
                        loadingView.stopAnimation();
                        Toast.makeText(LoginActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        gobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l = login.getText().toString();
                String p = password.getText().toString();
                if (!l.equals("") && !l.equals("")) {
                    loadingView.startAnimation();
                    EventorApp.getRetrofitProvider(LoginActivity.this).getLoginApi().go(new LoginRequest(l, p)).enqueue(new Callback<ResponseWrapper<UserInfo>>() {
                        @Override
                        public void onResponse(Call<ResponseWrapper<UserInfo>> call, Response<ResponseWrapper<UserInfo>> response) {
                            loadingView.stopAnimation();
                            ResponseWrapper<UserInfo> responseWrapper = response.body();
                            if (responseWrapper != null && responseWrapper.getStatus().equals("OK")) {
                                UserInfo pageInfo = responseWrapper.getData();
                                SharedPreferences pref = LoginActivity.this.getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("userID", pageInfo.getUserID());
                                editor.putString("fName", pageInfo.getFirstName());
                                editor.putString("sName", pageInfo.getSecondName());
                                editor.putString("profession", pageInfo.getProfession());
                                //intent.putExtra("busyDays", pageInfo.getBusyData());
                                editor.commit();
                                EventorApp.getRetrofitProvider(LoginActivity.this).getMyPageApi().sendNotificationToken(FirebaseInstanceId.getInstance().getToken()).enqueue(new Callback<ResponseWrapper>() {
                                    @Override
                                    public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseWrapper> call, Throwable t) {

                                    }
                                });
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect login/password", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseWrapper<UserInfo>> call, Throwable throwable) {
                            loadingView.stopAnimation();
                            Toast.makeText(LoginActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Введите данные!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
