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
 ******************************************************************************/

import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private char[] tiles;
    private char[] goal;
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

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int index = i * n + j;
                tiles[ index ] = Integer.toString(blocks[i][j]).charAt(0);
                goal[ index ] = Integer.toString(index + 1).charAt(0);
                if (blocks[i][j] == 0) indexOfZero = index;
            }

        goal[totalLen - 1] = '0';

        // for (int i = 0; i < totalLen; ++i) {
        //     boolean same = tiles[i] == '0';
        //     StdOut.println(tiles[i] + " " + goal[i] + " " + same);
        // }
    }

    public Board(char[] blocks) {
        totalLen = blocks.length;
        n        = (int)Math.sqrt(totalLen);
        tiles    = new char[totalLen];
        goal     = new char[totalLen];
        for (int i = 0; i < totalLen; i++) {
            tiles[i] = blocks[i];
            goal[i]  = Integer.toString(i + 1).charAt(0);
        }
        goal[totalLen - 1] = '0';
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
            if (i == indexOfZero || tiles[i] != goal[i]) continue;

            int offset = 0;
            while (goal[offset] != tiles[i]) offset++;

            int x = Math.abs(i / n - offset / n);
            int y = Math.abs(i % n - offset % n);

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

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        // int[][] blocks = new int[n][n];
        char[] blocks = new char[totalLen];
        int firstIndex = -1, secondIndex = -1;
        char c;

        for (int i = 0; i < totalLen; ++i) {
            c = tiles[i];
            boolean isNotZero = i != indexOfZero;

            // just convert
            if (secondIndex != -1) {
                // blocks[getY(i)][getX(i)] = Character.getNumericValue(c);
                blocks[i] = c;
            }
            else if (firstIndex > -1 && isNotZero) {
                secondIndex = 0;  // so will always do conversion after then
                char prevC = tiles[firstIndex];
                // blocks[getY(i)][getX(i)] = Character.getNumericValue(prevC);
                // blocks[getY(a)][getX(a)] = Character.getNumericValue(c);

                blocks[firstIndex] = c;
                blocks[i] = prevC;
            }
            // original case, begin without '0'
            else if (firstIndex == secondIndex && isNotZero) {
                firstIndex = i;
            }
            // original case, begin with '0'
            else {
                // blocks[0][0] = 0;
                blocks[0] = '0';
            }
        }

        return new Board(blocks);
    }

    private int getX(int num) { return num % n; }
    private int getY(int num) { return num / n; }

    private Board movedZero(int to) {
        char[] blocks = tiles.clone();
        blocks[indexOfZero] = blocks[to];
        blocks[to] = '0';
        return new Board(blocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        return (this.dimension() == that.dimension())
            && (this.hamming() == that.hamming())
            && (this.manhattan() == that.manhattan());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        int x = getX(indexOfZero);
        int y = getY(indexOfZero);

        if (x > 0) neighbors.add(movedZero(y * n + x - 1));
        if (x < n) neighbors.add(movedZero(y * n + x + 1));
        if (y > 0) neighbors.add(movedZero((y - 1) * n + x));
        if (y < n) neighbors.add(movedZero((y + 1) * n + x));

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2c ", tiles[ i * n + j ]));
            }
            s.append("\n");
        }
        return s.toString();
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

    //     StdOut.println(initial.toString());

    //     for (Board ne : initial.neighbors()) {
    //         StdOut.println(ne.toString());
    //     }

    //     // StdOut.println(aa.toString());

    // }
}
