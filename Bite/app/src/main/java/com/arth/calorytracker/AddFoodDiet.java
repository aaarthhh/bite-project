package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.arth.calorytracker.adapters.AddFoodAdapter;
import com.arth.calorytracker.adapters.Foodadapter;
import com.arth.calorytracker.models.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFoodDiet extends AppCompatActivity {

    RecyclerView rvfoods;
    ArrayList<Food> alfoods = new ArrayList<>();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_adapter);
        String title = getIntent().getStringExtra("name");
        setTitle(title);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        rvfoods = findViewById(R.id.rvfood);
        rvfoods.setHasFixedSize(true);
        rvfoods.setLayoutManager(new LinearLayoutManager(AddFoodDiet.this));


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

                    AddFoodAdapter foodadapter = new AddFoodAdapter(alfoods,AddFoodDiet.this);
                    rvfoods.setAdapter(foodadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
