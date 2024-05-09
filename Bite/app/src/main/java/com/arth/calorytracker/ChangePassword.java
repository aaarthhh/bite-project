package com.arth.calorytracker;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {
    EditText etOldPass, etNewPass, etConPass;

    Utility utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etConPass = findViewById(R.id.etConPass);

        utils = new Utility(ChangePassword.this);
        findViewById(R.id.btnChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (etOldPass.getText().toString() == null || etOldPass.getText().toString().length() == 0)
                    utils.toast("Please Enter Old password");

                else if (!Validation.passwordValidator(etNewPass))
                    utils.toast("Please Enter New password");
                else if (etConPass.getText().toString() == null || etConPass.getText().toString().length() == 0)
                    utils.toast("Please Enter Confirm password");
                else if (!etConPass.getText().toString().equalsIgnoreCase(etNewPass.getText().toString()))
                    utils.toast("Confirm password not matches New password");
                else {


                    final FirebaseUser user;
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    final String email = user.getEmail();
                    AuthCredential credential = EmailAuthProvider.getCredential(email,etOldPass.getText().toString().trim());

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(etNewPass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isSuccessful()){

                                            utils.toast("Something went wrong. Please try again later");

                                        }else {

                                            utils.toast("Password Successfully Modified");


                                        }
                                    }
                                });
                            }else {

                                utils.toast("Authentication Failed: Old Password may be wrong");

                            }
                        }
                    });
                }
            }


        });
    }
}
