package com.rolea.tututest.model;

/**
 * Created by rolea on 10/23/2016.
 */
public class City {
    private String countryTitle;

    private Point point;

    private String districtTitle;

    private String cityId;

    private String cityTitle;

    private String regionTitle;

    private Station[] stations;


    public City() {
    }

    public City(String cityId, String cityTitle, String countryTitle, String districtTitle, Point point, String regionTitle, Station[] stations) {
        this.cityId = cityId;
        this.cityTitle = cityTitle;
        this.countryTitle = countryTitle;
        this.districtTitle = districtTitle;
        this.point = point;
        this.regionTitle = regionTitle;
        this.stations = stations;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountryCityTitle() {
        return getCountryTitle() + ", " + getCityTitle();
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public String getDistrictTitle() {
        return districtTitle;
    }

    public void setDistrictTitle(String districtTitle) {
        this.districtTitle = districtTitle;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getRegionTitle() {
        return regionTitle;
    }

    public void setRegionTitle(String regionTitle) {
        this.regionTitle = regionTitle;
    }

    public Station[] getStations() {
        return stations;
    }

    public void setStations(Station[] stations) {
        this.stations = stations;
    }
}
