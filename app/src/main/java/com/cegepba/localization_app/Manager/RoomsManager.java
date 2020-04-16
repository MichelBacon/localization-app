package com.cegepba.localization_app.Manager;

import androidx.annotation.NonNull;
import com.cegepba.localization_app.Model.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RoomsManager {

    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Rooms> rooms;

    public RoomsManager() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void setRoomsArray() {
        rooms = new ArrayList<>();

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
                                        rooms.add(documentSnapshot.toObject(Rooms.class));
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public ArrayList<Rooms> getRooms() {
        return rooms;
    }
}
