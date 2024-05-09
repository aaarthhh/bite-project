package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arth.calorytracker.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Login extends AppCompatActivity {
    EditText uname,pw,etemail;
    Button button;
    Utility utility;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Dialog d;
    SharedPreferences sharedpreferences;
    boolean allpermission = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        button = (Button) findViewById(R.id.btnLogin);
        uname = (EditText) findViewById(R.id.etEmail);
        pw = (EditText) findViewById(R.id.etPassword);

        utility = new Utility(Login.this);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences("Users", Context.MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        requestPSPermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (allpermission) {
                    boolean b = true;

                    Validation validation = new Validation();
                    if(!validation.email_Validation(uname))
                    {
                        uname.setError("Enter Valid Email");
                        b=false;
                    }

                    if(!validation.passwordValidator(pw))
                    {
                        pw.setError("Enter valid Password");
                        b=false;
                    }

                    if(b) {
                        final ProgressDialog progressDialog = new ProgressDialog(Login.this);

                        progressDialog.setTitle("Login");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);


                        if (uname.getText().toString().trim().equalsIgnoreCase("admin@admin.com")
                                && pw.getText().toString().equalsIgnoreCase("Admin@123"))
                        {
                            progressDialog.show();
                            startActivity(new Intent(Login.this,AdminHome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                            progressDialog.dismiss();

                        }



                        else
                        {
                            firebaseAuth.signInWithEmailAndPassword(uname.getText().toString(),pw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.show();


                                    if (task.isSuccessful())
                                    {

                                        progressDialog.show();
                                        final Query user = databaseReference.orderByChild("email").
                                                equalTo(uname.getText().toString().trim());
                                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChildren())
                                                {
                                                    for (DataSnapshot usersnapshot:dataSnapshot.getChildren())
                                                    {
                                                        Users u = usersnapshot.getValue(Users.class);

                                                        switch (u.getUsertype().toString().trim())

                                                        {
                                                            case "User":
                                                                startActivity(new Intent(Login.this, DietActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                finish();
                                                                progressDialog.dismiss();
                                                                break;



                                                          /*  case "Coun":
                                                                startActivity(new Intent(Login.this, CounsellorHome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                finish();
                                                                progressDialog.dismiss();
                                                                break;

*/



                                                            default:utility.toast("User Not Found! Try Again..");
                                                                progressDialog.dismiss();
                                                                break;

                                                        }
                                                    }


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        progressDialog.dismiss();

                                    }
                                    else {
                                        utility.toast(task.getException().getMessage());
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                            progressDialog.dismiss();
                        }


                    }
                } else {
                    requestPSPermission();
                }
            }
        });


        ((TextView) findViewById(R.id.tvRegister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


        final TextView tvForPass = findViewById(R.id.tvForpass);

        tvForPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvForPass.setAlpha(0);
                tvForPass.animate().alpha(1).setDuration(300);

                d = utility.showdialog(R.layout.popup_forpass);

                etemail = d.findViewById(R.id.etemail);



                Button btnOk = d.findViewById(R.id.btnOk);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean b =true;

                        if (!Validation.email_Validation(etemail))
                        {
                            etemail.setError("Please Enter Valid Email");
                            b = false;
                        }

                        if (utility.checkInternetConnectionALL())
                        {

                            if (b)
                            {
                                firebaseAuth.sendPasswordResetEmail(etemail.getText().toString().trim())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(),"Reset link Email sent...",Toast.LENGTH_LONG).show();
                                                    d.dismiss();

                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(),"Invlid Email",Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                            }
                        }

                    }
                });



                d.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.show();
            }
        });


    }
    private void requestPSPermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            allpermission = true;
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }


                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                finish();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    protected void onStart() {
        super.onStart();

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);

        if(firebaseAuth.getCurrentUser() != null) {

            progressDialog.show();
            final Query user = databaseReference.orderByChild("email").
                    equalTo(firebaseAuth.getCurrentUser().getEmail().toString());
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                            Users u = usersnapshot.getValue(Users.class);

                            switch (u.getUsertype().toString().trim()) {
                                case "User":
                                    startActivity(new Intent(Login.this, DietActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                    progressDialog.dismiss();
                                    break;


                             /*   case "Coun":
                                    startActivity(new Intent(Login.this, CounsellorHome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                    progressDialog.dismiss();
                                    break;*/


                                default:
                                    utility.toast("User Not Found! Try Again..");
                                    progressDialog.dismiss();
                                    break;

                            }
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            progressDialog.dismiss();
        }

       /* if(firebaseAuth.getCurrentUser() != null)
        {
            progressDialog.show();

            startActivity(new Intent(Login.this,StudentHome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            progressDialog.dismiss();
        }*/
    }


}
