/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver <puzzle04.txt>
 *  Dependencies: Board.java
 *
 *  The solution for 8 Puzzle problem
 *
 *
 ******************************************************************************/

import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;


public class Solver {
    private int moves;
    private MinPQ<OrderedBoard> pq = new MinPQ<OrderedBoard>();
    private ArrayList<Board> q = new ArrayList<Board>();

    private class OrderedBoard implements Comparable<OrderedBoard> {
      private int moves;
      private int priority;
      private Board board;

      public OrderedBoard(Board boardIn, int currMove) {
        board    = boardIn;
        moves    = currMove;
        priority = boardIn.manhattan() + currMove;
        StdOut.println(" - moves: " + moves + ", manhattan: "+ boardIn.manhattan() + " priority: " + priority);
      }
      public int moves() { return moves; }
      public int priority() { return priority; }
      public boolean isSolved() { return board.isGoal(); }
      public Board board() { return board; }
      public Iterable<Board> neighbors() { return board.neighbors(); }

      public int compareTo(OrderedBoard that) {
        int pDiff = this.priority - that.priority;
        if (pDiff != 0) return pDiff;

        int mDiff = that.moves - this.moves;
        if (mDiff != 0) return mDiff;

        return this.board.hamming() - that.board.hamming();
      }

      public String toString() { return board.toString(); }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
      if (initial == null) throw new java.lang.NullPointerException();

      Board prev;
      OrderedBoard curr = new OrderedBoard(initial, moves);
      moves = 0;
      pq.insert(curr);

      while (!curr.isSolved() && moves < 20) {

        prev = curr.board();
        curr = pq.delMin();

        q.add(curr.board());
        StdOut.println("Step " + moves + ", prev: " + prev);

        ++moves;

        for (Board b: curr.neighbors()) {
          if (!prev.equals(b)) {
            pq.insert(new OrderedBoard(b, moves));
          }
        }
      }

      // Remove the pre-add move
      --moves;
    }

    // is the initial board solvable?
    public boolean isSolvable() { return moves > 0; }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
      if (!isSolvable()) return -1;
      return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
      if (!isSolvable()) return null;
      return q;
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
