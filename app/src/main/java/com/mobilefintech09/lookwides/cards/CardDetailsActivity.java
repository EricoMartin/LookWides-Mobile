package com.mobilefintech09.lookwides.cards;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobilefintech09.lookwides.MainActivity;
import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.entities.OrderResponses;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;
import com.mobilefintech09.lookwides.orders.CurrentOrderActivity;
import com.mobilefintech09.lookwides.orders.NewOrderActivity;

import java.util.Objects;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit2.Call;

public class CardDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CardDetailsActivity";

    ApiService service;
    Call<OrderResponses> call;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;

    TextInputEditText txtCardNumber, txtCardCvv;
    TextInputLayout txtCardExpiry;
    Button mButton;

    Intent previous_intent;

    String description;
    String location;
    String destination;
    String reciever_name;
    String reciever_address;
    String reciever_phone;
    String num_of_items;
    String weight;
    String avatar;
    String price;
    String user_id;
    String tranx_id;
    AlertDialog.Builder alertDialog;
    private String mAmountToPay;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);


        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());
        service = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);

        txtCardNumber = findViewById(R.id.card_number);
        txtCardCvv = findViewById(R.id.cvv);
        txtCardExpiry = findViewById(R.id.card_expiry);
        mButton = findViewById(R.id.button2);
        alertDialog = new AlertDialog.Builder(CardDetailsActivity.this);

        //get extras from calling intent
        previous_intent = getIntent();
        mAmountToPay = previous_intent.getStringExtra("Price");
        mEmail = mSharedPreferenceManager.getNewUser().getEmail();
        Objects.requireNonNull(txtCardExpiry.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 2 && !s.toString().contains("/")) {
                    s.append("/");
                }
            }
        });

        PaystackSdk.initialize(getApplicationContext());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCardCharge();
            }
        });
        

    }

    private void performCardCharge() {

        String cardNumber = txtCardNumber.getText().toString();
        String cardExpiry = txtCardExpiry.getEditText().getText().toString();
        String cvv = txtCardCvv.getText().toString();

        String[] cardExpiryArray = cardExpiry.split("/");
        int expiryMonth = Integer.parseInt(cardExpiryArray[0]);
        int expiryYear = Integer.parseInt(cardExpiryArray[1]);


        description = previous_intent.getStringExtra("Description");
        location = previous_intent.getStringExtra("Location");
        destination = previous_intent.getStringExtra("Destination");
        reciever_name = previous_intent.getStringExtra("Reciever_Name");
        reciever_address = previous_intent.getStringExtra("Reciever_Address");
        reciever_phone = previous_intent.getStringExtra("Reciever_Phone");
        num_of_items = previous_intent.getStringExtra("Num_of_items");
        weight = previous_intent.getStringExtra("Weight");
        avatar = previous_intent.getStringExtra("avatar");
        price = previous_intent.getStringExtra("Price");
        user_id = previous_intent.getStringExtra("User_Id");
        String id = previous_intent.getStringExtra("id");

        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

        //Convert amount to kobo for paystack
        int koboValueForPayStack = Integer.parseInt(mAmountToPay) * 100;

        if (card.isValid()) {
            Charge charge = new Charge();
            charge.setAmount(koboValueForPayStack);
            charge.setEmail(mEmail);
            charge.setCard(card);

            PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
                @Override
                public void onSuccess(Transaction transaction) {

                    Log.d("CardDetails Activity", "onSuccess: " + transaction.getReference());
//                    parseResponse(transaction.getReference());
                    tranx_id = transaction.getReference();

                    Intent intent = new Intent(CardDetailsActivity.this, CurrentOrderActivity.class);
                    intent.putExtra("tranx_id", tranx_id);
                    intent.putExtra("order_id", id);

                    alertDialog
                            .setView(getLayoutInflater().inflate(R.layout.success,null))
                            .setTitle("Success ")
                            .setMessage("Transaction Completed Successfully")
                            .setPositiveButton("Continue", (dialog, which) ->{
                                startActivity(intent);
                            } )
                            .create()
                            .show()
                    ;
                }

                @Override
                public void beforeValidate(Transaction transaction) {
                    Log.d("CardDetails Activity", "beforeValidate: " + transaction.getReference());
                }

                @Override
                public void onError(Throwable error, Transaction transaction) {
                    Log.d("CardDetails Activity", "onError: " + error.getLocalizedMessage());
                    alertDialog
                            .setView(getLayoutInflater().inflate(R.layout.error,null))
                            .setTitle("Unsuccessful")
                            .setMessage(error.getMessage() + "**Note: Unpaid orders will not be processed**")
                            .setPositiveButton("Try again?", (dialog, which) ->{
                                performCardCharge();
                            } )
                            .setNegativeButton("Cancel", (dialog, which) ->{
                                startActivity(new Intent(CardDetailsActivity.this, MainActivity.class));
                            } )
                            .create()
                            .show()
                    ;
                }

            });



        } else {

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notification(){
        int notificationId = 1;

        //init notification
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.lookwide_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.lookwide_logo))
                .setContentTitle("Payment Successful")
                .setContentText("You have successfully made a payment, Transaction Id: " + mSharedPreferenceManager.getNewOrder().getTranxId())
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT){
            String channnelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channnelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channnelId);
        }
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;

            if (alertDialog != null) {
                alertDialog.setCancelable(true);
                alertDialog = null;
            }
        }
    }
}
