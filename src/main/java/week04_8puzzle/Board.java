package week04_8puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Board {

    private final char[] tiles;

    private final int n;
    private int ham;
    private int manh;

    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException();
        }
        n = blocks.length;
        tiles = new char[n * n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                tiles[row * n + col] = (char) blocks[row][col];
            }
        }
        calculatePriorities();
    }

    @Deprecated
    private static int[][] copyTiles(final int[][] blocks) {
        int dim = blocks.length;
        int[][] copy = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(blocks[i], 0, copy[i], 0, dim);
        }
        return copy;
    }

    private void calculatePriorities() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                char value = tiles[row * n + col];
                if (value != 0) {
                    char requiredLinearValue = (char) (row * n + col + 1);
                    if (value != requiredLinearValue) {
                        // Calculate ham
                        ham++;
                        // Calculate manh

                        int iniRow = (value - 1) / n, iniCol = (value - 1) % n;
                        int curManh = Math.abs(row - iniRow) + Math.abs(col - iniCol);
//
//
//                        int diff = Math.abs(value - requiredLinearValue);
//                        int curManh = diff / n + diff % n;
                        manh += curManh;
                    }
                }
            }
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return ham;
    }

    public int manhattan() {
        return manh;
    }

    public boolean isGoal() {
        return manh == 0;
    }

    public Board twin() {
        int row, col;
        do {
            row = StdRandom.uniform(n);
            col = StdRandom.uniform(n);
        } while (tiles[row * n + col] == 0);

        for (Shift shift : Shift.ALL) {
            if (outOfRange(shift, row, col) || tiles[(row + shift.shiftRow) * n + col + shift.shiftCol] == 0) {
                continue;
            }
            return new Board(shiftTiles(shift, row, col));
        }
        throw new NullPointerException("can't create a twin");
    }

    public boolean equals(Object y) {
        if (y == null || !(y.getClass().equals(Board.class))) {
            return false;
        }
        Board that = (Board) y;
        return this.n == that.n && this.ham == that.ham && this.manh == that.manh && compareTiles(that.tiles);
    }

    private boolean compareTiles(char[] thoseTiles) {
        if (tiles.length != thoseTiles.length) {
            return false;
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row * n + col] != thoseTiles[row * n + col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        // -- Generate maximum 4 neighbors of the current board
        // -- Remove from those the one repeating the previous board (if the previous board is not null)
        // -- return a collections of remaining boards
        List<Board> neighbors = new ArrayList<>(4);

        int zeroFlatInd = searchZero();
        int zeroRow = zeroFlatInd / n;
        int zeroCol = zeroFlatInd % n;

        for (Shift shift : Shift.ALL) {
//            int[][] nTiles;
            if (outOfRange(shift, zeroRow, zeroCol)
                    /*|| (nTiles = shiftTiles(shift, zeroRow, zeroCol)) == null*/) {
                continue;
            }
            Board neighbor = new Board(shiftTiles(shift, zeroRow, zeroCol));
            neighbors.add(neighbor);
        }
        Collections.sort(neighbors, priorityComparator);
        return neighbors;
    }

    private boolean outOfRange(Shift shift, int row, int col) {
        return row + shift.shiftRow < 0 || row + shift.shiftRow >= n
                || col + shift.shiftCol < 0 || col + shift.shiftCol >= n;
    }

    /** Returns a copy of tiles with swapped tiles or null if requested swap is impossible */
    private int[][] shiftTiles(Shift shift, int zeroRow, int zeroCol) {
        int nRow = zeroRow + shift.shiftRow;
        int nColl = zeroCol + shift.shiftCol;

        int[][] nTiles = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                nTiles[row][col] = tiles[row * n + col];
            }
        }

        int temp = nTiles[zeroRow][zeroCol];
        nTiles[zeroRow][zeroCol] = nTiles[nRow][nColl];
        nTiles[nRow][nColl] = temp;
        return nTiles;
    }

    private int searchZero() {
        for (int i = 0; i < n * n; i++) {
            if (tiles[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    @Deprecated
    private int searchZeroDepr(int[] array) {
        for (int i = 0; i < n; i++) {
            if (array[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                s.append(String.format("%2d ", (int) tiles[row * n + col]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial);
    }

    private static Comparator<Board> priorityComparator = (o1, o2) -> o1.ham + o1.manh - o2.ham - o2.manh;

    private enum Shift {
        UP(-1, 0),
        RIGHT(0, 1),
        DOWN(1, 0),
        LEFT(0, -1);

        private final int shiftRow;
        private final int shiftCol;
        static final Shift[] ALL = Shift.values();

        Shift(int shiftRow, int shiftCol) {
            this.shiftRow = shiftRow;
            this.shiftCol = shiftCol;
        }
    }


}
