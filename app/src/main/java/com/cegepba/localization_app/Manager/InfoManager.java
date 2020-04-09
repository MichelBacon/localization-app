package com.cegepba.localization_app.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cegepba.localization_app.R;

public class InfoManager extends AppCompatActivity implements View.OnClickListener {

    Button btnAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAdmin = findViewById(R.id.button);
        btnAdmin.setOnClickListener(InfoManager.this);
    }


    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(InfoManager.this, PopManager.class);
        startActivity(myIntent);
    }
}