package ru.nsuorg.shiftorg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ru.nsuorg.shiftorg.animation.LoadingView;
import ru.nsuorg.shiftorg.fragments.EventsFragment;
import ru.nsuorg.shiftorg.fragments.NotificationsFragment;
import ru.nsuorg.shiftorg.fragments.UserPageFragment;


public class MainActivity extends AppCompatActivity {

    public static final String TAG_MY_PAGE_FRAGMENT = "my_page";
    public static final String TAG_NOTIFICATIONS_FRAGMENT = "notifications";
    public static final String TAG_EVENTS_FRAGMENT = "events";

    Toolbar toolbar;
    FrameLayout mainFrame;
    Drawer drawer;
    Fragment currentFragment;
    UserPageFragment userPageFragment;
    NotificationsFragment notificationsFragment;
    EventsFragment eventsFragment;

    Menu menu;
    MenuItem addEvent;

    Integer currentFragmentId;

    LoadingView loadingView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainFrame = findViewById(R.id.main_frame);

        loadingView = new LoadingView(this, (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0));

        if (savedInstanceState != null) {
            // create fragments
            currentFragmentId = savedInstanceState.getInt("fragmentId", 0);
            userPageFragment = (UserPageFragment) getSupportFragmentManager().findFragmentByTag(TAG_MY_PAGE_FRAGMENT);
            notificationsFragment = (NotificationsFragment) getSupportFragmentManager().findFragmentByTag(TAG_NOTIFICATIONS_FRAGMENT);
            eventsFragment = (EventsFragment) getSupportFragmentManager().findFragmentByTag(TAG_EVENTS_FRAGMENT);

            if (userPageFragment == null) {
                userPageFragment = new UserPageFragment();
            }
            if (notificationsFragment == null) {
                notificationsFragment = new NotificationsFragment();
            }
            if (eventsFragment == null) {
                eventsFragment = new EventsFragment();
            }

            currentFragment = getFragmentById(currentFragmentId);

        } else {
            // create fragments
            toolbar.setTitle(R.string.my_page);
            currentFragmentId = 1;
            userPageFragment = new UserPageFragment();
            notificationsFragment = new NotificationsFragment();
            eventsFragment = new EventsFragment();

            // initialize first fragment
            FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.add(R.id.main_frame, getFragmentById(currentFragmentId));
            fTrans.commit();
            currentFragment = getFragmentById(currentFragmentId);
        }

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.my_page),
                        new PrimaryDrawerItem().withName(R.string.my_events),
                        new PrimaryDrawerItem().withName(R.string.notifications),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.exit))
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                    // add new fragments here
                    switch (position) {
                        case 1:
                            setActivityStateForFragmentById(position);
                            fTrans.remove(currentFragment);
                            fTrans.add(R.id.main_frame, userPageFragment, TAG_MY_PAGE_FRAGMENT);
                            currentFragment = userPageFragment;
                            currentFragmentId = position;
                            fTrans.commit();
                            break;
                        case 2:
                            setActivityStateForFragmentById(position);
                            fTrans.remove(currentFragment);
                            fTrans.add(R.id.main_frame, eventsFragment, TAG_EVENTS_FRAGMENT);
                            currentFragment = eventsFragment;
                            currentFragmentId = position;
                            fTrans.commit();
                            break;
                        case 3:
                            setActivityStateForFragmentById(position);
                            fTrans.remove(currentFragment);
                            fTrans.add(R.id.main_frame, notificationsFragment, TAG_NOTIFICATIONS_FRAGMENT);
                            //fTrans.replace(R.id.main_frame,notificationsFragment);
                            currentFragment = notificationsFragment;
                            currentFragmentId = position;
                            fTrans.commit();
                            break;
                        case 5:
                           MainActivity.this.finish();
                            break;
                        default:
                            break;

                    }

                    return false;
                }).build();
        if (currentFragment == null) {
            currentFragment = new UserPageFragment();
        }
    }

    private Fragment getFragmentById(int id) {
        switch (id) {
            case 1:
                return userPageFragment;
            case 2:
                return eventsFragment;
            case 3:
                return notificationsFragment;
            default:
                return null;
        }
    }

    private void setActivityStateForFragmentById(int fragmentId) {
        switch (fragmentId) {
            case 1: // my page
                toolbar.setTitle(R.string.my_page);
                addEvent.setVisible(false);
                break;
            case 2: // events
                toolbar.setTitle(R.string.my_events);
                addEvent.setVisible(true);
                break;
            case 3: // notification
                toolbar.setTitle(R.string.notifications);
                addEvent.setVisible(false);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addEvent) {
            Intent intent = new Intent(this, CreateEventActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public LoadingView getLoadingView() {
        return loadingView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        this.menu = menu;
        addEvent = menu.findItem(R.id.addEvent);
        setActivityStateForFragmentById(currentFragmentId);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragmentId", currentFragmentId);
    }

    @Override
    public void onBackPressed() {

    }
}
