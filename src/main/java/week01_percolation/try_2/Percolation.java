package week01_percolation.try_2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufThrough;
    private boolean[] openedOneD;
    private final int TOP_VIRT_INDEX;
    private final int BTM_VIRT_INDEX;

    private interface PercUF {
        int count();

        int find(int p);

        boolean connected(int p, int q);

        void union(int p, int q);
    }

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(n + " is not a valid dimension");
        }
        this.n = n;
        openedOneD = new boolean[n * n];
        TOP_VIRT_INDEX = n * n;
        BTM_VIRT_INDEX = TOP_VIRT_INDEX + 1;
        // Added 1 virtual site for virtual connection of all the top real sites to it
        uf = new WeightedQuickUnionUF(TOP_VIRT_INDEX + 1);
        // Added 2 virtual sites for virtual connection of all the top and bottom real sites to them
        ufThrough = new WeightedQuickUnionUF(BTM_VIRT_INDEX + 1);
    }

    /** Assuming that site (row, col) is not yet opened, opens the site */
    public void open(int row, int col) {
        validateIndices(row, col);
        if (isOpen(row, col)) {
            return;
        }
        int site1 = convertToOneD(row, col);
        openedOneD[site1] = true;
        tryConnectOneD(site1, row - 1, col);
        tryConnectOneD(site1, row, col + 1);
        tryConnectOneD(site1, row + 1, col);
        tryConnectOneD(site1, row, col - 1);

        // Connecting both structures to TOP_VIRT_INDEX
        if (row == 1 && !uf.connected(site1, TOP_VIRT_INDEX)) {
            uf.union(site1, TOP_VIRT_INDEX);
            ufThrough.union(site1, TOP_VIRT_INDEX);
        }

        // Connection ufThrough structure to BTM_VIRT_INDEX
        if (row == n) {
            ufThrough.union(site1, BTM_VIRT_INDEX);
        }
    }

    private void tryConnectOneD(int site1, int row2, int col2) {
        if (indicesInRange(row2, col2)) {
            int site2 = convertToOneD(row2, col2);
            if (openedOneD[site2]) {
                uf.union(site1, site2);
                ufThrough.union(site1, site2);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return openedOneD[convertToOneD(row, col)];
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        return uf.connected(convertToOneD(row, col), TOP_VIRT_INDEX);
    }

    public boolean percolates() {
        return ufThrough.connected(TOP_VIRT_INDEX, BTM_VIRT_INDEX);
    }

    private boolean connected(int row1, int col1, int row2, int col2) {
        return uf.connected(convertToOneD(row1, col1), convertToOneD(row2, col2));
    }

    private void validateIndices(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IndexOutOfBoundsException("index is not between 1 and " + n);
        }
    }

    private boolean indicesInRange(int row, int col) {
        return (row > 0 && row <= n && col > 0 && col <= n);
    }

    private int convertToOneD(int row, int col) {
        return (row - 1) * n + col - 1;
    }

    private String printOpenedArray() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        int cols = 0;
        for (boolean b : openedOneD) {
            if (cols == n) {
                cols = 0;
                sb.append("\n");
            }
            cols++;
            sb.append(b ? 1 : 0).append(" ");
        }
        return sb.toString();
    }

    private String print() {
        return "success_1.Percolation{" +
                "n=" + n +
                ", uf=" + uf +
                ", ufThrough=" + ufThrough +
                ", openedOneD=" + printOpenedArray() +
                ", TOP_VIRT_INDEX=" + TOP_VIRT_INDEX +
                ", BTM_VIRT_INDEX=" + BTM_VIRT_INDEX +
                '}';
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);

        percolation.open(1, 2);
        System.out.println(percolation.printOpenedArray());
        System.out.println("percolation.percolates(): " + percolation.percolates());
        percolation.open(2, 2);
        System.out.println(percolation.printOpenedArray());
        System.out.println("percolation.percolates(): " + percolation.percolates());
        percolation.open(3, 2);
        System.out.println(percolation.printOpenedArray());
        System.out.println("percolation.percolates(): " + percolation.percolates());
        percolation.open(4, 3);
        System.out.println(percolation.printOpenedArray());
        System.out.println("percolation.percolates(): " + percolation.percolates());
        percolation.open(3, 3);
        System.out.println(percolation.printOpenedArray());
        System.out.println("percolation.percolates(): " + percolation.percolates());
        percolation.open(5, 2);
        System.out.println(percolation.printOpenedArray());
        System.out.println("percolation.percolates(): " + percolation.percolates());
        percolation.open(5, 3);
        System.out.println(percolation.printOpenedArray());
        System.out.println("percolation.percolates(): " + percolation.percolates());
    }


}
