package com.mobilefintech09.lookwides.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobilefintech09.lookwides.R;

public class AuthActivity extends AppCompatActivity {
    Button mButton, mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mButton = findViewById(R.id.button);
        mButton2 = findViewById(R.id.button_login);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
                    }
                });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthActivity.this, LoginActivity.class));
            }
        });
    }
}
