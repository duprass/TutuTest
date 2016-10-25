package com.rolea.tututest;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rolea.tututest.helpers.ToolbarManipulation;
import com.rolea.tututest.helpers.Util;
import com.rolea.tututest.model.Station;

import java.lang.reflect.Type;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ScheduleFragment.OnChooseStationListener,
        ToolbarManipulation, SearchStationFragment.OnSearchFragmentInteractionListener
{
    private static final String ARG_SELECTED_STATIONS_KEY = "selected-stations";

    // Views
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar toolbar;
    private Fragment fragment;
    private NavigationView navigationView;

    //Unique name of fragment
    private String name;

    // Chosen Station according to stationFrom/StationTo
    private HashMap<Integer, Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolbar();
        initializeNavigationView();
        stations = new HashMap<>();

        // handle orientations changes
        if (savedInstanceState == null) {
            setFirstFragment();
        } else { // i orientation was changed, then restore state of activity
            // set stations
            Gson gson = new Gson();
            Type listOfTestObject = new TypeToken<HashMap<Integer, Station>>() {
            }.getType();

            stations = gson.fromJson(savedInstanceState.getString(ARG_SELECTED_STATIONS_KEY), listOfTestObject);
        }

    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // if it's first fragment then exit from app
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    finish();
                } else { //otherwise return fragment from backstack
                    getSupportFragmentManager().popBackStackImmediate();
                }
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == android.R.id.home) {

            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            // Handle the camera action
            fragment = ScheduleFragment.newInstance();
            name = fragment.getClass().getName();
        } else if (id == R.id.nav_copyright) {
            fragment = CopyrightFragment.newInstance();
            name = fragment.getClass().getName();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                hideKeyboard();

                if (fragment !=null){
                    replaceFragment(fragment, name);
                    fragment = null;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }
        };
        drawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void setFirstFragment() {
        Fragment firstfragment;

        firstfragment = ScheduleFragment.newInstance();
        name = firstfragment.getClass().getName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, firstfragment, name).addToBackStack(name).commitAllowingStateLoss();
    }

    private void replaceFragment(Fragment fragment, String name, int enter, int ext) {
        String backStateName = name;
        String fragmentTag = backStateName;

        // get ragment from backstack if there is
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        //fragment not in back stack, create it.
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            if (enter != -1 && ext != -1) {
                ft.setCustomAnimations(enter, ext);
            }
            ft.replace(R.id.main_content, fragment, fragmentTag);

            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private void replaceFragment(Fragment fragment, String name) {
        replaceFragment(fragment, name, -1, -1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_SELECTED_STATIONS_KEY, Util.getHashMapStationJson(stations));
        super.onSaveInstanceState(outState);
    }

    /**
     * Method to move on Search Station Screen
     *
     * @param Type Type of Search Station Screen
     */
    @Override
    public void onStationChoose(int Type) {
        fragment = SearchStationFragment.newInstance(Type);
        name = String.format("%s%s", fragment.getClass().getName(), Type);
        replaceFragment(fragment, name);
    }

    @Override
    public String getStationFrom() {
        return stations.get(Util.TYPE_STATION_FROM) == null ? null : stations.get(Util.TYPE_STATION_FROM).getStationTitle();
    }

    @Override
    public String getStationTo() {
        return stations.get(Util.TYPE_STATION_TO) == null ? null : stations.get(Util.TYPE_STATION_TO).getStationTitle();
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    /**
     *  Method to control whether show back or haburger button on toolbar
     * @param show if show is true then show back buton, otherwis habmurger
     */
    @Override
    public void showBackButton(boolean show) {
        if (show) {
            mActionBarDrawerToggle.setDrawerIndicatorEnabled(!show);
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            mActionBarDrawerToggle.setDrawerIndicatorEnabled(!show);
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /**
     * Method to choose station stations and returhn on schedule screen
     * @param item chosen station
     * @param type type of SearchSctations screen: Stations from or Staions To
     */
    @Override
    public void onChooseInteraction(Station item, int type) {
        // add chosen stations
        stations.put(type, item);
        // replace fragments
        fragment = ScheduleFragment.newInstance();
        name = fragment.getClass().getName();
        replaceFragment(fragment, name);
    }

    /**
     * Method to move on screen with detail info about station
     * @param station
     */
    @Override
    public void onDetailStationViewInteraction(Station station) {
        fragment = DetailStationFragment.newInstance(station);
        name = fragment.getClass().getName();
        replaceFragment(fragment, name);
    }
}
