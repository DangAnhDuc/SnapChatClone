package com.example.admin.snapchatclone.LoginRegister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.admin.snapchatclone.R;

public class ChooseLoginRegistrationActivity extends AppCompatActivity {

    Button btnLogin,btnRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registration);

        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnRegistration=(Button) findViewById(R.id.btnRegistration);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ChooseLoginRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseLoginRegistrationActivity.this, RegistrationActivity.class);
                startActivity(intent);
                return;
            }
        });
    }
}
