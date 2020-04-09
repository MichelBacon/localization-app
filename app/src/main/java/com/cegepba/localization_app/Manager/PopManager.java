package com.cegepba.localization_app.Manager;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.cegepba.localization_app.Model.Rooms;
import com.cegepba.localization_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PopManager extends Activity {

    private FirebaseFirestore firebaseFirestore;

    private TextView roomName;
    private TextView descriptionText;
    private ImageView closeButton;

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

        firebaseFirestore.collection("Rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = firebaseFirestore.collection("Rooms").document(document.getId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Rooms room = documentSnapshot.toObject(Rooms.class);

                                        roomName.setText(room.getName());
                                        descriptionText.setText(room.getDescription());
                                    }
                                });
                            }
                        }
                    }
                });
    }
}