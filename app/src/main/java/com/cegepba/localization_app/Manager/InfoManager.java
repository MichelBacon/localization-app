package com.cegepba.localization_app.Manager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cegepba.localization_app.R;

import java.util.Objects;

public class InfoManager extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button btnAdmin = findViewById(R.id.button);
        btnAdmin.setOnClickListener(InfoManager.this);
    }

    @Override
    public void onClick(View v) {
    }
}