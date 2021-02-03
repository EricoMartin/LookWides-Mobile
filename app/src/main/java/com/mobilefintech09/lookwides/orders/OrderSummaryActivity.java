package com.mobilefintech09.lookwides.orders;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.lifecycle.ViewModelProvider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.cards.CardDetailsActivity;
import com.mobilefintech09.lookwides.entities.OrderResponse;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.entities.OrderResponses;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderSummaryActivity extends AppCompatActivity {

    private static final String TAG = "OrderSummaryActivity";

    ApiService service;
    Call<OrderResponse> call;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;

    Intent mIntent;
    TextView mTextView, mTextView1, textWeight, textNumber, textAmount;
    EditText mText, txtReceiverName, txtReceiverPhone;
    Button mButton;
    ImageView mImageView;
    private OrderSummaryViewModel mViewModel;
    private boolean mIsNewExtra;
    private String mDesc;
    private String mLoc;
    private String mDest;
    private String mWht;
    private String mNum;
    private String mAmt;
    private String rName;
    private String rAddress;
    private String rPhone;
    private Bitmap mBmp;
    private String mBitmap;

    AlertDialog.Builder alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());


        alertDialog = new AlertDialog.Builder(OrderSummaryActivity.this);

        //Boiler-plate viewModelProvider code
        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));

        //get viewModel
        mViewModel = viewModelProvider.get(OrderSummaryViewModel.class);

        if(mViewModel.mIsNewlyCreated && savedInstanceState != null)
            mViewModel.restoreState(savedInstanceState);
        mViewModel.mIsNewlyCreated = false;

        mIntent = getIntent();
        mDesc = mIntent.getStringExtra("Description");
        mLoc = mIntent.getStringExtra("Location");
        mDest = mIntent.getStringExtra("Destination");
        mWht = mIntent.getStringExtra("Weight");
        mNum = mIntent.getStringExtra("Items");
        rName = mIntent.getStringExtra("Reciever_Name");
        rAddress = mIntent.getStringExtra("Reciever_Address");
        rPhone = mIntent.getStringExtra("Reciever_Phone");
        mAmt = mIntent.getStringExtra("Price");
        mBitmap = mIntent.getStringExtra("Bitmap");
        if(mBitmap != null) {
//            mViewModel.getTaskRunner(mBitmap);
            mBmp = null;
            try {

                FileInputStream is = this.openFileInput(mBitmap);
                mBmp = BitmapFactory.decodeStream(is);

                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        saveExtraValues();

        mImageView = findViewById(R.id.imageView5);
        mImageView.setImageBitmap(mBmp);

        mTextView = findViewById(R.id.spinner);
        mTextView1 = findViewById(R.id.spinner2);

        mTextView.setText(mLoc);
        mTextView1.setText(mDest);

        mText = findViewById(R.id.editText);
        mText.setText(mDesc);

        textWeight = findViewById(R.id.editText4);
//        textWeight.setText(mWht);

        textNumber = findViewById(R.id.editText5);
        textNumber.setText(mNum);

        textAmount = findViewById(R.id.amount_textView);
        textAmount.setText("Naira: " + mAmt);

        txtReceiverName = findViewById(R.id.editText2);
        txtReceiverName.setText(rName);

        txtReceiverPhone = findViewById(R.id.editText3);
        txtReceiverPhone.setText(rPhone);

        mButton = findViewById(R.id.button_Pay);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mSharedPreferenceManager.getNewUser().getId().toString();
                call = service.order(mDesc, mLoc, mDest, rName, rAddress, rPhone, mNum, mWht, String.valueOf(mBmp), mAmt, id);
                call.enqueue(new Callback<OrderResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(@NotNull Call<OrderResponse> call, @NotNull Response<OrderResponse> response) {
                        if(!response.isSuccessful()){
                            Log.d("CardDetails Activity", "onErrorResponse: " + response.message());
                            Log.w(TAG, "Unsuccessful: " + response.code() + response);

                            alertDialog
                                    .setView(getLayoutInflater().inflate(R.layout.error,null))
                                    .setTitle("Unsuccessful")
                                    .setMessage("Unsuccessful Response")
                                    .setNegativeButton("Try again ?", (dialog, which) ->{
                                        startActivity(new Intent(OrderSummaryActivity.this, NewOrderActivity.class));
                                    } )
                                    .create()
                                    .show()
                            ;


                        }else if(response.isSuccessful()){
                            OrderResponse data = response.body();

                            Log.d(TAG, "onSuccessResponse: " + data);
                            mSharedPreferenceManager.orderStatus(data);


                            notification();

                            Intent newIntent = new Intent(OrderSummaryActivity.this, CardDetailsActivity.class);
                            newIntent.putExtra("Description", mDesc);
                            newIntent.putExtra("Location", mLoc);
                            newIntent.putExtra("Destination", mDest);
                            newIntent.putExtra("Reciever_Name", rName);
                            newIntent.putExtra("Reciever_Address", rAddress);
                            newIntent.putExtra("Reciever_Phone", rPhone);
                            newIntent.putExtra("Num_of_items", mNum);
                            newIntent.putExtra("Weight", mWht);
                            newIntent.putExtra("id", String.valueOf(data.getOrder().getId()));

                            try {

                                FileOutputStream stream = new FileOutputStream(String.valueOf(mBmp));
                                mBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                                //Cleanup

                                newIntent.putExtra("avatar", (Parcelable) stream);

                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            newIntent.putExtra("Price", mAmt);

                            newIntent.putExtra("User_Id", id);


                            alertDialog
                                    .setView(getLayoutInflater().inflate(R.layout.success,null))
                                    .setTitle("Success ")
                                    .setMessage("Transaction Completed Successfully")
                                    .setPositiveButton("Pay", (dialog, which) ->{
                                        startActivity(newIntent);
                                    } )
                                    .create()
                                    .show()
                            ;
//                    Intent intent = new Intent(CardDetailsActivity.this, CurrentOrderActivity.class);
//                    startActivity(intent);

                            //intent.putExtra("Description", data.getData().indexOf(1));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OrderResponse> call, @NotNull Throwable t) {
                        Log.w(TAG, "onFailure: " + t.getMessage());

                        alertDialog
                                .setView(getLayoutInflater().inflate(R.layout.error,null))
                                .setTitle("Unsuccessful")
                                .setMessage(t.getMessage().toString())
                                .create()
                                .show()
                        ;
                    }
                });

            }
        });
    }
    private void displayToast(String label) {
        Toast.makeText(this, label, Toast.LENGTH_LONG).show();
    }

    private void saveExtraValues() {
        if(mIsNewExtra){
            return;
        }
        mViewModel.mDescription = mDesc;
        mViewModel.mLocation = mLoc;
        mViewModel.mDestination = mDest;
        mViewModel.mWeight = mWht;
        mViewModel.mItems = mNum;
        mViewModel.mPrice = mAmt;
        mViewModel.mBitmap = mBitmap;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notification(){
        int notificationId = 1;

        //init notification
        Notification.Builder builder = new Notification.Builder(this)
            .setSmallIcon(R.drawable.lookwide_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.lookwide_logo))
                .setContentTitle("New Order Created")
                .setContentText("You have successfully created a new order, Order Id: " + mSharedPreferenceManager.getNewOrder().getId())
                .setStyle(new Notification.BigTextStyle().bigText("Click to view more"))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);
        Intent intent = new Intent(this, CurrentOrderActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.ic_menu_view, "View", resultPendingIntent);
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT <  Build.VERSION_CODES.O){
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT){
            String channnelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channnelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channnelId);
        }
        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null) {
            call.cancel();
            call = null;
        }

            if (alertDialog != null) {
                alertDialog.setCancelable(true);
                alertDialog = null;
            }

    }
}
