package week01_percolation.success_1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int n;
    private int n2;
    private int trials;
    private double[] treshold;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        n2 = n * n;
        this.trials = trials;
        treshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            launchTrial(i);
        }
    }

    private void launchTrial(int trialIndex) {
        Percolation perc = new Percolation(n);
        // Opening until percolates
        long count = 0;
        int row, col;
        do {
            do {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
            } while (perc.isOpen(row, col));
            perc.open(row, col);
            count++;
        } while (!perc.percolates());
        treshold[trialIndex] = (double) count / n2;
        // perform some actions after test is finished

    }

    public double mean() {
        return StdStats.mean(treshold);
    }

    public double stddev() {
        return trials <= 1 ? Double.NaN : StdStats.stddev(treshold);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / (Math.sqrt(trials));
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / (Math.sqrt(trials));
    }

    public static void main(String[] args) {
//        success_1.PercolationStats stats = new success_1.PercolationStats(100, 50);
        PercolationStats stats = new PercolationStats(500, 100);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
