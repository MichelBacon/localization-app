package com.cegepba.localization_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class Pop extends AppCompatActivity {

    private TextView roomName;
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        roomName = findViewById(R.id.roomId);
        descriptionText = findViewById(R.id.descriptionText);

        roomName.setText("B-534");
        descriptionText.setText("Local Informatique");
    }
}
