package com.cegepba.localization_app;

import android.util.Log;
import androidx.annotation.NonNull;
import com.cegepba.localization_app.Model.Connection;
import com.cegepba.localization_app.Model.Node;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Objects;

public class RouteFinder {

    private FirebaseFirestore db;
    private HashMap<String, Node> nodes;
    private List<String> currentRoad;
    private int numberOfTimeNotGood = 0;

    public RouteFinder() {
        nodes = new HashMap<>();
        db = FirebaseFirestore.getInstance();

        //FirestoreRepository repo = new FirestoreRepository();

        /*repo.getNodeWithConnection().addOnCompleteListener(new OnCompleteListener<List<Node>>() {
            @Override
            public void onComplete(@NonNull Task<List<Node>> task) {
                for (Node nodeInList: task.getResult()) {
                    Log.d("TEST123", nodeInList.toString());
                }
            }
        });*/
    }

    public void getRoad(final String startNode, final String destinationNode, final boolean isAnUpdate, final FirebaseCallback firebaseCallback) {
        db.collection("nodes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Collection connectionNodeQueryTasks = new ArrayList<Task>();
                            for (QueryDocumentSnapshot documentNode : Objects.requireNonNull(task.getResult())) {
                                final Node node = documentNode.toObject(Node.class);
                                nodes.put(documentNode.getId(), node);
                                DocumentReference docRefNode = documentNode.getReference();

                                Task newTask = getTaskToAddConnectionToNode(docRefNode, node);
                                connectionNodeQueryTasks.add(newTask);
                            }
                            Tasks.whenAllComplete(connectionNodeQueryTasks).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    List<String> road;
                                    road = dijkstra(nodes, startNode, destinationNode);

                                    if(currentRoad == null) {
                                        currentRoad = road;
                                    }

                                    if(isAnUpdate) {
                                        int sizeOfSubtractionOfRoadAndCurrentRoad = currentRoad.size() - road.size();
                                        if(currentRoad.size() < road.size() || !currentRoad.get(sizeOfSubtractionOfRoadAndCurrentRoad).equals(road.get(0))) {
                                            int[][] position = new int[road.size()][road.size() +1];
                                            int xArrayPos = 0;
                                            int yArrayPos;
                                            try {
                                                for(String node : road) {
                                                    yArrayPos = 0;
                                                    Node nodeToGetPosition = nodes.get(node);
                                                    position[xArrayPos][yArrayPos] = nodeToGetPosition.getXpos();
                                                    position[xArrayPos][yArrayPos + 1] = nodeToGetPosition.getYpos();
                                                    position[xArrayPos][yArrayPos + 2] = nodeToGetPosition.getFloorNum();
                                                    xArrayPos++;
                                                }
                                            }catch(Exception e){
                                                Log.d("123123", "e = " + e.getMessage());
                                            }

                                            currentRoad = road;
                                            numberOfTimeNotGood += 1;
                                            firebaseCallback.onCallback(true, position, numberOfTimeNotGood);
                                        } else {
                                            for(int i=0; i<sizeOfSubtractionOfRoadAndCurrentRoad-1;i++) {
                                                currentRoad.remove(i);
                                            }
                                            numberOfTimeNotGood = 0;
                                            getPosition(firebaseCallback, road);
                                        }
                                    } else {
                                        numberOfTimeNotGood = 0;
                                        getPosition(firebaseCallback, road);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void getPosition(FirebaseCallback firebaseCallback, List<String> road) {
        int[][] position = new int[road.size()][road.size() +1];
        int xArrayPos = 0;
        int yArrayPos;
        try {
            for(String node : road) {
                yArrayPos = 0;
                Node nodeToGetPosition = nodes.get(node);
                position[xArrayPos][yArrayPos] = nodeToGetPosition.getXpos();
                position[xArrayPos][yArrayPos + 1] = nodeToGetPosition.getYpos();
                position[xArrayPos][yArrayPos + 2] = nodeToGetPosition.getFloorNum();
                xArrayPos++;
            }
        }catch(Exception e){
            Log.d("123123", "e = " + e.getMessage());
        }
        firebaseCallback.onCallback(position);
    }

    private Task<QuerySnapshot> getTaskToAddConnectionToNode(DocumentReference docRefNode, final Node node) {
        return docRefNode.collection("connections").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentConnections : Objects.requireNonNull(task.getResult())) {
                        Connection connection = documentConnections.toObject(Connection.class);
                        node.addConnection(connection);
                    }
                }
            }
        });
    }

    private List<String> dijkstra(HashMap<String, Node> nodes, String startNode, String destinationNode) {
        HashMap<String, Integer> distance = new HashMap<>();
        HashMap<String, String> previous = new HashMap<>();
        List<String> unvisitedNode = new ArrayList<>();

        for (Map.Entry<String, Node> node: nodes.entrySet()) {
            distance.put(node.getKey(), Integer.MAX_VALUE);
            previous.put(node.getKey(), null);
            unvisitedNode.add(node.getKey());
        }

        distance.put(startNode, 0);
        while(unvisitedNode.size() > 0) {
            String currentNodeKey = findMinValue(distance, unvisitedNode);
            unvisitedNode.remove(currentNodeKey);
            for(Connection connection : nodes.get(currentNodeKey).getConnections()) {
                Integer newDistance = connection.getDistance() + distance.get(currentNodeKey);
                if(newDistance < distance.get(connection.getConnectionRef().getId())) {
                    distance.put(connection.getConnectionRef().getId(), newDistance);
                    previous.put(connection.getConnectionRef().getId(), currentNodeKey);
                }
            }
        }

        String currentNode = destinationNode;
        List<String> roadMap = new ArrayList<>();
        roadMap.add(currentNode);

        while(!currentNode.equals(startNode)) {
            currentNode = previous.get(currentNode);
            roadMap.add(0, currentNode);
        }

        return roadMap;
    }

    private String findMinValue(HashMap<String, Integer> distance, List<String> unvisitedNode) {
        String minDistanceNodeKey = null;
        int minDistance = 0;
        for (String unvisitedNodeKey: unvisitedNode) {
            if (minDistanceNodeKey == null || minDistance > distance.get(unvisitedNodeKey)) {
                minDistanceNodeKey = unvisitedNodeKey;
                minDistance = distance.get(unvisitedNodeKey);
            }
        }
        return minDistanceNodeKey;
    }

    interface FirebaseCallback{
        void onCallback(int[][] list);
        void onCallback(boolean isNotOnGoodPath, int[][] list, int notOnGoodPathNumber);
    }
}
