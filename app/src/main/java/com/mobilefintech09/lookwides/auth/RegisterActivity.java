package com.mobilefintech09.lookwides.auth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.mobilefintech09.lookwides.MainActivity;
import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.entities.AccessToken;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.TokenManager;

import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {
    EditText mEditText, mEditText1, mEditText2;
    Button mButton;
    TextView mView;

    ApiService service;
    Call<AccessToken> call;
    AwesomeValidation validator;
    TokenManager mTokenManager;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        service = RetrofitBuilder.createService(ApiService.class);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        mEditText = findViewById(R.id.username);
        mEditText1 = findViewById(R.id.email);
        mEditText2 = findViewById(R.id.password);

        mView = findViewById(R.id.go_to_login);

        mProgressBar = findViewById(R.id.loading);
        mProgressBar.setVisibility(View.GONE);

        mButton = findViewById(R.id.register);

        if(mTokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String name = mEditText.getEditableText().toString();
                String email = mEditText1.getEditableText().toString();
                String password = mEditText2.getEditableText().toString();

                if(validateInputs(name, mEditText) && validateInputs(email, mEditText1) && validateInputs(password, mEditText2)){
                    Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);

                }
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

    }
    public void goToLogin(){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
