package com.mobilefintech09.lookwides.orders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilefintech09.lookwides.R;
import com.mobilefintech09.lookwides.entities.OrderResponses;

import java.util.List;

public class CompletedOrdersAdapter extends RecyclerView.Adapter<CompletedOrdersAdapter.OrderAdapterVH>{
    private List<OrderResponses> mOrderResponses;
    private Context context;

    public CompletedOrdersAdapter(){
        super();

    }
    public void setData(List<OrderResponses> mOrderResponses){
        this.mOrderResponses = mOrderResponses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompletedOrdersAdapter.OrderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CompletedOrdersAdapter.OrderAdapterVH(LayoutInflater.from(context).inflate(R.layout.item_all_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrdersAdapter.OrderAdapterVH holder, int position) {
        List<OrderResponses> orderResponse = mOrderResponses;

            holder.mTextNum.setText(String.valueOf(position + 1));
            holder.mTextView1.setText("Transaction ID: " + orderResponse.get(position).getTranxId());
            holder.mTextView2.setText("Location: " + orderResponse.get(position).getLocation());
            holder.mTextView3.setText("Destination: " + orderResponse.get(position).getDestination());
            holder.mTextView4.setText("Status: " + orderResponse.get(position).getStatus());

    }


    @Override
    public int getItemCount() {
        try{
            return mOrderResponses.size();
        }catch (Exception e){
            Log.e(String.valueOf(mOrderResponses), "Raised an exception during getItemCount Method", e);
        }
        return mOrderResponses.size();
    }

    public class OrderAdapterVH extends RecyclerView.ViewHolder{

        CardView mCardView;
        TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextNum;

        public OrderAdapterVH(View viewItem) {
            super(viewItem);
            mCardView = viewItem.findViewById(R.id.cardView_background);
            mTextView1 = viewItem.findViewById(R.id.text_view_large);
            mTextView2 = viewItem.findViewById(R.id.text_view_location);
            mTextView3 = viewItem.findViewById(R.id.text_view_destination);
            mTextView4 = viewItem.findViewById(R.id.text_view_status);
            mTextNum = viewItem.findViewById(R.id.text_num);
        }
    }
}
