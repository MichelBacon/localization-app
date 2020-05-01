package com.cegepba.localization_app.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.cegepba.localization_app.Model.Legend;
import com.cegepba.localization_app.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class LegendManager extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView firestoreList;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_legend);

            firebaseFirestore = FirebaseFirestore.getInstance();
            firestoreList = findViewById(R.id.recyclerView);

            setRecyclerView();

            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void setRecyclerView() {
        Query query = firebaseFirestore.collection("Legends").orderBy("name");

        FirestoreRecyclerOptions<Legend> options = new FirestoreRecyclerOptions.Builder<Legend>()
                .setQuery(query, Legend.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Legend, LegendsViewHolder>(options) {
            @NonNull
            @Override
            public LegendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_legend,parent,false);
                return new LegendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull LegendsViewHolder holder, int position, @NonNull Legend model) {
                holder.textView_name.setTextColor(Color.parseColor(model.getColor()));
                holder.textView_name.setText(model.getName());
            }
        };

        firestoreList.setHasFixedSize(true);
        firestoreList.setLayoutManager(new LinearLayoutManager(this));
        firestoreList.setAdapter(adapter);
    }

    private static class LegendsViewHolder extends RecyclerView.ViewHolder{

        private TextView textView_name;

        LegendsViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.name_text);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}