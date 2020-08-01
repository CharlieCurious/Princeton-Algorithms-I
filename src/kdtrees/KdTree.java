package kdtrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	private Node root;
	private int size;
	private class Node {

		Node left;
		Node right;
		final RectHV rect;
		final Point2D point;
		final double x;
		final double y;

		Node(Point2D point, RectHV rect) {
			this.point = point;
			this.rect = rect;
			this.x = point.x();
			this.y = point.y();
		}

		double comparePoint(Point2D that, boolean isVertical) {
			if(isVertical) {
				return that.x() - x;
			}
			return that.y() - y;
		}
	}


	// Is the set empty?
	public boolean isEmpty() {
		return root == null;
	}


	// Number of points in the set.
	public int size() {
		return size;
	}


	// Add the point to the set (if it is not already in the set).
	public void insert(Point2D p) {
		if(p == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		root = insert(root, p, true, new RectHV(0, 0, 1, 1));
	}


	// Recursive method similar to BST implemention in the book Algorithms.
	private Node insert(Node node, Point2D point, boolean isVertical, RectHV rect) {

		if(node == null) {
			size++;
			return new Node(point, rect);
		}

		double cmp = node.comparePoint(point, isVertical);

		if(cmp < 0 && isVertical) 
			if(node.left == null)
				node.left = insert(node.left, point, !isVertical, new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax()));
			else
				node.left = insert(node.left, point, !isVertical, node.left.rect);

		else if(cmp < 0 && !isVertical)
			if(node.left == null)
				node.left = insert(node.left, point, !isVertical, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y));
			else
				node.left = insert(node.left, point, !isVertical, node.left.rect);

		else if(cmp > 0 && isVertical)
			if(node.right == null)
				node.right = insert(node.right, point, !isVertical, new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax()));
			else
				node.right = insert(node.right, point, !isVertical, node.right.rect);

		else if(cmp > 0 && !isVertical)
			if(node.right == null)
				node.right = insert(node.right, point, !isVertical, new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax()));
			else
				node.right = insert(node.right, point, !isVertical, node.right.rect);

		else if(!node.point.equals(point))
			node.right = insert(node.right, point, !isVertical, rect);

		return node;
	}

	// Does the set contain point p? 
	public boolean contains(Point2D p) {
		if(p == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		return contains(root, p, true);
	}


	// Recursive method similar to BST implemention in the book Algorithms.
	private boolean contains(Node node, Point2D point, boolean isVertical) {
		if(node == null) {
			return false;
		}
		if(node.point.equals(point)) {
			return true;
		}

		double cmp = node.comparePoint(point, isVertical);

		if(cmp < 0) {
			return contains(node.left, point, !isVertical);
		}
		return contains(node.right, point, !isVertical);
	}

	// Draw all points to standard draw
	public void draw() {
		draw(root, true);
	}


	//Fixe. Rever com atenção.
	private void draw(Node n, boolean isVertical) {
		if(n == null) return;

		draw(n.left, !isVertical);

		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		n.point.draw();

		StdDraw.setPenRadius();
		if(isVertical) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(n.x, n.rect.ymin(), n.x, n.rect.ymax());
		}
		else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(n.rect.xmin(), n.y, n.rect.xmax(), n.y);
		}

		draw(n.right, !isVertical);
	}


	//Rever com atenção
	// All points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if(rect == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Stack<Point2D> points = new Stack<>();

		if(root == null) {
			return points;
		}

		Stack<Node> nodes = new Stack<>();
		nodes.push(root);

		// Verify Nodes one at a time. Push each children to stack to check after.
		while(!nodes.isEmpty()) {
			Node tmp = nodes.pop();

			if(rect.contains(tmp.point)) {
				points.push(tmp.point);
			}
			if(tmp.left != null && rect.intersects(tmp.left.rect)) {
				nodes.push(tmp.left);
			}
			if(tmp.right != null && rect.intersects(tmp.right.rect)) {
				nodes.push(tmp.right);
			}
		}

		return points;

	}


	// A nearest neighbor in the set to point p; null if the set is empty.
	public Point2D nearest(Point2D p) {
		if(p == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if(isEmpty()) {
			return null;
		}
		return nearest(root, p, root.point, true);
	}


	private Point2D nearest(Node node, Point2D point, Point2D champion, boolean isVertical) {

		// Base case.
		if(node == null) {
			return champion;
		}
		if(node.point.equals(point)) {
			return node.point; // The point overlaps with an existing point.
		}
		if(node.point.distanceSquaredTo(point) < champion.distanceSquaredTo(point)) {
			champion = node.point;
		}

		double cmp = node.comparePoint(point, isVertical);

		if(cmp < 0) {
			champion = nearest(node.left, point, champion, !isVertical);
			if(node.right != null 
					&& champion.distanceSquaredTo(point) >= node.right.rect.distanceSquaredTo(point)) {
				champion = nearest(node.right, point, champion, !isVertical);
			}
		}
		else {
			champion = nearest(node.right, point, champion, !isVertical);
			if(node.left != null 
					&& champion.distanceSquaredTo(point) >= node.left.rect.distanceSquaredTo(point)) {
				champion = nearest(node.left, point, champion, !isVertical);
			}
		}
		return champion;
	}


	// Unit testing of the methods (optional).
	public static void main(String[] args) {                  

		KdTree kd = new KdTree();
		kd.insert(new Point2D(0.3, 0.4));
		kd.insert(new Point2D(0.1, 0.1));
		kd.insert(new Point2D(0.1, 0.08));

	}

}

