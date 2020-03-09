/*
 File: WeightedGraph.java
 Names: Alex Yuk
 Date: March 6th, 2020
 */

public class WeightedGraph {

    // Size of graph
    int vertices;
    // Adjacency list
    Vertex[] adjList;

    // Constructor
    WeightedGraph(int vertices) {
        this.vertices = vertices;
        adjList = new Vertex[vertices];
    }

    public int size() {
        return vertices;
    }

    // Creates Vertex
    void createVertex(int index, int x, int y) {
        adjList[index] = new Vertex(index, x, y);
    }

    // Adds edge
    void addEdge(int source, int destination) {
        adjList[source].connected.add(destination);
        adjList[destination].connected.add(source);
    }

    // Prints Graph
    void printGraph() {
        for (int i = 0; i < vertices; i++) {
            System.out.print("Vertex " + i + " Connected [" );
            for (int j = 0; j < adjList[i].connected.size(); j++) {
                if (j == adjList[i].connected.size() - 1)
                    System.out.print(adjList[i].connected.get(j));
                else
                    System.out.print(adjList[i].connected.get(j) + ", ");
            }
            System.out.println("]");
        }
    }

    void printInfo() {
        for (int i = 0; i < vertices; i++) {
            System.out.println("Vertex " + i + " Weight " + adjList[i].weight + " Parent " + adjList[i].parent);
        }
    }

    static class Vertex implements Comparable<Vertex> {
        int index;
        int x;
        int y;
        double weight;
        int parent;
        // List of connected
        LinkedList<Integer> connected;

        // Construct0r
        Vertex(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.weight = Double.MAX_VALUE;
            this.parent = -1;
            this.connected = new LinkedList<>();
        }

        void setWeight(double weight) {
            this.weight = weight;
        }

        void setParent(int parent) {
            this.parent = parent;
        }

        @Override
        public int compareTo(Vertex vertex) {
            return Double.compare(this.weight, vertex.weight);
        }
    }

    //
    LinkedList<Vertex> dijkstra(int source, int destination) {
        MinPQ<Vertex> pq = new MinPQ<>();
        // Keeps track of visited vertices to avoid repeats
        boolean[] visited = new boolean[vertices];

        for (int i = 0; i < vertices; i++) {
            adjList[i].setWeight(Double.MAX_VALUE);
            adjList[i].setParent(-1);
        }

        // The current vertex used to find the adjacent vertices
        Vertex current = adjList[source];
        // The distance from the source to itself is 0
        current.weight = 0;


        while (true) {
//            System.out.println(current.index);
//            printInfo();
            visited[current.index] = true;

            // When it reaches destination
            if (current.index == destination)
                break;
            // If the initial source vertex has no connections
            if (current.connected.size() == 0)
                break;

            // Temp vertex used in the for loop
            Vertex currentAdj;
            for (int i = 0; i < current.connected.size(); i++) {
                currentAdj = adjList[current.connected.get(i)];
                // Skip if already visited
                if(visited[currentAdj.index]) continue;
                // Calculate weight
                double adjWeight = current.weight + calcDistance(current.index, current.connected.get(i));
                // Only set new weight if it is smaller than the previous
                if (adjWeight < currentAdj.weight)
                    currentAdj.setWeight(adjWeight);

                currentAdj.setParent(current.index);
                // Add to priority queue
                pq.insert(currentAdj);
            }

            do {
                current = pq.delMin();
            } while (visited[current.index]);
        }

        LinkedList<Vertex> path = new LinkedList<>();

        System.out.println("Path:");
        Vertex temp = adjList[destination];
        path.add(temp);
        while (temp.index != source) {
            System.out.print("Vertex " + temp.index + " Weight " + temp.weight + "\n");
            temp = adjList[temp.parent];
            path.add(temp);
        }

        return path;
    }

    // Returns the distance between vertices source and destination
    private double calcDistance(int source, int destination) {
        return Math.sqrt(Math.pow(adjList[source].x - adjList[destination].x, 2)
                + Math.pow(adjList[source].y - adjList[destination].y, 2));
    }
}
