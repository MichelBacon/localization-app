package com.cegepba.localization_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Legend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}