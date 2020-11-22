package com.dankout.eitg01;

import com.google.android.gms.maps.model.LatLng;

public class Stop {
    private final String stopId; // Identifies a stop, station, or station entrance.
    private final String stopName; // Name of the location
    private final String longitude; // Longitude of the location.
    private final String latitude; // Latitude of the location.
    private final String locationType; // Type of the location: [0] stop or platform [1] station
    private String parentStation; //Id that references the stopID
    private String platformCode; //Platform identifier
    private final LatLng mLatLng;


    public Stop(String stop_id, String stop_name, String latitude, String longitude, String locationType) {
        this.stopId = stop_id;
        this.stopName = stop_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.platformCode = null;
        this.parentStation = null;
        this.locationType = locationType;
        this.mLatLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

    }


    /**
     *
     * @return the location type: 1 if station, 0 if stop or platform
     */
    public String getLocationType() {
        return locationType;
    }
    public String getPlatformCode() {
        return this.platformCode;
    }


    public String getStopId() {
        return stopId;
    }


    public String getStopName() {
        return stopName;
    }


    public String getLongitude() {
        return longitude;
    }


    public String getLatitude() {
        return latitude;
    }


    public LatLng getPosition() {
        return this.mLatLng;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }
}
