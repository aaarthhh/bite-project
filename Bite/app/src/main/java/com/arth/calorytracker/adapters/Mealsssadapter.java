package com.arth.calorytracker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arth.calorytracker.R;
import com.arth.calorytracker.models.Food;
import com.arth.calorytracker.models.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Mealsssadapter extends RecyclerView.Adapter<Mealsssadapter.VH> {


    ArrayList<String> alname;
    Context context;

    DatabaseReference dr;

    public Mealsssadapter(ArrayList<String> alname, Context context, DatabaseReference dr) {
        this.alname = alname;
        this.context = context;
        this.dr = dr;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardmeals, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {


        final String s = alname.get(position);


        holder.tvmealname.setText(s);

        setData(holder, s);
     /*   holder.tvpro.setText("Protein\n"+f.getPro()+" gms");
        holder.tvcarbs.setText("Carbs\n"+f.getCarbs()+" gms");
        holder.tvfat.setText("Fat\n"+f.getFat()+" gms");
        holder.tvtotal.setText("Toatal Calories \n"+f.getCalories());


*/
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ;


                builder.setMessage("Do you want to Delete this Item ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String f = alname.get(position);
                                dr.child(f).removeValue();

                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("foods").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                Query query = databaseReference.orderByChild("name").equalTo(f);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChildren()) {
                                            Food f = null;
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                f = data.getValue(Food.class);
                                            }

                                            databaseReference.child(f.getId()).removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();


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

    private void setData(final VH holder, String s) {

        dr.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuffer s = new StringBuffer("( ");
                double fat = 0.0, carbs = 0.0, pro = 0.0, total = 0.0;
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Meal m = data.getValue(Meal.class);

                        fat = fat + Double.parseDouble(m.getFat());
                        carbs = carbs + Double.parseDouble(m.getCarbs());
                        pro = pro + Double.parseDouble(m.getPro());
                        total = total + Double.parseDouble(m.getCalories());

                        s.append(m.getName() + " ,");


                    }
                    s.deleteCharAt(s.length() - 1);
                    holder.tvfat.setText("Total Fat\n"+String.valueOf(fat));
                    holder.tvcarbs.setText("Total Carbs\n"+String.valueOf(carbs));
                    holder.tvpro.setText("Total Protein\n"+String.valueOf(pro));
                    holder.tvtotal.setText("Total Calories\n"+String.valueOf(total));
                    holder.tvfoods.setText(s + ")");

//.............................................................................................


                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("foods").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                        boolean b = true;


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if (dataSnapshot.hasChildren()) {

                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    Food s = d.getValue(Food.class);
                                    if ((s.getName().equals(holder.tvmealname.getText().toString()))) {
                                        b = false;
                                    }
                                }

                                if (b) {
                                    String id = databaseReference.push().getKey();

                                    String p = holder.tvpro.getText().toString().replace("Total Protein\n","");
                                   String f = holder.tvfat.getText().toString().replace("Total Fat\n","");
                                   String c = holder.tvcarbs.getText().toString().replace("Total Carbs\n","");
                                   String t = holder.tvtotal.getText().toString().replace("Total Calories\n","");
                                    Food catagorymodel = new Food(id, holder.tvmealname.getText().toString()
                                            , p
                                            , f
                                            , c
                                            ,t
                                            , "Meal"
                                            , "https://firebasestorage.googleapis.com/v0/b/abwb-d2c6d.appspot.com/o/foods%2F277604?alt=media&token=5d004305-5186-4bb8-aa50-d040382001a2");
                                    databaseReference.child(id).setValue(catagorymodel);

                                }

                            } else {
                                String id = databaseReference.push().getKey();

                                Food catagorymodel = new Food(id, holder.tvmealname.getText().toString()
                                        , holder.tvpro.getText().toString()
                                        , holder.tvfat.getText().toString()
                                        , holder.tvcarbs.getText().toString()
                                        , holder.tvtotal.getText().toString()
                                        , "Meal"
                                        , "https://firebasestorage.googleapis.com/v0/b/abwb-d2c6d.appspot.com/o/foods%2F277604?alt=media&token=5d004305-5186-4bb8-aa50-d040382001a2");
                                databaseReference.child(id).setValue(catagorymodel);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return alname.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvmealname, tvfat, tvcarbs, tvpro, tvtotal, tvfoods;
        LinearLayout linearLayout;
        View v1;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvmealname = itemView.findViewById(R.id.tvmealname);
            tvpro = itemView.findViewById(R.id.tvpro);
            tvcarbs = itemView.findViewById(R.id.tvcarbs);
            tvfat = itemView.findViewById(R.id.tvfat);
            tvtotal = itemView.findViewById(R.id.tvtotal);
            v1 = itemView.findViewById(R.id.v1);
            linearLayout = itemView.findViewById(R.id.llcard);
            tvfoods = itemView.findViewById(R.id.tvfoods);

        }
    }
}
