package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arth.calorytracker.adapters.AddFoodAdapter;
import com.arth.calorytracker.models.Datecheck;
import com.arth.calorytracker.models.Food;
import com.arth.calorytracker.models.HisCalo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DietActivity extends AppCompatActivity {

    TextView tvbf, tvlunch, tvev, tvdinner;

    TextView tvbftotal, tvlunchtotal, tvevetotal, tvdinnertotal, tvtotalcalories, tvvh;
    DatabaseReference drbf, drlun, dreve, drdinner, hisref, drtodaycal;
    double totalbf = 0.0;
    double totallunch = 0.0;
    double totaleve = 0.0;
    double totaldinner = 0.0;
    String dbdate = "";

    static ArrayList<Food> alfoods = new ArrayList<>();

    DatabaseReference dateref;
    double totalcalories = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);


        tvbf = findViewById(R.id.tvbf);
        tvlunch = findViewById(R.id.tvlunch);
        tvev = findViewById(R.id.tvevbf);
        tvdinner = findViewById(R.id.tvdinner);
        tvbftotal = findViewById(R.id.tvbftotal);
        tvlunchtotal = findViewById(R.id.tvlunchtotal);
        tvevetotal = findViewById(R.id.tvevetotal);
        tvdinnertotal = findViewById(R.id.tvdinnertotal);
        tvtotalcalories = findViewById(R.id.tvtotalcalories);
        tvvh = findViewById(R.id.tvvh);

        tvvh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DietActivity.this, ViewHistory.class));
            }
        });
        dateref = FirebaseDatabase.getInstance().getReference("dc")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        drtodaycal = FirebaseDatabase.getInstance().getReference("totalcal")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        hisref = FirebaseDatabase.getInstance().getReference("calhistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        drbf = FirebaseDatabase.getInstance().getReference("BreakFast").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        drlun = FirebaseDatabase.getInstance().getReference("Lunch").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dreve = FirebaseDatabase.getInstance().getReference("EveningSnack").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        drdinner = FirebaseDatabase.getInstance().getReference("Dinner").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        getTotalCalories(drbf, drlun, dreve, drdinner);
        datesorcalories();


        tvbf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BreakfastActivity.class).putExtra("name", tvbf.getText().toString()).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        });


        tvlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LunchActivity.class).putExtra("name", tvlunch.getText().toString())
                        .
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        });


        tvev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EveningSnack.class).
                        putExtra("name", tvev.getText().toString()).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        });

        tvdinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dinner.class).
                        putExtra("name", tvdinner.getText().toString()).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        });


        findViewById(R.id.llAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Account.class));
            }
        });
        findViewById(R.id.llfood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WeightActivity.class));
            }
        });

        findViewById(R.id.lldiet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminHome.class));
            }
        });


    }


    private void getTotalCalories(DatabaseReference drbf, DatabaseReference drlun,
                                  DatabaseReference dreve, DatabaseReference drdinner) {

        alfoods.clear();
        drbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Food f = data.getValue(Food.class);
                        totalbf = totalbf + Double.parseDouble(f.getCalories());
                        tvbftotal.setText(String.valueOf(totalbf));
                        alfoods.add(f);
                    }
                    //   drtodaycal.child("Total Calories").setValue(String.format("%.2f", totalbf));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        drlun.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Food f = data.getValue(Food.class);
                        totallunch = totallunch + Double.parseDouble(f.getCalories());
                        tvlunchtotal.setText(String.valueOf(totallunch));
                        alfoods.add(f);
                    }
                    // drtodaycal.child("Total Calories").setValue(String.format("%.2f", totallunch));

                } else {
                    totalcalories = 0.0;

                    for (Food food : alfoods) {

                        totalcalories = totalcalories + Double.parseDouble(food.getCalories());

                        //Toast.makeText(getApplicationContext(),String.valueOf(alfoods.size()),Toast.LENGTH_SHORT).show();
                        tvtotalcalories.setText("Total = " + String.format("%.2f", totalcalories));
                    }
                    // Toast.makeText(getApplicationContext(), String.valueOf(alfoods.size()), Toast.LENGTH_SHORT).show();

                    drtodaycal.child("Total Calories").setValue(String.format("%.2f", totalcalories));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dreve.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Food f = data.getValue(Food.class);
                        totaleve = totaleve + Double.parseDouble(f.getCalories());
                        tvevetotal.setText(String.valueOf(totaleve));
                        alfoods.add(f);

                    }
                    // drtodaycal.child("Total Calories").setValue(String.format("%.2f", totaleve));

                } else {
                    totalcalories = 0.0;

                    for (Food food : alfoods) {

                        totalcalories = totalcalories + Double.parseDouble(food.getCalories());

                        //Toast.makeText(getApplicationContext(),String.valueOf(alfoods.size()),Toast.LENGTH_SHORT).show();
                        tvtotalcalories.setText("Total = " + String.format("%.2f", totalcalories));
                    }
                    // Toast.makeText(getApplicationContext(), String.valueOf(alfoods.size()), Toast.LENGTH_SHORT).show();

                    drtodaycal.child("Total Calories").setValue(String.format("%.2f", totalcalories));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        drdinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                totalcalories = 0.0;

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Food f = data.getValue(Food.class);
                        totaldinner = totaldinner + Double.parseDouble(f.getCalories());
                        tvdinnertotal.setText(String.valueOf(totaldinner));
                        alfoods.add(f);


                    }

                    for (Food food : alfoods) {

                        totalcalories = totalcalories + Double.parseDouble(food.getCalories());

                        //Toast.makeText(getApplicationContext(),String.valueOf(alfoods.size()),Toast.LENGTH_SHORT).show();
                        tvtotalcalories.setText("Total = " + String.format("%.2f", totalcalories));
                    }
                   // Toast.makeText(getApplicationContext(), String.valueOf(alfoods.size()), Toast.LENGTH_SHORT).show();

                    drtodaycal.child("Total Calories").setValue(String.format("%.2f", totalcalories));

                } else {
                    totalcalories = 0.0;

                    for (Food food : alfoods) {

                        totalcalories = totalcalories + Double.parseDouble(food.getCalories());

                        //Toast.makeText(getApplicationContext(),String.valueOf(alfoods.size()),Toast.LENGTH_SHORT).show();
                        tvtotalcalories.setText("Total = " + String.format("%.2f", totalcalories));
                    }
                   // Toast.makeText(getApplicationContext(), String.valueOf(alfoods.size()), Toast.LENGTH_SHORT).show();

                    drtodaycal.child("Total Calories").setValue(String.format("%.2f", totalcalories));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void datesorcalories() {

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        final String todayAsString = dateFormat.format(today);
        final String tomorrowAsString = dateFormat.format(tomorrow);

        System.out.println("date:" + todayAsString);
        System.out.println("date " + tomorrowAsString);

        dateref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    Datecheck dc = dataSnapshot.getValue(Datecheck.class);
                    dbdate = dc.getDate();

                    if (dbdate.equals(todayAsString)) {

                        System.out.println("Do Nothing" + totalcalories);
                    } else {

                        drtodaycal.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {

                                    String totalcal = dataSnapshot.child("Total Calories").getValue(String.class);
                                    System.out.println("total" + totalcal);

                                    String id = hisref.push().getKey();
                                    HisCalo hisCalo = new HisCalo(id, dbdate, totalcal);
                                    hisref.child(id).setValue(hisCalo);
                                    Datecheck datecheck = new Datecheck(todayAsString); // need to change
                                    dateref.setValue(datecheck);


                                    drbf.removeValue();
                                    drlun.removeValue();
                                    dreve.removeValue();
                                    drdinner.removeValue();


                                } else {
                                    System.out.println("noData ");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
