package com.example.samrtcarduser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samrtcarduser.R;
import com.example.samrtcarduser.helper.NumberCardHelper;
import com.example.samrtcarduser.helper.TransactionItem;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    public Context context;
    public List<TransactionItem> list;


    public TransactionAdapter(Context context, List<TransactionItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_item_transaction, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TransactionItem helper = list.get(position);

        holder.tvPriceNumber.setText(helper.getPrice());
        holder.tvCardNumber.setText(helper.getCardNumber());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvCardNumber, tvPriceNumber;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCardNumber = itemView.findViewById(R.id.tv_card_number);
            tvPriceNumber = itemView.findViewById(R.id.tv_card_price);

        }
    }
}
