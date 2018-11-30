package ru.nsuorg.shiftorg;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.ServiceInfo;
import ru.nsuorg.shiftorg.entity_models.TimePeriodInfo;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class CreateEventActivity extends AppCompatActivity {
    private EditText name_event;
    private EditText data_start;
    private EditText data_end;
    private Spinner prof1;
    private EditText to1;
    private EditText from1;
    private EditText infoText;

    LoadingView loadingView;

    List<View> services = new ArrayList<>();
    Button add_position_button;
    Button continue_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        loadingView = new LoadingView(this, (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0));

        linkViews();

        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("____-__-__ __:__");
        MaskImpl mask = MaskImpl.createTerminated(slots);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(data_start);
        watcher = new MaskFormatWatcher(mask);
        watcher.installOn(data_end);
        watcher = new MaskFormatWatcher(mask);
        watcher.installOn(from1);
        watcher = new MaskFormatWatcher(mask);
        watcher.installOn(to1);


        final LinearLayout linLayout = findViewById(R.id.list);
        final LayoutInflater ltInflater = getLayoutInflater();

        continue_button.setOnClickListener(view -> {
            if (!name_event.getText().toString().equals("") && !data_start.getText().toString().equals("") && !data_end.getText().toString().equals("")) {
                ArrayList<ServiceInfo> serviceInfos = new ArrayList<>(2);
                if (!prof1.getSelectedItem().toString().equals("") && !to1.getText().toString().equals("") && !from1.getText().toString().equals("")) {
                    serviceInfos.add(new ServiceInfo(null, prof1.getSelectedItem().toString(), new TimePeriodInfo(from1.getText().toString(), to1.getText().toString()), false));
                }
                for (View v :
                        services) {
                    Spinner etProf = v.findViewById(R.id.etProfession);
                    EditText etFrom = v.findViewById(R.id.etFrom);
                    EditText etTo = v.findViewById(R.id.etTo);
                    if (!etProf.getSelectedItem().toString().equals("") && !etFrom.getText().toString().equals("") && !etTo.getText().toString().equals("")) {
                        serviceInfos.add(new ServiceInfo(null, etProf.getSelectedItem().toString(), new TimePeriodInfo(etFrom.getText().toString(), etTo.getText().toString()), false));
                    }
                }
                if (serviceInfos.size() == 0)
                    Toast.makeText(CreateEventActivity.this, "Enter data", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
                String userID = pref.getString("userID", "0");
                EventInfo info = new EventInfo(null, name_event.getText().toString(), new TimePeriodInfo(data_start.getText().toString(), data_end.getText().toString()),
                        userID, null, null, null, serviceInfos, infoText.getText().toString(), null);
                loadingView.startAnimation();
                EventorApp.getRetrofitProvider(CreateEventActivity.this).getEventsApi().createEvent(info).enqueue(new Callback<ResponseWrapper<String>>() {
                    @Override
                    public void onResponse(Call<ResponseWrapper<String>> call, Response<ResponseWrapper<String>> response) {
                        loadingView.stopAnimation();
                        ResponseWrapper wrapper = response.body();
                        if (wrapper != null) {
                            if (wrapper.getStatus().equals("OK")) {
                                Toast.makeText(CreateEventActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            if (wrapper.getStatus().equals("USER_NOT_FOUND")) {
                                Toast.makeText(CreateEventActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                            if (wrapper.getStatus().equals("BAD_TIME")) {
                                Toast.makeText(CreateEventActivity.this, "Bad time", Toast.LENGTH_SHORT).show();
                            }
                            if (wrapper.getStatus().equals("BUSY_DATE")) {
                                Toast.makeText(CreateEventActivity.this, "Busy date", Toast.LENGTH_SHORT).show();
                            }
                            if (wrapper.getStatus().equals("BAD_SERVICE_DATE")) {
                                Toast.makeText(CreateEventActivity.this, "Bad service date", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWrapper<String>> call, Throwable t) {
                        loadingView.stopAnimation();
                        Toast.makeText(CreateEventActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(CreateEventActivity.this, "Enter data", Toast.LENGTH_SHORT).show();
            }
        });
        add_position_button.setOnClickListener(view -> {
            View view_profession = ltInflater.inflate(R.layout.view_profession, linLayout, false);
            Slot[] slots1 = new UnderscoreDigitSlotsParser().parseSlots("____-__-__ __:__");
            MaskImpl mask1 = MaskImpl.createTerminated(slots1);
            FormatWatcher watcher1 = new MaskFormatWatcher(mask1);
            watcher1.installOn((view_profession.findViewById(R.id.etFrom)));
            watcher1 = new MaskFormatWatcher(mask1);
            watcher1.installOn((view_profession.findViewById(R.id.etTo)));
            linLayout.addView(view_profession);
            services.add(view_profession);
            Log.d("TAGGGG", String.valueOf(linLayout.getChildCount()));
        });


    }

    private void linkViews() {
        name_event = findViewById(R.id.eventName);
        data_start = findViewById(R.id.startDate);
        data_end = findViewById(R.id.endDate);
        prof1 = findViewById(R.id.prof1);
        to1 = findViewById(R.id.to1);
        from1 = findViewById(R.id.from1);
        continue_button = findViewById(R.id.continueButton);
        add_position_button = findViewById(R.id.addPosition);
        infoText = findViewById(R.id.info);
    }
}
