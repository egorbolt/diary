package ru.nsuorg.shiftorg.fragments.events;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsuorg.shiftorg.EventorApp;
import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.adapters.EventsAdapter;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.entity_models.ResponseWrapper;

import static android.content.Context.MODE_PRIVATE;

public class PageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    LoadingView loadingView;
    private EventsAdapter adapter;
    private String role;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View containerView;

    public PageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_page, container, false);
        linkViews();
        initEventsAdapter();
        initRefresher();
        initRecyclerView();
        role = getArguments().getString("role", "ORG");
        return containerView;
    }

    public void loadEvents(boolean animate) {
        if (animate)
            try {
                loadingView.startAnimation();
            } catch (Exception e) {
            }
        SharedPreferences pref = getContext().getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        String userID = pref.getString("userID", "0");
        EventorApp.getRetrofitProvider(getActivity()).getEventsApi().getEvents(userID, 100, 0, role).enqueue(new Callback<ResponseWrapper<List<EventInfo>>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWrapper<List<EventInfo>>> call, @NonNull Response<ResponseWrapper<List<EventInfo>>> response) {
                loadingView.stopAnimation();
                swipeRefreshLayout.setRefreshing(false);
                ResponseWrapper<List<EventInfo>> wrapper = response.body();
                if (wrapper != null) {
                    adapter.clearItems();
                    List<EventInfo> list = new ArrayList<>();
                    for (EventInfo event :
                            wrapper.getData()) {
                        if (role.equals("ORG")) {
                            if (event.getOrganizer().getUserID().equals(userID)) {
                                list.add(event);
                            }
                        } else {
                            if (!event.getOrganizer().getUserID().equals(userID)) {
                                list.add(event);
                            }
                        }
                    }
                    adapter.setItems(list);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWrapper<List<EventInfo>>> call, @NonNull Throwable t) {
                try {
                    loadingView.stopAnimation();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
            }
        }
    });
}

    private void initEventsAdapter() {
        adapter = new EventsAdapter();
        adapter.setLoadingView(loadingView);
        adapter.setContext(getContext());
    }

    private void initRefresher() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void linkViews() {
        recyclerView = containerView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = containerView.findViewById(R.id.pullToRefresh);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents(true);
    }

    public void setLoadingView(LoadingView loadingView) {
        this.loadingView = loadingView;
    }

    @Override
    public void onRefresh() {
        loadEvents(false);
    }
}
