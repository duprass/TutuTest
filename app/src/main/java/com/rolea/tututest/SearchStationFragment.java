package com.rolea.tututest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.rolea.tututest.helpers.ToolbarManipulation;
import com.rolea.tututest.helpers.Util;
import com.rolea.tututest.model.JsonResponse;
import com.rolea.tututest.model.Station;

import java.io.IOException;
import java.io.InputStream;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnSearchFragmentInteractionListener}
 * interface.
 */
public class SearchStationFragment extends Fragment implements SearchView.OnQueryTextListener {

    // TODO: Customize parameter argument names
    private static final String STATE_TASK_RUNNING = "task-running";
    private static final String SEARCH_QUERY_KEY = "search-query";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static JsonResponse listCities;
    // TODO: Customize parameters
    private int type = 0;
    private OnSearchFragmentInteractionListener mListener;
    private ToolbarManipulation mToolbarCallback;
    private RecyclerView recyclerView;
    private StationRecyclerViewAdapter adapter;
    private CitiesParse loadCitysyncTask;

    private String searchQuery;
    private String searchQueryToRestore;
    private MenuItem searchItem;
    private SearchView searchView;
    private ImageView searchIcon;
    private ProgressBar progressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchStationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchStationFragment newInstance(int type) {
        SearchStationFragment fragment = new SearchStationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchView.setOnQueryTextListener(this);
        if (adapter == null) {
            searchIcon.setEnabled(false);
        } else {
            searchIcon.setEnabled(true);

            if (!TextUtils.isEmpty(searchQueryToRestore)) {
                restoreSearchView();

            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchQuery = newText.toLowerCase();
        adapter.filter(searchQuery);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = query.toLowerCase();
        adapter.filter(searchQuery);

        return true;
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_statioin_list, container, false);
            setHasOptionsMenu(true);
            mToolbarCallback.showBackButton(true);
            initializeViews(view);

            // handle orientaition changes
            if (savedInstanceState == null) {
                searchQuery = "";
                searchQueryToRestore = "";
                if (listCities == null) {
                    startLoadCity();
                } else {
                    setAdapter();
                }
            } else {
                searchQueryToRestore = savedInstanceState.getString(SEARCH_QUERY_KEY);
                if (savedInstanceState.getBoolean(STATE_TASK_RUNNING)) {
                    startLoadCity();
                } else {
                    setAdapter();
                }
        }
        return view;
        }

    private void setAdapter() {
        adapter = new StationRecyclerViewAdapter(listCities.getCitiesByType(type), type, mListener);
        recyclerView.setAdapter(adapter);
        Util.changeVisibleView(progressBar, false);
        Util.changeVisibleView(recyclerView, true);
    }

    private void initializeViews(View view) {
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    /**
     * Restore State of SearchView
     */
    private void restoreSearchView() {
        searchView.onActionViewExpanded();
        searchView.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(searchQueryToRestore)) {
                    searchView.setQuery(searchQueryToRestore, true);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbarCallback.setTitle(getTitle());
    }

    private String getTitle() {
        return type == Util.TYPE_STATION_FROM ? getString(R.string.station_from) : getString(R.string.station_to);
    }

    private void startLoadCity() {
        loadCitysyncTask = new CitiesParse();
        loadCitysyncTask.execute();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFragmentInteractionListener) {
            mListener = (OnSearchFragmentInteractionListener) context;
            mToolbarCallback = (ToolbarManipulation) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchFragmentInteractionListener");
        }
    }

    private boolean isTaskRunning() {
        return (loadCitysyncTask != null) && (loadCitysyncTask.getStatus() == AsyncTask.Status.RUNNING);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mToolbarCallback = null;

        // cancel Task if it's runnig
        if (isTaskRunning()) {
            loadCitysyncTask.cancel(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If the task is running, save it in our state
        outState.putBoolean(STATE_TASK_RUNNING, isTaskRunning());
        // save current search query
        outState.putString(SEARCH_QUERY_KEY, searchQuery);
    }

    /**
     * Load json from allStations.json
     *
     * @return json String with info from file
     */
    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getContext().getAssets().open("allStations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public interface OnSearchFragmentInteractionListener {

        void onChooseInteraction(Station item, int type);

        void onDetailStationViewInteraction(Station station);
    }

    private class CitiesParse extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            listCities = gson.fromJson(loadJSONFromAsset(), JsonResponse.class);

            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            //set recycler view
            setAdapter();

            //enable search view
            searchIcon.setEnabled(true);
        }
    }


}
