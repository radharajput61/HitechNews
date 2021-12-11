package com.app.hitechnews.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.app.hitechnews.Activity.Auth.LoginActivity;
import com.app.hitechnews.R;

public class UserTypeSelect extends AppCompatActivity {
LinearLayout lluser,llreporter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_select);
        lluser=findViewById(R.id.lluser);
        llreporter=findViewById(R.id.llreporter);

        lluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserTypeSelect.this, LoginActivity.class).putExtra("type","User"));
           finish();
            }
        });
        llreporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserTypeSelect.this, LoginActivity.class).putExtra("type","Reporter"));
                finish();
            }
        });

    }
}