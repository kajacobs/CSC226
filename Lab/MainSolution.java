import java.io.File;
import java.io.FileNotFoundException;
//import java.util.Scanner;
import java.util.*;

public class MainSolution {

    public static void main(String[] args) {

        System.out.println("Randomized Quickselect Solution");
        RandomizedQuickSelectSolution instance = new RandomizedQuickSelectSolution();

        // load the benchmark here
        // mydata_2: 95061
        // mydata_3: 95087
        // mydata_4: 95184
        final int SIZE = 95184;
        String path = ""; // ENTER PATH HERE

        File file = new File(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int[] benchmark = new int[SIZE];
        int i = 0;
        while (scanner.hasNextInt()) {
            benchmark[i] = scanner.nextInt();
            i++;
        }

        long startTime = System.currentTimeMillis();
        final int k = 100;
        int result = instance.quickSelect(benchmark, 0, benchmark.length - 1, k-1);
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);

        System.out.println("k-th smallest value is: " + benchmark[result]);
        System.out.println("runtime duration in milliseconds: " + duration);

        System.out.println("node size history: " + instance.sizeHistory.toString());

        int sum = 0;
        Iterator it = instance.sizeHistory.iterator();
        while(it.hasNext()){
            sum += (int) (it.next());
        }
        System.out.println("total number of comparisons: " + sum);
    }

}

class RandomizedQuickSelectSolution {

    LinkedList<Integer> sizeHistory;

    public RandomizedQuickSelectSolution() {
        sizeHistory = new LinkedList<Integer>();
    }


    /**
     * Swaps arr[i] with array[j]
     *
     * @param arr input array
     * @param i   index of an element to be swapped with arr[j]
     * @param j   index of an element to be swapped with arr[i]
     */
    public void swap(int arr[], int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Generates a discrete uniform random integer in [min, max]
     *
     * @param min minimum value of outcome
     * @param max maximum value of outcome
     * @return a random integer in [min, max]
     */
    public int randomGenerator(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1) + min);
    }


    /**
     * Partitions the array in two, with all the elements on the left side of arr[p] being smaller than arr[p], and
     * all the elements on the right being greater than it.
     *
     * @param arr        input array
     * @param p          index of where the array starts
     * @param right      index of where the array ends
     * @param pivotIndex index of the pivot
     * @return index of the random pivot after performing the procedure
     */

    public int partition(int arr[], int p, int right, int pivotIndex) {
        swap(arr, right, pivotIndex);
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


    /**
     * Quickselect algorithm on an array input, searching over elements {arr[left], arr[left+1], ..., arr[right]}
     *
     * @param arr   input array
     * @param left  index of where the array starts
     * @param right index of where the array ends
     * @param k     value of k to look for the k-th order statistic
     * @return kth order statistic of array
     */
    public int quickSelect(int arr[], int left, int right, int k) {
        sizeHistory.add(right - left + 1);

        // generate a random pivot in the range of left and right
        int pivotIndex = randomGenerator(left, right);

        // Uncomment to use a deterministic pivot instead of random
//        pivotIndex = left;

        // partition the array using the randomPivotIndex as k
        int p = partition(arr, left, right, pivotIndex);

        // If the index of partition is exactly k, we are done
        if (p == k) {
            return p;
        }
        // else, we should continue looking for kth element, by either going left or right

        // recur left if k is smaller
        else if (p > k) {
            return quickSelect(arr, left, p - 1, k);
        }

        // recur right if k is larger
        else {
            return quickSelect(arr, p + 1, right, k);
        }
    }


}