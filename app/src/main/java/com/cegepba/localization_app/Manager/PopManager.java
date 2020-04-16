package com.cegepba.localization_app.Manager;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.cegepba.localization_app.Model.Rooms;
import com.cegepba.localization_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopManager extends Activity {

    private FirebaseFirestore firebaseFirestore;

    private TextView roomName;
    private TextView descriptionText;
    private ImageView closeButton;
    private Rooms room;

    public PopManager() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        firebaseFirestore = FirebaseFirestore.getInstance();

        roomName = findViewById(R.id.roomId);
        descriptionText = findViewById(R.id.descriptionText);
        closeButton = findViewById(R.id.x_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        room = (Rooms)getIntent().getSerializableExtra("room");

        setPopWindowSize();
        setTextView();
    }

    private void setPopWindowSize()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.4));
    }

    private void setTextView() {
        //TODO add loading bar (spinner)

        roomName.setText(room.getName());
        descriptionText.setText(room.getDescription());
    }
}