package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arth.calorytracker.adapters.WeightAdapter;
import com.arth.calorytracker.models.Food;
import com.arth.calorytracker.models.Weightmodel;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeightActivity extends AppCompatActivity {

    GraphView graph;
    TextView tvdate;
    EditText etweight;
    Button btnadd;
    DatabaseReference databaseReference;
    RecyclerView rvweight;

    LineGraphSeries series;
    ArrayList<Weightmodel> wm = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        graph = findViewById(R.id.graph);
        tvdate = findViewById(R.id.tvdate);
        etweight = findViewById(R.id.etweight);
        btnadd = findViewById(R.id.btnaddweight);
        String title = "Weight Tracking";
        setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        rvweight = findViewById(R.id.rvweights);
        rvweight.setHasFixedSize(true);
        rvweight.setLayoutManager(new LinearLayoutManager(WeightActivity.this));




        databaseReference = FirebaseDatabase.getInstance().getReference("weight")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(WeightActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {

                                month = month + 1;
                                String date = day + "/" + month + "/" + year;
                                tvdate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (etweight.getText().toString().matches("")) {
                    etweight.setError("Enter Weight");
                } else {

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                boolean isthere = true;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Weightmodel t = ds.getValue(Weightmodel.class);

                                    if (t.getDate().equals(tvdate.getText().toString().trim())) {
                                        isthere = false;
                                        break;
                                    }

                                }
                                if (isthere) {
                                    String id = databaseReference.push().getKey();

                                    Weightmodel ff = new Weightmodel(id, tvdate.getText().toString().trim(), etweight.getText().toString().trim());
                                    databaseReference.child(id).setValue(ff);
                                    Snackbar snackbar = Snackbar.make(view, "successfully Added", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(view, "Weight Entry is Exists on Selected Date", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                }

                            } else {
                                String id = databaseReference.push().getKey();

                                Weightmodel ff = new Weightmodel(id, tvdate.getText().toString().trim(), etweight.getText().toString().trim());
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

        setGraph();
        getWeights();
      //  graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM");

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {


                if (isValueX) {
                        return format.format(new Date((long)value));

                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        series = new LineGraphSeries();
       graph.addSeries(series);


    }

    private void getWeights() {

        DatabaseReference reweight = FirebaseDatabase.getInstance().getReference("weight").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        reweight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wm.clear();
                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot data: dataSnapshot.getChildren())
                    {
                        Weightmodel w = data.getValue(Weightmodel.class);
                        wm.add(w);


                    }

                    Collections.reverse(wm);
                    WeightAdapter weightAdapter = new WeightAdapter(wm,WeightActivity.this);
                    rvweight.setAdapter(weightAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setGraph() {

       // final LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        //final ArrayList<Weightmodel> weightmodels = new ArrayList<>();

      //  weightmodels.clear();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    int index = 0;
                    DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                    Date date = null;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Weightmodel w = data.getValue(Weightmodel.class);
                        String dtStart = w.getDate();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            date = format.parse(dtStart);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dp[index] = new DataPoint(date, Double.valueOf(w.getWeight()));
                        index++;
                    }
                    series.resetData(dp);
                }
                else
                {
                    Toast.makeText(getApplication(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}



/*

      for (DataSnapshot data : dataSnapshot.getChildren()) {

              Weightmodel w = data.getValue(Weightmodel.class);
        weightmodels.add(w);


        }

        for (Weightmodel w : weightmodels) {

        String dtStart = w.getDate();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
        Date date = format.parse(dtStart);
        series.appendData(new DataPoint(date,
        Double.valueOf(w.getWeight())), true, 5);
        System.out.println("Date = "+w.getDate());

        } catch (ParseException e) {
        e.printStackTrace();
        }
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM");

        graph.getGridLabelRenderer().
        setLabelFormatter(new DateAsXAxisLabelFormatter(WeightActivity.this,format));
        graph.getGridLabelRenderer().setNumHorizontalLabels(6); // only 4 because of the space

        graph.addSeries(series);*/
