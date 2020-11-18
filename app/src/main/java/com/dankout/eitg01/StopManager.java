package com.dankout.eitg01;


import android.content.Context;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class StopManager {
    private ArrayList<Stop> mStops;
    private ArrayList<Stop> mStations;
    private static StopManager instance;

    private StopManager(Context context) {
        mStops = new ArrayList<>();
        mStations = new ArrayList<>();
        readFromFile(context);
    }

    /**
     * @return An instance of the StopManager
     */
    public static StopManager getInstance(Context context) {
        if (instance == null) {
            instance = new StopManager(context);
        }
        return instance;
    }

    /* [0] = stop_id
       [1] = stop_name
       [2] = stop_lat
       [3] = stop_lon
       [4] = location_type
       [5] = parent_station
       [6] = platform_code
     */
    private void prePopulateList(String s) {
        String[] parameters = s.split(",");

        Stop stop = new Stop(parameters[0], parameters[1], parameters[2],
                parameters[3], parameters[4]);

        if (parameters.length > 5) {
            stop.setParentStation(parameters[5]);
        }
        if (parameters.length > 6) {
            stop.setPlatformCode(parameters[6]);
        }

        if(parameters[4].equals("1")) {
            mStations.add(stop);
        } else {
            mStops.add(stop);
        }

    }

    private void readFromFile(Context context) {

        Scanner reader;

        try {
            InputStream textFileStream = new DataInputStream(context.getAssets().open("Stops/stops.txt"));
            reader = new Scanner(new InputStreamReader(textFileStream));
            //Ignore first line
            reader.nextLine();

            while (reader.hasNextLine()) {
                String s = reader.nextLine();
                prePopulateList(s);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param searchText the stop to search for can be the full name of the stop
     *                   or a part of the name
     * @return An arraylist of stops that matches the search text
     */
    public ArrayList<Stop> getFilteredStops(String searchText) {

        ArrayList<Stop> temp = new ArrayList<>();

        if (searchText.isEmpty()) {
            return temp;
        }

        for (Stop stop : mStops) {
            if (stop.getStopName().toLowerCase().contains(searchText.trim().toLowerCase())) {
                temp.add(stop);
            }
        }

        return temp;
    }

    /**
     * @return An arraylist of all stops specified in stops.txt
     */
    public ArrayList<Stop> getStops() {
        return this.mStops;
    }
}
