package ru.nsuorg.shiftorg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.UserInfo;

public class UserPageActivity extends AppCompatActivity {

    private TextView userName;
    private TextView profession;
    private TextView email;
    private TextView phone;
    private LoadingView loadingView;


    private static final String USER = "user";

    public static void start(Context context, UserInfo userInfo) {
        final Intent intent = new Intent(context, UserPageActivity.class);
        intent.putExtra(USER, userInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        loadingView = new LoadingView(this, (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0));

        userName = findViewById(R.id.userName);
        profession = findViewById(R.id.profession);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        UserInfo user = (UserInfo) getIntent().getSerializableExtra(USER);

        userName.setText(user.getLogin());
        profession.setText(user.getProfession());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
    }
}
