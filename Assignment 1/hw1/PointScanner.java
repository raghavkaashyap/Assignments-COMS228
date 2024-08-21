package edu.iastate.cs228.hw1;

/**
 * 
 * @author raghavk
 *
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

/**
 * 
 * This class sorts all the points in an array of 2D points to determine a
 * reference point whose x and y coordinates are respectively the medians of the
 * x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for
 * comparison.
 *
 */
public class PointScanner {
	private Point[] points; // array of points

	private Point medianCoordinatePoint; // point whose x and y coordinates are respectively the medians of
											// the x coordinates and y coordinates of those points in the array
											// points[].
	private Algorithm sortingAlgorithm; // sorting algorithm

	protected long scanTime; // execution time in nanoseconds.

	/**
	 * variable to hold the time when sorting algorithm begins executing
	 */
	private long startTime;
	/**
	 * variable to hold the time when sorting algorithm ends executing
	 */
	private long endTime;

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[].
	 * 
	 * @param pts input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts == null || pts.length == 0) {
			throw new IllegalArgumentException();
		}

		points = new Point[pts.length];

		for (int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}

		sortingAlgorithm = algo;
	}

	/**
	 * This constructor reads points from a file.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException if the input file contains an odd number of
	 *                                integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException {
		sortingAlgorithm = algo;
		File f = new File(inputFileName);
		Scanner s = new Scanner(f);
		int count = 0;
		int x;
		int y;

		try {
			while (s.hasNextInt()) {
				s.nextInt();
				count++;
			}
			s.close();

			if (count % 2 != 0) {
				throw new InputMismatchException();
			}

			points = new Point[count / 2];
			s = new Scanner(f);
			for (int i = 0; i < points.length; i++) {
				x = s.nextInt();
				y = s.nextInt();
				points[i] = new Point(x, y);
			}
		} finally {
			s.close();
		}
	}

	/**
	 * Carry out two rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b) Sort
	 * points[] again by the y-coordinate to get the median y-coordinate. c)
	 * Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter,
	 * InsertionSorter, MergeSorter, or QuickSorter to carry out sorting.
	 * 
	 * @param
	 * @return
	 */
	public void scan() {
		// create an object to be referenced by aSorter according to sortingAlgorithm.
		// for each of the two
		// rounds of sorting, have aSorter do the following:
		//
		// a) call setComparator() with an argument 0 or 1.
		//
		// b) call sort().
		//
		// c) use a new Point object to store the coordinates of the
		// medianCoordinatePoint
		//
		// d) set the medianCoordinatePoint reference to the object with the correct
		// coordinates.
		//
		// e) sum up the times spent on the two sorting rounds and set the instance
		// variable scanTime.

		AbstractSorter aSorter;

		if (sortingAlgorithm.equals(Algorithm.SelectionSort)) {
			aSorter = new SelectionSorter(points);
		} else if (sortingAlgorithm.equals(Algorithm.InsertionSort)) {
			aSorter = new InsertionSorter(points);
		} else if (sortingAlgorithm.equals(Algorithm.MergeSort)) {
			aSorter = new MergeSorter(points);
		} else {
			aSorter = new QuickSorter(points);
		}

		aSorter.setComparator(0);
		startTime = System.nanoTime();
		aSorter.sort();
		endTime = System.nanoTime();
		scanTime = endTime - startTime;
		int x = aSorter.getMedian().getX();

		aSorter.setComparator(1);
		startTime = System.nanoTime();
		aSorter.sort();
		endTime = System.nanoTime();
		scanTime += (endTime - startTime);
		int y = aSorter.getMedian().getY();

		medianCoordinatePoint = new Point(x, y);

	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description.
	 */
	public String stats() {
		String output = String.format("%-15s%-10d%-15d", sortingAlgorithm.toString(), points.length, scanTime);
		return output;
	}

	/**
	 * Write MCP after a call to scan(), in the format "MCP: (x, y)" The x and y
	 * coordinates of the point are displayed on the same line with exactly one
	 * blank space in between.
	 */
	@Override
	public String toString() {
		String mcp = "MCP: (" + medianCoordinatePoint.getX() + ", " + medianCoordinatePoint.getY() + ")";
		return mcp;
	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException {
		try {
			PrintWriter p = new PrintWriter("outputFile.txt");
			for (int i = 0; i < points.length; i++) {
				p.println(points[i].toString());
			}
			p.close();
		} finally {
		}
	}

}
