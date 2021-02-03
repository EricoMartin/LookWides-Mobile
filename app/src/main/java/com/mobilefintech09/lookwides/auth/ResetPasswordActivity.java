package com.mobilefintech09.lookwides.auth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.mobilefintech09.lookwides.MainActivity;
import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.entities.AccessToken;
import com.mobilefintech09.lookwides.entities.UserResponse;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";

    EditText mEditUsername, mEditPassword, mEditPassword2;
    Button mButton;

    ApiService mApiService;
    ApiService service;
    Call<AccessToken> call;
    Call<UserResponse> caller;
    AwesomeValidation validator;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mEditUsername = findViewById(R.id.username);
        mEditPassword = findViewById(R.id.password);
        
        mEditPassword2 = findViewById(R.id.password2);
        
        mButton = findViewById(R.id.login);
        mProgressBar = findViewById(R.id.loading);
        mProgressBar.setVisibility(View.GONE);

        service = RetrofitBuilder.createService(ApiService.class);
        mTokenManager = TokenManager.getInstance((getSharedPreferences("prefs", MODE_PRIVATE)));
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());
        mApiService = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String username = mEditUsername.getEditableText().toString();
                String password = mEditPassword.getEditableText().toString();
                String confirm_password = mEditPassword2.getEditableText().toString();

                if(password == confirm_password && validateInputs(username, mEditUsername) && validateInputs(password, mEditPassword) && validateInputs(confirm_password, mEditPassword2)){
                    call = service.resetPassword(password, confirm_password);
                    call.enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            Log.w(TAG, "onResponse: " + response);

                            if (response.isSuccessful()) {
                                mTokenManager.saveToken(response.body());

                                startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
                                finish();
                            } else {
                                if (response.code() == 422) {
                                    Log.w(TAG, "onResponse: " + response.errorBody());
                                    Toast.makeText(ResetPasswordActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                }
                                if (response.code() == 401) {
                                    Log.w(TAG, "onResponse: " + response.errorBody());
                                    Toast.makeText(ResetPasswordActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            Log.w(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(ResetPasswordActivity.this,  t.getMessage(), Toast.LENGTH_LONG).show();
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
}
