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
import com.arth.calorytracker.Utility;
import com.arth.calorytracker.models.Datecheck;
import com.arth.calorytracker.models.Food;
import com.arth.calorytracker.models.Meal;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealAddAdapter extends RecyclerView.Adapter<MealAddAdapter.VH> {


    ArrayList<Meal> alcos;
    Context context;

    String dietcat;
    DatabaseReference dr;
    DatabaseReference databaseReference;

    Utility utility;

    public MealAddAdapter(ArrayList<Meal> alcos, Context context, DatabaseReference dr, String dietcat) {
        this.alcos = alcos;
        this.context = context;
        this.dr = dr;
        this.dietcat = dietcat;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        utility = new Utility(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cardaddfod, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAddAdapter.VH holder, final int position) {


        final Meal f = alcos.get(position);

        holder.tvname.setText(f.getName()+"(qty : "+f.getEtqty()+")");
        holder.tvpro.setText("Protein\n" + f.getPro() + " gms");
        holder.tvcarbs.setText("Carbs\n" + f.getCarbs() + " gms");
        holder.tvfat.setText("Fat\n" + f.getFat() + " gms");
        holder.tvtotal.setText("Toatal Calories \n" + f.getCalories());

        Picasso.with(context)
                .load(f.getImageurl())
                .centerCrop()
                .fit()
                .into(holder.ivcon);


        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                databaseReference = FirebaseDatabase.getInstance().getReference("meals")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dietcat);
                if (utility.checkInternetConnectionALL()) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChildren()) {
                                boolean isthere = true;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Meal t = ds.getValue(Meal.class);

                                    if ((t.getMealname().equals(f.getMealname())) ) {
                                        isthere = false;
                                        break;
                                    }

                                }
                                if (isthere) {
                                    String id = databaseReference.push().getKey();
                                    Meal ff = new Meal(id, f.getName(), f.getPro(),
                                            f.getFat(), f.getCarbs(), f.getCalories(), f.getEtqty(), f.getImageurl(),dietcat);
                                    databaseReference.child(id).setValue(ff);
                                    Snackbar snackbar = Snackbar.make(view, "successfully Added", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                  //  adddate();
                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(view, "Meal Entry is Already Exists", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                }

                            } else {
                                String id = databaseReference.push().getKey();
                                Meal ff = new Meal(id, f.getName(), f.getPro(),
                                        f.getFat(), f.getCarbs(), f.getCalories(), f.getEtqty(), f.getImageurl(),dietcat);
                                databaseReference.child(id).setValue(ff);
                                Snackbar snackbar = Snackbar.make(view, "successfully Added", Snackbar.LENGTH_SHORT);

                                snackbar.show();
                               // adddate();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

    /*    holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
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
        });*/


    }


    @Override
    public int getItemCount() {
        return alcos.size();
    }

    public void filterList(ArrayList<Meal> filterllist) {

        alcos = filterllist;

        notifyDataSetChanged();
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView ivcon;
        TextView tvname, tvfat, tvcarbs, tvpro, tvtotal;
        LinearLayout linearLayout;
        Button btnadd;
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
            btnadd = itemView.findViewById(R.id.btnadd);
            linearLayout = itemView.findViewById(R.id.llcard);

        }
    }
}

