import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.*;

/**
 * Created by josephchiou on 7/24/17.
 */
public class Dijkstra {
    // global variables
    static ArrayList<Node> distanceList; // this arrayList contains every node's distance from source
    static ArrayList<Node> predecessorList; //this arrayList stores each node's shortest predecessor. The final path can be tracked here
    static ArrayList<String> path;
    static double shortestDistance;
    private static Scanner sc = new Scanner(System.in);
    private static int totalNodes;
    private static ArrayList<ArrayList<String>> edgeInputs = new ArrayList<>(); // inner arrayList: 0= nodeA_ID, 1= weight, 2= nodeB_ID
    private static ArrayList<String> SOURCE_DESTINATION = new ArrayList<>();
    public static ArrayList<String> log = new ArrayList<>(); // this nodeList contains edges being updated due to multiple inputs
    static int edgeCount = 1;
    static boolean stopTakingEdgeInput = false;

    public static void main(String args[]){
        // instantiate variables
        distanceList = new ArrayList<Node>();
        predecessorList = new ArrayList<Node>();
        path = new ArrayList<>();

        Graph graph = new Graph();
        /*
        Node a = new Node("nodeA");
        Node b = new Node("nodeB");
        Node c = new Node("nodeC");
        Node d = new Node("nodeD");
        Node e = new Node("nodeE");
        Node f = new Node("nodeF");
        Node g = new Node("nodeG");
        Node h = new Node("nodeH");
        Node i = new Node("nodeI");
        */
        /*
        graph.addEdges(d,1,a); // will be replaced by #1
        graph.addEdges(a,2,f); // will be replaced by #2
        graph.addEdges(b,3,c); // will be replaced by #3
        graph.addEdges(d,6,a); // #1
        graph.addEdges(a,5,b);
        graph.addEdges(b,4,e);
        graph.addEdges(e,3,g);
        graph.addEdges(a,3,f); // #2
        graph.addEdges(f,10,g);
        graph.addEdges(b,7,f);
        graph.addEdges(b,2,c); // #3
        graph.addEdges(c,4,f);
        graph.addEdges(c,1,e);
        shortestPath(graph, "nodeG", "nodeD");
        */
        /*
        graph.addEdges(c,3,f);
        graph.addEdges(f,3,g);
        graph.addEdges(a,3,e);
        graph.addEdges(e,8,h);
        graph.addEdges(d,9,b);
        graph.addEdges(b,5,i);
        graph.addEdges(c,2,a);
        graph.addEdges(f,1,e);
        graph.addEdges(g,6,h);
        graph.addEdges(a,1,d);
        graph.addEdges(e,7,b);
        graph.addEdges(h,8,i);
        graph.addEdges(c,8,b);
        graph.addEdges(f,4,d);
        graph.addEdges(f,8,i);
        graph.addEdges(g,2,b);
        shortestPath(graph, "nodeA", "nodeB");
        Collections.reverse(path);
        */

        try {
            graph = createGraph();
            System.out.println("\n=================\tProcessing Dijkstra algorithm:\t=================\n");
            shortestPath(graph,SOURCE_DESTINATION.get(0),SOURCE_DESTINATION.get(1));
            System.out.println("=================\tDone processing Dijkstra algorithm:\t===========\n");
            Collections.reverse(path);

            if(log.size()>0){
                System.out.println("Duplicate edges found: (new inputs will replace the old inputs)");
                for(String s : log){
                    System.out.println(s);
                }
            }
        }catch (Exception ex){
            sc.close();
            System.out.println(ex);
        }

        System.out.println("\n=================\tOutput:\t=================");
        System.out.println("Shortest Path: " + path);
        System.out.println("Shortest Distance: " + shortestDistance);
        sc.close();
    }

    public static Graph createGraph(){
        HashMap<String, Node> nodes = new HashMap<>();
        Graph graph = new Graph();
        boolean validInput = false;

        System.out.println("How many number of nodes is going to be in the graph? ");
        do{ //
            validInput = getInput(0);
        }while(validInput!= true);

        System.out.println("Please enter a sequence of edgeNodes with its associated length in the following format: NodeA, length, NodeB ");
        System.out.println("Enter -1 when there's no more edges");
        do{
            getInput(1);
        }while(stopTakingEdgeInput == false);

        System.out.print("Please enter the source node and the destination node: ");
        do{
            validInput = getInput(2);
        }while(validInput != true);

        for(int index = 0; index < edgeInputs.size(); index++){
            String nodeID1 = edgeInputs.get(index).get(0);
            String nodeID2 = edgeInputs.get(index).get(2);
            double weight = Double.parseDouble(edgeInputs.get(index).get(1));
            if(!nodes.containsKey(nodeID1)){
                nodes.put(nodeID1,new Node(nodeID1));
            }
            if(!nodes.containsKey(nodeID2)){
                nodes.put(nodeID2, new Node(nodeID2));
            }
            graph.addEdges(nodes.get(nodeID1),weight,nodes.get(nodeID2));
        }
        return graph;
    }

    private static boolean getInput(int type) {
        Pattern pattern;
        Matcher matcher;
        sc = new Scanner(System.in);
        // 0: enter number of nodes;
        // 1: enter edgeNodes
        // 2: enter source and destination
        switch (type){
            case 0: // case 0 for entering number of nodes
                try{
                    totalNodes = sc.nextInt();
                    return true;
                }catch (Exception e){
                    System.out.println("Invalid number format. Please enter a number.");
                }
                return false;
            case 1: // case 1 for entering edgeNodes
                pattern = Pattern.compile("^[\\w]*[,][-+]?[0-9]*\\.?[0-9]+[,][\\w]*$"); //Match format: [words] , [Integer or decimal] , [words]
                System.out.print("Please enter the edge # " + edgeCount++ + " : ");
                String case1Input = sc.nextLine();
                case1Input = case1Input.replaceAll("\\s","").trim(); //remove all white space
                if(case1Input.equals("-1")){
                    stopTakingEdgeInput = true;
                    //System.out.println("Stop getting edge input");
                    return false;
                }
                matcher = pattern.matcher(case1Input);
                if(matcher.find()){
                    ArrayList<String> args = new ArrayList<>(3);
                    for(String s : case1Input.split(",")){
                        args.add(s);
                    }
                    if(!args.get(0).equals(args.get(2))){ // add the edge if two nodes are not the same
                        edgeInputs.add(args);
                    }else{
                        System.out.println("Two nodes can not be the same");
                        edgeCount--;
                        return false;
                    }
                    return true;
                }
                edgeCount --;
                System.out.println("Invalid format for an edge. Please enter in the following format: NodeA, length, NodeB");
                return false;
            case 2: // case 2 for entering source and destination
                pattern = Pattern.compile("^[\\w]+[,][\\w]+$");
                String case2Input = sc.nextLine();
                case1Input = case2Input.replaceAll("\\s","").trim(); //remove all white space
                matcher = pattern.matcher(case1Input);
                if(matcher.find()){
                    for(String s : case1Input.split(",")){
                        SOURCE_DESTINATION.add(s);
                    }
                    return true;
                }
                System.out.println("Invalid format. Please enter in the following format: sourceNode, destNode");
                return false;
        }
        return false;
    }

    //this method takes a graph, source node's id, and destination node's id
    public static void shortestPath (Graph g, String source, String destination){
        Node sourceNode = g.getNodeById(source);
        Node destinationNode = g.getNodeById(destination);

        if(sourceNode == null || destinationNode == null){
            //either source or destination is missing in the graph, program terminate.
            System.out.println("\nSource/Destination not existed");
        }else{
            // create a min heap to store all nodes in the graph, the node with min distance will be remove at each iteration.
            // this min heap can be treated as a to-do nodeList for the algorithm. The program terminate when this heap is empty.
            MinHeap nodeMinHeap = new MinHeap();

            // get every node's id in the graph.
            ArrayList<String> nodeIDSet = new ArrayList<String>(g.getAllNodes().keySet());
            // add all nodes from the graph into the min-heap. Use node's id to find a node put the node instance to the min-heap
            for(int index = 0; index < nodeIDSet.size(); index++){
                String currentNodeID = nodeIDSet.get(index);
                if(currentNodeID == sourceNode.getNodeID()){
                    g.getAllNodes().get(currentNodeID).setDistance(0); //initialize source node's distance to 0
                }
                nodeMinHeap.add(g.getAllNodes().get(currentNodeID));
            }

            //****** start Dijkstra's shortest path algorithm ******
            while(!nodeMinHeap.isEmpty()){ // the process keeps running until there's nothing in the min-heap that stores all the unprocessed nodes
                System.out.println("Min-heap's nodes and distances: " + nodeMinHeap);

                Node currentNode = nodeMinHeap.getMin();
                distanceList.add(currentNode);
                predecessorList.add(currentNode);
                String parentID = (currentNode.getParent()==null) ? "null" :currentNode.getParent().getNodeID();
                System.out.print("Current node: "+ currentNode +
                        " ,parent: \"" + parentID + "\"" + " , connecting nodes: " );

                HashMap<Node,Double> adjNodes = currentNode.getAdjacencyNodes();
                ArrayList<Node> adjNodesSet = new ArrayList<Node>(adjNodes.keySet());
                System.out.println(adjNodesSet);
                for(int index = 0; index < adjNodesSet.size(); index++){ //iterating each edge that connects to the current node

                    Node connectingNode = adjNodesSet.get(index);
                    if(nodeMinHeap.contains(connectingNode)){ // check if the connecting node is in the min-heap (to-do nodeList)
                        double edgeWeight = adjNodes.get(connectingNode); //use node as key --> get edge value

                        //update the distance of a node if a smaller distance is found.
                        if((currentNode.getDistance() + edgeWeight) < connectingNode.getDistance()){
                            DecimalFormat df = new DecimalFormat("#.###");
                            String connectingNodeCurrentDistance = (connectingNode.getDistance() == Double.MAX_VALUE)? "\"Undetermined\"" : String.valueOf(df.format(connectingNode.getDistance()));
                            System.out.print("\t+\tupdate " + connectingNode.getNodeID() + "'s distance from " + connectingNodeCurrentDistance);
                            connectingNode.setDistance(currentNode.getDistance() + edgeWeight); //update distance
                            System.out.print(" to " + connectingNode.getDistance());

                            System.out.println(" , update " + connectingNode.getNodeID() + "'s parent to " + currentNode.getNodeID());
                            connectingNode.setParent(currentNode); // update predecessor
                        }else{
                            System.out.println("\t-\tfrom " + currentNode + " to " + connectingNode +
                                    " will take " + edgeWeight + " , which will make " + connectingNode.getNodeID() + "'s distance becomes " + (currentNode.getDistance()+edgeWeight)+
                                    " --> it is >= " + connectingNode.getNodeID() + "'s existing distance: " + connectingNode.getDistance() +
                                    " --> skip");
                        }
                    }else{
                        System.out.println("\t-\tconnecting node: " + connectingNode.getNodeID() + " (" + connectingNode.getDistance()+ ") is not in the min-heap  --> skip");
                    }
                }
                nodeMinHeap.extractMin(); //remove the current node from the min-heap
                if(nodeMinHeap.size()>1){
                    System.out.println("\tShortest path from current node: " + nodeMinHeap.getMin());
                }
                System.out.println();
            }
        }

        // back-tracking path
        for(int index = 0; index < predecessorList.size(); index++){
            if(predecessorList.get(index).getNodeID() == destinationNode.getNodeID()){
                shortestDistance = predecessorList.get(index).getDistance();
                track(predecessorList.get(index));
            }
        }
    }

    public static Node track(Node node){
        if(node == null){
            return null;
        }
        path.add(node.getNodeID());
        return track(node.getParent());
    }

}

class Edge implements Comparable<Edge>{
    String nodeA; //nodeA's id
    String nodeB; //nodeB's id
    private HashMap<String, Node> nodes;    //the two nodes that made up this edge. Key: node's id, value: the node instance
    private HashMap<Node, Node> nodeToNode;   // from one node to the other node. NodeA -> NodeB. NodeB -> NodeA
    private double weight; //the weight (length) of this edge

    public Edge(Node a, Node b, double weight){
        this.nodeA = a.getNodeID();
        this.nodeB = b.getNodeID();
        nodes = new HashMap<String, Node>();
        nodeToNode = new HashMap<Node, Node>();
        nodes.put(nodeA,a);
        nodes.put(nodeB,b);

        nodeToNode.put(a,b);
        nodeToNode.put(b,a);
        this.weight = weight;

        a.makeEdge(b,weight);
        b.makeEdge(a,weight);
    }

    public HashMap<String, Node> getEdgeNodes(){ //get the two nodes that make up this edge
        return nodes;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public double getWeight(){
        return weight;
    }

    public String printEdge(){
        return "Connecting: " + nodes.get(nodeA).getNodeID() + " and " + nodes.get(nodeB).getNodeID() + ". Length: " + weight;
    }

    @Override
    public int compareTo(Edge e) {
        return (int)(this.weight - e.weight);
    }
}


class Node implements Comparable<Node>{
    private String nodeID;
    private double distance; //distance from source
    private Node parent; // a node's predecessor (based on shortest path)
    private HashMap<Node, Double> edgeNodes = new HashMap<>(); // a node has a nodeList of edgeNodes connecting to different nodes

    public Node(String nodeID){
        this.nodeID = nodeID;
        //Initialize every node's distance to infinity.
        this.distance = Double.MAX_VALUE; //this value represents infinity, which means the distance is not determined yet.
    }
    public HashMap<Node, Double> getAdjacencyNodes(){
        return edgeNodes;
    } // return a nodeList of edgeNodes that connect to this node

    // make an edge that connect to the current node to a new node: b
    public void makeEdge(Node b, double weight){
        edgeNodes.put(b,weight);
        b.edgeNodes.put(this,weight);
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    public double getDistance(){
        return this.distance;
    }

    public void setParent(Node predecessor){
        this.parent = predecessor;
    }

    public Node getParent(){
        return parent;
    }

    public String getNodeID(){
        return nodeID;
    }

    @Override
    public int compareTo(Node n) {
        return (int)(this.getDistance()- n.getDistance());
    }

    public String toString(){
        String distance = (getDistance() == Double.MAX_VALUE)?"Undetermined":String.valueOf(getDistance());
        return getNodeID() + " (" + distance + ")";
    }
}

class Graph{
    HashMap<String, Node> nodes;
    HashMap<String, Edge> edges; // hash two nodes' id and use that value to be the key of an edge
    // hash(a) + hash(b) = hash(b) + hash(a)

    public Graph (){
        nodes = new HashMap<String, Node>();
        edges = new HashMap<>();
    }

    public Node getNodeById(String nodeID){
        return nodes.get(nodeID);
    }

    public void addEdges(Edge e){
        addEdges(e.getEdgeNodes().get(e.nodeA),e.getWeight(), e.getEdgeNodes().get(e.nodeB));
    }

    public void addEdges(Node a, double weight, Node b){
        //the two edges are the same if its two nodes' id are the same.
        String edgeKey = Integer.toString(a.getNodeID().hashCode()* b.getNodeID().hashCode() +a.getNodeID().hashCode()^2+ b.getNodeID().hashCode()^2);

        if(edges.containsKey(edgeKey)){ // if found duplicate edge, update the edge's weight instead
            //                             --> By deleting the current edge and then make a new one.
            Dijkstra.log.add("Change edge: " + a.getNodeID()+ " , " + b.getNodeID() +
                    " 's weight from " + edges.get(edgeKey).getWeight() + " to " + weight);
            edges.remove(edgeKey);
            edges.put(edgeKey, new Edge(a,b,weight));
            nodes.put(a.getNodeID(),a);
            nodes.put(b.getNodeID(),b);
        }else{
            edges.put(edgeKey, new Edge(a,b,weight));
            nodes.put(a.getNodeID(),a);
            nodes.put(b.getNodeID(),b);
        }
    }

    public HashMap<String, Node> getAllNodes(){
        return nodes;
    }
}

class MinHeap { // simple list that deal with remove the node with min-distance from the list
    private ArrayList<Node> nodeList;

    public MinHeap() {
        this.nodeList = new ArrayList<>();
    }

    public void add(Node node) {
        nodeList.add(node);
    }

    public boolean contains(Node node){
        for(int index= 0; index < nodeList.size(); index++){
            if(node.getNodeID().equals(nodeList.get(index).getNodeID())){
                return true;
            }
        }
        return false;
    }

    public void extractMin() {
        nodeList.remove(getMin());
    }

    public Node getMin() {
        Double min = Double.MAX_VALUE;
        int nodeIndex = -1;
        for(int index = 0; index< nodeList.size(); index++){
            if(nodeList.get(index).getDistance() < min){
                min = nodeList.get(index).getDistance();
                nodeIndex = index;
            }
        }
        return nodeList.get(nodeIndex);
    }

    public boolean isEmpty() {
        return nodeList.size() == 0;
    }

    public int size(){
        return nodeList.size();
    }

    public String toString(){
        return nodeList.toString();
    }
}
