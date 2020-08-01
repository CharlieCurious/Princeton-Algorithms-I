package kdtrees;

import java.util.ArrayList;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

	private TreeSet<Point2D> set;


	// Construct an empty set of points. 
	public PointSET() {
		set = new TreeSet<Point2D>();
	}


	// Is the set empty?
	public boolean isEmpty() {
		return set.isEmpty();
	}


	// Number of points in the set.
	public int size() {
		return  set.size(); 
	}


	// Add the point to the set (if it is not already in the set).
	public void insert(Point2D p) {
		if(p == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		set.add(p);
	}


	// Does the set contain point p? 
	public boolean contains(Point2D p) {
		if(p == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return set.contains(p);
	}


	// Draw all points to standard draw
	public void draw() {
		set.forEach(p -> p.draw());
	}


	// All points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if(rect == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		ArrayList<Point2D> it = new ArrayList<>();
		set.forEach(p -> {
			if(rect.contains(p)) {
				it.add(p);
			}
		});
		return it;
	}


	// A nearest neighbor in the set to point p; null if the set is empty.
	public Point2D nearest(Point2D p) {
		if(p == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if(!isEmpty()) {
			Point2D champion = set.first();
			double minDistance = champion.distanceSquaredTo(p);
			for(Point2D point : set) {
				if(point.distanceSquaredTo(p) < minDistance) {
					champion = point;
					minDistance = champion.distanceSquaredTo(p);
				}
			}
			return champion;
		}
		return null;
	}


	// Unit testing of the methods (optional).
	public static void main(String[] args) {                  
		PointSET set = new PointSET();
		set.insert(new Point2D(0.23, 0.57));
		set.insert(new Point2D(0.10, 0.57));
		set.insert(new Point2D(0.53, 0.8));
		set.draw();
		System.out.println(set.nearest(new Point2D(0.24, 0.58)).toString());

	}

}
