/*
* Katherine Jacobs
* CSC 226 Programming Assignment #3
* Last Modified: Nov 6 2019
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;

public class ShortestPaths{

    public static int n; // number of vertices
    public static int[] d; // distances
    public static int[] pi; // minimum paths

    /* ShortestPaths(adj)
       Given an adjacency list for an undirected, weighted graph, calculates and stores the
       shortest paths to all the vertices from the source vertex.

       ** Dijkstra's Algorithm taken from slides on Nishant Mehta's website:
       ** http://web.uvic.ca/~nmehta/csc226_fall2019/
       ** From slides on Shortest Paths II slides 41-43
       ** http://web.uvic.ca/~nmehta/csc226_fall2019/lecture11_12_v3.pdf

    */
    static void ShortestPaths(int[][][] adj, int source){
        n = adj.length;
        d = new int[n];
        pi = new int[n];
        //visited = new boolean[n];
        IndexMinPQ pq = new IndexMinPQ(adj.length);
        int infinity = (n*n)*1001;

        // initializing int[]'s
        for (int i = 0; i < n; i++){
            d[i] = infinity;
            pi[i] = 0;
        }
        // distance of source node = 0
        d[source] = 0;

        //initializiing IndexMinPQ
        for (int i = 0; i < n; i++){
            pq.insert(i, d[i]);
        }

        // Dijkstra's algorithm from slides
        while (!pq.isEmpty()){
            int u = pq.minIndex();
            int distance = pq.delMin();

            for (int j = 0; j < adj[u].length; j++){
                int v = adj[u][j][0];
                int edgeWeight = adj[u][j][1];
                if (d[u] + edgeWeight < d[v]){
                    d[v] = d[u] + edgeWeight;
                    pi[v] = u;
                    pq.changeKey(v, d[v]);
                }
            }
        } // end of while
    } // end of ShortestPaths

    /*
    * Prints all the paths from the source node to each other node in the graph,
    * and prints the distance to each node.
    */
    static void PrintPaths(int source){
        Stack<Integer> paths = new Stack<Integer>();
        n = pi.length;

        for (int j = 0; j < n; j++){
            int dist = d[j];
            int curr = j;
            while (source != pi[curr]){
                paths.push(curr);
                curr = pi[curr];
            }
            paths.push(curr);


            System.out.print("The path from 0 to " + j + " is: " + source);
            while(!paths.empty()){
                int m = paths.pop();
                if (m != 0){
                    System.out.print(" --> " + m);
                }
            }
            System.out.print(" and the total distance is : " + dist);
            System.out.println("");
        }

    } // end of PrintPaths

    public static void main(String[] args) throws FileNotFoundException{
	Scanner s;
	if (args.length > 0){
	    //If a file argument was provided on the command line, read from the file
	    try{
		s = new Scanner(new File(args[0]));
	    } catch(java.io.FileNotFoundException e){
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    //System.out.printf("Reading input values from %s.\n",args[0]);
	}
	else{
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}

	int graphNum = 0;
	double totalTimeSeconds = 0;

	//Read graphs until EOF is encountered (or an error occurs)
	while(true){
	    graphNum++;
	    if(graphNum != 1 && !s.hasNextInt())
		break;
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();
	    int[][][] adj = new int[n][][];

	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++){
		LinkedList<int[]> edgeList = new LinkedList<int[]>();
		for (int j = 0; j < n && s.hasNextInt(); j++){
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
	    if (valuesRead < n * n){
		System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		break;
	    }

        /*
	    // output the adjacency list representation of the graph
	    for(int i = 0; i < n; i++) {
	    	System.out.print(i + ": ");
	    	for(int j = 0; j < adj[i].length; j++) {
	    	    System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
	    	}
	    	System.out.print("\n");
	    }
        */

	    long startTime = System.currentTimeMillis();

	    ShortestPaths(adj, 0);
	    PrintPaths(0);
	    long endTime = System.currentTimeMillis();
	    totalTimeSeconds += (endTime-startTime)/1000.0;

	    //System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
	}
	graphNum--;
    System.out.print("Processed 2 graphs.");
	//System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
    }
}
