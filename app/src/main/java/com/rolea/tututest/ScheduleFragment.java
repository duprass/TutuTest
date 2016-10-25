package com.rolea.tututest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.rolea.tututest.helpers.ToolbarManipulation;
import com.rolea.tututest.helpers.Util;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnChooseStationListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    private static final String SELECTED_DATE_KEY = "selected_day";


    private OnChooseStationListener mListener;
    private Button buttonDateDeparture;
    private Button buttonStationFrom;
    private Button buttonStationTo;
    private ToolbarManipulation mToolbarCallback;
    private CalendarView calendarView;
    private Date dateDeparture;
    private Calendar calendar;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        mToolbarCallback.showBackButton(false);
        InitializeView(view);

        calendar = Calendar.getInstance();
        // handle orientation changes
        if (savedInstanceState == null) {
            dateDeparture = calendar.getTime();
        } else {
            // restore selected date
            calendar.setTimeInMillis(savedInstanceState.getLong(SELECTED_DATE_KEY));
            dateDeparture = calendar.getTime();
        }

        setValuesToViews();
        return view;
    }

    private void setValuesToViews() {
        String stationFrom = mListener.getStationFrom();
        String stationTo = mListener.getStationTo();
        if (stationFrom != null){
            buttonStationFrom.setText(stationFrom);
        }
        if (stationTo != null){
            buttonStationTo.setText(stationTo);
        }
        setDateText();
        calendarView.setDate(dateDeparture.getTime());
    }

    private void InitializeView(View view) {
        buttonStationTo = (Button) view.findViewById(R.id.input_station_to);
        buttonStationFrom = (Button) view.findViewById(R.id.input_station_from);
        buttonDateDeparture = (Button) view.findViewById(R.id.date_departure_button);

        buttonStationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStationChoose(Util.TYPE_STATION_FROM);
            }
        });
        buttonStationTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStationChoose(Util.TYPE_STATION_TO);
            }
        });
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                dateDeparture = calendar.getTime();
                setDateText();
            }
        });
    }

    private void setDateText() {
        buttonDateDeparture.setText(getString(R.string.date_departure) + ":" + Util.getFormatDate(dateDeparture));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dateDeparture == null) {
            calendar = Calendar.getInstance();
            dateDeparture = calendar.getTime();
        }
        // save selected state
        outState.putLong(SELECTED_DATE_KEY, dateDeparture.getTime());
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

    @Override
    public void onResume() {
        super.onResume();
        mToolbarCallback.setTitle(getString(R.string.schedule));
    }

    public interface OnChooseStationListener {
        void onStationChoose(int Type);
        String getStationFrom();
        String getStationTo();
    }
}
