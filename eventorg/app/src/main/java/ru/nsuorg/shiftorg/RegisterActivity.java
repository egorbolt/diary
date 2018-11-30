package ru.nsuorg.shiftorg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.UserInfo;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private Button backToLoginButton;
    private EditText login;
    private EditText firsname;
    private EditText secondname;
    private EditText email;
    private EditText password;
    private EditText confirm;
    private EditText phone;
    private String profession;
    private List<String> professionsList;
    private LoadingView loadingView;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phone = findViewById(R.id.phone);
        login = findViewById(R.id.loginReg);
        secondname = findViewById(R.id.secondname);
        registerButton = findViewById(R.id.register);
        backToLoginButton = findViewById(R.id.login);
        firsname = findViewById(R.id.firstname);
        secondname = findViewById(R.id.secondname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.passwordConfirm);
        professionsList = getIntent().getStringArrayListExtra("professions");
        Log.d("professions", professionsList.toString());
                //profession = findViewById(R.id.profession);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, professionsList);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner = findViewById(R.id.professions);
        spinner.setAdapter(adapter);
                spinner.setSelection(0);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        spinner.setSelection(i);
                        profession = professionsList.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        loadingView = new LoadingView(this, (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0));

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n1 = firsname.getText().toString();
                String n2 = secondname.getText().toString();
                String l = login.getText().toString();
                String m = email.getText().toString();
                String p = password.getText().toString();
                String ph = phone.getText().toString();
                String c = confirm.getText().toString();
                String pr = profession;
                if (!n1.equals("") && (!n2.equals("")) && (!pr.equals("")) && (!l.equals("")) && (!m.equals("")) && !p.equals("") && (!c.equals("")) && (!ph.equals(""))) {
                    if (p.equals(c)) {
                        loadingView.startAnimation();
                        UserInfo userInfo = new UserInfo(null, pr, n1, n2, l, m, p, ph, null);
                        EventorApp.getRetrofitProvider(RegisterActivity.this).getRegisterApi().register(userInfo).enqueue(new Callback<ResponseWrapper<UserInfo>>() {
                            @Override
                            public void onResponse(Call<ResponseWrapper<UserInfo>> call, Response<ResponseWrapper<UserInfo>> response) {
                                loadingView.stopAnimation();
                                ResponseWrapper<UserInfo> registerInfo = response.body();
                                if (registerInfo != null && registerInfo.getStatus().equals("OK")) {
                                    Toast.makeText(RegisterActivity.this, "Registration was successful", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                                if (registerInfo != null && registerInfo.getStatus().equals("USER_EXIST")) {
                                    Toast.makeText(RegisterActivity.this, "Email address is busy", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseWrapper<UserInfo>> call, Throwable throwable) {
                                loadingView.stopAnimation();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Wrong confirmation", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}