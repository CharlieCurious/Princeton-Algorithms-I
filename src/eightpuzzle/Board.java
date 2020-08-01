package eightpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

	private final int[][] tiles; // The Board.
	private final int N;        // Lenght of the side of the Board.
	private final int manhattan;
	private final int hamming;

	// Create a board from an n-by-n array of tiles,
	// where tiles[row][col] = tile at (row, col)
	public Board(int[][] tiles) {
		this.tiles = tiles;
		this.N = tiles.length;

		// Ensure immutability.
		this.manhattan = calculateManhattan();
		this.hamming = calculateHamming();
	}	


	// String representation of this board.
	public String toString() {
		String representation = String.valueOf(tiles.length) + "\n";
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				representation += " " + tiles[i][j];
			}
			representation += "\n";
		}
		return representation;
	}


	// Board dimension N.
	public int dimension() {
		return N;
	}


	// Number of tiles out of place.
	public int hamming() {
		return hamming;
	}


	private int calculateHamming() {
		int hamming = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {

				// correctSpot gives the number correspondent to the tile
				// currently in the loop. For example, if N = 3, i = 1, j = 2 yields 6,
				// which is correct: the middle tile in the rightmost column.
				// The exception is the bottom-right corner, which is not 9, but 0.
				int correctTile = (j+1) + N*i;
				if(tiles[i][j] != correctTile && tiles[i][j] != 0) {
					hamming++;
				}
			}
		}
		return hamming;
	}


	// Sum of Manhattan distances between tiles and goal.
	public int manhattan() {
		return manhattan;
	}


	private int calculateManhattan() {
		int manhattan = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {

				int currentTile = tiles[i][j];

				if(currentTile != 0) {

					// Use modular arithmetic to calculate correct row and collumn.
					// Subtract 1 to the tile, because we're starting at 0 through N-1.
					int correctRow = (currentTile-1) / N;
					int correctCol = (currentTile-1) % N;

					// Check if current position in the loop correspondes to correct position of the tile.
					if(correctRow != i || correctCol != j) {
						manhattan += Math.abs(correctRow - i) + Math.abs(correctCol - j);
					}
				}
			}
		}
		return manhattan;
	}


	// Is this board the goal board?
	// N² worst case, O(1) best and N on average.
	public boolean isGoal() {
		return hamming == 0;
	}


	// Does this board equal y?
	@Override
	public boolean equals(Object y) {
		if(y == this) {
			return true;
		}
		if(y == null) {
			return false;
		}
		if(!(y instanceof Board)) {
			return false;
		}
		if(((Board) y).N != N) {
			return false;
		}
		for(int array = 0; array < N; array++) {
			if(!Arrays.equals(this.tiles[array], ((Board) y).tiles[array])) {
				return false;
			}
		}
		return true;
	}


	// Finds the empty tile (0).
	private int[] findEmptyTile() {
		int[] rowAndCol = new int[2]; 
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(tiles[i][j] == 0) {
					rowAndCol[0] = i;
					rowAndCol[1] = j;
				}
			}
		}
		return rowAndCol;
	}


	// All neighboring boards.
	public Iterable<Board> neighbors(){
		List<Board> neighbors = new ArrayList<>();
		int[] directions = {0, 0, 1, -1};

		// We start by checking movement in the columns.
		int rowMove = 0;
		int colMove = 2;
		// emptyTile[0] = row, emptyTile[1] = column.
		int[] emptyTile = findEmptyTile();

		// There are only for directions of possible movement.
		// We can use modular  arithmetic to go through the directions array,
		// such that when colMove = 4, it turns to 0, whereas rowMove will be 1.
		for(int i = 0; i < 4; i++) {
			int checkRow = emptyTile[0] + directions[rowMove];
			int checkCol = emptyTile[1] + directions[colMove];

			if(isWithinBounds(checkRow, checkCol)) {
				int[][] newNeighbor = copyBoard();
				exchange(newNeighbor, emptyTile[0], emptyTile[1], checkRow, checkCol);
				neighbors.add(new Board(newNeighbor));
			}

			// Use modular arithmatic to go through direcitons array.
			rowMove = (rowMove + 1) % 4;
			colMove = (colMove + 1) % 4;
		}
		return neighbors;
	}


	private int[][] copyBoard() {
		int[][] copy = new int[N][N];
		for(int i = 0; i < N; i++) {
			copy[i] = Arrays.copyOf(tiles[i], N);
		}
		return copy;
	}


	// A board that is obtained by exchanging any pair of tiles.
	// The tiles must to be exchanged must be adjacent.
	public Board twin() {

		int[][] newBoard = copyBoard();

		for(int i = 0; i < N; i++){
			for(int j = 0;j < N-1; j++){
				if(newBoard[i][j] > 0 && newBoard[i][j+1] > 0) {
					exchange(newBoard, i, j, i, j+1);
					return new Board(newBoard);
				}
			}
		}
		throw new IllegalStateException("Board contains more than on empty spot.");
	}


	// Checks wheter a given spot exists in a given board.
	private boolean isWithinBounds(int row, int col) {
		return (row >= 0 && row < N && col >= 0 && col < N);
	}


	private void exchange(int[][] board, int xRow, int xCol, int yRow, int yCol) {
		int t = board[xRow][xCol];
		board[xRow][xCol] = board[yRow][yCol];
		board[yRow][yCol] = t;
	}


	// Unit testing (not graded).
	public static void main(String[] args) {
		int[][] matrix = new int[3][];
		int[] row0 = {1, 2, 3};
		int[] row1 = {4, 5, 0};
		int[] row2 = {7, 8, 6};
		matrix[0] = row0;
		matrix[1] = row1;
		matrix[2] = row2;
		Board b = new Board(matrix);
		System.out.println(b.toString());

		System.out.println("Manhattan: " + b.manhattan());
		System.out.println("Is Goal: " + b.isGoal());
		//Board t = b.twin();
		//System.out.println("twin: " + t.toString());

		Iterable<Board> it = b.neighbors();
		System.out.println("Neighbors: ");
		for(Board n: it)
			System.out.println("___________\n" + n.toString() + "_______________\n");
	}

}

