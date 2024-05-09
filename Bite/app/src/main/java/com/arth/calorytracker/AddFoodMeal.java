package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.arth.calorytracker.adapters.FooAddAdapter;
import com.arth.calorytracker.adapters.Foodadapter;
import com.arth.calorytracker.adapters.MealAddAdapter;
import com.arth.calorytracker.adapters.Mealadapter;
import com.arth.calorytracker.models.Food;
import com.arth.calorytracker.models.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFoodMeal extends AppCompatActivity {
    Food f;
    private static RequestQueue requestQueue;
    RecyclerView rvfoods, rvaddfood;
    ArrayList<Meal> alfoods = new ArrayList<>();
    ArrayList<Meal> aladdfoods = new ArrayList<>();
    Utility utility;
    MealAddAdapter fadapter;
    EditText etsearch;
    String title;

    DatabaseReference databaseReference;
    TextView tvmealname,tvtotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_meal);
        tvtotal = findViewById(R.id.tvtotal);

        title = getIntent().getStringExtra("name");
        setTitle("Make Meal");

        tvmealname = findViewById(R.id.tvmealname);
        tvmealname.setText(title);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        etsearch = findViewById(R.id.etsearch);
        rvfoods = findViewById(R.id.rvfood);
        rvfoods.setHasFixedSize(true);
        rvfoods.setLayoutManager(new LinearLayoutManager(AddFoodMeal.this));

        databaseReference = FirebaseDatabase.getInstance().getReference("meals")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(title);
        rvaddfood = findViewById(R.id.rvselfood);
        rvaddfood.setHasFixedSize(true);
        rvaddfood.setLayoutManager(new LinearLayoutManager(AddFoodMeal.this));

        utility = new Utility(AddFoodMeal.this);
        buildRecyclerview();

        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String s = String.valueOf(charSequence);

                if (s.equalsIgnoreCase("")) {
                    rvaddfood.setVisibility(View.GONE);
                } else {
                    filter(s);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double tc = 0.0;
                alfoods.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        alfoods.add(data.getValue(Meal.class));
                        tc = tc + Double.parseDouble(data.getValue(Meal.class).getCalories());

                    }

                    tvtotal.setText("Total Calories = " +String.valueOf(tc));
                    Mealadapter foodadapter= new Mealadapter(alfoods, AddFoodMeal.this, databaseReference);
                    rvfoods.setAdapter(foodadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void buildRecyclerview() {

        final DatabaseReference foodref = FirebaseDatabase.getInstance().getReference("foods")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        foodref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Meal food = data.getValue(Meal.class);
                        aladdfoods.add(food);
                    }
                    fadapter = new MealAddAdapter(aladdfoods, AddFoodMeal.this, foodref, title);
                    rvaddfood.setAdapter(fadapter);

                } else {
                    rvaddfood.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Meal> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Meal item : aladdfoods) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
                rvaddfood.setVisibility(View.VISIBLE);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            rvaddfood.setVisibility(View.GONE);

            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            fadapter.filterList(filteredlist);
        }
    }

}
