package com.app.hitechnews.Activity.Post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.hitechnews.R;

public class SelectLocationActivity extends AppCompatActivity {

    EditText etCity,etFullAddress;
    TextView submit;
    public static final String EXTRA_CITY = "com.example.android.wordlistsql.REPLY";
    public static final String EXTRA_ADDRESS = "com.example.android.wordlistsql.COLUMN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        etCity=findViewById(R.id.etCity);
        etFullAddress=findViewById(R.id.etFullAddress);
        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCity.getText().toString().isEmpty())
                {
                    etCity.setError("Please Enter City Name");
                }
                else if(etFullAddress.getText().toString().isEmpty())
                {
                    etFullAddress.setError("Please Enter Full Addess");
                }
                else
                {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_CITY, etCity.getText().toString().trim());
                    replyIntent.putExtra(EXTRA_ADDRESS, etFullAddress.getText().toString().trim());
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });
    }
}