package com.rolea.tututest.helpers;

import android.view.View;

import java.text.DateFormat;
import java.util.Date;

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

}
