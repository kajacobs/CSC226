/*
* CSC 226 Programming Assignment #2
* Katherine Jacobs
* V00783178
* Last Modified Oct 23 2019
*/

import java.util.Scanner;
import java.io.File;
import java.util.*;

public class MST {

    /*
    * Cited below are resources I used for this assignment:
    * Java doc for PQ https://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html
    * Java doc for Queue https://docs.oracle.com/javase/7/docs/api/java/util/Queue.html
    *
    * Kruskal's algorithm on lines 33-43, 48-51 from slides Nishant Mehta's website:
    * http://web.uvic.ca/~nmehta/csc226_fall2019/
    * from slides on Minimum Spanning trees, code on slide 39:
    * http://web.uvic.ca/~nmehta/csc226_fall2019/lecture7_8_9.pdf
    * Credit to textbook: Algorithms, Robert Sedgewick, Kevin Wayne
    */
    static int mst(int[][][] adj) {
	int n = adj.length;

    QuickFindUF weightedUnion = new QuickFindUF(adj.length);
    PriorityQueue<Edge> edges = new PriorityQueue<Edge>();
    boolean[][] doubles = new boolean[adj.length][adj.length];
    Queue<Integer> mst = new LinkedList<>();

    // create all edge objects
    for (int i=0; i < adj.length; i++) {
        for (int j=0; j < adj[i].length; j++) {
            Edge newEdge = new Edge(i, adj[i][j][0], adj[i][j][1]);
            edges.add(newEdge);
        }
    }

    while (!edges.isEmpty() && mst.size() < (adj.length - 1)){
        Edge e = edges.poll();
        int v = e.either();
        int w = e.other(v);


        // boolean array checks for duplicate edges
        if (doubles[w][v] == true || doubles[v][w] == true){
        } else {
            doubles[w][v] = true;
            if (!weightedUnion.connected(v,w)){
                weightedUnion.union(v, w);
                mst.add((int)e.weight());
            }
        }
    }

	/* Add the weight of each edge in the minimum spanning tree
	   to totalWeight, which will store the total weight of the tree.
	*/
	int totalWeight = 0;
    while (mst.peek() != null){
        totalWeight += mst.poll();
    }
	return totalWeight;
    }

    public static void main(String[] args) {
	int graphNum = 0;
	Scanner s;

	if (args.length > 0) {
	    //If a file argument was provided on the command line, read from the file
	    try {
		s = new Scanner(new File(args[0]));
	    }
	    catch(java.io.FileNotFoundException e) {
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n",args[0]);
	}
	else {
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}

	//Read graphs until EOF is encountered (or an error occurs)
	while(true) {
	    graphNum++;
	    if(!s.hasNextInt()) {
		break;
	    }
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();

	    int[][][] adj = new int[n][][];


	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++) {
		LinkedList<int[]> edgeList = new LinkedList<int[]>();
		for (int j = 0; j < n && s.hasNextInt(); j++) {
		    int weight = s.nextInt();
		    if(weight > 0) {
			edgeList.add(new int[]{j, weight});
		    }
		    valuesRead++;
		}
		adj[i] = new int[edgeList.size()][2];
		Iterator it = edgeList.iterator();
		for(int k = 0; k < edgeList.size(); k++) {
		    adj[i][k] = (int[]) it.next();
		}
	    }
	    if (valuesRead < n * n) {
		System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		break;
	    }

        /*
        * Commented code below for testing runtime in milliseconds.
        */

        //long start = System.nanoTime();
	    int totalWeight = mst(adj);
        //long end = System.nanoTime();
	    System.out.printf("Graph %d: Total weight of MST is %d\n",graphNum,totalWeight);
        //long time = (end-start)/1000000;
        //System.out.printf("Time: %f ms\n", (float)time);
	}
    }


}

/*
* Edge class taken from slides on Nishant Mehta's website:
* http://web.uvic.ca/~nmehta/csc226_fall2019/
* from slides on Minimum Spanning trees, code on slide 26:
* http://web.uvic.ca/~nmehta/csc226_fall2019/lecture7_8_9.pdf
* Credit to textbook: Algorithms, Robert Sedgewick, Kevin Wayne
*/

class Edge implements Comparable<Edge>{
    private final int v,w;
    private final double weight;

    public Edge(int v, int w, double weight){
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int either(){
        return v;
    }

    public int other(int vertex) {
        if (vertex == v){
            return w;
        } else {
            return v;
        }
    }

    public double weight(){
        return weight;
    }

    public int compareTo(Edge that){
        if (this.weight < that.weight) {
            return -1;
        } else if (this.weight > that.weight) {
            return 1;
        } else {
            return 0;
        }
    }
}

/*
* QuickFindUF class taken from slides on Nishant Mehta's website:
* http://web.uvic.ca/~nmehta/csc226_fall2019/
* from slides on Union Find, code from slide 24:
* http://web.uvic.ca/~nmehta/csc226_fall2019/lecture10.pdf
* Credit to textbook: Algorithms, Robert Sedgewick, Kevin Wayne
*/

class QuickFindUF{

    private int[] id;
    private int[] sz;

    public QuickFindUF(int N){
        id = new int[N];
        sz = new int[N];

        for (int i = 0; i < N; i++){
            id[i] = i;
            sz[i] = 1;
        }
    }

    /*
    * With Path Compression, slide 40
    */
    public int find(int i){
        while (i != id[i]){
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    /*
    * Weighted Quick Union, slide 31
    */
    public void union(int p, int q){
        int i = find(p);
        int j = find(q);
        if (i == j){
            return;
        }
        if (sz[i] < sz[j]){
            id[i] = j;
            sz[j] += sz[i];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }

    /*
    * Connected returns true if the vertices share a root node.
    */
    public boolean connected(int v, int w){
        if (find(v) == find(w)){
            return true;
        }
        return false;
    }

}
