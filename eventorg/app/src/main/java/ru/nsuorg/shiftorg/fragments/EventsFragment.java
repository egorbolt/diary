package ru.nsuorg.shiftorg.fragments;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.nsuorg.shiftorg.MainActivity;
import ru.nsuorg.shiftorg.R;
import ru.nsuorg.shiftorg.adapters.EventsAdapter;
import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.entity_models.EventInfo;
import ru.nsuorg.shiftorg.fragments.events.EventsPageAdapter;
import ru.nsuorg.shiftorg.fragments.events.PageFragment;


public class EventsFragment extends Fragment {

    public enum Role {
        ORG,
        PART
    }

    PageFragment orgFragment;
    PageFragment partFragment;


    private View containerView;
    private LoadingView loadingView;
    List<EventInfo> orgList;
    List<EventInfo> partList;
    ViewPager pager;

    public EventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        containerView = inflater.inflate(R.layout.fragment_event, container, false);
        pager = containerView.findViewById(R.id.pager);
        loadingView = ((MainActivity) getActivity()).getLoadingView();
        EventsPageAdapter pageAdapter = new EventsPageAdapter(getChildFragmentManager());
        orgFragment = new PageFragment();
        partFragment = new PageFragment();

        orgFragment.setLoadingView(loadingView);
        partFragment.setLoadingView(loadingView);

        Bundle orgBundle = new Bundle();
        orgBundle.putString("role", "ORG");
        orgFragment.setArguments(orgBundle);
        Bundle partBundle = new Bundle();
        partBundle.putString("role", "PART");
        partFragment.setArguments(partBundle);
        pageAdapter.addFragment(orgFragment, getString(R.string.org));
        pageAdapter.addFragment(partFragment, getString(R.string.part));
        TabLayout tabs = containerView.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(pager);
        pager.setAdapter(pageAdapter);


        return containerView;
    }
}
