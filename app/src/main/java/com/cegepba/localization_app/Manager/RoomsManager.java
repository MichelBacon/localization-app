package com.cegepba.localization_app.Manager;

import androidx.annotation.NonNull;
import com.cegepba.localization_app.Model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class RoomsManager {

    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Room> rooms;

    public RoomsManager() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    //TODO know how to structure better the code with firebase

    public void setRoomsArray() {
        rooms = new ArrayList<>();

        firebaseFirestore.collection("rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                DocumentReference docRef = firebaseFirestore.collection("rooms").document(document.getId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        rooms.add(documentSnapshot.toObject(Room.class));
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
