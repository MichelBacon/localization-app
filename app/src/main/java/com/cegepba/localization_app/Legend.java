package com.cegepba.localization_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Legend extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private TextView textViewColor;
    private TextView textViewName;
    private List<Legends> legends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_legend);

            firebaseFirestore = FirebaseFirestore.getInstance();
            textViewColor = findViewById(R.id.textView_color);
            textViewName = findViewById(R.id.textView_name);

            setTextView();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void setTextView() {
        /*firebaseFirestore.collection("Legends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 legends.add(document.toObject(Legends.class));
                            }
                        }
                    }
                });*/
    }
}
