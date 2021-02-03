package com.mobilefintech09.lookwides;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilefintech09.lookwides.orders.AllOrdersActivity;
import com.mobilefintech09.lookwides.orders.CompletedOrdersActivity;
import com.mobilefintech09.lookwides.orders.CurrentOrderActivity;
import com.mobilefintech09.lookwides.orders.FAQActivity;
import com.mobilefintech09.lookwides.price.PriceActivity;
import com.mobilefintech09.lookwides.profile.ProfileActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Integer> imageList;
    private final List<String> txtList2;
    private final List<String> txtList;
    private final List<String> tagList;

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Context context, List<Integer> imageList, List<String> txtList, List<String> txtList2,  List<String> tagList) {
        this.imageList = imageList;
        this.txtList = txtList;
        this.txtList2 = txtList2;
        this.tagList = tagList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_list_orders;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mImageView.setImageResource(imageList.get(position));
        holder.mTextView.setText(txtList.get(position));
        holder.mTextView2.setText(txtList2.get(position));
        holder.mImageView.setTag(tagList.get(position));
        holder.mCurrentPosition = position;
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView mImageView;
        final TextView mTextView;
        final TextView mTextView2;

         int mCurrentPosition;

         ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.img_item_orders);
            mTextView = view.findViewById(R.id.text_view_large);
            mTextView2 = view.findViewById(R.id.text_view_medium);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;

                    switch(mImageView.getTag().toString()){
                        case "All orders":
                            intent = new Intent(mContext, AllOrdersActivity.class);
                            intent.putExtra("Current Position: ", 1);
                            mContext.startActivity(intent);
                            break;
                        case "Current Orders":
                            intent = new Intent(mContext, CurrentOrderActivity.class);
                            intent.putExtra("Current Position: ", 2);
                            mContext.startActivity(intent);
                            break;
                        case "Completed Orders":
                            intent = new Intent(mContext, CompletedOrdersActivity.class);
                            intent.putExtra("Current Position: ", 3);
                            mContext.startActivity(intent);
                            break;
                        case "Our Prices":
                            intent = new Intent(mContext, PriceActivity.class);
                            intent.putExtra("Current Position: ", 4);
                            mContext.startActivity(intent);
                            break;
                        case "Profile":
                            intent = new Intent(mContext, ProfileActivity.class);
                            intent.putExtra("Current Position: ", 5);
                            mContext.startActivity(intent);
                            break;
                        case "FAQ":
                            intent = new Intent(mContext, FAQActivity.class);
                            intent.putExtra("Current Position: ", 6);
                            mContext.startActivity(intent);
                            break;

                    }
                }
            });
        }


    }
}
