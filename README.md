## Assignment 10 README

## Files

**Java Files:** `WeightedGraph.java`, `Solver.java`, `View.java`, `MinPQ.java`, `LinkedList.java`

**Text Files:** `usa.txt`, `usamap.png`

The two files that you should run are Solver and View. Solver is the un-graphical version of the problem and the View contains a GUI. Instructions are built into the code when you run them. 

## WeightedGraph.java



In my undirected weighted adjacency graph, there is a integer keeping track of the size of the graph called `vertices` and an array of Vertex called `adjList`. The API of the vertex class is shown here.

```java
private static class Vertex implements Comparable<Vertex> {
  // Index of this vertex
	int index;
  // X and Y coordinates of the Vertex
  int x;
	int y;
  // Weight from this vertex to the source (defined later)
	double weight;
  // Index of parent of this vertex
	int parent;
	// List of connected vertices
	LinkedList<Integer> connected;
  
	// Constructor
	Vertex(int index, int x, int y) {...}
  
	void setWeight(double weight) {...}
	void setParent(int parent) {...}

  // Compares this vertex to another
	public int compareTo(Vertex vertex) {
		return Double.compare(this.weight, vertex.weight);
	}
}
```

To quickly explain how the graph works, each Vertex in the graph has a list of vertices that it is connected with. So when I read `usa.txt` at the start of the program, all I have to do is adding those connections to each vertex when I read them. The `weight` of each vertex is used when I run the Dijkstra's Algorithm to keep track of the distance between the current vertex and the source.

#### Dijkstra's Algorithm

My Dijkstra's Algorithm returns a LinkedList of vertices which is the path between the source and the destination. The following are the steps to the algorithm.

1. Set all weights of vertices in `adjList` to infinity
2. Set a `current` pointer to the source vertex
3. Mark `current`'s index in the `visited` boolean array as true to not have repeats
4. Check if `current` is destination or if its `connected` LinkedList is empty
5. For every vertex in `current`'s `connected` LinkedList that is not `true` in the `visited` array
   1. Calculate the weight between itself and the source
   2. Replace its own weight if it is smaller than what it is now
   3. Set its parent to `current`
   4. Add it to the MinPQ
6. Set `current` to the `delMin()` of the MinPQ  that is not`true` in the `visited` array
7. Repeat steps 3 to 6 until 4 is true
8. Trace back the path from the destination using `parent` and return the LinkedList

## View.txt

**Sub-classes**

I used two utility classes here to do the GUI.

```java
private static class Location {
  // X and Y coordinate
	int x;
	int y;
  // Constructor
	Location(int x, int y) {...}
}
```

```java
private static class Node {
  // Index of this Node
	int index;
  // The color of this Node
	int color;
  // Constructor
	Node(int index, int color) {...}
}
```

Nothing special with the Location class, very standard so I won't explain anything. The Node class is interesting because in my `View.java` I have a 2D array of these Nodes which has the information I need to show the GUI.

**Scaling and Array**

The main problem I ran into when trying to do the graphic is the scaling of the coordinates from `usa.txt` into a window that can be shown on screen. I first tried to scale the dots based on a image I got which didn't really work since it made the clicks very imprecise. In the end, I decided to use a 2D array which represents all of the vertices in the `usa.txt` file witch a ratio of 10. Every time a user clicks, the program will try to match the user's mouse's location to a Node in the 2D array. A Location object will keep track of the location of the Node that the program matched to and the program will wait for the second selection. Once the user does that, the program will draw out a path using the LinkedList returned from the Dijkstra's Algorithm function.