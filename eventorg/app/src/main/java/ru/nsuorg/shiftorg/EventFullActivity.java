package ru.nsuorg.shiftorg;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.nsuorg.shiftorg.adapters.ParticipantsAdapter;
import ru.nsuorg.shiftorg.adapters.ServiceAdapter;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.ServiceInfo;
import ru.nsuorg.shiftorg.entity_models.UserInfo;

public class EventFullActivity extends AppCompatActivity {
    private TextView tvEventName;
    private TextView tvDate;
    private RecyclerView participantsView;
    private RecyclerView servicesView;
    private ParticipantsAdapter partAdaper;
    private ServiceAdapter serviceAdapter;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        tvDate = findViewById(R.id.date);
        tvEventName = findViewById(R.id.eventName);
        participantsView = findViewById(R.id.participantList);
        servicesView = findViewById(R.id.needList);
        edit = findViewById(R.id.editButton);

        final EventInfo event = (EventInfo) getIntent().getSerializableExtra("event");

        tvEventName.setText(event.getEventName());
        String date = event.getPeriod().getStart() + " - " + event.getPeriod().getEnd();
        tvDate.setText(date);
        partAdaper = new ParticipantsAdapter();
        serviceAdapter = new ServiceAdapter();
        serviceAdapter.setEventID(event.getEventID());
        serviceAdapter.setContext(this);


        ArrayList<UserInfo> partsWithService = new ArrayList<>();
        ArrayList<ServiceInfo> serviceWithoutParts = new ArrayList<>();
        ArrayList<ServiceInfo> services = new ArrayList<>();

        for (ServiceInfo serviceInfo : event.getRequiredProfessions()
                ) {
            if (event.getPerformers().containsKey(serviceInfo.getServiceID())) {
                for (UserInfo user:
                        event.getParticipantsInfo()
                     ) {
                    if (user.getUserID().equals(event.getPerformers().get(serviceInfo.getServiceID()))) {
                        partsWithService.add(user);
                        services.add(serviceInfo);
                    }
                }
            } else {
                serviceWithoutParts.add(serviceInfo);
            }
        }

        partAdaper.setItems(partsWithService, services);
        partAdaper.setContext(this);
        serviceAdapter.setItems(serviceWithoutParts);
        participantsView.setAdapter(partAdaper);
        servicesView.setAdapter(serviceAdapter);
        participantsView.setLayoutManager(new LinearLayoutManager(this));
        servicesView.setLayoutManager(new LinearLayoutManager(this));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditEventActivity.start(EventFullActivity.this, event);
            }
        });
    }
}
