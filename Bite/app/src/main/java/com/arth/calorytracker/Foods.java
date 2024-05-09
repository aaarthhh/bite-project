package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.arth.calorytracker.adapters.Foodadapter;
import com.arth.calorytracker.models.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Foods extends AppCompatActivity {

    RecyclerView rvfoods;
    ArrayList<Food> alfoods = new ArrayList<>();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        setTitle("Foods");
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        rvfoods = findViewById(R.id.rvfood);
        rvfoods.setHasFixedSize(true);
        rvfoods.setLayoutManager(new LinearLayoutManager(Foods.this));


        databaseReference = FirebaseDatabase.getInstance().getReference("foods");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot data: dataSnapshot.getChildren())
                    {

                        alfoods.add(data.getValue(Food.class));

                    }

                    Foodadapter foodadapter = new Foodadapter(alfoods,Foods.this,databaseReference);
                    rvfoods.setAdapter(foodadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
