package com.dankout.eitg01;

import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import com.google.transit.realtime.GtfsRealtime.VehicleDescriptor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Watcher {
    private Stop mStop;
    private Thread GtsfRealTimeThread;
    private StopManager mStopManager;
    private static Watcher instance;
    private boolean mExit;
    private Context mContext;
    private String API_KEY_GTFS = "API_KEY_HERE";
    private String tripUpdatesURL = "https://opendata.samtrafiken.se/gtfs-rt/skane/TripUpdates.pb?key=";
    private String serviceAlertsURL = "https://opendata.samtrafiken.se/gtfs-rt/skane/ServiceAlerts.pb?key=";
    private String vehiclePositionURL = "https://opendata.samtrafiken.se/gtfs-rt/skane/VehiclePositions.pb?key=";


    private Watcher(Context context) {
        this.mStopManager = StopManager.getInstance(context);
        this.GtsfRealTimeThread = new Thread(new GTFSFetcher());
        this.GtsfRealTimeThread.start();
        this.mContext = context;
        mExit = true;
    }

    public static Watcher getInstance(Context context) {
        if (instance == null) {
            instance = new Watcher(context);
        }
        return instance;
    }

    /**
     * Start watching a specified stop and sends a push notification when a bus is closing in.
     */
    public void startWatching() {
        this.mExit = false;
        Log.d("Watcher", "Watching started");
    }

    public boolean isWatching() {
        return !mExit;
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stop_watch)
                .setContentTitle("Bus Alert")
                .setContentText("Bus is now closing in")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        manager.notify(1, builder.build());

        stopWatching();
    }

    /**
     * Sets the stop to watch
     *
     * @param stop      The stop to watch
     * @param stopsAway Amount of stops from the selected stop the push notification should be sent
     */
    public void setStopToWatch(Stop stop, int stopsAway) {
        this.mStop = stop;
        Log.d("Watcher", "Stop set" + " id: " + mStop.getStopId().trim());
    }

    /**
     * Stops the watching of the stop currently being watched
     */
    public void stopWatching() {
        this.mExit = true;
        Log.d("Watcher", "watching ended");
    }


    private class GTFSFetcher implements Runnable {

        @Override
        public void run() {

            while (true) {
                while (!mExit) {

                    URL url = null;
                    URL url2 = null;
                    try {
                        url = new URL(tripUpdatesURL + API_KEY_GTFS);
                        url2 = new URL(vehiclePositionURL + API_KEY_GTFS);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    FeedMessage feedTripUpdates = null;
                    FeedMessage feedVehicle = null;
                    try {
                        feedTripUpdates = FeedMessage.parseFrom(url.openStream());
                        feedVehicle = FeedMessage.parseFrom(url2.openStream());
                        Log.d("Watcher", "Getting trip updates..");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (FeedEntity entity : feedTripUpdates.getEntityList()) {


                        TripUpdate tripUpdate = entity.getTripUpdate();
                        TripDescriptor tripDescriptor = tripUpdate.getTrip();
                        List<StopTimeUpdate> list =  tripUpdate.getStopTimeUpdateList();

                        VehicleDescriptor vehicleDescriptor = tripUpdate.getVehicle();
                        String id = vehicleDescriptor.getId();


                        int stopsAway = -1;

                        for (int i = 0; i < list.size(); i++) {

                            if (list.get(i).getStopId().equals(mStop.getStopId())) {
                                long timestamp = (System.currentTimeMillis() / 1000L)  - tripUpdate.getTimestamp();
                                stopsAway = i;
                                Log.d("Watcher", "\n" +
                                                 "Current stop is: " + mStopManager.getStop(list.get(0).getStopId()).getStopName()
                                        + "\n" + "Watching: " + mStopManager.getStop(mStop.getStopId()).getStopName()
                                        + "\n" + "Sequence number for watched stop is: " +  list.get(i).getStopSequence()
                                        + "\n" + "Stops away: " + stopsAway
                                        + "\n" + "trip id: " + tripDescriptor.getTripId());
                            }
                        }

                        if (stopsAway < 2 && stopsAway != -1) {
                            sendNotification();
                            break;
                        }
                    }

                    try {
                        Thread.sleep(15000); //wait 15 seconds until next API CALL

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }


            }
        }

    }


}


