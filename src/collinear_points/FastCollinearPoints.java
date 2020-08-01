package collinear_points;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

	private ArrayList<LineSegment> lineSegments = new ArrayList<>();

	// finds all line segments containing 4 or more points
	public FastCollinearPoints(Point[] points) {

		// Checks for corner cases. If none is found, returns a sorted copy of points
		// to maintain integrity of original points array.
		Point[] ptsCopy = checkForCornerCases(points);

		for(int p = 0; p < ptsCopy.length-3; p++) {

			// It's important to sort the array at the beginning of each new iteration.
			// Since they will be later sorted according to slopes, we need to put them back
			// in order, so that no point is left out and no point is checked more than once.
			Arrays.sort(ptsCopy);

			Arrays.sort(ptsCopy, ptsCopy[p].slopeOrder());

			// The point at ptsCopy[0] is the reference, so we start at ptsCopy[1] and use
			// another pointer to go forth and check if the slope is still equal to ptsCopy[first]
			// or not. If not update first to be at the 'combo breaker'.
			Point origin = ptsCopy[0];
			for(int first = 1, last = 2; last < ptsCopy.length; last++) {

				while(last < ptsCopy.length && origin.slopeTo(ptsCopy[first]) == origin.slopeTo(ptsCopy[last])) {
					last++;
				}
				// In order to know if the segment about to be included is unique, it is necessary
				// to verify that origin < comboStart, since the points array is now sorted
				// according to slopes and not point coordinates. Otherwise, comboStart may already
				// have been considered in a previous iteration of the outer loop.
				if(last - first > 2 && origin.compareTo(ptsCopy[first]) < 0) {
					lineSegments.add(new LineSegment(origin, ptsCopy[last-1]));
				}
				first = last;
			}
		}
	}

	// The number of line segments.
	public int numberOfSegments() {
		return lineSegments.size();
	}

	// The line segments.
	public LineSegment[] segments() {
		return lineSegments.toArray(new LineSegment[lineSegments.size()]);
	}

	// Check corner cases.
	private Point[] checkForCornerCases(Point[] points) {
		if(points == null) {
			throw new IllegalArgumentException("'points' arrays is null.");
		}
		if(!Arrays.stream(points).allMatch(pt -> pt != null)) {
			throw new IllegalArgumentException("'points' arrays contains null elements.");
		}
		Point[] ptsCopy = points.clone();
		Arrays.sort(ptsCopy);
		for(int i = 0; i < points.length-1; i++) {
			if(ptsCopy[i].compareTo(ptsCopy[i+1]) == 0) {
				throw new IllegalArgumentException("'points' array contains duplicate elements.");
			}
		}
		return ptsCopy;
	}
}





