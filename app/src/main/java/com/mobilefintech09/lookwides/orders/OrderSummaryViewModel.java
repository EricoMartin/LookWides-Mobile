package com.mobilefintech09.lookwides.orders;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class OrderSummaryViewModel extends ViewModel {

    public TaskRunner mTaskRunner;
    public LiveData mLiveData;
    private OrderSummaryActivity mOrderSummaryActivity;

    public static final String DESCRIPTION = "com.mobilefintech09.lookwides.orders.DESCRIPTION";
    public static final String LOCATION = "com.mobilefintech09.lookwides.orders.LOCATION";
    public static final String DESTINATION = "com.mobilefintech09.lookwides.orders.DESTINATION";
    public static final String WEIGHT = "com.mobilefintech09.lookwides.orders.WEIGHT";
    public static final String NUMBER_ITEMS = "com.mobilefintech09.lookwides.orders.NUMBER_ITEMS";
    public static final String PRICE = "com.mobilefintech09.lookwides.orders.PRICE";
    public static final String BITMAP = "com.mobilefintech09.lookwides.orders.BITMAP";

    public String mDescription;
    public String mLocation;
    public String mDestination;
    public String mWeight;
    public String mItems;
    public String mPrice;
    public String mBitmap;
    public boolean mIsNewlyCreated = true;

    public void saveState(Bundle outState){
        outState.putString(DESCRIPTION, mDescription);
        outState.putString(LOCATION, mLocation);
        outState.putString(DESTINATION, mDestination);
        outState.putString(WEIGHT, mWeight);
        outState.putString(NUMBER_ITEMS, mItems);
        outState.putString(PRICE, mPrice);
        outState.putString(BITMAP, mBitmap);

    }
    public void restoreState(Bundle inState){
        mDescription = inState.getString(DESCRIPTION);
        mLocation = inState.getString(LOCATION);
        mDestination = inState.getString(DESTINATION);
        mWeight = inState.getString(WEIGHT);
        mItems = inState.getString(NUMBER_ITEMS);
        mPrice = inState.getString(PRICE);
        mBitmap = inState.getString(BITMAP);
        getTaskRunner(mBitmap);
    }

    public void getTaskRunner(String bitmap) {
        mTaskRunner.executeAsync(new BitmapExtraTask(bitmap), (data) -> {
            // MyActivity activity = activityReference.get();
            // activity.progressBar.setVisibility(View.GONE);
            // populateData(activity, data) ;
            mLiveData.getValue();

            mOrderSummaryActivity.mImageView.setImageBitmap(data);

        });
    }


}
