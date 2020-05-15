package com.cegepba.localization_app.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cegepba.localization_app.MainActivity;
import com.cegepba.localization_app.Model.Room;
import com.cegepba.localization_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminManager extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText edit_name;
    private EditText edit_description;
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
        int xTR = 0;
        int xTL = 0;
        int xBR = 0;
        int xBL = 0;
        int yTR = 0;
        int yTL = 0;
        int yBR = 0;
        int yBL = 0;
        int floor = 1;
        String name = edit_name.getText().toString();
        String description = edit_description.getText().toString();
        if(!edit_floor.getText().toString().equals("")){
            floor = Integer.parseInt(edit_floor.getText().toString());
        }
        if(!edit_xTR.getText().toString().equals("")) {
            xTR = Integer.parseInt(edit_xTR.getText().toString())*-1;
        }
        if(!edit_xTL.getText().toString().equals("")) {
            xTL = Integer.parseInt(edit_xTL.getText().toString())*-1;
        }
        if(!edit_xBR.getText().toString().equals("")) {
            xBR = Integer.parseInt(edit_xBR.getText().toString())*-1;
        }
        if(!edit_xBL.getText().toString().equals("")) {
            xBL = Integer.parseInt(edit_xBL.getText().toString())*-1;
        }
        if(!edit_yTR.getText().toString().equals("")) {
            yTR = Integer.parseInt(edit_yTR.getText().toString())*-1;
        }
        if(!edit_yTL.getText().toString().equals("")) {
            yTL = Integer.parseInt(edit_yTL.getText().toString())*-1;
        }
        if(!edit_yBR.getText().toString().equals("")) {
            yBR = Integer.parseInt(edit_yBR.getText().toString())*-1;
        }
        if(!edit_yBL.getText().toString().equals("")) {
            yBL = Integer.parseInt(edit_yBL.getText().toString())*-1;
        }

        final Room room = new Room(null, name, floor, description,xBL,xBR,xTL,xTR,yTL,yTR,yBL,yBR);
        db.collection("rooms").whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if(task.getResult() != null){
                              if(task.getResult().isEmpty()) {
                                  AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(AdminManager.this, R.style.myDialog));

                                  builder.setTitle("Attention");
                                  builder.setMessage("Voulez-vous vraiment ajouter ce local ?");

                                  builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                                      public void onClick(DialogInterface dialog, int which) {
                                          db.collection("rooms").add(room);
                                          Toast.makeText(getApplicationContext(), "Ajout de local fait", Toast.LENGTH_SHORT).show();
                                      }
                                  });

                                  builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {
                                          dialog.cancel();
                                      }
                                  });

                                  AlertDialog alert = builder.create();
                                  alert.show();
                              } else {
                                  Toast.makeText(getApplicationContext(), "Local déjà existant", Toast.LENGTH_SHORT).show();
                              }
                          }
                      }
                });
    }
}
