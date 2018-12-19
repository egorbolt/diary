package diaryteam.nsu.ru.motivationaldiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.*;

import static diaryteam.nsu.ru.motivationaldiary.MainActivity.client;
import diaryteam.nsu.ru.motivationaldiary.entity_models.UserInfo;

public class SignupActivity extends AppCompatActivity {
    private static final String tag = "SignupActivity";
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private TextView backToLogin;
    private Button signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        backToLogin = findViewById(R.id.backToLogin);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup() {
        Log.d(tag, "Signup");

        String _name = name.getText().toString();
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        String _passwordConfirm = passwordConfirm.getText().toString();

        if (!validate(_name, _email, _password, _passwordConfirm)) {
            Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
            signupButton.setEnabled(true);
            return;
        }

        UserInfo userInfo = new UserInfo(null, _name, _email, _password.hashCode());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), createJSON(_name, _email, _password.hashCode()).toString());
        Request request = new Request.Builder()
                .url("http://localhost:8080/registration")
                .post(body)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            handleResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean validate(String _name, String _email, String _password, String _passwordConfirm) {
        boolean valid = true;


        if (_name.isEmpty()) {
            name.setError("enter a name");
            valid = false;
        } else {
            name.setError(null);
        }

        if (_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (_password.isEmpty() || _passwordConfirm.isEmpty() || !_passwordConfirm.equals(_password)) {
            password.setError("password confirmation does not match");
            passwordConfirm.setError("password confirmation does not match");
            valid = false;
        } else {
            password.setError(null);
            passwordConfirm.setError(null);
        }

        return valid;
    }

    private JSONObject createJSON(String name, String email, int hashPassword) {
        JSONObject json = new JSONObject();
        try {
            json.put("login", name);
            json.put("email", email);
            json.put("hashPassword", hashPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void handleResponse(Response response) throws IOException {
        if (response.code() == 401) {
            Toast.makeText(SignupActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
        }
        if (response.code() == 404) {
            Toast.makeText(SignupActivity.this, "404 :c", Toast.LENGTH_SHORT).show();
        }
        if (response.code() == 200) {
            Toast.makeText(SignupActivity.this, "Registration was successful", Toast.LENGTH_SHORT).show();
        }
    }
}
