package com.app.hitechnews.Activity.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class UpdateProfileActivity extends AppCompatActivity {

    String[] genderList = {"--Select Gender--", "Male", "Female"};
    EditText etName,etEmail,etAbout;
    Spinner etGender;
    TextView tvSubmit;
    SpinKitView skLoader;
    String gender;
    MyPreferences myPreferences;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        myPreferences=new MyPreferences(getApplicationContext());
        UserId=myPreferences.getUserId();
        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etAbout=findViewById(R.id.etAbout);
        etGender=findViewById(R.id.etGender);
        tvSubmit=findViewById(R.id.tvSubmit);
        skLoader=findViewById(R.id.skLoader);

        ArrayAdapter afe = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, genderList);
        afe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etGender.setAdapter(afe);
        etGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = etGender.getSelectedItem().toString();
                Log.v("gender", "" + gender);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getUserProfileData(UserId);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().isEmpty())
                {
                    etName.setError("Please Enter Name.");
                }

                else
                {
                    skLoader.setVisibility(View.VISIBLE);
                    tvSubmit.setVisibility(View.GONE);
                    tvSubmit.setEnabled(false);
                    getUpdateProfileData(etAbout.getText().toString().trim(),etEmail.getText().toString().trim(),
                            gender,etName.getText().toString().trim(),"",UserId);
                }
            }
        });
    }

    private void getUserProfileData(final String UserId)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("UserId", UserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("ksjdf",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.UserProfile, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    boolean status=response.getBoolean("Status");

                    String Msg=response.getString("Message");
                    if(status)
                    {

                        String AboutUs=response.getString("AboutUs");
                        String Email=response.getString("Email");//
                        String Gender=response.getString("Gender");//
                        String MobileNo=response.getString("MobileNo");//
                        String Name=response.getString("Name");//
                        String ProfilePic=response.getString("ProfilePic");
                        String TotalFollower=response.getString("TotalFollower");
                        String TotalFollowing=response.getString("TotalFollowing");
                        String TotalGroup=response.getString("TotalGroup");
                        if(!Name.equalsIgnoreCase(""))
                            etName.setText(Name);

                        if(!Email.equalsIgnoreCase(""))
                            etEmail.setText(Email);


                        if(!AboutUs.equalsIgnoreCase(""))
                            etAbout.setText(AboutUs);

                        gender=Gender;
                        if(Gender.equals("Male"))
                        etGender.setSelection(1);
                        else if(Gender.equals("Female"))
                        etGender.setSelection(2);
                        else if(Gender.equals(""))
                            etGender.setSelection(0);

                    }else{

                        Toast.makeText(getApplicationContext(), ""+Msg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("jgdkjhkj",error.toString());

            }
        });
        requestQueue.add(request);
    }
    private void getUpdateProfileData(final String AboutUs, final String Email,final String Gender,final String Name,String ProfilePic,String UserId)
    {
        final JSONObject obj = new JSONObject();
        try
        {
            obj.put("AboutUs", AboutUs);
            obj.put("Email", Email);
            obj.put("Gender", Gender);
            obj.put("Name", Name);
            obj.put("ProfilePic", ProfilePic);
            obj.put("UserId", UserId);
            obj.put("TotalFollower", "");
            obj.put("TotalFollowing", "");
            obj.put("TotalGroup", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("ksjdf",obj.toString());
        final RequestQueue requestQueue= Volley.newRequestQueue(UpdateProfileActivity.this);

        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, API.UpdateUserProfile, obj, new Response.Listener<JSONObject>() {
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
                        tvSubmit.setEnabled(false);

                        Toast.makeText(UpdateProfileActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                        if (myPreferences.getUsertype().equals("Reporter"))
                        {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else {
                            startActivity(new Intent(getApplicationContext(), UDashbord.class));
                            finish();
                        }


                    }else{

                        Toast.makeText(UpdateProfileActivity.this, ""+Msg, Toast.LENGTH_LONG).show();
                        skLoader.setVisibility(View.GONE);
                        tvSubmit.setVisibility(View.VISIBLE);
                        tvSubmit.setEnabled(true);
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
                tvSubmit.setEnabled(true);
            }
        });
        requestQueue.add(request);
    }
}