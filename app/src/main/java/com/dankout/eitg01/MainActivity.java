package com.dankout.eitg01;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
        private TextView mMonitorTextView;
        private EditText mSearchStopField;
        private CardView mCardView;
        private RecyclerView mRecyclerView;
        private StopManager mStopManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStopManager = StopManager.getInstance(this);

        mMonitorTextView = findViewById(R.id.monitor_question_text_field);
        mSearchStopField = findViewById(R.id.search_bus_stop_edit_field);
        mCardView = findViewById(R.id.stop_card_view);
        mRecyclerView = findViewById(R.id.bus_stop_list_view);


        final StopViewAdapter adapter = new StopViewAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mSearchStopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filterList(mStopManager.getFilteredStops(editable.toString()));
            }
        });

    }

    private class StopViewAdapter extends RecyclerView.Adapter<StopViewAdapter.StopHolder> {
        List<Stop> mStops;

        public StopViewAdapter() {
            mStops = new ArrayList<Stop>();
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
        public void onBindViewHolder(@NonNull StopHolder holder, int position) {
                //Binds data to the textView that the stopHolder holds
                holder.mBusStopNameTextView.setText(mStops.get(position).getStopName());
                holder.mPlatformCodeTextView.setText(getString(R.string.platform_code,mStops.get(position).getPlatformCode()));
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


