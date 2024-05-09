package com.arth.calorytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arth.calorytracker.R;
import com.arth.calorytracker.models.Food;
import com.arth.calorytracker.models.Weightmodel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WVH> {

    ArrayList<Weightmodel> wmlist;
    Context context;


    public WeightAdapter() {
    }

    public WeightAdapter(ArrayList<Weightmodel> wmlist, Context context) {
        this.wmlist = wmlist;
        this.context = context;
    }

    @NonNull
    @Override
    public WVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_weight, parent, false);
        return new WVH(view);    }

    @Override
    public void onBindViewHolder(@NonNull WVH holder, int position) {

        Weightmodel w = wmlist.get(position);

        holder.tvweight.setText(w.getWeight()+" kgs");
        holder.tvdate.setText(w.getDate());

    }

    @Override
    public int getItemCount() {
        return wmlist.size();
    }


    public static class WVH extends RecyclerView.ViewHolder {

        TextView tvweight,tvdate;
        public WVH(@NonNull View itemView) {
            super(itemView);

            tvdate = itemView.findViewById(R.id.tvdate);
            tvweight = itemView.findViewById(R.id.tvweight);


        }
    }
}
