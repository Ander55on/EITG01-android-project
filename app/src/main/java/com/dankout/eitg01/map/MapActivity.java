package com.dankout.eitg01.map;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.dankout.eitg01.R;
import com.dankout.eitg01.Stop;
import com.dankout.eitg01.StopManager;
import com.dankout.eitg01.Watcher;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {

    private GoogleMap mMap;
    private HashMap<Marker, Stop> mMarkerStopHashMap;
    private ArrayList<Stop> stops;
    private float oldZoom = 0f;
    private boolean stopsVisible;
    private Watcher mWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mWatcher = Watcher.getInstance();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        StopManager manager = StopManager.getInstance(this);
        stops = manager.getStops();
        mMarkerStopHashMap = new HashMap<>();


        for (Stop s : stops) {
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(s.getPosition())
                    .title(s.getStopName())
                    .snippet(s.getPlatformCode()));

            m.setVisible(false);
            mMarkerStopHashMap.put(m, s);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.990257, 13.595769), 7));

        mMap.setOnCameraIdleListener(new OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition pos = mMap.getCameraPosition();

                //Zoom and not just panning around
                if (oldZoom != pos.zoom) {
                    //only loops through the list of markers if visibility should be changed
                    if (pos.zoom > 14 && !stopsVisible || pos.zoom < 14 && stopsVisible) {
                        stopsVisible = pos.zoom > 14;
                        for (Map.Entry<Marker, Stop> m : mMarkerStopHashMap.entrySet()) {
                            m.getKey().setVisible(stopsVisible);
                        }
                    }

                }
                oldZoom = pos.zoom;
            }
        });

        mMap.setOnMarkerClickListener(this);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        Stop stop = mMarkerStopHashMap.get(marker);
        mWatcher.setStopToWatch(stop, 2);
        return true;
    }
}