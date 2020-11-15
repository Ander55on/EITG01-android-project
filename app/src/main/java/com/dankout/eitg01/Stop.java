package com.dankout.eitg01;

public class Stop {
    private String stopId;
    private String stopName;
    private String longitude;
    private String latitude;

    public Stop(String stop_id, String stop_name, String longitude, String latitude) {
        this.stopId = stop_id;
        this.stopName = stop_name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stop_id) {
        this.stopId = stop_id;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stop_name) {
        this.stopName = stop_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }





}
