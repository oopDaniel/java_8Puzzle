/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver <puzzle04.txt>
 *  Dependencies: Board.java
 *
 *  The solution for 8 Puzzle problem
 *
 *
 ******************************************************************************/

import java.util.HashMap;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private int moves;
    private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
    private SearchNode result;
    // private HashMap<String, Integer> manhattanMap = new HashMap<String, Integer>();

    private class SearchNode implements Comparable<SearchNode> {
        private int moves;
        private int m;
        // private int priority;
        private boolean isTwin;
        private Board board;
        private SearchNode parent;

        public SearchNode(Board board, SearchNode parent, boolean isTwin) {
            this.board  = board;
            this.parent = parent;
            this.moves  = parent == null ? 0 : parent.moves + 1;
            this.isTwin = isTwin;

            this.m = board.manhattan();

            // String id = board.toString();
            // Integer tmp = manhattanMap.get(id);
            // if (tmp == null) {
            //     this.m = board.manhattan();
            //     manhattanMap.put(id, this.m);
            // } else {
            //     this.m = tmp;
            // }
            // StdOut.println("this.m: " + this.m);
        }

        public void show() {
            StdOut.println(" - moves: " + moves + ", manhattan: "+ m + ", priority: " + (moves+m));
        }

        public boolean isSolved() { return board.isGoal(); }

        public int compareTo(SearchNode that) {
            int thisMH = this.board.manhattan();
            int thatMH = that.board.manhattan();
            // , thatMH;

            // String id = that.board.toString();
            // Integer tmp = manhattanMap.get(id);
            // if (tmp == null) {
            //     thatMH = that.board.manhattan();
            //     manhattanMap.put(id, thatMH);
            // } else {
            //     thatMH = tmp;
            // }

            int pDiff = thisMH + this.moves - thatMH - that.moves;
            if (pDiff != 0) return pDiff;

            int mDiff = thisMH - thatMH;
            if (mDiff != 0) return mDiff;

            return this.board.hamming() - that.board.hamming();
        }

        // public String toString() { return board.toString(); }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.NullPointerException();

        moves = 0;
        Board prev;
        SearchNode curr = new SearchNode(initial, null, false);
        // SearchNode twin = new SearchNode(initial.twin(), null, true);
        pq.insert(curr);
        pq.insert(new SearchNode(initial.twin(), null, true));

        while (!curr.isSolved()) {
            // StdOut.println("~~~~ Step " + moves);

            prev = curr.board;
            curr = pq.delMin();

            // StdOut.println( " curr: " + curr.board);
            // curr.show();
            // for (SearchNode qq: pq) {
            //     qq.show();
            //     // StdOut.println( " qq: " + qq.board);
            // }
            // curr.show();

            // ++moves;
            // if (moves > 25) break;

            for (Board neighbor: curr.board.neighbors()) {
                if (!prev.equals(neighbor)) {
                    pq.insert(new SearchNode(neighbor, curr, curr.isTwin));
                }
            }
        }

        moves = curr.isTwin ? -1 : curr.moves;
        result = curr;

    }

    // is the initial board solvable?
    public boolean isSolvable() { return moves != -1; }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() { return moves; }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> s = new Stack<Board>();
        SearchNode currNode = result;

        while (currNode != null) {
            s.push(currNode.board);
            currNode = currNode.parent;
        }

        return s;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // int id = initial.toString().hashCode();

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
