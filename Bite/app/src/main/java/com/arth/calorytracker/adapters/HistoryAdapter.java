package com.arth.calorytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arth.calorytracker.R;
import com.arth.calorytracker.models.HisCalo;
import com.arth.calorytracker.models.Weightmodel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.WVH> {

    ArrayList<HisCalo> wmlist;
    Context context;


    public HistoryAdapter() {
    }

    public HistoryAdapter(ArrayList<HisCalo> wmlist, Context context) {
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

        HisCalo w = wmlist.get(position);

        holder.tvcal.setText(w.getTotalcal()+" kcal");
        holder.tvdate.setText(w.getDate());

    }

    @Override
    public int getItemCount() {
        return wmlist.size();
    }


    public static class WVH extends RecyclerView.ViewHolder {

        TextView tvcal,tvdate;
        public WVH(@NonNull View itemView) {
            super(itemView);

            tvdate = itemView.findViewById(R.id.tvdate);
            tvcal = itemView.findViewById(R.id.tvweight);


        }
    }
}