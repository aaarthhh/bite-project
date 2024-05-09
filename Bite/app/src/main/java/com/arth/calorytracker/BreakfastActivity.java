package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arth.calorytracker.adapters.FooAddAdapter;
import com.arth.calorytracker.adapters.Foodadapter;
import com.arth.calorytracker.models.Food;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BreakfastActivity extends AppCompatActivity {

    Food f;
    private static RequestQueue requestQueue;
    RecyclerView rvfoods, rvaddfood;
    ArrayList<Food> alfoods = new ArrayList<>();
    ArrayList<Food> aladdfoods = new ArrayList<>();
    Utility utility;
    LinearLayout llfoodcard;
    String url;
    EditText etsearch;
    DatabaseReference databaseReference;
    ImageView ivcon;
    FooAddAdapter fadapter;
    TextView tvname, tvfat, tvcarbs, tvpro, tvtotal;
    private static String[] nutrients = {
            "nf_calories",
            "nf_total_fat",
            "nf_saturated_fat",
            "nf_cholesterol",
            "nf_sodium",
            "nf_total_carbohydrate",
            "nf_dietary_fiber",
            "nf_sugars",
            "nf_protein",
            "nf_potassium"
    };

    private static Map<String, String> nutritionDict = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);
        String title = getIntent().getStringExtra("name");
        setTitle(title);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


        etsearch = findViewById(R.id.etsearch);
        databaseReference = FirebaseDatabase.getInstance().getReference("BreakFast")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ivcon = findViewById(R.id.ivfood);
        tvname = findViewById(R.id.tvfoodname);
        tvpro = findViewById(R.id.tvpro);
        tvcarbs = findViewById(R.id.tvcarbs);
        tvfat = findViewById(R.id.tvfat);
        tvtotal = findViewById(R.id.tvtotal);
        llfoodcard = findViewById(R.id.llfoodcard);
        requestQueue = Volley.newRequestQueue(this);
        rvfoods = findViewById(R.id.rvfood);
        rvfoods.setHasFixedSize(true);
        rvfoods.setLayoutManager(new LinearLayoutManager(BreakfastActivity.this));

        rvaddfood = findViewById(R.id.rvselfood);
        rvaddfood.setHasFixedSize(true);
        rvaddfood.setLayoutManager(new LinearLayoutManager(BreakfastActivity.this));

        utility = new Utility(BreakfastActivity.this);
        buildRecyclerview();
 /*       etsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etsearch.getText().toString().matches("")) {
                    etsearch.setError("Enter Food Name");

                } else {
                    String foodItem = etsearch.getText().toString().trim();
                   // startAPICall(foodItem);

                }
            }
        });*/

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

                alfoods.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        alfoods.add(data.getValue(Food.class));

                    }

                    Foodadapter foodadapter = new Foodadapter(alfoods, BreakfastActivity.this, databaseReference);
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

                        Food food = data.getValue(Food.class);
                        aladdfoods.add(food);
                    }
                    fadapter = new FooAddAdapter(aladdfoods, BreakfastActivity.this, foodref, "BreakFast");
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


    void startAPICall(final String foodItem) {
        try {
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("query", foodItem);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://trackapi.nutritionix.com/v2/natural/nutrients",
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                apiCallDone(response);
                            } catch (Exception e) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    final ProgressBar waitingBar = findViewById(R.id.pg);
                    Context context = getApplicationContext();
                    CharSequence warning = "No info is available for this food item";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, warning, duration);
                    toast.show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("x-app-id", "cb62f9c5");
                    params.put("x-app-key", "88831702f2346577a3743d52cd50cd84");
                    params.put("x-remote-user-id", "0");
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void apiCallDone(final JSONObject response) {
        try {
            JSONObject parsed = parseIt(response);
            nutritionDict = generateNutritionDict(parsed);
            showImage(parseIt(response));

            setFood(parsed);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    JSONObject parseIt(JSONObject response) {
        try {
            return response.getJSONArray("foods").getJSONObject(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> generateNutritionDict(JSONObject jsonObject) {
        Map<String, String> nutritionDict = new HashMap<>();
        String value;
        for (String nutrient : nutrients) {
            try {
                value = jsonObject.getString(nutrient);
            } catch (Exception e) {
                value = "no info available";
            }
            nutritionDict.put(nutrient, value);
        }
        return nutritionDict;
    }


    public Food setFood(final JSONObject jsonObject) {
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("foods");
            String id, name, pro, fat, carbs, calories, imageurl;

            id = databaseReference.push().getKey().toString();
            name = etsearch.getText().toString();
            pro = jsonObject.getString("nf_protein");
            fat = jsonObject.getString("nf_total_fat");
            carbs = jsonObject.getString("nf_total_carbohydrate");
            calories = jsonObject.getString("nf_calories");
            imageurl = url;

            f = new Food(id, name, pro, fat, carbs, calories, f.getEtqty(), imageurl);
            tvfat.setText("fat : " + f.getFat());
            tvcarbs.setText("carbs : " + f.getCarbs());
            tvpro.setText("pro : " + f.getPro());
            tvtotal.setText("total : " + f.getCalories());
            tvname.setText(name);
            Glide.with(this).load(url).into(ivcon);

            llfoodcard.setVisibility(View.VISIBLE);
            return f;

        } catch (JSONException e) {
            e.printStackTrace();
            llfoodcard.setVisibility(View.GONE);
            return null;
        }
    }

    void showImage(JSONObject jsonObject) {
        try {
            url = jsonObject.getJSONObject("photo").getString("thumb");
            final ImageView imageView = findViewById(R.id.imageView);
            Glide.with(this).load(url).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Food> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Food item : aladdfoods) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), DietActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


/*

     findViewById(R.id.btnaddfood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                if (utility.checkInternetConnectionALL())
                {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren())
                            {
                                boolean isthere = true;
                                for (DataSnapshot ds:dataSnapshot.getChildren())
                                {
                                    Food t = ds.getValue(Food.class);

                                    if (t.getName().equals(etsearch.getText().toString().trim()))
                                    {
                                        isthere = false;
                                        break;
                                    }

                                }
                                if (isthere)
                                {
                                    String id = databaseReference.push().getKey();
                                    Food ff =new Food(id,f.getName(),f.getPro(),f.getFat(),f.getCarbs(),f.getCalories(),f.getEtqty(),f.getImageurl());
                                    databaseReference.child(id).setValue(ff);
                                    Snackbar snackbar = Snackbar.make(view, "successfully Added", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                }
                                else
                                {
                                    Snackbar snackbar = Snackbar
                                            .make(view, "Food Entry is Already Exists", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                }

                            }
                            else
                            {
                                String id = databaseReference.push().getKey();
                                Food ff =new Food(id,f.getName(),f.getPro(),f.getFat(),f.getCarbs(),f.getCalories(),f.getEtqty(),f.getImageurl());
                                databaseReference.child(id).setValue(ff);
                                Snackbar snackbar = Snackbar.make(view, "successfully Added", Snackbar.LENGTH_SHORT);

                                snackbar.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });
 */
