package com.mobilefintech09.lookwides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.mobilefintech09.lookwides.auth.LoginActivity;
import com.mobilefintech09.lookwides.entities.AccessToken;
import com.mobilefintech09.lookwides.entities.UserResponse;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;
import com.mobilefintech09.lookwides.orders.FAQActivity;
import com.mobilefintech09.lookwides.settings.SettingsActivity;
import com.mobilefintech09.lookwides.ui.gallery.ProfileFragment;
import com.mobilefintech09.lookwides.ui.home.HomeFragment;
import com.mobilefintech09.lookwides.ui.slideshow.SlideshowFragment;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ApiService service, mApiService;
    Call<AccessToken> call;
    Call<UserResponse> caller;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;

    private AppBarConfiguration mAppBarConfiguration;
    NavController mNavController;
    private Handler mHandler;
    private RecyclerViewAdapter mRcvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = findViewById(R.id.button);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        PreferenceManager.setDefaultValues(this, R.xml.header_preferences, false);
        PreferenceManager.setDefaultValues(this, R.xml.messages_preferences,false);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, mNavController);

        service = RetrofitBuilder.createService(ApiService.class);
        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), getApplicationContext());
        mApiService = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);

        navigationSetUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            updateNavigationHeader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayContent();
    }

    private void navigationSetUp() {
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.nav_home){
                    mHandler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //Display fragment after 5 seconds appears
                            displayContent();
                        }
                    };

                    mHandler.postDelayed(runnable, 100);

                }else if(destination.getId() == R.id.nav_gallery){
                    mHandler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //Second fragment after 5 seconds appears
                            displayProfile();
                        }
                    };

                    mHandler.postDelayed(runnable, 100);

                }else if(destination.getId() == R.id.nav_slideshow){
                    mHandler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //Second fragment after 5 seconds appears
                            displayContact();
                        }
                    };

                    mHandler.postDelayed(runnable, 100);

                }
            }
        });
    }

    private void updateNavigationHeader() throws IOException {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);

        TextView tvName = view.findViewById(R.id.edit_name);
        TextView tvEmail = view.findViewById(R.id.edit_email);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String avatar = mSharedPreferenceManager.getNewUser().getAvatar();


        String username =  pref.getString("user_name", "Display Name");
        String user_email = pref.getString("user_email", "example@email.com");
        tvName.setText(username);
        tvEmail.setText(user_email);
        ImageView ivBank = view.findViewById(R.id.image_view);

//        if(avatar!= null){
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(avatar));
//            ivBank.setImageBitmap(bitmap);
//        }else{
            caller = mApiService.getUser();
            caller.enqueue(new Callback<UserResponse>() {

                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful()) {
                        mSharedPreferenceManager.userLogin(response.body());
                        String image = response.body().getUser().getAvatar();
                        if(image != null) {
                            Picasso.get()
                                    .load(image)
                                    .centerCrop()
                                    .fit().into(ivBank);
                        }
                        Log.w(TAG, "onSuccess: " + response.body());
                    }else
                    {
                        if (response.code() == 422) {
                            Log.w(TAG, "onResponse: " + response.errorBody());

                        }
                        if (response.code() == 401) {
                            Log.w(TAG, "onResponse: " + response.errorBody());

                        }

                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());

                }
            });
//        }


    }

    private void displayContact() {
        SlideshowFragment slideFragment = new SlideshowFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, slideFragment).addToBackStack(null).commit();
        slideFragment.setRetainInstance(true);
    }

    private void displayProfile() {
        ProfileFragment profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profileFragment).addToBackStack(null).commit();
        profileFragment.setRetainInstance(true);
    }

    private void displayContent() {
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
        homeFragment.setRetainInstance(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings :
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_logout :
                signOut();
                break;
            case R.id.faq :
                startActivity(new Intent(this, FAQActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        call = service.logout();

        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w( TAG,"onResponse: " + response);

                mTokenManager.deleteToken();
                mSharedPreferenceManager.logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
