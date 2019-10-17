/**
 *
 * Last Modified Oct 3, 2019
 * CSC 226 - Fall 2019
 * Katherine Jacobs
 * V00783178
 */
import java.util.*;
import java.io.*;
import java.lang.Math;

/*
* Resources Used for this assignment:
* MainSolution.java on connex, see notes below for the exact code Uused
* Lecture slides "selecting the kth smallest element" on Nishant's course site:
* http://web.uvic.ca/~nmehta/csc226_fall2019/
*/


public class Quickselect {

    /*
    * Quickselect calls quickSelectRecurs to utilize the left and right boundaries
    * of the array recursively.
    * input: int array and k value
    * returns the kth smallest element
    */

    public static int Quickselect(int[] A, int k) {
        int kthElement = quickSelectRecurs(A, 0, A.length-1, k-1);
        return A[kthElement];
    }

    /*
    * quickSelectRecurs uses the medianofMedians function to find a pivot, and
    * recursively narrows down the array to find the kth smallest element
    * Input: int array, left and right boundaries, k value
    * returns index of kth value
    * lines 36-52 were taken from MainSolution.java on connex
    */
    public static int quickSelectRecurs(int[] A, int left, int right, int k){
        int[] copyArray = new int[right-left+1];
        for (int i=left, j=0; i<=right; i++, j++) {
            copyArray[j] = A[i];
        }
        int pivot = medianofMedians(copyArray);
        int index = 0;

        for (int i = 0; i < A.length; i++){
            if (A[i] == pivot){
                index = i;
            }
        }

        int p = partition(A, left, right, index);

        // If the index of partition is exactly k, we are done
        if (p == k) {
            return p;
        }
        // else, we should continue looking for kth element, by either going left or right

        // recur left if k is smaller
        else if (p > k) {
            return quickSelectRecurs(A, left, p - 1, k);
        }

        // recur right if k is larger
        else {
            return quickSelectRecurs(A, p + 1, right, k);
        }

    }

    /*
    * This partition method was taken from MainSolution.java on connex
    */

    public static int partition(int arr[], int p, int right, int index) {
        swap(arr, right, index);
        int x = arr[right];
        for (int j = p; j < right; j++) {
            if (arr[j] <= x) {
                swap(arr, p, j);
                p++;
            }
        }
        swap(arr, p, right);
        return p;
    }

    /*
    * This swap function was taken from MainSolution.java on connex
    */

    public static void swap(int arr[], int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /*
    * Function to sort an array of size at most 9, and find the median
    */
    public static int findMedian(int[] subArray){
        int medIndex = subArray.length/2;
        Arrays.sort(subArray);
        return subArray[medIndex];
    }

    /*
    * Recursive function that finds the median of medians
    *
    */
    public static int medianofMedians(int[] A){
        int len = A.length;
        if (len <= 9){
            int med = findMedian(A);
            return med;
        }
        int arrsize = (int)Math.ceil((float)len/9.0);
        int[] medians = new int[arrsize];
        int[] subArray;
        //int to keep track of position in original array
        int i = 0;
        //int to keep track of position in median array
        int j = 0;


        while (i < len){
            int count = 0;
            // if there are fewer than 9 elements left
            if (len - i < 9){
                //create subarray with the number of elements left
                subArray = new int[len - i];
            } else {
                // else create subarray of size 9
                subArray = new int[9];
            }
            // adding elements to a smaller array of size 9
            while (count < subArray.length){
                subArray[count] = A[i];
                count++;
                i++;
            }
            medians[j] = findMedian(subArray);
            j++;
        }
        return medianofMedians(medians);
    }

    public static void main(String[] args) {
        Scanner s;
        int[] array;
        int k;
        if(args.length > 0) {
	    try{
		s = new Scanner(new File(args[0]));
		int n = s.nextInt();
		array = new int[n];
		for(int i = 0; i < n; i++){
		    array[i] = s.nextInt();
		}
	    } catch(java.io.FileNotFoundException e) {
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n", args[0]);
        }
	else {
	    s = new Scanner(System.in);
	    System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
	    int temp = s.nextInt();
	    ArrayList<Integer> a = new ArrayList<Integer>();
	    while(temp >= 0) {
		a.add(temp);
		temp=s.nextInt();
	    }
	    array = new int[a.size()];
	    for(int i = 0; i < a.size(); i++) {
		array[i]=a.get(i);
	    }

	    System.out.println("Enter k");
        }
        k = s.nextInt();
        System.out.println("The " + k + "th smallest number is the list is "
			   + Quickselect(array,k));
    }
}
