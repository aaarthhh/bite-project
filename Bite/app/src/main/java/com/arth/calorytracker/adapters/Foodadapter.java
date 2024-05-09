package com.arth.calorytracker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arth.calorytracker.R;
import com.arth.calorytracker.models.Food;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Foodadapter extends RecyclerView.Adapter<Foodadapter.VH> {


    ArrayList<Food> alcos;
    Context context;

    DatabaseReference dr;

    public Foodadapter(ArrayList<Food> alcos, Context context,DatabaseReference dr) {
        this.alcos = alcos;
        this.context = context;
        this.dr= dr;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardfod, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {


        Food f = alcos.get(position);

        holder.tvname.setText(f.getName()+"( "+f.getEtqty()+" )");
        holder.tvpro.setText("Protein\n"+f.getPro()+" gms");
        holder.tvcarbs.setText("Carbs\n"+f.getCarbs()+" gms");
        holder.tvfat.setText("Fat\n"+f.getFat()+" gms");
        holder.tvtotal.setText("Toatal Calories \n"+f.getCalories());

        Picasso.with(context)
                .load(f.getImageurl())
                .centerCrop()
                .fit()
                .into(holder.ivcon);

       holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               AlertDialog.Builder  builder = new AlertDialog.Builder(context);
                   builder.setMessage("Do you want to Delete this Item ?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {

                               Food f = alcos.get(position);
                               dr.child(f.getId()).removeValue();
                               Toast.makeText(context,"Item Removed",Toast.LENGTH_SHORT).show();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               //  Action for 'NO' Button
                               dialog.cancel();

                           }
                       });
               AlertDialog alert = builder.create();
               alert.show();

               return false;
           }
       });


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
        public VH(@NonNull View itemView) {
            super(itemView);

            ivcon = itemView.findViewById(R.id.ivfood);
            tvname = itemView.findViewById(R.id.tvfoodname);
            tvpro = itemView.findViewById(R.id.tvpro);
            tvcarbs = itemView.findViewById(R.id.tvcarbs);
            tvfat = itemView.findViewById(R.id.tvfat);
            tvtotal = itemView.findViewById(R.id.tvtotal);
            v1 = itemView.findViewById(R.id.v1);
            linearLayout = itemView.findViewById(R.id.llcard);

        }
    }
}
