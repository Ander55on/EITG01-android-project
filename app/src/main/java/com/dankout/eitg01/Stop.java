package com.dankout.eitg01;

public class Stop {
    private String stopId; // Identifies a stop, station, or station entrance.
    private String stopName; // Name of the location
    private String longitude; // Longitude of the location.
    private String latitude; // Latitude of the location.
    private String locationType; // Type of the location: [0] stop or platform [1] station
    private String parentStation; //Id that references the stopID
    private String platformCode; //Platform identifier


    public Stop(String stop_id, String stop_name, String latitude, String longitude, String locationType) {
        this.stopId = stop_id;
        this.stopName = stop_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.platformCode = null;
        this.parentStation = null;
        this.locationType = null;

    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }

    public String getPlatformCode() {
        return this.platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
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
