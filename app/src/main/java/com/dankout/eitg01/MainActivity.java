package com.dankout.eitg01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
        private TextView mMonitorTextView;
        private EditText mSearchStopField;
        private CardView mCardView;
        private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region test purposes
        Stop stop1 = new Stop("100", "Helsingborg C", "20", "20");
        Stop stop2 = new Stop("100", "Malmö C", "20", "20");
        ArrayList<Stop> stop = new ArrayList<>();
        stop.add(stop1);
        stop.add(stop2);
        //endregion

        mMonitorTextView = findViewById(R.id.monitor_question_text_field);
        mSearchStopField = findViewById(R.id.search_bus_stop_edit_field);
        mCardView = findViewById(R.id.stop_card_view);
        mRecyclerView = findViewById(R.id.bus_stop_list_view);

        StopViewAdapter adapter = new StopViewAdapter(stop);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private static class StopViewAdapter extends RecyclerView.Adapter<StopViewAdapter.StopHolder> {
        ArrayList<Stop> mStops;

        public StopViewAdapter(ArrayList<Stop> stops) {
            mStops = stops;
        }

        @NonNull
        @Override
        public StopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bus_stop_list_item, parent, false);
            return new StopHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StopHolder holder, int position) {
                holder.mBusStopNameTextView.setText(mStops.get(position).getStopName());
        }

        @Override
        public int getItemCount() {
            return mStops.size();
        }

        private static class StopHolder extends RecyclerView.ViewHolder {

           private ImageView mImageView;
           private TextView mBusStopNameTextView;
           private CardView mCardView;

            public StopHolder(@NonNull View itemView) {
                super(itemView);
                mCardView = itemView.findViewById(R.id.stop_list_item_card_view);
                mImageView = itemView.findViewById(R.id.image_view_place);
                mBusStopNameTextView = itemView.findViewById(R.id.text_view_bus_stop_name);

            }
        }

    }







}


