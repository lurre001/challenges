import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
// Author Lukas Eriksson
// Solution to kattis problem. The issue is to find the minimum spanning tree of a graph.
class Node {
    private Coordinate coordinate;

    public Node(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
        
    
    public Coordinate getCoordinate() {
        return coordinate;
    }
    public String toString() {
        return coordinate.toString();
    }
    @Override
    public int hashCode(){
        return coordinate.hashCode();
    }

}

class Coordinate {
    private float x;
    private float y;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

class Edge implements Comparable<Edge>{
    private Node dest;
    private Node src;
    private float weight;

    public Edge(Node src, Node dest, float weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
    public String toString(){
        return "\n" + weight;
    }
    public Node getDestination(){
        return dest;
    }
    public Node getSource(){
        return src;
    }
    public float getWeight(){
        return weight;
    }
    @Override
    public int compareTo(Edge e){
        if(this.weight < e.weight){
            return -1;
        }
        else if(this.weight > e.weight){
            return 1;
        }
        else if(this.getDestination().equals(e.getDestination())){
            return 0;
        }
        else{
            return 1;
        }
    }
    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof Edge)){
            return false;
        }
        Edge e = (Edge) o;
        return this.weight == e.weight && this.src == e.src && this.dest == e.dest;
    }
}

public class problemB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US);
        int numTest = sc.nextInt();
        ArrayList<Node> nodes = new ArrayList<Node>();
        TreeSet<Edge> edges = new TreeSet<Edge>();
        for (int i = 0; i < numTest; i++) {
            int numNodes = sc.nextInt();
            for (int j = 0; j < numNodes; j++) {
                Node n = new Node(new Coordinate(sc.nextFloat(), sc.nextFloat()));
                nodes.add(n);
            }
            generateAllEdges(nodes, edges);
            
            minimumSpanningTree(edges, nodes);
            
            nodes.clear();
            edges.clear();

        }

        sc.close();
    }

    public static void minimumSpanningTree(TreeSet<Edge> edges, ArrayList<Node> nodes) {
        //first node
        Node startNode = nodes.get(0);
    
        // visited
        Set<Node> visited = new HashSet<>();
        visited.add(startNode);
    
        // chosen edges
        Set<Edge> mstEdges = new HashSet<>();
        float totalLength = 0;
    
        while (visited.size() < nodes.size()) {
            Edge minEdge = null;
            for (Edge edge : edges) {
                if ((visited.contains(edge.getSource()) && !visited.contains(edge.getDestination())) 
                    || (visited.contains(edge.getDestination()) && !visited.contains(edge.getSource()))) {
                    minEdge = edge;
                    break;
                }
            }
    
            if (minEdge == null) {
                break;
            } else {
                mstEdges.add(minEdge);
                totalLength += minEdge.getWeight();
                if (!visited.contains(minEdge.getSource())) visited.add(minEdge.getSource());
                if (!visited.contains(minEdge.getDestination())) visited.add(minEdge.getDestination());
                edges.remove(minEdge);
            }
        }
    
        System.out.println(totalLength);
    }
    
    public static float distance(Coordinate a, Coordinate b) {
        return (float) Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    public static void generateAllEdges(ArrayList<Node> nodes, TreeSet<Edge> edges) {
        for (int i = 0; i < nodes.size(); i++) {
            Node n1 = nodes.get(i);
            Coordinate c1 = n1.getCoordinate();
            for (int j = i + 1; j < nodes.size(); j++) {
                Node n2 = nodes.get(j);
                Coordinate c2 = n2.getCoordinate();
                edges.add(new Edge(n1, n2, distance(c1, c2)));
            }
        }
    }
    
    
}
