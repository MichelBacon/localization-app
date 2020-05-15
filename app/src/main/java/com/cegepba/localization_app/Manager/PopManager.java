package com.cegepba.localization_app.Manager;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.cegepba.localization_app.Model.Room;
import com.cegepba.localization_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PopManager extends Activity {

    private TextView roomName;
    private TextView descriptionText;
    private Room room;

    public PopManager() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        roomName = findViewById(R.id.roomId);
        descriptionText = findViewById(R.id.descriptionText);
        ImageView closeButton = findViewById(R.id.x_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        room = (Room)getIntent().getSerializableExtra("room");
        room.setNodeRef(FirebaseFirestore.getInstance().document((String)getIntent().getSerializableExtra("path")));

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
        roomName.setText(room.getName());
        descriptionText.setText(room.getDescription());
    }
}