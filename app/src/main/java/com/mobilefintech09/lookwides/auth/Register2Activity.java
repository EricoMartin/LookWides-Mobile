package com.mobilefintech09.lookwides.auth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.mobilefintech09.lookwides.MainActivity;
import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.entities.AccessToken;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register2Activity extends AppCompatActivity {

    private static final String TAG = "Register2Activity";
    EditText mEditPhone, mEditAddress;
    Button mButton;

    ApiService service;
    Call<AccessToken> call;
    AwesomeValidation validator;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mEditPhone = findViewById(R.id.phone);
        mEditAddress = findViewById(R.id.address);

        mButton = findViewById(R.id.register);

        service = RetrofitBuilder.createService(ApiService.class);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        mSharedPreferenceManager = SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String name = intent.getStringExtra("name");
                String email  = intent.getStringExtra("email");
                String password = intent.getStringExtra("password");

                String phone = mEditPhone.getEditableText().toString();
                String address = mEditAddress.getEditableText().toString();

                if(validateInputs(phone, mEditPhone) && validateInputs(address, mEditAddress) ){
                    call = service.register(name, email, password, phone, address);
                    call.enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            Log.w(TAG, "onResponse: " + response);

                            if (response.isSuccessful()) {

                                mTokenManager.saveToken(response.body());
                                startActivity(new Intent(Register2Activity.this, MainActivity.class));

                                finish();
                            } else {
                                Log.w(TAG, "onResponse: " + response.errorBody());
                                startActivity(new Intent(Register2Activity.this, RegisterActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            Log.w(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });

    }
    private boolean validateInputs(String value, EditText view) {
        if (value.isEmpty()) {
            view.setError(view.getHint() + " Field must not be empty");
            view.requestFocus();
            return false;
        } else {
            view.setError(null);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }
}
