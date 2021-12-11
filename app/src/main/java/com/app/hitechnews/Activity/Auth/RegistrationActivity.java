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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName,etMobile,etPassword;
    LinearLayout llSubmit;
    TextView tvSubmit,tvLogin;
    SpinKitView skLoader;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etName=findViewById(R.id.etName);
        etMobile=findViewById(R.id.etMobile);
        etPassword=findViewById(R.id.etPassword);
        llSubmit=findViewById(R.id.llSubmit);
        tvLogin=findViewById(R.id.tvLogin);
        tvSubmit=findViewById(R.id.tvSubmit);
        skLoader=findViewById(R.id.skLoader);
Intent i=getIntent();
type=i.getStringExtra("type");
Log.e("reg_login",type);
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().isEmpty())
                {
                    etName.setError("Please Enter Name");
                }else if(etMobile.getText().toString().isEmpty())
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
                    getRegistrationData(etName.getText().toString().trim(),etMobile.getText().toString().trim(),etPassword.getText().toString().trim());
                }
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void getRegistrationData(final String Name,final String MobileNo, final String Password)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("Name", Name);
            obj.put("MobileNo", MobileNo);
            obj.put("Password", Password);
            obj.put("Type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("getRegistrationData",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(RegistrationActivity.this);

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.UserRegistration, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");
                    String Msg=response.getString("Message");
                    JSONArray array=response.getJSONArray("Response");
                    if(status)
                    {
                        skLoader.setVisibility(View.GONE);
                        tvSubmit.setVisibility(View.VISIBLE);
                        llSubmit.setEnabled(false);
                        for (int i=0;i<array.length();i++)
                        {
                            Log.v("shddfty",array.toString());
                            JSONObject object=array.getJSONObject(i);
                            String MobileNo=object.getString("MobileNo");
                            String Name=object.getString("Name");
                            String UserId=object.getString("UserId");

                            MyPreferences myPreferences=new MyPreferences(getApplicationContext());
                            myPreferences.setMobile(MobileNo);
                            myPreferences.setName(Name);
                            myPreferences.setUserId(UserId);
                            myPreferences.setUsertype(type);
                        }
                        if (type.equals("Reporter"))
                        {
                            Toast.makeText(RegistrationActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(RegistrationActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegistrationActivity.this, UDashbord.class));
                            finish();
                        }
//                        Toast.makeText(RegistrationActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        finish();
                    }else{
                        Toast.makeText(RegistrationActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
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