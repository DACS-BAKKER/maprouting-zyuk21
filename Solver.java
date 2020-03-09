/*
 File: Solver.java
 Names: Alex Yuk
 Date: March 6th, 2020
 */

import java.io.*;
import java.util.*;

public class Solver {

    private static final String GRAPH_DIRECTORY = "usa.txt";
    public static WeightedGraph graph;

    public static void main(String[]args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Non-graphical pathfinder for the US Map");
        System.out.println("Largest number you can enter is 87575");
        System.out.print("Please enter source\n? ");
        int source = sc.nextInt();
        System.out.print("Please enter destination\n? ");
        int destination = sc.nextInt();

        Solver S = new Solver(source, destination);
    }

    public Solver() {
        readFile();
    }

    public Solver(int source, int destination) {
        readFile();
//        graph.printGraph();
        graph.dijkstra(source,destination);
    }

    // Reads file into graph
    private static void readFile() {
        try {
            Scanner sc = new Scanner(new File(GRAPH_DIRECTORY));
            // First number in usa.txt is the number of vertices
            int graphSize = Integer.parseInt(sc.next());
            // Second number in usa.txt is the number of edges
            int edgeCount = Integer.parseInt(sc.next());
            graph = new WeightedGraph(graphSize);

            // Second section contains vertices and their coordinates
            int x, y;
            for (int vertex = 0; vertex < graphSize; vertex++) {
                sc.next();
                x = Integer.parseInt(sc.next());
                y = Integer.parseInt(sc.next());
//                System.out.println("Vertex " + vertex + " coordinates: " + "(" + x + ", " + y + ")");
                graph.createVertex(vertex,  x, y);
            }

            // Third section contains the edges
            int source, destination;
            for (int i = 0; i < edgeCount; i++) {
                source = Integer.parseInt(sc.next());
                destination = Integer.parseInt(sc.next());
//                System.out.println("[" + source + ", " + destination + "]");
                graph.addEdge(source, destination);
                graph.addEdge(destination, source);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }


}
