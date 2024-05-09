package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arth.calorytracker.Font.EditText_Regular;
import com.arth.calorytracker.adapters.Foodadapter;
import com.arth.calorytracker.adapters.Mealadapter;
import com.arth.calorytracker.adapters.Mealsssadapter;
import com.arth.calorytracker.models.Food;
import com.arth.calorytracker.models.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateMeal extends AppCompatActivity {
    RecyclerView rvfoods;
    DatabaseReference databaseReference;
    ArrayList<Meal> al = new ArrayList<>();
    ArrayList<String> alnames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);
        rvfoods = findViewById(R.id.rvmeals);
        rvfoods.setHasFixedSize(true);
        rvfoods.setLayoutManager(new LinearLayoutManager(CreateMeal.this));

        String title = "Meals";
        setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("meals")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                alnames.clear();
                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {

                       alnames.add(data.getKey());

                    }

                    Mealsssadapter fooAddAdapter = new Mealsssadapter(alnames,CreateMeal.this,databaseReference);
                    rvfoods.setAdapter(fooAddAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.btncm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateMeal.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.cardadd, viewGroup, false);

                final EditText etname = dialogView.findViewById(R.id.etcm);
                Button btnadd = dialogView.findViewById(R.id.btnadd);


                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (etname.getText().toString().equals("")) {
                            etname.setError("Enter Meal Name");
                        } else {

                            startActivity(new Intent(CreateMeal.this, AddFoodMeal.class).
                                    putExtra("name", etname.getText().toString()));

                        }
                    }
                });


                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }
}
