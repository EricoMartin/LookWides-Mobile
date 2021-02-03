package com.mobilefintech09.lookwides.orders;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilefintech09.lookwides.MainActivity;
import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.auth.ResetPasswordActivity;
import com.mobilefintech09.lookwides.entities.Order;
import com.mobilefintech09.lookwides.entities.OrderResponse;
import com.mobilefintech09.lookwides.entities.Orders;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentOrderActivity extends AppCompatActivity {

    private static final String TAG = "CurrentOrderActivity";
    TextView mDesc, mLoc, mDest, mWeight, mNumItems, mStatus;
    ImageView mImageView;
    Button btn_cancel, btn_complete;

    AlertDialog.Builder alertDialog;

    ApiService service;
    Call<OrderResponse> call;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mDesc = findViewById(R.id.editText);
         mLoc =findViewById(R.id.textInputLayout);
        mDest = findViewById(R.id.spinner2);
        mWeight = findViewById(R.id.editText4);
        mNumItems = findViewById(R.id.editText5);
        mStatus = findViewById(R.id.status_textView1);
        mImageView = findViewById(R.id.imageView5);
        btn_cancel = findViewById(R.id.button3);
        btn_complete = findViewById(R.id.button4);

        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());

        alertDialog = new AlertDialog.Builder(CurrentOrderActivity.this);

        displayCurrentOrder();

        Intent dataIntent = getIntent();


        String id  = mSharedPreferenceManager.getNewOrder().getId().toString();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = service.cancelOrder(id, id);
                call.enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: " + response.message());
                            alertDialog
                                    .setView(getLayoutInflater().inflate(R.layout.success, null))
                                    .setTitle("Successful")
                                    .setMessage("Order Cancelled Successfully")
                                    .setPositiveButton("Continue", (dialog, which) -> {
                                        startActivity(new Intent(CurrentOrderActivity.this, MainActivity.class));
                                    })
                                    .create()
                                    .show()
                            ;
                        }else{
                            Log.w(TAG, "onResponseFailed: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        Log.w(TAG, "onResponse: " + t.getLocalizedMessage());
                    }
                });
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = service.completeOrder(id, id);
                call.enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: " + response.message());
                            alertDialog
                                    .setView(getLayoutInflater().inflate(R.layout.success, null))
                                    .setTitle("Successful")
                                    .setMessage("Order Completed Successfully")
                                    .setPositiveButton("Continue", (dialog, which) -> {
                                        startActivity(new Intent(CurrentOrderActivity.this, MainActivity.class));
                                    })
                                    .create()
                                    .show()
                            ;
                        }else{
                            Log.w(TAG, "onResponseFailed: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        Log.w(TAG, "onResponse: " + t.getLocalizedMessage());
                    }
                });
            }
        });


    }

    private void displayCurrentOrder() {
        Intent paidIntent = getIntent();
        String tranx_id = paidIntent.getStringExtra("tranx_id");
        String id = mSharedPreferenceManager.getNewOrder().getId().toString();

        call = service.paidOrder(tranx_id, id);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, @NotNull Response<OrderResponse> response) {
                if (!response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response.message() + "  " + response);
                        displayOrder();
                } else {

                    OrderResponse data = response.body();
                    assert data != null;
                    mSharedPreferenceManager.orderStatus(data);
                    Log.d(TAG, "onSuccessResponse: " + data);
                    mDesc.setText(data.getOrder().getDescription());
                    mLoc.setText(data.getOrder().getLocation());
                    mDest.setText(data.getOrder().getDestination());
                    mWeight.setText((CharSequence) data.getOrder().getWeight());

                    mStatus.setText(data.getOrder().getStatus());
                    mImageView.setImageBitmap((Bitmap) data.getOrder().getAvatar());

                }
            }
            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(CurrentOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                mStatus.setText(new StringBuilder().append(getString(R.string.error)).append(t.getLocalizedMessage()).toString());
            }
        });

    }

    @Override
    protected void onResume() {
        displayOrder();
        super.onResume();
    }

    public void displayOrder(){
        String status = mSharedPreferenceManager.getNewOrder().getStatus();

            Orders order =  mSharedPreferenceManager.getNewOrder();
            mDesc.setText(order.getDescription());
            mLoc.setText(order.getLocation());
            mDest.setText(order.getDestination());
            mNumItems.setText(order.getNumOfItems().toString());

            mStatus.setText(order.getStatus());
            mImageView.setImageBitmap((Bitmap) order.getAvatar());


    }

}
