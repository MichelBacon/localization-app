package com.cegepba.localization_app.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cegepba.localization_app.Model.Connection;
import com.cegepba.localization_app.Model.Node;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FirestoreRepository {

//    private FirebaseFirestore db;
//
//    public FirestoreRepository() {
//        db = FirebaseFirestore.getInstance();
//    }
//
//    public Task<List<Node>> getNodeWithConnection() {
//        final List<Node> nodes = new ArrayList<>();
//        return db.collection("nodes")
//                .get()
//                .continueWithTask(new Continuation<QuerySnapshot, Task<List<QuerySnapshot>>>() {
//                    @Override
//                    public Task<List<QuerySnapshot>> then(@NonNull Task<QuerySnapshot> task) throws Exception {
//                        List<Task<QuerySnapshot>> connectionNodeQueryTasks = new ArrayList<>();
//                        if(task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentNode : Objects.requireNonNull(task.getResult())) {
//                                final Node node = documentNode.toObject(Node.class);
//                                DocumentReference docRefNode = documentNode.getReference();
//                                nodes.add(node);
//
//                                Task newTask = getTaskToAddConnectionToNode(docRefNode, node);
//                                connectionNodeQueryTasks.add(newTask);
//                            }
//
//                            /*.addOnCompleteListener(new OnCompleteListener() {
//                                @Override
//                                public void onComplete(@NonNull Task task) {
//                                    return nodes;
//                                }
//                            }).getResult();*/
//                        } else {
//                            Log.e("ERROR", "erreur");
//                        }
//                        return Tasks.whenAllSuccess(connectionNodeQueryTasks);
//                    }
//                }).continueWithTask(new Continuation<List<QuerySnapshot>, List<Node>>() {
//                    @Override
//                    public List<Node> then(@NonNull Task<List<QuerySnapshot>> task) throws Exception {
//                        return nodes;
//                    }
//                });
//                /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()) {
//                            Collection connectionNodeQueryTasks = new ArrayList<Task>();
//                            for (QueryDocumentSnapshot documentNode : Objects.requireNonNull(task.getResult())) {
//                                final Node node = documentNode.toObject(Node.class);
//                                DocumentReference docRefNode = documentNode.getReference();
//
//                                Task newTask = getTaskToAddConnectionToNode(docRefNode, node);
//                                connectionNodeQueryTasks.add(newTask);
//                            }
//                            Tasks.whenAllComplete(connectionNodeQueryTasks).addOnCompleteListener(new OnCompleteListener() {
//                                @Override
//                                public void onComplete(@NonNull Task task) {
//
//                                }
//                            });
//                        }
//                    }
//                });*/
//    }
//
//    private Task<QuerySnapshot> getTaskToAddConnectionToNode(DocumentReference docRefNode, final Node node) {
//        return docRefNode.collection("connections").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    for (QueryDocumentSnapshot documentConnections : Objects.requireNonNull(task.getResult())) {
//                        Connection connection = documentConnections.toObject(Connection.class);
//                        DocumentReference docRefConnection = connection.getConnectionRef();
//                        node.addConnection(connection);
//                    }
//                }
//            }
//        });
//    }
}
