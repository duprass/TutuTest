package com.rolea.tututest.model;

import java.util.ArrayList;

/**
 * Created by rolea on 10/23/2016.
 */
public class JsonResponse {
    private ArrayList<City> citiesFrom;
    private ArrayList<City> citiesTo;

    public JsonResponse() {
    }

    public JsonResponse(ArrayList<City> citiesFrom, ArrayList<City> citiesTo) {
        this.citiesFrom = citiesFrom;
        this.citiesTo = citiesTo;
    }

    public ArrayList<City> getCitiesFrom() {
        return citiesFrom;
    }

    public void setCitiesFrom(ArrayList<City> citiesFrom) {
        this.citiesFrom = citiesFrom;
    }

    public ArrayList<City> getCitiesTo() {
        return citiesTo;
    }

    public void setCitiesTo(ArrayList<City> citiesTo) {
        this.citiesTo = citiesTo;
    }

    public ArrayList<City> getCitiesByType(int Type){
        if (Type == 0){
            return citiesFrom;
        } else  {
            return citiesTo;
        }

    }
}
