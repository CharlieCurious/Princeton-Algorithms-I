package eightpuzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

	private Node lastNode = null;

	private class Node implements Comparable<Node> {

		private final Board board;
		private final int moves;
		private final Node previous;
		private final int priority;
		private final int manhattan;

		Node(Board board, Node previous) {
			this.board = board;
			this.previous = previous;
			this.manhattan = board.manhattan();

			if(previous == null) {
				moves = 0;
			}else {
				moves = previous.moves + 1;
			}
			this.priority = manhattan + moves;
		}

		@Override
		public int compareTo(Node node) {
			if(priority == node.priority) {
				return manhattan - node.manhattan;
			}
			return priority - node.priority;
		}
	}


	// Find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		if(initial == null) {
			throw new IllegalArgumentException("Null argument passed to the constructor");
		}
		MinPQ<Node> pq = new MinPQ<>();
		MinPQ<Node> twinPQ = new MinPQ<>();
		pq.insert(new Node(initial, null));
		twinPQ.insert(new Node(initial.twin(), null));

		// The strategy consists in applying A* to both PQs. Eventually
		// either will reach the goal, and the other wont.
		// The one that does reach, is solvable, the other isn't.
		boolean pqIsSolvable = false;
		boolean twinPQIsSolvable = false;

		// While neither has reached a solution.
		while(!pqIsSolvable && !twinPQIsSolvable) {
			lastNode = expand(pq);
			pqIsSolvable = (lastNode != null);
			twinPQIsSolvable = (expand(twinPQ) != null);
		}
	}


	// This function represents a single step in the A* algorithm.
	private Node expand(MinPQ<Node> pq) {
		if(pq.isEmpty()) {
			return null;
		}
		Node current = pq.delMin();
		if(current.board.isGoal()) {
			return current;
		}
		for(Board b: current.board.neighbors()) {
			if(current.previous == null || !b.equals(current.previous.board)) {
				pq.insert(new Node(b, current));
			}
		}
		return null;
	}


	// Is the initial board solvable? (see below)
	public boolean isSolvable() {
		return lastNode != null;
	}


	// Min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if(lastNode != null) {
			return lastNode.moves;
		}
		return -1;
	}


	// Sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution(){
		if(!isSolvable()) {
			return null;
		}
		Stack<Board> boards = new Stack<>();
		for(Node current = lastNode; current != null; current = current.previous) {
			boards.push(current.board);
		}
		return boards;
	}


	// Test client. 
	public static void main(String[] args) {
		int[][] matrix = new int[3][];
		int[] row0 = {4, 1, 3};
		int[] row1 = {2, 0, 5};
		int[] row2 = {7, 8, 6};
		matrix[0] = row0;
		matrix[1] = row1;
		matrix[2] = row2;
		Board b = new Board(matrix);
		System.out.println("Start: \n" + b.toString());

		Solver solver = new Solver(b);
		Iterable<Board> it = solver.solution();

		for(Board bd: it) {
			System.out.println("-----------\n Step \n");
			System.out.println(bd.toString());
			System.out.println("___________");
		}
	}

}

