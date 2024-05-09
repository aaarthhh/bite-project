package com.arth.calorytracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arth.calorytracker.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {

    TextView tvuname,tvemail,tvmob;
    DatabaseReference databaseReference;

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        sharedpreferences = getSharedPreferences("Users", Context.MODE_PRIVATE);

        tvuname = findViewById(R.id.tvUsername);
        tvemail = findViewById(R.id.tvemail);
        tvmob = findViewById(R.id.tvmob);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().trim());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    ProgressDialog progressDialog = new ProgressDialog(Account.this);
                    progressDialog.setMessage("PLease wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    for (DataSnapshot data:dataSnapshot.getChildren())

                    {
                        Users users = data.getValue(Users.class);
                        tvuname.setText(users.getName());
                        tvemail.setText(users.getEmail());
                        tvmob.setText(users.getMob());
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        findViewById(R.id.llChangPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChangePassword.class));
            }
        });

        findViewById(R.id.llHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DietActivity.class));
            }
        });
        findViewById(R.id.llfoods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),WeightActivity.class));
            }
        });





        findViewById(R.id.llLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences.edit().remove("uname").commit();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DietActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        });


    }
}
