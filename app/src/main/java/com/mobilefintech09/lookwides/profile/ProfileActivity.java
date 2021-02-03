package com.mobilefintech09.lookwides.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.entities.ImageResponse;
import com.mobilefintech09.lookwides.entities.OrderResponses;
import com.mobilefintech09.lookwides.entities.UserResponse;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;
import com.mobilefintech09.lookwides.ui.gallery.GalleryViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    Call<ImageResponse> mCall;

    TextView username, email, phone, date_created, txt_customer_name, txt_customer_order;
    private GalleryViewModel galleryViewModel;

    Call<List<OrderResponses>> caller;
    ApiService service;
    Call<UserResponse> call;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;
    FloatingActionButton mFab;
    View mRoot;
    UserResponse mUserResponse;
    private Bitmap bitmap;
    File file;
    String id;
    Uri filePath;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        date_created = findViewById(R.id.date_registered);
        mImageView = findViewById(R.id.image_avatar);
        mFab = findViewById(R.id.floatingActionButton);
        txt_customer_name = findViewById(R.id.user_name_txt);
        txt_customer_order = findViewById(R.id.num_complete_orders);


        mTokenManager = TokenManager.getInstance(getSharedPreferences("prefs", Context.MODE_PRIVATE));
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getSharedPreferences("lookwidesuserdetails", Context.MODE_PRIVATE), getApplicationContext());
        service = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);
        if(mSharedPreferenceManager == null){
            viewProfile();
            getCompletedOrders();
        } else {
            viewProfile();
            getCompletedOrders();
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

    }
    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        String[] mimeTypes = new String[] { "image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                file =  new File(Objects.requireNonNull(filePath.getPath()));

                mImageView.setImageBitmap(bitmap);

                if(bitmap != null){
                    postAvatar();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void postAvatar() throws IOException {

        ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(filePath, "r", null);
        if(descriptor == null){
            return;
        }

        InputStream inputStream = new FileInputStream(descriptor.getFileDescriptor());

        File file1 =  new File(getCacheDir(), file.getName());

        OutputStream outputStream = new FileOutputStream(file1);

        copy(inputStream, outputStream);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file1));
        //RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), avatar);

        mCall = service.uploadAvatar(fileToUpload);
        mCall.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (!response.isSuccessful()) {
                    Log.w(TAG, "Unsuccessful: " + response.code() + response);
                    return;
                } else {
                    Log.w(TAG, "Success: " + response.body());
                    ImageResponse message = response.body();
                }
            }
            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1)
                break;
            out.write(buffer, 0, bytesRead);
        }
    }

    public void getCompletedOrders() {
        caller = service.getCompletedOrders();
        caller.enqueue(new Callback<List<OrderResponses>>() {
            @Override
            public void onResponse(Call<List<OrderResponses>> call, Response<List<OrderResponses>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body());
                    List<OrderResponses> responses = response.body();
                    String completedOrders = String.valueOf(responses.size());
                    txt_customer_order.setText(completedOrders);
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponses>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void viewProfile() {
        call = service.getUser();
        call.enqueue(new Callback<UserResponse>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    mSharedPreferenceManager.userLogin(response.body());
                    username.setText( response.body().getUser().getName());
                    email.setText(response.body().getUser().getEmail());
                    phone.setText(response.body().getUser().getPhone());
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                    try {
                        date_created.setText(formatter.format(Objects.requireNonNull(parser.parse(response.body().getUser()
                                .getCreatedAt()))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    txt_customer_name.setText(response.body().getUser().getName());
                    mUserResponse = response.body();
                    String image = response.body().getUser().getAvatar();
                    if(image != null) {
                        Picasso.get()
                                .load(image)
                                .centerCrop()
                                .fit().into(mImageView);
                    }
                    Log.w(TAG, "onSuccess: " + response.body());
                    Toast.makeText(ProfileActivity.this, response.message(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.code() == 422) {
                        Log.w(TAG, "onResponse: " + response.errorBody());
                        Toast.makeText(ProfileActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 401) {
                        Log.w(TAG, "onResponse: " + response.errorBody());
                        Toast.makeText(ProfileActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void updateProfile() {
        String name = mSharedPreferenceManager.getNewUser().getName();
        String phones = mSharedPreferenceManager.getNewUser().getPhone();
        String emails = mSharedPreferenceManager.getNewUser().getEmail();
        String date_registered = String.format(mSharedPreferenceManager.getNewUser().getCreatedAt(), "dd MMMMM yyyyy");
        String completed = mSharedPreferenceManager.getNewOrder().getStatus();
        if(name != null){
            username.setText(name);
        }
        if(emails != null){
            email.setText(emails);
        }
        if(phones != null){
            phone.setText(phones);
        }
        if(date_registered != null){

            date_created.setText(date_registered);
        }
        if(name != null){
            txt_customer_name.setText(name);
        }


    }
}
