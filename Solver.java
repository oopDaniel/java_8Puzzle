/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver <puzzle04.txt>
 *  Dependencies: Board.java, HashMap
 *
 *  The solution for 8 Puzzle problem
 *
 *  Use HashMap caching the string result of board to reduce time and space
 *
 ******************************************************************************/

import java.util.HashMap;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;


public class Solver {
    private int moves;
    private SearchNode result;
    private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
    private HashMap<Integer, Integer> manhattanMap
        = new HashMap<Integer, Integer>();

    /**
     * The board with priority, and a flag to ensure if it's a twin
     */
    private class SearchNode implements Comparable<SearchNode> {
        private short moves;
        private int m;
        private boolean isTwin;
        private Board board;
        private SearchNode parent;

        // Add parent for the trace after resolved
        public SearchNode(Board board, SearchNode parent, boolean isTwin) {
            this.board  = board;
            this.parent = parent;
            this.moves  = parent == null ? 0 : (short) (parent.moves + 1);
            this.isTwin = isTwin;

            // Better to use other property as the hash key,
            // but the only available public API is toString()
            int id = board.toString().hashCode();

            if (manhattanMap.containsKey(id)) {
                this.m = manhattanMap.get(id);
            } else {
                this.m = board.manhattan();
                manhattanMap.put(id, this.m);
            }
        }

        /**
         * Determine the end of searching
         *
         * @return whether the board is goal
         */
        public boolean hasSolved() { return board.isGoal(); }

        /**
         * Compare two SearchNode, the Comparable method
         * will be used for priority queue
         *
         * @return integer of comparing result
         */
        public int compareTo(SearchNode that) {
            int thisMH = this.m;
            int thatMH = that.board.manhattan();

            int pDiff = thisMH + this.moves - thatMH - that.moves;
            if (pDiff != 0) return pDiff;

            int mDiff = thisMH - thatMH;
            if (mDiff != 0) return mDiff;

            return this.board.hamming() - that.board.hamming();
        }

        // Debugging
        // public void show() {
        //     StdOut.println(" - moves: " + moves + ", manhattan: "+ m + ", priority: " + (moves + m));
        // }
        // public String toString() { return board.toString(); }
    }

    /**
     * Find a solution to the initial board (using the A* algorithm)
     */
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.NullPointerException();

        moves = 0;

        SearchNode curr = new SearchNode(initial, null, false);
        pq.insert(curr);
        pq.insert(new SearchNode(initial.twin(), null, true));

        while (!curr.hasSolved()) {
            curr = pq.delMin();

            for (Board neighbor: curr.board.neighbors()) {
                // If a neighbor cached already, enqueue will be redundant
                if (manhattanMap.get(neighbor.toString().hashCode()) == null)
                    pq.insert(new SearchNode(neighbor, curr, curr.isTwin));
            }
        }

        moves  = curr.isTwin ? -1 : curr.moves;
        result = curr;
    }

    /**
     * Will try to solve and determine whether the initial board is solvable
     *
     * @return whether the initial board is solvable
     */
    public boolean isSolvable() { return moves != -1; }

    /**
     * Min number of moves to solve initial board
     *
     * @return min number of moves to solve; -1 if unsolvable
     */
    public int moves() { return moves; }


    /**
     * Sequence of boards in a shortest solution
     *
     * @return Sequence of boards in a shortest solution; null if unsolvable
     */
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
