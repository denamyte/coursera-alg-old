package week04_8puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {

    private boolean solvable;
    private int movesCount = -1;
    private Stack<Board> solutionStack;

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }
        solve(initial);
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return movesCount;
    }

    public Iterable<Board> solution() {
        return solutionStack;
    }

    private void solve(Board initial) {
        BoardWrap solved;
        if (initial.isGoal()) {
            solved = new BoardWrap(null, initial);
        } else {
            BPQ pqMain = new BPQ(priorityComparator);
            pqMain.insert(new BoardWrap(null, initial));
            BPQ pqSecond = new BPQ(priorityComparator);
            pqSecond.insert(new BoardWrap(null, initial.twin()));
            do {
                solved = processPQ(pqMain);
            } while (solved == null && processPQ(pqSecond) == null);

        }
        // At this point, one of the queues has returned a solved board
        if (solved != null) {
            solvable = true;
            // Assigning the solutionStack
            solutionStack = new Stack<>();
            movesCount = solved.movesCount;
            do {
                solutionStack.push(solved.curBoard);
                solved = solved.prevWrap;
            } while (solved != null);
        }
    }

    /** Deletes the minimum element from the queue, checks if it is solved, and returns it if it is;
     * otherwise, gets the neighbors from the board, adds them to the queue and returns null.
     * @param bpq the queue for class {@link Board}
     * @return the solved board or null
     */
    private BoardWrap processPQ(BPQ bpq) {
        if (bpq == null || bpq.isEmpty()) {
            return null;
        }
        BoardWrap bw = bpq.delMin();
        if (bw.curBoard.isGoal()) {
            return bw;
        }
        for (Board nei : bw.curBoard.neighbors()) {
            if ((bw.prevWrap == null) || !nei.equals(bw.prevWrap.curBoard)) {
                BoardWrap neighbourBW = new BoardWrap(bw, nei);
                bpq.insert(neighbourBW);
            }
        }
        return null;
    }

    public static void main(String[] args) {

    }

    private static Comparator<BoardWrap> priorityComparator = new Comparator<BoardWrap>() {
        @Override
        public int compare(BoardWrap bw1, BoardWrap bw2) {
            return (int) (bw1.curBoard.hamming() * 1.5 + bw1.curBoard.manhattan() + bw1.movesCount
                    - bw2.curBoard.hamming() * 1.5 - bw2.curBoard.manhattan() - bw2.movesCount);
        }
    };

    private static class BPQ extends MinPQ<BoardWrap> {
        public BPQ(Comparator<BoardWrap> priorityComparator) {
            super(priorityComparator);
        }
    }

    private static class BoardWrap {

        private BoardWrap prevWrap;
        private Board curBoard;
        private int movesCount;

        private BoardWrap(BoardWrap prevWrap, Board curBoard) {
            this.prevWrap = prevWrap;
            this.curBoard = curBoard;
            movesCount = prevWrap == null ? 0 : prevWrap.movesCount + 1;
        }
    }
}
