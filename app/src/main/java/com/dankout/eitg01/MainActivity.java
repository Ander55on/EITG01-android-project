package com.dankout.eitg01;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dankout.eitg01.map.MapActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogOnlickListener{
        private EditText mSearchStopField;
        private CardView mChooseMapCardView;
        private RecyclerView mRecyclerView;
        private StopManager mStopManager;
        private Watcher mWatcher;
        private TextView mIsWatchingTextView;
        public static final String CHANNEL_ID = "BusNotificationChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creates notification channels that is needed for API level 26+ will be ignored for lower APIs
        createNotificationChannel();

        setContentView(R.layout.activity_main);

        //Class used for API calls and to check for if bus is close to a stop
        this.mWatcher = Watcher.getInstance(this);

        //Create stopmanager that will handle stops
        this.mStopManager = StopManager.getInstance(this);

        //Inflate widgets in the acitivity_main xml file
        this.mSearchStopField = findViewById(R.id.search_bus_stop_edit_field);
        this.mRecyclerView = findViewById(R.id.bus_stop_list_view);
        this.mChooseMapCardView = findViewById(R.id.choose_map_card_view);
        this.mIsWatchingTextView = findViewById(R.id.watching_ongoing);

        String text;

        if(mWatcher.isWatching()) {
            text = getString(R.string.watching_is_ongoing, "Pågår");
            mIsWatchingTextView.setText(text);
        } else {
            text = getString(R.string.watching_is_ongoing, "stoppad");
        }
        mIsWatchingTextView.setText(text);

        final StopViewAdapter adapter = new StopViewAdapter();
        this.mRecyclerView.setAdapter(adapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set clicklistener to start google maps
        this.mChooseMapCardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startMapActivity();
            }
        });


        mSearchStopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Send the list to be displayed to the recycler adapter
                adapter.filterList(mStopManager.getFilteredStops(editable.toString()));
            }
        });



    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String text;

        if(mWatcher.isWatching()) {
            text = getString(R.string.watching_is_ongoing, "Pågår");
            mIsWatchingTextView.setText(text);
        } else {
            text = getString(R.string.watching_is_ongoing, "stoppad");
        }
        mIsWatchingTextView.setText(text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Set a menu button -> stop watch button
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    private void startMapActivity() {
        //creates a new intent to start the map activity
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mWatcher.stopWatching();
        String text = getString(R.string.watching_is_ongoing, "stoppad");
        mIsWatchingTextView.setText(text);
        return true;
    }

    @Override
    public void onPositiveClick() {
        Log.d("MainActivity", "Positive click");
        mWatcher.startWatching();
        String text = getString(R.string.watching_is_ongoing, "Pågår");
        mIsWatchingTextView.setText(text);
    }

    @Override
    public void onNegativeClick() {
        Log.d("MainActivity", "Negative click");
    }

    private class StopViewAdapter extends RecyclerView.Adapter<StopViewAdapter.StopHolder> {
        List<Stop> mStops;

        public StopViewAdapter() {
            mStops = new ArrayList<>();
        }

        /**
         *
         * @param filteredList The new list to be displayed in the view
         */
        public void filterList(@NonNull ArrayList<Stop> filteredList) {
            mStops = filteredList;
            //This method invokes both onCreateViewHolder and onBindViewHolder
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public StopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bus_stop_list_item, parent, false);
            return new StopHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StopHolder holder, final int position) {
                //Binds data to the textView that the stopHolder holds
                holder.mBusStopNameTextView.setText(mStops.get(position).getStopName());
                holder.mPlatformCodeTextView.setText(getString(R.string.platform_code,mStops.get(position).getPlatformCode()));

                //Set an Onclicklistener on every item in the recycler view
                holder.mCardView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mWatcher.setStopToWatch(mStops.get(position),2);
                        WatchDialogFragment dialog = new WatchDialogFragment();
                        dialog.show(getSupportFragmentManager(), null);
                    }
                });
        }

        @Override
        public int getItemCount() {
            return mStops.size();
        }

        private class StopHolder extends RecyclerView.ViewHolder {

           private ImageView mImageView;
           private TextView mBusStopNameTextView;
           private TextView mPlatformCodeTextView;
           private CardView mCardView;

            public StopHolder(@NonNull View itemView) {
                super(itemView);
                mCardView = itemView.findViewById(R.id.stop_list_item_card_view);
                mImageView = itemView.findViewById(R.id.image_view_place);
                mPlatformCodeTextView = itemView.findViewById(R.id.text_view_platform_code);
                mBusStopNameTextView = itemView.findViewById(R.id.text_view_bus_stop_name);

            }
        }




    }







}


