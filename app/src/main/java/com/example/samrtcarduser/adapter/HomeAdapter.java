package com.example.samrtcarduser.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samrtcarduser.R;
import com.example.samrtcarduser.activates.CardNumberActivity;
import com.example.samrtcarduser.activates.CategoryActivity;
import com.example.samrtcarduser.activates.HomeActivity;
import com.example.samrtcarduser.activates.TypeActivity;
import com.example.samrtcarduser.helper.HomeHelper;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    public Context context;
    public List<HomeHelper> list;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public HomeAdapter(Context context, List<HomeHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_home, parent, false);
        ViewHolder vh = new ViewHolder(view, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HomeHelper helper = list.get(position);

        holder.tvName.setText(helper.getName());
        Picasso.with(context).load(helper.getImageView()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = helper.getName();
                Intent intent = null;
                if (context instanceof HomeActivity) {
                    SharedPreferences preferences = context.getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ROOT", name);
                    editor.commit();
                    intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("NAME_CARD", name);
                    context.startActivity(intent);
                }
                else if (context instanceof CategoryActivity) {
                    SharedPreferences preferences = context.getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("PARENT", name);
                    editor.commit();
                    intent = new Intent(context, TypeActivity.class);
                    intent.putExtra("NAME_CARD", name);
                    context.startActivity(intent);
                }
                else if (context instanceof TypeActivity) {
                    intent = new Intent(context, CardNumberActivity.class);
                    intent.putExtra("NAME_CARD", name);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView tvName;
        public ImageView imageView;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            tvName = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.image_view);


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
