package com.mobilefintech09.lookwides.orders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.settings.SettingsActivity;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String textList[]  = {
                getResources().getString(R.string.accounts),
                getResources().getString(R.string.accounts_ans),
                getResources().getString(R.string.details),
                getResources().getString(R.string.details_ans),
                getResources().getString(R.string.transfers),
                getResources().getString(R.string.transfers_ans),
                getResources().getString(R.string.pin_details),
                getResources().getString(R.string.pin_details_ans),
                getResources().getString(R.string.withdrawal),
                getResources().getString(R.string.withdrawal_ans)
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_f_a_q, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings :
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_call :
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 09131174726"));
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
