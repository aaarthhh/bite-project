package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.arth.calorytracker.adapters.HistoryAdapter;
import com.arth.calorytracker.adapters.WeightAdapter;
import com.arth.calorytracker.models.HisCalo;
import com.arth.calorytracker.models.Weightmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

public class ViewHistory extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView rvweight;

    ArrayList<HisCalo> wm = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        String title = "Calories Tracking";
        setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        rvweight = findViewById(R.id.rvhistory);
        rvweight.setHasFixedSize(true);
        rvweight.setLayoutManager(new LinearLayoutManager(ViewHistory.this));



        databaseReference = FirebaseDatabase.getInstance().getReference("calhistory")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    for (DataSnapshot data: dataSnapshot.getChildren())
                    {
                        HisCalo w = data.getValue(HisCalo.class);
                        wm.add(w);


                    }

                    Collections.reverse(wm);
                    HistoryAdapter weightAdapter = new HistoryAdapter(wm,ViewHistory.this);
                    rvweight.setAdapter(weightAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
