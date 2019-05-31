package com.example.admin.snapchatclone.LoginRegister;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.snapchatclone.Start.MainActivity;
import com.example.admin.snapchatclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private Button btnRegistration;
    private EditText mEmail,mPassword,mName;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        database=FirebaseDatabase.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(RegistrationActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                    return;
                }
            }
        };

        mAuth=FirebaseAuth.getInstance();

        btnRegistration=(Button) findViewById(R.id.registration);
        mEmail=(EditText) findViewById(R.id.rgemail);
        mPassword=(EditText) findViewById(R.id.rgpassword);
        mName=(EditText) findViewById(R.id.rgname);


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=mName.getText().toString();
                final String email= mEmail.getText().toString();
                final String password=mPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplication(), "Sign up ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String userId= mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb= database.getReference().child("users").child(userId);
                            Map userInfor=new HashMap<>();
                            userInfor.put("email",email);
                            userInfor.put("name",name);
                            userInfor.put("profileImageUrl","default");

                            currentUserDb.updateChildren(userInfor);


                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
