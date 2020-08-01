package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private int tries;
	private double[] results;
	private double stddev;
	private double mean;

	public PercolationStats(int n, int trials) {
		if (n < 1 || trials < 1) {
			throw new IllegalArgumentException("N < 1 || T < 1");
		}
		this.tries = trials;
		this.results = new double[trials];
		for (int i = 0; i < trials; i++) {
			Percolation percolation = new Percolation(n);
			while (!percolation.percolates()) {
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				if (!percolation.isOpen(row, col)) {
					percolation.open(row, col);
				}
			}
			double result = (double) percolation.numberOfOpenSites() / (n * n);
			results[i] = result;

		}
		this.stddev = StdStats.stddev(results);
		this.mean = StdStats.mean(results);
	}

	// Sample mean of percolation threshold.
	public double mean() {
		return mean;
	}

	// Sample standard deviation of percolation threshold.
	public double stddev() {
		return stddev;
	}

	// Low endpoint of 95% confidence interval.
	public double confidenceLo() {
		return mean - ((1.96 * stddev) / Math.sqrt(tries)); 
	}

	// High endpoint of 95% confidence interval.
	public double confidenceHi() {
		return mean + ((1.96 * stddev) / Math.sqrt(tries));
	}

	// Test client.
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		PercolationStats test = new PercolationStats(n, T);
		System.out.println("mean                          = " + test.mean());
		System.out.println("stddev                        = " + test.stddev());
		System.out.println("95% confidence interval       = " + "[" + test.confidenceLo() + ", " 
				+  test.confidenceHi() + "]");

	}


}
