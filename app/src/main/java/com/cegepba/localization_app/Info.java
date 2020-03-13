package com.cegepba.localization_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cegepba.localization_app.ui.login.AdminLogin;

public class Info extends AppCompatActivity {

    private TextView textInfo;
    private int clickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textInfo = findViewById(R.id.text_info);

        textInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount=clickCount+1;
                if(clickCount==3)
                {
                    Intent myIntent = new Intent(Info.this, AdminLogin.class);
                    startActivity(myIntent);
                }
            }
        });
    }
}
