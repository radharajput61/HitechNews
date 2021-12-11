package com.app.hitechnews.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hitechnews.MainActivity;
import com.app.hitechnews.R;
import com.app.hitechnews.UserDashbord.UDashbord;
import com.app.hitechnews.Util.API;
import com.app.hitechnews.Util.MyPreferences;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText etMobile,etPassword;
    LinearLayout llSubmit;
    TextView tvSubmit,tvRegistration;
    SpinKitView skLoader;
   String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etMobile=findViewById(R.id.etMobile);
        etPassword=findViewById(R.id.etPassword);
        llSubmit=findViewById(R.id.llSubmit);
        tvRegistration=findViewById(R.id.tvRegistration);
        tvSubmit=findViewById(R.id.tvSubmit);
        skLoader=findViewById(R.id.skLoader);
         Intent i=getIntent();
         type=i.getStringExtra("type");
         Log.e("usertype",type);
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMobile.getText().toString().isEmpty())
                {
                    etMobile.setError("Please Enter Mobile No.");
                }
                else if(etPassword.getText().toString().isEmpty())
                {
                    etPassword.setError("Please Enter Password");
                }
                else
                {
                    skLoader.setVisibility(View.VISIBLE);
                    tvSubmit.setVisibility(View.GONE);
                    llSubmit.setEnabled(false);
                    getLoginData(etMobile.getText().toString().trim(),etPassword.getText().toString().trim());
                }
            }
        });
        tvRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class).putExtra("type",type));
                finish();
            }
        });
    }

    private void getLoginData(final String MobileNo, final String password)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("MobileNo", MobileNo);
            obj.put("Password", password);
            obj.put("Type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("getLoginData",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.UserLogin, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");
                    String Msg=response.getString("Message");
                    if(status)
                    {
                        skLoader.setVisibility(View.GONE);
                        tvSubmit.setVisibility(View.VISIBLE);
                        llSubmit.setEnabled(false);
                        String MobileNo=response.getString("MobileNo");
                        String Name=response.getString("Name");
                        String UserId=response.getString("UserId");
                        MyPreferences myPreferences=new MyPreferences(getApplicationContext());
                        myPreferences.setMobile(MobileNo);
                        myPreferences.setName(Name);
                        myPreferences.setUserId(UserId);
                        myPreferences.setUsertype(type);
                       Log.e("log",type);
                       if (type.equals("Reporter"))
                       {
                           Toast.makeText(LoginActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));
                           finish();
                       }else {
                           Toast.makeText(LoginActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                           startActivity(new Intent(getApplicationContext(), UDashbord.class));
                           finish();
                       }

                    }else{

                        Toast.makeText(LoginActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                        skLoader.setVisibility(View.GONE);
                        tvSubmit.setVisibility(View.VISIBLE);
                        llSubmit.setEnabled(true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());
                skLoader.setVisibility(View.GONE);
                tvSubmit.setVisibility(View.VISIBLE);
                llSubmit.setEnabled(true);
            }
        });
        requestQueue.add(request);
    }
}