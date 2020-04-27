package com.cegepba.localization_app;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cegepba.localization_app.Model.Connections;
import com.cegepba.localization_app.Model.Nodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RouteFinder {

    int  maxBeacon = 4;
    FirebaseFirestore db;
    int map[][];
    int count = 0 ;
    onMessageListener onMessageListener;
    HashMap<String, Nodes> nodes;

    public RouteFinder() {

    }

    public RouteFinder(final RouteFinder.onMessageListener onMessageListener) {
        map = new int[maxBeacon][maxBeacon];
        this.onMessageListener = onMessageListener;
        nodes = new HashMap<>();
        db = FirebaseFirestore.getInstance();

        db.collection("nodes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentNode : Objects.requireNonNull(task.getResult())) {
                                Nodes node = documentNode.toObject(Nodes.class);
                                nodes.put(documentNode.getId(), node);
                                DocumentReference docRefNode = documentNode.getReference();
                                docRefNode.collection("connections").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentConnections : Objects.requireNonNull(task.getResult())) {
                                                Connections connection = documentConnections.toObject(Connections.class);
                                                DocumentReference docRefConnection = connection.getConnectionRef();
                                                nodes.get(docRefConnection.getId());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

//        db.collection("Floors")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentFloor : Objects.requireNonNull(task.getResult())) {
//                                DocumentReference docRefFloor = documentFloor.getReference();
//                                docRefFloor.collection("Node").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onSuccess(QuerySnapshot documentSnapshot) {
//                                        for (QueryDocumentSnapshot documentNode : Objects.requireNonNull(documentSnapshot)) {
//                                            Node node = new Node();
//                                            node = documentNode.toObject(Node.class);
//                                                try {
//                                                    onMessageListener.setNodeList(node, count);
//                                                    Log.d("TOAST", documentNode.getId());
//                                                    map[count][0] = node.getDistanceToA();
//                                                    map[count][1] = node.getDistanceToB();
//                                                    map[count][2] = node.getDistanceToC();
//                                                    map[count][3] = node.getDistanceToD();
//                                                }catch (Exception e) {
//                                                    Log.d("TOAST missing data ",String.valueOf(count) + " node found");
//                                                    Log.d("TOAST missing data ",e.toString());
//
//                                                }
//                                            count++;
//                                        }
//                                    }
//                                });
//                            }
//                        }else{
//                            String msg = task.getException().toString();
//                            onMessageListener.onMessage(msg);
//                        }
//                        count = 0;
//                    }
//                });
        dijkstra(map, 0);
    }

    private int minDistance(int[] distance, Boolean[] visited){
        int minDistance = Integer.MAX_VALUE;
        int index = -1;

        for(int i=0;i<maxBeacon;i++){
            if(!visited[i] && distance[i] <= minDistance){
                minDistance = distance[i];
                index = i;
            }
        }
        return index;
    }
    public int[] dijkstra(int[][] map, int src ){

        int[] distance = new int[maxBeacon];
        Boolean[] visited = new Boolean[maxBeacon];
        for(int i=0; i<maxBeacon; i++){
            distance[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        distance[src] = 0;
        for(int count = 0; count <maxBeacon; count++){
            int shortestNode = minDistance(distance, visited);
            visited[shortestNode] = true;
            for(int i=0;i<maxBeacon;i++){
                try {
                    if (!visited[i] && map[shortestNode][i] != 0 && distance[shortestNode] != Integer.MAX_VALUE && distance[shortestNode] + map[shortestNode][i] < distance[i]) {

                        distance[i] = distance[shortestNode] + map[shortestNode][i];
                    }
                }catch (Exception e){
                    System.out.println("Error " + e.getMessage());
                }
            }

        }
        return distance;
    }

//    public static void main(int  map[][]){
//
//    RouteFinder routeFinder =  new RouteFinder();
//    routeFinder.dijkstra(map, 0);
//    }
    interface onMessageListener{
        void onMessage(String msg);
        void setNodeList(Node node, int nodePosition);
    }

}
