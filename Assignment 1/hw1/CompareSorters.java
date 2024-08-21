package edu.iastate.cs228.hw1;

/**
 *  
 * @author raghavk
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

/**
 * this class contains the main method and compares the run time for different
 * sorters.
 */
public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Use them as coordinates to construct points. Scan these points with
	 * respect to their median coordinate point four times, each time using a
	 * different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {

		// Conducts multiple rounds of comparison of four sorting algorithms. Within
		// each round,
		// set up scanning as follows:
		//
		// a) If asked to scan random points, calls generateRandomPoints() to initialize
		// an array
		// of random points.
		//
		// b) Reassigns to the array scanners[] (declared below) the references to four
		// new
		// PointScanner objects, which are created using four different values
		// of the Algorithm type: SelectionSort, InsertionSort, MergeSort and QuickSort.
		//
		//

		System.out.println("Performance of Four Sorting Algorithms in Point Scanning");
		System.out.println();
		System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
		Scanner s = new Scanner(System.in);
		int key = 0;
		int trialCount = 1;
		Point[] points = null;
		PointScanner[] scanners = new PointScanner[4];

		// For each input of points, do the following.
		//
		// a) Initialize the array scanners[].
		//
		// b) Iterate through the array scanners[], and have every scanner call the
		// scan()
		// method in the PointScanner class.
		//
		// c) After all four scans are done for the input, print out the statistics
		// table from
		// section 2.
		//
		// A sample scenario is given in Section 2 of the project description.

		while (key != 3) {

			System.out.print("Trial " + trialCount + ": ");
			key = s.nextInt();
			trialCount++;

			if (key == 1) {

				System.out.print("Enter number of random points: ");
				Random rand = new Random();
				points = generateRandomPoints(s.nextInt(), rand);
				System.out.println();
				scanners[0] = new PointScanner(points, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(points, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(points, Algorithm.MergeSort);
				scanners[3] = new PointScanner(points, Algorithm.QuickSort);

			}

			else if (key == 2) {

				System.out.println("Points from a file");
				System.out.print("File name: ");
				s.nextLine();
				String file = s.nextLine().trim();
				scanners[0] = new PointScanner(file, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(file, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(file, Algorithm.MergeSort);
				scanners[3] = new PointScanner(file, Algorithm.QuickSort);
				System.out.println();

			}

			else {
				break;
			}

			System.out.printf("%-15s%-10s%-15s\n", "Algorithm", "Size", "Time (ns)");
			System.out.println("----------------------------------");

			for (int i = 0; i < scanners.length; i++) {
				scanners[i].scan();
				System.out.println(scanners[i].stats());
			}

			System.out.println("----------------------------------");
			System.out.println();
		}
		s.close();

	}

	/**
	 * This method generates a given number of random points. The coordinates of
	 * these points are pseudo-random numbers within the range [-50,50] � [-50,50].
	 * Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts number of points
	 * @param rand   Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		Point[] pts = new Point[numPts];

		for (int i = 0; i < numPts; i++) {
			pts[i] = new Point(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
		}

		return pts;
	}
}
