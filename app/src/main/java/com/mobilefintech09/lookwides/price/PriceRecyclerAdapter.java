package com.mobilefintech09.lookwides.price;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.RecyclerViewAdapter;

import java.util.List;

public class PriceRecyclerAdapter extends RecyclerView.Adapter<PriceRecyclerAdapter.ViewHolder> {

    private static final String TAG = "PriceRecyclerAdapter";
    private List<String>  text_view_large, text_view_location_price;
    private List<Integer> text_num;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public PriceRecyclerAdapter(List<Integer> text_num, List<String> text_view_large, List<String> text_view_location_price, Context context) {
        this.text_num = text_num;
        this.text_view_large = text_view_large;
        this.text_view_location_price = text_view_location_price;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_price_orders, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.w(TAG, String.valueOf(position));
        holder.text_number.setText(String.valueOf(text_num.get(position)));
        holder.text_view_large.setText(text_view_large.get(position));
        holder.text_view_location.setText("Naira: " + text_view_location_price.get(position));
    }

    @Override
    public int getItemCount() {
        return text_view_large.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView text_number, text_view_large, text_view_location;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_number = itemView.findViewById(R.id.text_num);
            text_view_large = itemView.findViewById(R.id.text_view_large);
            text_view_location = itemView.findViewById(R.id.text_view_location);

        }
    }
}
