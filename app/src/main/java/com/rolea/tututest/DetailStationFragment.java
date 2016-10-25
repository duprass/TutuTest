package com.rolea.tututest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rolea.tututest.helpers.ToolbarManipulation;
import com.rolea.tututest.model.Station;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DetailStationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailStationFragment extends Fragment {
    private static final String ARG_STATION = "arg-station";
    // TODO: Rename and change types of parameters
    private Station station;

    private ToolbarManipulation mToolbarCallback;
    private TextView country;

    public DetailStationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param station Station to detail view.
     * @return A new instance of fragment DetailStationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailStationFragment newInstance(Station station) {
        DetailStationFragment fragment = new DetailStationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATION, new Gson().toJson(station));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            station = new Gson().fromJson(getArguments().getString(ARG_STATION), com.rolea.tututest.model.Station.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detail_station, container, false);
        mToolbarCallback.showBackButton(true);
        mToolbarCallback.setTitle(station.getStationTitle());
        country = (TextView) view.findViewById(R.id.title_info);
        String Info = getInfo();
        country.setText(Info);
        return  view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarManipulation) {
            mToolbarCallback = (ToolbarManipulation) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mToolbarCallback = null;
    }


    public String getInfo() {
        return getString(R.string.country)+station.getCountryTitle()+
                "\n\n"+getString(R.string.region)+station.getRegionTitle()+
                "\n\n"+getString(R.string.city)+station.getCityTitle()+
                "\n\n"+getString(R.string.district)+station.getDistrictTitle();
    }
}
