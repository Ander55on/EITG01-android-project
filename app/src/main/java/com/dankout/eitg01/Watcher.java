package com.dankout.eitg01;

import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Watcher {
    private Stop mStop;
    private Thread GtsfRealTimeThread;
    private static Watcher instance;
    private boolean mExit;
    private Context mContext;

    private Watcher(Context context) {
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
        Log.d("Watcher", "Stop set");
    }

    /**
     * Stops the watching of the stop currently being watched
     */
    public void stopWatching() {
        this.mExit = true;
        Log.d("Watcher", "watching ended");
    }


    private class GTFSFetcher implements Runnable{

        @Override
        public void run() {
            while (true) {
                int i = 10;

                while(!mExit) {

                    try {
                        Thread.sleep(1000);
                        i--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d("Watcher", "Bus coming in " + i);

                    if(i <=0) {
                        sendNotification();
                    }
                }
            }
        }

        private boolean busIsClose() {
            return true;
        }

    }

}
