package com.dankout.eitg01;

import android.util.Log;

public class Watcher implements Runnable {
    private Stop mStop;
    private Thread GtsfRealTimeThread;
    private static Watcher instance;
    private boolean mExit;

    private Watcher() {
        this.GtsfRealTimeThread = new Thread(this);
        this.mExit = true;
    }

    public static Watcher getInstance() {
        if(instance == null) {
            instance = new Watcher();
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

    /**
     * Sets the stop to watch
     * @param stop The stop to watch
     * @param stopsAway Amount of stops from the selected stop the push notification should be sent
     */
    public void setStopToWatch(Stop stop, int stopsAway) {
        this.mStop = stop;
        Log.d("Watcher", "Stop set");
    }

    /**
     * Stops the watching of the stop currently being watched
     */
    public void stopWatching() {
        this.mExit = true;
    }

    @Override
    public void run() {
            while(!mExit) {
                Log.d("Watcher", "Running background thread..");
            }
        Log.d("Watcher", "BackGround thread exited");
    }

}
