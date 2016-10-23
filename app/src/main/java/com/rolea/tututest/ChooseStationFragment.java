package com.rolea.tututest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rolea.tututest.helpers.ToolbarManipulation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnChooseStationListener} interface
 * to handle interaction events.
 * Use the {@link ChooseStationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseStationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnChooseStationListener mListener;
    private Button buttonDateDeparture;
    private Button buttonStationFrom;
    private Button buttonStationTo;
    private ToolbarManipulation mToolbarCallback;

    public ChooseStationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseStationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseStationFragment newInstance(String param1, String param2) {
        ChooseStationFragment fragment = new ChooseStationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        mToolbarCallback.showBackButton(false);
        InitializeView(view);

        String stationFrom = mListener.getStationFrom();
        String stationTo = mListener.getStationTo();
        if (stationFrom != null){
            buttonStationFrom.setText(stationFrom);
        }
        if (stationTo != null){
            buttonStationTo.setText(stationTo);
        }
        return view;
    }

    private void InitializeView(View view) {
        buttonStationTo = (Button) view.findViewById(R.id.input_station_to);
        buttonStationFrom = (Button) view.findViewById(R.id.input_station_from);
        buttonDateDeparture = (Button) view.findViewById(R.id.date_departure_button);

        buttonStationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStationChoose(0);
            }
        });
        buttonStationTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStationChoose(1);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChooseStationListener) {
            mToolbarCallback = (ToolbarManipulation) context;
            mListener = (OnChooseStationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChooseStationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mToolbarCallback = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnChooseStationListener {
        // TODO: Update argument type and name
        void onStationChoose(int Type);
        String getStationFrom();
        String getStationTo();
    }
}
