package com.arth.calorytracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arth.calorytracker.models.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminHome extends AppCompatActivity {
    EditText etname, etpro, etcar, etfat, etcalo, etqty;
    Button btnlogout, btnadd;
    ImageView imageView;
    TextView tvviewfoods, tvaddmeal;
    DatabaseReference databaseReference;
    Uri uri;
    ProgressDialog progressDialog;
    StorageReference firebaseStorage;
    private static final int CODE = 2;
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        String title = "Add Macros";
        setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        btnadd = findViewById(R.id.btnadd);
        //btnlogout = findViewById(R.id.btnlogout);
        etname = findViewById(R.id.etfood);
        etfat = findViewById(R.id.etfat);
        etcar = findViewById(R.id.etcarbs);
        etpro = findViewById(R.id.etpro);
        etcalo = findViewById(R.id.ettotalcal);
        etqty = findViewById(R.id.etqty);
        tvviewfoods = findViewById(R.id.tvviewfoods);
        tvaddmeal = findViewById(R.id.tvaddmeal);


        firebaseStorage = FirebaseStorage.getInstance().getReference("foods");
        databaseReference = FirebaseDatabase.getInstance().getReference("foods").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        imageView = findViewById(R.id.im1);
        utility = new Utility(AdminHome.this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, CODE);
            }
        });


        progressDialog = new ProgressDialog(AdminHome.this);
        progressDialog.setTitle("Food");
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Validation v = new Validation();
                boolean check = true;
                if (!v.nullValidator(etname)) {
                    etname.setError("Enter Food");
                    check = false;
                }


                if (!v.nullValidator(etcalo)) {
                    etcalo.setError("Enter Calories");
                    check = false;
                }

                if (!v.nullValidator(etcar)) {
                    etcar.setError("Enter Carbs");
                    check = false;
                }


                if (!v.nullValidator(etpro)) {
                    etpro.setError("Enter Protein");
                    check = false;
                }

                if (!v.nullValidator(etfat)) {
                    etfat.setError("Enter Fat");
                    check = false;
                }
                if (!v.nullValidator(etqty)) {
                    etfat.setError("Enter Qty");
                    check = false;
                }
                if (check) {
                    if (utility.checkInternetConnectionALL()) {

                       // if (uri != null) {
                            progressDialog.show();

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                boolean b = true;

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            Food s = d.getValue(Food.class);
                                            if ((s.getName().equals(etname.getText().toString()))
                                                    && (s.getEtqty().equals(etqty.getText().toString()))) {
                                                b = false;
                                            }
                                        }
                                        if (b) {


                                            String id = databaseReference.push().getKey();
                                            final Food catagorymodel = new Food(id, etname.getText().toString()
                                                    , etpro.getText().toString()
                                                    , etfat.getText().toString()
                                                    , etcar.getText().toString()
                                                    , etcalo.getText().toString()
                                                    , etqty.getText().toString()
                                                    , "https://firebasestorage.googleapis.com/v0/b/abwb-d2c6d.appspot.com/o/foods%2F277605?alt=media&token=3aa7ace0-4515-4a7f-8158-23f326aabf4b");


                                            databaseReference.child(id).setValue(catagorymodel);
                                            Toast.makeText(AdminHome.this, "Food Added", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();


                                        } else {
                                            Toast.makeText(AdminHome.this, "Food Already Added", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    } else {
                                        String id = databaseReference.push().getKey();
                                        final Food catagorymodel = new Food(id, etname.getText().toString()
                                                , etpro.getText().toString()
                                                , etfat.getText().toString()
                                                , etcar.getText().toString()
                                                , etcalo.getText().toString()
                                                , etqty.getText().toString()
                                                , "https://firebasestorage.googleapis.com/v0/b/abwb-d2c6d.appspot.com/o/foods%2F277605?alt=media&token=3aa7ace0-4515-4a7f-8158-23f326aabf4b");
                                        databaseReference.child(id).setValue(catagorymodel);
                                        Toast.makeText(AdminHome.this, "Food Added", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        /*}else {
                            progressDialog.dismiss();
                            Toast.makeText(AdminHome.this, "Select Image", Toast.LENGTH_LONG).show();

                        }*/
                    }
                }


            }


        });

        findViewById(R.id.llhome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DietActivity.class));
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

        tvaddmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateMeal.class));

            }
        });

        findViewById(R.id.lldiet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(getApplicationContext(), MealActivity.class));
            }
        });
        tvviewfoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MealActivity.class));

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            ProgressDialog progressDialog = new ProgressDialog(AdminHome.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
                progressDialog.dismiss();
            }

        }
    }


}
