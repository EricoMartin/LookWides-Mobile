package com.mobilefintech09.lookwides.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilefintech09.lookwides.MainActivity;
import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.RecyclerViewAdapter;
import com.mobilefintech09.lookwides.auth.LoginActivity;
import com.mobilefintech09.lookwides.entities.AccessToken;
import com.mobilefintech09.lookwides.entities.UserResponse;
import com.mobilefintech09.lookwides.network.ApiService;
import com.mobilefintech09.lookwides.network.RetrofitBuilder;
import com.mobilefintech09.lookwides.network.SharedPreferenceManager;
import com.mobilefintech09.lookwides.network.TokenManager;
import com.mobilefintech09.lookwides.orders.NewOrderActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    List<Integer> iconList = new ArrayList<>();
    List<String> textList = new ArrayList<>();
    List<String> textList2 = new ArrayList<>();

    //Add a List of tags to dynamically call the different activity classes
    List<String> tagList = new ArrayList<>();
    ApiService service, mApiService;
    Call<AccessToken> call;
    TokenManager mTokenManager;
    SharedPreferenceManager mSharedPreferenceManager;
    TextView mTextView;
    ImageView mImageView;
    private View mRoot;
    private RecyclerViewAdapter mRcvAdapter;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mMLayoutManager;
    Call<UserResponse> caller;
    UserResponse mUserResponse;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.fragment_home, container, false);
        Button btn = mRoot.findViewById(R.id.button2);
        mTextView = mRoot.findViewById(R.id.textView3);
        mImageView = mRoot.findViewById(R.id.imageView4);

        service = RetrofitBuilder.createService(ApiService.class);
        mTokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        mSharedPreferenceManager= SharedPreferenceManager.getInstance(getActivity().getSharedPreferences("lookwidesuserdetails", MODE_PRIVATE), requireContext().getApplicationContext());
        mApiService = RetrofitBuilder.createAuthService(ApiService.class, mTokenManager);

        displayUser();
        mRecyclerView = mRoot.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mMLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mMLayoutManager);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewOrderActivity.class));
            }
        });

        displayRecyclerContent();

        return mRoot;
    }

    private void displayUser() {
        caller = mApiService.getUser();
        caller.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    mSharedPreferenceManager.userLogin(response.body());
                    mTextView.setText("Welcome, " + response.body().getUser().getName());
                    String image = response.body().getUser().getAvatar();
                    if(image != null) {
                        Picasso.get()
                                .load(image)
                                .centerCrop()
                                .fit().into(mImageView);
                    }
                    mUserResponse = response.body();
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
        if(mUserResponse == null) {
            mTextView.setText("Welcome");
        }
        String images = mSharedPreferenceManager.getNewUser().getAvatar();

        if(images != null) {
            Picasso.get()
                    .load(images)
                    .centerCrop()
                    .fit().into(mImageView);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mRcvAdapter.notifyDataSetChanged();
    }

    private void displayRecyclerContent() {

        iconList.add(R.drawable.order_icon);
        iconList.add(R.drawable.current_icon);
        iconList.add(R.drawable.complete_icon);
        iconList.add(R.drawable.price_icon);
        iconList.add(R.drawable.ic_person_black_24dp);
        iconList.add(R.drawable.sync);

        textList.add("All orders");
        textList.add("Current Orders");
        textList.add("Completed Orders");
        textList.add("Our Prices");
        textList.add("Profile");
        textList.add("FAQ");


        textList2.add("All orders");
        textList2.add("Current Orders");
        textList2.add("Completed Orders");
        textList2.add("Our Prices");
        textList2.add("Profile");
        textList.add("FAQ");

        tagList.add("All orders");
        tagList.add("Current Orders");
        tagList.add("Completed Orders");
        tagList.add("Our Prices");
        tagList.add("Profile");
        textList.add("FAQ");

        mRcvAdapter = new RecyclerViewAdapter(mRoot.getContext(), iconList, textList, textList2,  tagList);

        mRecyclerView.setAdapter(mRcvAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }
}
