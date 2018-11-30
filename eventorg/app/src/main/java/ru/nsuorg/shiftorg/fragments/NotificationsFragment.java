package ru.nsuorg.shiftorg.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.EventorApp;
import ru.nsuorg.shiftorg.LoginActivity;
import ru.nsuorg.shiftorg.MainActivity;
import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.adapters.NotificationAdapter;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.animation.SimpleDividerItemDecoration;
import ru.nsuorg.shiftorg.entity_models.NotificationInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;

    View containerView;
    RecyclerView recyclerView;
    LoadingView loadingView;
    NotificationAdapter adapter = new NotificationAdapter();

    public NotificationsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        containerView = inflater.inflate(R.layout.fragment_notifications, container, false);
        linkViews();
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter.clearItems();
        adapter.setContext(getContext());
        adapter.setLoadingView(loadingView);
        return containerView;
    }

    public void loadNotifications(boolean animate) {
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        String userID = pref.getString("userID", "0");
        if (animate)
            loadingView.startAnimation();
        EventorApp.getRetrofitProvider(getContext()).getNotificationApi().getNotifications(userID, 100, 0).enqueue(new Callback<ResponseWrapper<List<NotificationInfo>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWrapper<List<NotificationInfo>>> call, @NonNull Response<ResponseWrapper<List<NotificationInfo>>> response) {
                loadingView.stopAnimation();
                swipeRefreshLayout.setRefreshing(false);
                ResponseWrapper<List<NotificationInfo>> wrapper = response.body();
                if (wrapper != null) {
                    adapter.clearItems();
                    adapter.setItems(wrapper.getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWrapper<List<NotificationInfo>>> call, @NonNull Throwable t) {
                loadingView.stopAnimation();
                try{
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                }catch (Exception e){}
            }
        });
    }

    private void linkViews() {
        loadingView = ((MainActivity) getActivity()).getLoadingView();
        recyclerView = containerView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = containerView.findViewById(R.id.swipeToRefresh);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotifications(true);
    }

    @Override
    public void onRefresh() {
        loadNotifications(false);
    }
}
