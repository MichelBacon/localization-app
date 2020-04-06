package com.cegepba.localization_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Legend extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private TextView textViewColor;
    private TextView textViewName;
    private ArrayList<Legends> legends;

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
        firebaseFirestore.collection("Legends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = firebaseFirestore.collection("Legends").document(document.getId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Legends legend = documentSnapshot.toObject(Legends.class);

                                        if(legend != null)
                                        {
                                            legends = new ArrayList<>();
                                            legends.add(legend);
                                            textViewColor.setText(legends.get(0).getColor());
                                            textViewName.setText(legends.get(0).getName());
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }
}
