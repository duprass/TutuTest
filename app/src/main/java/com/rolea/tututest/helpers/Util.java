package com.rolea.tututest.helpers;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rolea.tututest.model.Station;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by rolea on 10/25/2016.
 */
public class Util {
    public static final int TYPE_STATION_FROM = 0;
    public static final int TYPE_STATION_TO = 1;

    public static String getFormatDate(Date date) {
        return DateFormat.getDateInstance().format(date);

    }

    public static void changeVisibleView(View view, boolean visible) {
        if (!visible) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static String getHashMapStationJson(HashMap<Integer, Station> stations) {

        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<HashMap<Integer, Station>>() {
        }.getType();
        String s = gson.toJson(stations, listOfTestObject);
        return s;
    }
}
