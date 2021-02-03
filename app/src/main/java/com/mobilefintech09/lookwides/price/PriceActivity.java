package com.mobilefintech09.lookwides.price;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mobilefintech09.lookwides.R;

import java.util.ArrayList;
import java.util.List;

public class PriceActivity extends AppCompatActivity {

    private static final String TAG = "PriceActivity";
    TextView mTextView;
    List<String> textList = new ArrayList<>();
    List<String> textList2 = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mTextView = findViewById(R.id.text_all_orders);
        mRecyclerView = findViewById(R.id.recyclerView);
        displayRecyclerView();
    }

    private void displayRecyclerView() {


        String[] locations = this.getResources().getStringArray(R.array.locations_array);
        String [] prices = this.getResources().getStringArray(R.array.prices_array);
        List<Integer> num = new ArrayList<>();

            for(int i = 0; i < locations.length; i++) {
                num.add(i + 1);
                textList.add(locations[i]);
                textList2.add(prices[i]);
                //Log.w(TAG,  num.toString());
            }
        final LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(PriceActivity.this);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(new PriceRecyclerAdapter(num, textList, textList2, this));

    }
}
