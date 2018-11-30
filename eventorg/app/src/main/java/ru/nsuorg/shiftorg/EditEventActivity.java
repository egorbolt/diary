package ru.nsuorg.shiftorg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.adapters.EditNeedAdapter;
import ru.nsuorg.shiftorg.adapters.EditParticipantsAdapter;
import ru.nsuorg.shiftorg.adapters.ParticipantsAdapter;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.ServiceInfo;
import ru.nsuorg.shiftorg.entity_models.TimePeriodInfo;

public class EditEventActivity extends AppCompatActivity {
    private EditText eventName;
    private EditText startDate;
    private EditText endDate;
    private RecyclerView needView;
    private RecyclerView participantsView;
    private EditNeedAdapter needAdaper;
    private EditParticipantsAdapter participantsAdapter;
    private Button addPosition;
    private Button continueButton;
    private Button deleteAll;
    List<View> services = new ArrayList<>();
    LoadingView loadingView;

    private static final String EVENT = "event";

    public static void start(Context context, EventInfo eventInfo) {
        final Intent intent = new Intent(context, EditEventActivity.class);
        intent.putExtra(EVENT, eventInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        loadingView = new LoadingView(this, (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0));

        eventName = findViewById(R.id.eventName);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        participantsView = findViewById(R.id.
                partList);
        needView = findViewById(R.id.needList);
        addPosition = findViewById(R.id.addPosition);
        continueButton = findViewById(R.id.continueButton);
        deleteAll = findViewById(R.id.deleteEvent);

        EventInfo event = (EventInfo) getIntent().getSerializableExtra(EVENT);

        eventName.setText(event.getEventName());
        startDate.setText(event.getPeriod().getStart());
        endDate.setText(event.getPeriod().getEnd());

        needAdaper = new EditNeedAdapter();
        participantsAdapter = new EditParticipantsAdapter();
        needAdaper.setItems(event.getRequiredProfessions());
        participantsAdapter.setItems(event.getParticipantsInfo());
        participantsAdapter.setContext(this);
        needView.setAdapter(needAdaper);
        participantsView.setAdapter(participantsAdapter);
        participantsView.setLayoutManager(new LinearLayoutManager(this));
        needView.setLayoutManager(new LinearLayoutManager(this));
        final String eventID = event.getEventID();

        final LayoutInflater ltInflater = getLayoutInflater();

        addPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view_profession = ltInflater.inflate(R.layout.view_profession, needView, false);
                needView.addView(view_profession);
                services.add(view_profession);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!eventName.getText().toString().equals(""))||(!startDate.getText().toString().equals(""))||(!endDate.getText().toString().equals(""))||(!participantsAdapter.getItems().equals(event.getParticipantsInfo()))||(!needAdaper.getItems().equals(event.getRequiredProfessions()))) {
                    TimePeriodInfo period = new TimePeriodInfo(startDate.getText().toString(), endDate.getText().toString());

                    if (eventName.getText().toString().equals(""))
                        eventName.setText(eventName.getHint());
                    if (startDate.getText().toString().equals(""))
                        startDate.setText(startDate.getHint());
                    if (endDate.getText().toString().equals(""))
                        endDate.setText(endDate.getHint());

                    for (int i = 0; i < participantsView.getAdapter().getItemCount(); ++i) {
                        EditParticipantsAdapter.ParticipantInfoViewHolder holder = (EditParticipantsAdapter.ParticipantInfoViewHolder)participantsView.findViewHolderForAdapterPosition(i);
                        if (holder.getStartDate().getText().toString().equals("")){
                            holder.getStartDate().setText(holder.getStartDate().getHint());
                        }
                        if (holder.getEndDate().getText().toString().equals("")){
                            holder.getEndDate().setText(holder.getEndDate().getHint());
                        }
                    }

                    for (int i = 0; i < needView.getAdapter().getItemCount(); ++i) {
                        EditNeedAdapter.ViewHolder holder = (EditNeedAdapter.ViewHolder)needView.findViewHolderForAdapterPosition(i);
                        if (holder.getStartDate().getText().toString().equals("")){
                            holder.getStartDate().setText(holder.getStartDate().getHint());
                        }
                        if (holder.getEndDate().getText().toString().equals("")){
                            holder.getEndDate().setText(holder.getEndDate().getHint());
                        }
                    }

                    EventInfo info = new EventInfo(eventID, eventName.getText().toString(), period, event.getOrgId(), event.getOrganizer(), event.getParticipantsInfo(), event.getParticipantsId(), event.getRequiredProfessions(), event.getInfo(), event.getPerformers());
                    loadingView.startAnimation();

                    EventorApp.getRetrofitProvider(EditEventActivity.this).getEventsApi().editEvent(info).enqueue(new Callback<ResponseWrapper>() {
                        @Override
                        public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                            loadingView.stopAnimation();
                            ResponseWrapper wrapper = response.body();
                            if (wrapper != null) {
                                if (wrapper.getStatus().equals("OK")) {
                                    Toast.makeText(EditEventActivity.this, "Event edited", Toast.LENGTH_SHORT).show();
                                    EditEventActivity.this.onBackPressed();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                            loadingView.stopAnimation();
                            Toast.makeText(EditEventActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventorApp.getRetrofitProvider(EditEventActivity.this).getEventsApi().deleteEvent(eventID).enqueue(new Callback<ResponseWrapper<String>>() {
                    @Override
                    public void onResponse(Call<ResponseWrapper<String>> call, Response<ResponseWrapper<String>> response) {
                        loadingView.stopAnimation();
                        ResponseWrapper wrapper = response.body();
                        if (wrapper != null) {
                            if (wrapper.getStatus().equals("OK")) {
                                Toast.makeText(EditEventActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                                EditEventActivity.this.onBackPressed();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWrapper<String>> call, Throwable t) {
                        loadingView.stopAnimation();
                        Toast.makeText(EditEventActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
