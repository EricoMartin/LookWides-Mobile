package com.mobilefintech09.lookwides.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Integer> iconList = new ArrayList<>();
    List<String> textList = new ArrayList<>();
    List<String> textList2 = new ArrayList<>();

    //Add a List of tags to dynamically call the different activity classes
    List<String> tagList = new ArrayList<>();
//    private HomeViewModel homeViewModel;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        displayRecyclerContent();
        return mRoot;
    }

    private void displayRecyclerContent() {
        final RecyclerView mRecyclerView = mRoot.findViewById(R.id.recycler_view);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        iconList.add(R.drawable.order_icon);
        iconList.add(R.drawable.current_icon);
        iconList.add(R.drawable.complete_icon);
        iconList.add(R.drawable.price_icon);
        iconList.add(R.drawable.ic_person_black_24dp);

        textList.add("All orders");
        textList.add("Current Orders");
        textList.add("Completed Orders");
        textList.add("Order Pricing");
        textList.add("profile");

        textList2.add("All orders");
        textList2.add("Current Orders");
        textList2.add("Completed Orders");
        textList2.add("Order Pricing");
        textList2.add("profile");

        tagList.add("All orders");
        tagList.add("Current Orders");
        tagList.add("Completed Orders");
        tagList.add("withdraw");
        tagList.add("Order Pricing");
        tagList.add("profile");

        mRecyclerView.setAdapter(new RecyclerViewAdapter(getContext(), iconList, textList, textList2,  tagList));
    }
}
