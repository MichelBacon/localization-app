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

import com.cegepba.localization_app.Model.Legends;
import com.cegepba.localization_app.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
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
import com.google.firestore.v1.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

//TODO draw text with color (textcolor = color)

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

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void setRecyclerView() {
        Query query = firebaseFirestore.collection("Legends");

        FirestoreRecyclerOptions<Legends> options = new FirestoreRecyclerOptions.Builder<Legends>()
                .setQuery(query, Legends.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Legends, LegendsViewHolder>(options) {
            @NonNull
            @Override
            public LegendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_simple_legend,parent,false);
                return new LegendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull LegendsViewHolder holder, int position, @NonNull Legends model) {
                holder.textView_name.setTextColor(Color.parseColor(model.getColor()));
                holder.textView_name.setText(model.getName());
            }
        };
        firestoreList.setHasFixedSize(true);
        firestoreList.setLayoutManager(new LinearLayoutManager(this));
        firestoreList.setAdapter(adapter);
    }

    private class LegendsViewHolder extends RecyclerView.ViewHolder{

        private TextView textView_name;

        public LegendsViewHolder(@NonNull View itemView) {
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