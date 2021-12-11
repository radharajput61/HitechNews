package com.app.hitechnews.Activity.SplashAndIntro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.app.hitechnews.Activity.Auth.LoginActivity;
import com.app.hitechnews.Activity.UserTypeSelect;
import com.app.hitechnews.MainActivity;
import com.app.hitechnews.R;
import com.app.hitechnews.UserDashbord.DynamicTab.DynamicActivity;
import com.app.hitechnews.UserDashbord.UDashbord;
import com.app.hitechnews.Util.MyPreferences;

public class SplashActivity extends AppCompatActivity {

    MyPreferences myPreferences;
    String UserId,UserType;
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        runthread();
    }

    private void runthread() {
        myPreferences=new MyPreferences(this);
        UserId=myPreferences.getUserId();
        UserType=myPreferences.getUsertype();
     //  Log.e("type",UserType);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
//                startActivity(new Intent(getApplicationContext(), DynamicActivity.class));
//                finish();
                if(UserId!=null)
                {

               if (UserType!=null)
               {
                   if (UserType.equals("Reporter"))
                   {
                       startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       finish();
                   }else {
                       startActivity(new Intent(getApplicationContext(), UDashbord.class));
                       finish();
                   }

               }

                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), UserTypeSelect.class));
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}