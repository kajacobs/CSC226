/*
* Katherine Jacobs
* V00783178
* CSC 226 PA #4
* Last Modified Nov 26 2019
*/
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


/* RESOURCES USED FOR THIS ASSIGMENT
* Credit for the KMP algorithm and search method (lines 22-58)
* were taken from Algorithms, Robert Sedgewick, Kevin Wayne page 781.
* and from string search slides on Nishant Mehta's website
* (slides 35 and 60)
* web.uvic.ca/~nmehta/csc226_fall2019/lecture17_18.pdf
*/

public class KMP{
    private String pattern;
    private int[][] DFA;


    public KMP(String pattern){
        this.pattern = pattern;
        int length = pattern.length();
        int R = 256;
        DFA = new int[R][length];
        DFA[pattern.charAt(0)][0] = 1;

        for (int X = 0, j = 1; j < length; j++){
            //compute DFA
            for (int c = 0; c < R; c++){
                DFA[c][j] = DFA[c][X]; // copy mismatch cases
            } // end of inner for loop
            DFA[pattern.charAt(j)][j] = j+1; // set match case
            X = DFA[pattern.charAt(j)][X]; // update restart state
        } // end of outer for loop

    }

    public int search(String txt){
        // simulate operation of DFA on text
        int N = txt.length();
        int M = pattern.length();
        int i, k;

        for (i = 0, k = 0; i < N && k < M; i++){
            k = DFA[txt.charAt(i)][k];
        }
        if (k == M) {
            return (i - M); // found pattern
        } else {
            return N;
        }
    }


    public static void main(String[] args) throws FileNotFoundException{
	Scanner s;
	if (args.length > 0){
	    try{
		s = new Scanner(new File(args[0]));
	    }
	    catch(java.io.FileNotFoundException e){
		System.out.println("Unable to open "+args[0]+ ".");
		return;
	    }
	    System.out.println("Opened file "+args[0] + ".");
	    String text = "";
	    while(s.hasNext()){
		text += s.next();
	    }

	    for(int i = 1; i < args.length; i++){
		KMP k = new KMP(args[i]);
		int index = k.search(text);
		if(index >= text.length()){
		    System.out.println("The pattern \"" + args[i] + "\" was not found.");
		}
		else System.out.println("The string \"" + args[i] + "\" was found at index " + index + ".");
	    }
	}
	else{
	    System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");
	}
    }
}
