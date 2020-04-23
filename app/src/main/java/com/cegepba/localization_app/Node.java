package com.cegepba.localization_app;

    class Node  {
        private String name;
        private int XPos;
        private int YPos;
        private int distanceToA;
        private int distanceToB;
        private int distanceToC;
        private int distanceToD;

        public Node() {
            XPos = YPos = distanceToA = distanceToB = distanceToC = distanceToD = 0;
        }
        public Node(String name, int XPos, int YPos, int distanceToA, int distanceToB, int distanceToC, int distanceToD) {
            this.name = name;
            this.XPos = XPos;
            this.YPos = YPos;
            this.distanceToA = distanceToA;
            this.distanceToB = distanceToB;
            this.distanceToC = distanceToC;
            this.distanceToD = distanceToD;
        }
//region getter
        public String getName() {
            return name;
        }

        public int getXPos() {
            return XPos;
        }

        public int getYPos() {
            return YPos;
        }

        public int getDistanceToA() {
            return distanceToA;
        }

        public int getDistanceToB() {
            return distanceToB;
        }

        public int getDistanceToC() {
            return distanceToC;
        }

        public int getDistanceToD() {
            return distanceToD;
        }
   //endregion
    }














//        public int node;
//        public int cost;
//
//        public Node()
//        {
//        }
//
//        public Node(int node, int cost)
//        {
//            this.node = node;
//            this.cost = cost;
//        }
//
//        @Override
//        public int compare(Node node1, Node node2)
//        {
//            if (node1.cost < node2.cost)
//                return -1;
//            if (node1.cost > node2.cost)
//                return 1;
//            return 0;
//        }
    // Goes with this code in RouteFinder.java
//package com.cegepba.localization_app;
//
//        import androidx.appcompat.app.AppCompatActivity;
//
//        import java.util.ArrayList;
//        import java.util.HashSet;
//        import java.util.List;
//        import java.util.PriorityQueue;
//        import java.util.Set;
//
//public class RouteFinder{
//
//    private int dist[];
//    private Set<Integer> settled;
//    private PriorityQueue<Node> priorityQueue;
//    private int verticesNbr; // Number of vertices
//    List<List<Node>> adj;
//
//    public RouteFinder(int vertices)
//    {
//        this.verticesNbr = vertices;
//        dist = new int[vertices];
//        settled = new HashSet<Integer>();
//        priorityQueue = new PriorityQueue<Node>(vertices, new Node());
//    }
//
//    // Function for Dijkstra's Algorithm
//    public void dijkstra(List<List<Node> > adj, int src)
//    {
//        this.adj = adj;
//
//        for (int i = 0; i < verticesNbr; i++)
//            dist[i] = Integer.MAX_VALUE;
//
//        // Add source node to the priority queue
//        priorityQueue.add(new Node(src, 0));
//
//        // Distance to the source is 0
//        dist[src] = 0;
//        while (settled.size() != verticesNbr) {
//
//            // remove the minimum distance node
//            // from the priority queue
//            int u = priorityQueue.remove().node;
//
//            // adding the node whose distance is
//            // finalized
//            settled.add(u);
//
//            e_Neighbours(u);
//        }
//    }
//
//    // Function to process all the neighbours
//    // of the passed node
//    private void e_Neighbours(int u)
//    {
//        int edgeDistance = -1;
//        int newDistance = -1;
//
//        // All the neighbors of v
//        for (int i = 0; i < adj.get(u).size(); i++) {
//            Node vertices = adj.get(u).get(i);
//
//            // If current node hasn't already been processed
//            if (!settled.contains(vertices.node)) {
//                edgeDistance = vertices.cost;
//                newDistance = dist[u] + edgeDistance;
//
//                // If new distance is cheaper in cost
//                if (newDistance < dist[vertices.node])
//                    dist[vertices.node] = newDistance;
//
//                // Add the current node to the queue
//                priorityQueue.add(new Node(vertices.node, dist[vertices.node]));
//            }
//        }
//    }
//
//    // Driver code
//    public static void main(String arg[])
//    {
//        int vertices = 5;
//        int source = 0;
//
//        // Adjacency list representation of the
//        // connected edges
//        List<List<Node> > adj = new ArrayList<List<Node> >();
//
//        // Initialize list for every node
//        for (int i = 0; i < vertices; i++) {
//            List<Node> item = new ArrayList<Node>();
//            adj.add(item);
//        }
//
//        // Inputs for the RouteFinder graph
//        adj.get(0).add(new Node(1, 9));
//        adj.get(0).add(new Node(2, 6));
//        adj.get(0).add(new Node(3, 5));
//        adj.get(0).add(new Node(4, 3));
//
//        adj.get(2).add(new Node(1, 2));
//        adj.get(2).add(new Node(3, 4));
//
//        // Calculate the single source shortest path
//        RouteFinder dpq = new RouteFinder(vertices);
//        dpq.dijkstra(adj, source);
//
//        // Print the shortest path to all the nodes
//        // from the source node
//        System.out.println("The shorted path from node :");
//        for (int i = 0; i < dpq.dist.length; i++)
//            System.out.println(source + " to " + i + " is "
//                    + dpq.dist[i]);
//    }
//}//end class

// Driver method
//    public static void main(String[] args)
//    {
//        /* Let us create the example graph discussed above */
//        int graph[][] = new int[][] { { 0, 4, 0, 0, 0, 0, 0, 8, 0 },
//                { 4, 0, 8, 0, 0, 0, 0, 11, 0 },
//                { 0, 8, 0, 7, 0, 4, 0, 0, 2 },
//                { 0, 0, 7, 0, 9, 14, 0, 0, 0 },
//                { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
//                { 0, 0, 4, 14, 10, 0, 2, 0, 0 },
//                { 0, 0, 0, 0, 0, 2, 0, 1, 6 },
//                { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
//                { 0, 0, 2, 0, 0, 0, 6, 7, 0 } };
//        ShortestPath t = new ShortestPath();
//        t.dijkstra(graph, 0);
//    }
//    int[] TotalDistance;
//    int[] ShortestNodeRoad;
//    Node[] AllNode;
//    int  totalNodes = 4;
//    public RouteFinder(Context context) {
//        //super(context);
//
//    }
//    public RouteFinder(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//
//    }
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//
//    }
//
//    public void FindRoute(){
//        //TEMPO 4
//        fillArray();
//
//        for (Node node: AllNode) {
//
//        }
//    }
//    private void fillArray(){
//
//          for(int x = 0; x < totalNodes; x++){
//              TotalDistance[x] = 9999;
//              ShortestNodeRoad[x] = 0;
//          }
////        AllNode;
//    }


