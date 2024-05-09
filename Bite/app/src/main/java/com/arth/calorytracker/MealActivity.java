package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.arth.calorytracker.adapters.FooAddAdapter;
import com.arth.calorytracker.adapters.Foodadapter;
import com.arth.calorytracker.models.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MealActivity extends AppCompatActivity {

    RecyclerView rvfoods;
    DatabaseReference databaseReference;
    ArrayList<Food> al = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        rvfoods = findViewById(R.id.rvfoods);
        rvfoods.setHasFixedSize(true);
        rvfoods.setLayoutManager(new LinearLayoutManager(MealActivity.this));

        databaseReference = FirebaseDatabase.getInstance().getReference("foods")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                al.clear();
                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {

                        Food f = data.getValue(Food.class);
                        al.add(f);
                    }

                    Foodadapter fooAddAdapter = new Foodadapter(al,MealActivity.this,databaseReference);
                    rvfoods.setAdapter(fooAddAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
