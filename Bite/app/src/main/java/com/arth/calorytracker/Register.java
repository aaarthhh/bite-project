package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arth.calorytracker.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText etname,etmob,etemail,etpass,etage,etconpass;
    Button btnreg;
    Utility utility;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname = findViewById(R.id.etName);
        etmob = findViewById(R.id.etMobileno);
        etemail = findViewById(R.id.etEmail);
        etpass = findViewById(R.id.etPassword);
        etage = findViewById(R.id.etAge);
        etconpass = findViewById(R.id.etcPassword);

        utility = new Utility(Register.this);
        btnreg = findViewById(R.id.btnRegister);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                boolean b = true;

                if (!Validation.nullValidator(etname))
                {
                    b = false;
                    etname.setError("Enter Name");
                }

                if (!Validation.email_Validation(etemail))
                {
                    b = false;
                    etemail.setError("Enter Email");
                }

                if (!Validation.phoneValidator(etmob))
                {
                    b = false;
                    etmob.setError("Enter Mobile Number");
                }
                if (!Validation.passwordValidator(etpass))
                {
                    b = false;
                    etpass.setError("Enter Password");
                }

                if (!Validation.numberValidator(etage))
                {
                    b = false;
                    etage.setError("Enter Age");
                }

                if (!Validation.passwordValidator(etconpass))
                {
                    b = false;
                    etconpass.setError("Enter Password");
                }

                if (!etpass.getText().toString().equals(etconpass.getText().toString()))
                {
                    b = false;
                    etconpass.setError("Password Not Match");
                }

                if (utility.checkInternetConnectionALL())
                {
                    if (b)
                    {


                        firebaseAuth.createUserWithEmailAndPassword(etemail.getText().toString().trim(),
                                etpass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful())
                                {

                                    String id = firebaseAuth.getCurrentUser().getUid();
                                    Users users = new
                                            Users(id,etname.getText().toString().trim(),etemail.getText().toString().trim(),
                                            etmob.getText().toString().trim(),etage.getText().toString().trim(),"User");
                                    databaseReference.child(id).setValue(users);
                                    utility.toast("Registred Successfully");
                                    firebaseAuth.signOut();
                                    startActivity(new Intent(getApplicationContext(),Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }

                                else
                                {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Snackbar snackbar = Snackbar
                                                .make(view, "Email Already Exist", Snackbar.LENGTH_LONG);

                                        snackbar.show();
                                    }
                                    else
                                    {
                                        Snackbar snackbar = Snackbar
                                                .make(view, task.getException().getMessage(), Snackbar.LENGTH_LONG);

                                        snackbar.show();
                                    }
                                }

                            }
                        });

                    }
                }
            }
        });



        ( (TextView)findViewById(R.id.tvLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }


}
