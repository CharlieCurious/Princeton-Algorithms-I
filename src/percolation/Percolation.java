package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private boolean[] grid;
	private int size;
	private int end;
	private int openSites = 0;
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF ufFull;

	// Creates n-by-n grid, with all sites initially blocked.
	public Percolation(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("Size < 1.");
		}
		this.grid = new boolean[n * n];
		this.size = n;
		this.end = n * n + 1;
		this.uf = new WeightedQuickUnionUF(n * n + 2);
		this.ufFull = new WeightedQuickUnionUF(n * n + 1);
	}

	private int getUfIndex(int row, int col) {
		validate(row, col);
		return size * (row - 1) + col;
	}

	private int getGridIndex(int row, int col) {
		validate(row, col);
		return size * (row - 1) + col - 1;
	}

	private void validate(int row, int col) {
		if (row < 1 || row > size || col < 1 || col > size) {
			throw new IllegalArgumentException("Such row, or collumn, does not exist.");
		}
	}

	private void connect(int row, int col, int toConnect) {
		if(isOpen(row, col)) {
			int index = getUfIndex(row, col);
			uf.union(index, toConnect);
			ufFull.union(index, toConnect);
		}
	}

	// Opens the site (row, col) if it is not open already.
	public void open(int row, int col) {
		validate(row, col);
		if (isOpen(row, col)) {
			return;
		}
		int index = getUfIndex(row, col);
		grid[getGridIndex(row, col)] = true;
		openSites++;
		if (row == 1) {
			connect(row, col , 0);
		}
		if (row == size) {
			uf.union(index, end);
		}
		if (row > 1) {
			connect(row - 1, col, index);
		}
		if (row < size) {
			connect(row + 1, col, index);
		}
		if (col > 1) {
			connect(row, col - 1, index);
		}
		if (col < size) {
			connect(row, col + 1, index);
		}

	}

	public boolean isOpen(int row, int col) {
		validate(row, col);
		int index = getGridIndex(row, col);
		return grid[index];
	}

	// is the site (row, col) full?
	public boolean isFull(int row, int col) {
		validate(row, col);
		int index = getUfIndex(row, col);
		return ufFull.find(index) == ufFull.find(0);
	}

	// Returns the number of open sites.
	public int numberOfOpenSites() {
		return openSites;
	}

	// Does the system percolate?
	public boolean percolates() {
		return uf.find(0) == uf.find(end);
	}

	// Test client (optional).
	public static void main(String[] args) {
		int size = 1;
		Percolation test = new Percolation(size);
		test.open(1, 1);
		System.out.println(test.isOpen(1, 1));
		System.out.println(test.percolates());
	} 
}
