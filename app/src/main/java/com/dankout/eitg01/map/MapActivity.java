package com.dankout.eitg01.map;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.dankout.eitg01.R;
import com.dankout.eitg01.Stop;
import com.dankout.eitg01.StopManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Stop, Marker> mMarkerStopHashMap;
    private ArrayList<Stop> stops;
    private float oldZoom = 0f;
    private boolean stopsVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
            mMarkerStopHashMap.put(s, m);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.990257, 13.595769), 7));

        mMap.setOnCameraIdleListener(new OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition pos = mMap.getCameraPosition();

                //Zoom and not just panning around
                if (oldZoom != pos.zoom) {

                    if (pos.zoom > 14 && !stopsVisible || pos.zoom < 14 && stopsVisible) {
                        stopsVisible = pos.zoom > 14;
                        for (Stop s : stops) {
                            mMarkerStopHashMap.get(s).setVisible(stopsVisible);
                        }
                    }

                }
                oldZoom = pos.zoom;
            }
        });

    }


}