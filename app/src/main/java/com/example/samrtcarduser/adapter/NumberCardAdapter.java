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


import java.util.List;

public class NumberCardAdapter extends RecyclerView.Adapter<NumberCardAdapter.ViewHolder> {

    public Context context;
    public List<NumberCardHelper> list;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public NumberCardAdapter(Context context, List<NumberCardHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_item_card_number, parent, false);
        ViewHolder vh = new ViewHolder(view, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final NumberCardHelper helper = list.get(position);

        holder.tvNumberCard.setText(helper.getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        private CardView cardView;
        public TextView tvNumberCard;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            tvNumberCard = itemView.findViewById(R.id.tv_number_card);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }

                    }
                }
            });

        }
    }
}
