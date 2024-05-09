package com.arth.calorytracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arth.calorytracker.R;
import com.arth.calorytracker.models.Food;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddFoodAdapter extends RecyclerView.Adapter<AddFoodAdapter.VH> {


    ArrayList<Food> alcos;
    Context context;


    public AddFoodAdapter(ArrayList<Food> alcos, Context context) {
        this.alcos = alcos;
        this.context = context;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardaddfod, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {


        Food f = alcos.get(position);

        holder.tvname.setText(f.getName());
        holder.tvpro.setText("Protein\n"+f.getPro()+" gms");
        holder.tvcarbs.setText("Carbs\n"+f.getCarbs()+" gms");
        holder.tvfat.setText("Fat\n"+f.getFat()+" gms");
        holder.tvtotal.setText("Toatal Calories \n"+f.getCalories());

        holder.btnadd.setVisibility(View.GONE);

        Picasso.with(context)
                .load(f.getImageurl())
                .centerCrop()
                .fit()
                .into(holder.ivcon);



    }

    @Override
    public int getItemCount() {
        return alcos.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView ivcon;
        TextView tvname,tvfat,tvcarbs,tvpro,tvtotal;
        LinearLayout linearLayout;
        View v1;
        Button btnadd;
        public VH(@NonNull View itemView) {
            super(itemView);

            ivcon = itemView.findViewById(R.id.ivfood);
            tvname = itemView.findViewById(R.id.tvfoodname);
            tvpro = itemView.findViewById(R.id.tvpro);
            tvcarbs = itemView.findViewById(R.id.tvcarbs);
            tvfat = itemView.findViewById(R.id.tvfat);
            tvtotal = itemView.findViewById(R.id.tvtotal);
            btnadd = itemView.findViewById(R.id.btnaddfood);
            v1 = itemView.findViewById(R.id.v1);

        }
    }
}
