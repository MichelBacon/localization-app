package com.cegepba.localization_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Legend extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView legends;
    private LegendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_legend);

            db = FirebaseFirestore.getInstance();
            legends = findViewById(R.id.recycler_view);

            setUpRecyclerView();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void setUpRecyclerView() {
        Query query = db.collection("Legends");
        FirestoreRecyclerOptions<Legends> options = new FirestoreRecyclerOptions.Builder<Legends>()
                .setQuery(query, Legends.class)
                .build();

        adapter = new LegendsAdapter(options);

        legends.setHasFixedSize(true);
        legends.setLayoutManager(new LinearLayoutManager(this));
        legends.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
