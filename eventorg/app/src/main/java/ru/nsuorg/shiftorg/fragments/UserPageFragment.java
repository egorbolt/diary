package ru.nsuorg.shiftorg.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.EventorApp;
import ru.nsuorg.shiftorg.MainActivity;
import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;
import ru.nsuorg.shiftorg.entity_models.TimePeriodInfo;
import ru.nsuorg.shiftorg.entity_models.UserInfo;

import static android.content.Context.MODE_PRIVATE;

public class UserPageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View containerView;
    LoadingView loadingView;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView tvName;
    TextView tvProfession;
    CompactCalendarView calendar;

    public UserPageFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_user_page, container, false);

        linkViews();
        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        setFields(prefs.getString("fName", "empty") + " " + prefs.getString("sName", "empty"), prefs.getString("profession", "empty"));

        return containerView;
    }

    public void loadInfo(final boolean animate) {
        if (animate)
            loadingView.startAnimation();
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        String userID = pref.getString("userID", "0");
        EventorApp.getRetrofitProvider(getContext()).getMyPageApi().getData(userID).enqueue(new Callback<ResponseWrapper<UserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWrapper<UserInfo>> call, @NonNull Response<ResponseWrapper<UserInfo>> response) {
                loadingView.stopAnimation();
                swipeRefreshLayout.setRefreshing(false);
                ResponseWrapper<UserInfo> wrapper = response.body();
                if (wrapper != null) {
                    UserInfo result = wrapper.getData();
                    Log.d(getString(R.string.log_tag), "Got user info " + result.getFirstName() + " " + result.getSecondName() + " " + result.getProfession());
                    setFields(result.getFirstName() + " " + result.getSecondName(), result.getProfession());
                    List<TimePeriodInfo> days = result.getBusyData();
                    if (days != null)
                        Log.d("MY_TAGGAGGA", "days: " + days.size());
                    setCalendar(days);
                } else {
                    Log.d(getString(R.string.log_tag), "Null result");
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseWrapper<UserInfo>> call, @NonNull Throwable t) {
                try {
                    Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    loadingView.stopAnimation();
                    swipeRefreshLayout.setRefreshing(false);
                }catch (Exception e){}
            }
        });
    }

    private void setCalendar(List<TimePeriodInfo> days) {
        if (days != null)
            for (TimePeriodInfo day : days) {
                try {
                    Log.d("MY_TAGGAGGA", day.getStart() + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(day.getStart()).getTime());
                    calendar.addEvent(new Event(Color.RED, new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(day.getStart()).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInfo(true);
    }

    private void setFields(String name, String prof) {
        tvName.setText(name);
        tvProfession.setText(prof);
    }

    private void linkViews() {
        tvName = containerView.findViewById(R.id.tvName);
        tvProfession = containerView.findViewById(R.id.tvProfession);
        loadingView = ((MainActivity) getActivity()).getLoadingView();
        calendar = containerView.findViewById(R.id.calendar);
        swipeRefreshLayout = containerView.findViewById(R.id.pullToRefresh);
    }

    @Override
    public void onRefresh() {
        loadInfo(false);

    }
}
