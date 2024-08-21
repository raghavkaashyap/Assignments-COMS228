package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;
import java.util.InputMismatchException;

/**
 *  
 * @author raghavk
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.
 *
 */

public class MergeSorter extends AbstractSorter {

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts input array of integers
	 */
	public MergeSorter(Point[] pts) {
		super(pts);
		algorithm = "mergesort";
	}

	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		mergeSortRec(points);
	}

	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of
	 * points. One way is to make copies of the two halves of pts[], recursively
	 * call mergeSort on them, and merge the two sorted subarrays into pts[].
	 * 
	 * @param pts point array
	 */
	private void mergeSortRec(Point[] pts) {
		if (pts.length <= 1) {
			return;
		}

		int mid = pts.length / 2;
		Point[] leftPoints = new Point[mid];
		Point[] rightPoints = new Point[pts.length - mid];
		for (int i = 0; i < mid; i++) {
			leftPoints[i] = pts[i];
		}
		for (int j = mid; j < pts.length; j++) {
			rightPoints[j - mid] = pts[j];
		}

		mergeSortRec(leftPoints);
		mergeSortRec(rightPoints);

		merge(pts, leftPoints, rightPoints);
	}

	/**
	 * helper method to merge two sorted arrays into one sorted array
	 * 
	 * @param pts
	 * @param left
	 * @param right
	 */
	private void merge(Point[] pts, Point[] left, Point[] right) {

		int i = 0;
		int j = 0;
		int k = 0;

		while (i < left.length && j < right.length) {
			if (pointComparator.compare(left[i], right[j]) <= 0) {
				pts[k] = left[i];
				i++;
			} else {
				pts[k] = right[j];
				j++;
			}
			k++;
		}

		while (i < left.length) {
			pts[k] = left[i];
			k++;
			i++;
		}

		while (j < right.length) {
			pts[k] = right[j];
			k++;
			j++;
		}
	}

}
