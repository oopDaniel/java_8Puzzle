/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java Board
 *  Dependencies: none
 *
 *  Current board
 *
 *  e.g.
 *
 *    1  3         6  3  8
 *    4  2  5      4  2  5
 *    7  8  6      7     1
 *
 *
 *  Time complexity: All methods with O(n^2) at least
 *
 ******************************************************************************/

import java.util.ArrayList;
// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;

public class Board {
    private char[] tiles;
    private char[] goal;
    // private int[][] cloneBlocks;
    private char cZero = (char) 0;
    private int n;
    private int totalLen;
    private int indexOfZero;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n        = blocks.length;
        totalLen = n * n;
        tiles    = new char[totalLen];
        goal     = new char[totalLen];
        // cloneBlocks = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int index = i * n + j;
                tiles[ index ] = (char) blocks[i][j];
                goal[ index ] = (char) (index + 1);
                // cloneBlocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) indexOfZero = index;
            }

        goal[totalLen - 1] = cZero;

    }

    // board dimension n
    public int dimension() { return n; }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < totalLen; ++i) {
            if (i == indexOfZero) continue;
            if (tiles[i] != goal[i]) ++count;
        }
        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < totalLen; ++i) {
            if (i == indexOfZero || tiles[i] == goal[i]) continue;

            int targetPos = (int) tiles[i] - 1;
            int x = Math.abs(getX(i) - getX(targetPos));
            int y = Math.abs(getY(i) - getY(targetPos));

            count += x + y;
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < totalLen; ++i) {
            if (tiles[i] != goal[i]) return false;
        }
        return true;
    }

    public Board twin() {
        int[][] blocks = get2dIntArr();

        int x = 0, y = n - 1;

        if      (blocks[0][x]     == 0) ++x;
        else if (blocks[n - 1][y] == 0) --y;

        int tmp          = blocks[0][x];
        blocks[0][x]     = blocks[n - 1][y];
        blocks[n - 1][y] = tmp;

        return new Board(blocks);
    }

    private Board movedZero(int to) {
        int[][] blocks = get2dIntArr();
        blocks[getY(indexOfZero)][getX(indexOfZero)] = blocks[getY(to)][getX(to)];
        blocks[getY(to)][getX(to)] = 0;
        return new Board(blocks);
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < totalLen; ++i) {
            if (this.tiles[i] != that.tiles[i]) return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        int x = getX(indexOfZero);
        int y = getY(indexOfZero);

        if (x > 0)     neighbors.add(movedZero(y * n + x - 1));
        if (x < n - 1) neighbors.add(movedZero(y * n + x + 1));
        if (y > 0)     neighbors.add(movedZero((y - 1) * n + x));
        if (y < n - 1) neighbors.add(movedZero((y + 1) * n + x));

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) tiles[ i * n + j ]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int getX(int num) { return num % n; }
    private int getY(int num) { return num / n; }

    private int[][] get2dIntArr() {
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                blocks[i][j] = (int) tiles[ i * n + j ];
        return blocks;
    }


    // unit tests (not graded)
    // public static void main(String[] args) {
    //     In in = new In(args[0]);
    //     int n = in.readInt();
    //     int[][] blocks = new int[n][n];
    //     for (int i = 0; i < n; i++)
    //         for (int j = 0; j < n; j++)
    //             blocks[i][j] = in.readInt();
    //     Board initial = new Board(blocks);
    //     Board aa = initial.twin();
    //     Board bb = aa.twin();

    //     StdOut.println(initial.toString());
    //     StdOut.println(aa.toString());
    //     StdOut.println(bb.toString());

    //     // for (Board ne : initial.neighbors()) {
    //     //     StdOut.println(ne.toString());
    //     // }


    //     StdOut.println(aa.equals(bb));
    //     StdOut.println(initial.equals(bb));

    // }
}
