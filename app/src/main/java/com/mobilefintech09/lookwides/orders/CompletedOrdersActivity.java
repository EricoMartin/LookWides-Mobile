package com.mobilefintech09.lookwides.orders;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.entities.OrderResponses;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedOrdersActivity extends AppCompatActivity {

    private static final String TAG = "CompletedOrdersActivity";

    RecyclerView mRecyclerView;
    TextView mTextView;
    private CompletedOrdersAdapter mOrdersAdapter;

    ApiService service;
    Call<List<OrderResponses>> call;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_completed_orders);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mTextView = findViewById(R.id.text_all_orders);
        mRecyclerView = findViewById(R.id.recyclerViewActive);

        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());


        displayRecyclerView();
    }

    private void displayRecyclerView() {

        final LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(CompletedOrdersActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mOrdersAdapter = new CompletedOrdersAdapter();
        getCompletedOrders();

    }

    public void getCompletedOrders(){
        call = service.getCompletedOrders();
        call.enqueue(new Callback<List<OrderResponses>>() {
            @Override
            public void onResponse(Call <List<OrderResponses>> call, Response<List<OrderResponses>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: " + response.body());
                    List<OrderResponses> responses = response.body();
                    mOrdersAdapter.setData(responses);
                    mRecyclerView.setAdapter(mOrdersAdapter);
                }
            }

            @Override
            public void onFailure(Call <List<OrderResponses>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }
}
