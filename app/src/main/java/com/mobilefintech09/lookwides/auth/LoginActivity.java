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
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText mEditUsername, mEditPassword;
    Button mButton;

    ApiService mApiService;
    ApiService service;
    Call<AccessToken> call;
    Call<UserResponse> caller;
    AwesomeValidation validator;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;
    TextView mView, mView2;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mEditUsername = findViewById(R.id.username);
        mEditPassword = findViewById(R.id.password);
        mButton = findViewById(R.id.login);
        mProgressBar = findViewById(R.id.loading);
        mProgressBar.setVisibility(View.GONE);
        mView = findViewById(R.id.go_to_login);
        mView2 = findViewById(R.id.reset);


        service = RetrofitBuilder.createService(ApiService.class);
        mTokenManager = TokenManager.getInstance((getSharedPreferences("prefs", MODE_PRIVATE)));
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());
        mApiService = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);

        if(mTokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String username = mEditUsername.getEditableText().toString();
                String password = mEditPassword.getEditableText().toString();

                if(validateInputs(username, mEditUsername) && validateInputs(password, mEditPassword) ){
                    call = service.login(username, password);
                    call.enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            Log.w(TAG, "onResponse: " + response);

                            if (response.isSuccessful()) {
                                mTokenManager.saveToken(response.body());

//                                caller = mApiService.getUser();
//                                caller.enqueue(new Callback<UserResponse>() {
//
//                                                   @Override
//                                                   public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                                                       if (response.isSuccessful()) {
//                                                           mSharedPreferenceManager.userLogin(response.body());
//                                                       }
//                                                   }
//
//                                                   @Override
//                                                   public void onFailure(Call<UserResponse> call, Throwable t) {
//                                                       Log.w(TAG, "onFailure: " + t.getMessage());
//                                                       Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                                                   }
//                                               });
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                if (response.code() == 422) {
                                    Log.w(TAG, "onResponse: " + response.errorBody());
                                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                }
                                if (response.code() == 401) {
                                    Log.w(TAG, "onResponse: " + response.errorBody());
                                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                }

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

        mView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReset();
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    public void goToRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
    public void goToReset(){
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
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
