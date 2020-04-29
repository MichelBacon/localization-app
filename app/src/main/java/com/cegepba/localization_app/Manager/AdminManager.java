package com.cegepba.localization_app.Manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cegepba.localization_app.Model.Room;
import com.cegepba.localization_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminManager extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText edit_name;
    private EditText edit_description;
    private EditText edit_beaconId;
    private EditText edit_floor;
    private EditText edit_xTR;
    private EditText edit_xTL;
    private EditText edit_xBR;
    private EditText edit_xBL;
    private EditText edit_yTR;
    private EditText edit_yTL;
    private EditText edit_yBR;
    private EditText edit_yBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager);
        db = FirebaseFirestore.getInstance();

        edit_name = findViewById(R.id.editText_name);
        edit_description = findViewById(R.id.editText_description);
        edit_beaconId = findViewById(R.id.editText14);
        edit_floor = findViewById(R.id.editText_floor);
        edit_xTR = findViewById(R.id.xtopRight);
        edit_xTL = findViewById(R.id.xtopLeft);
        edit_xBR = findViewById(R.id.xbotRight);
        edit_xBL = findViewById(R.id.xbotLeft);
        edit_yTR = findViewById(R.id.ytopRight);
        edit_yTL = findViewById(R.id.ytopLeft);
        edit_yBR = findViewById(R.id.ybotRight);
        edit_yBL = findViewById(R.id.ybotLeft);
        Button btn_add_room = findViewById(R.id.btn_add_class);

        btn_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoom();
            }
        });

    }

    private void addRoom() {
        String name = edit_name.getText().toString();
        String description = edit_description.getText().toString();
        String beaconId = edit_beaconId.getText().toString();
        int floor = Integer.parseInt(edit_floor.getText().toString());
        int xTR = Integer.parseInt(edit_xTR.getText().toString());
        int xTL = Integer.parseInt(edit_xTL.getText().toString());
        int xBR = Integer.parseInt(edit_xBR.getText().toString());
        int xBL = Integer.parseInt(edit_xBL.getText().toString());
        int yTR = Integer.parseInt(edit_yTR.getText().toString());
        int yTL = Integer.parseInt(edit_yTL.getText().toString());
        int yBR = Integer.parseInt(edit_yBR.getText().toString());
        int yBL = Integer.parseInt(edit_yBL.getText().toString());

        Room room = new Room(null, name, floor, description,beaconId,xBL,xBR,xTL,xTR,yTL,yTR,yBL,yBR);
        db.collection("Rooms").add(room);

        Toast.makeText(this, "Ajout de local fait", Toast.LENGTH_SHORT).show();
    }
}
