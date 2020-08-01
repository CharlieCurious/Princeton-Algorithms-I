package collinear_points;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

	private ArrayList<LineSegment> lineSegments = new ArrayList<>();


	public BruteCollinearPoints(Point[] points) {

		Point[] ptsCopy = checkForCornerCases(points);

		for(int i = 0; i < ptsCopy.length-3; i++)
			for(int j = i+1; j < ptsCopy.length-2; j++)
				for(int k = j+1; k < ptsCopy.length-1; k++)
					for(int z = k+1; z < ptsCopy.length; z++) {
						if(ptsCopy[i].slopeTo(ptsCopy[j]) == ptsCopy[i].slopeTo(ptsCopy[k]) &&
								ptsCopy[i].slopeTo(ptsCopy[j]) == ptsCopy[i].slopeTo(ptsCopy[z]))
							lineSegments.add(new LineSegment(ptsCopy[i], ptsCopy[z]));
					}
	}

	// Checks for corner cases.
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

	// The number of line segments.
	public int numberOfSegments() {
		return lineSegments.size();
	}

	// The line segments.
	public LineSegment[] segments() {
		return lineSegments.toArray(new LineSegment[lineSegments.size()]);
	}

}






















