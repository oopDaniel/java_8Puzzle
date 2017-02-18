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

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int index = i * n + j;
                tiles[ index ] = (char) blocks[i][j];
                goal[ index ] = (char) (index + 1);
                if (blocks[i][j] == 0) indexOfZero = index;
            }

        goal[totalLen - 1] = cZero;

        // for (int i = 0; i < totalLen; ++i) {
        //     StdOut.println((int)tiles[i] + " " + (int)goal[i]);
        // }
        // StdOut.println("indexOfZero" + indexOfZero + goal[totalLen - 1]);
    }

    public Board(char[] blocks) {
        totalLen = blocks.length;
        n        = (int) Math.sqrt(totalLen);
        tiles    = new char[totalLen];
        goal     = new char[totalLen];
        for (int i = 0; i < totalLen; i++) {
            tiles[i] = blocks[i];
            goal[i]  = (char) (i + 1);
            if (blocks[i] == cZero) indexOfZero = i;
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

    // a board that is obtained by exchanging any pair of blocks
    // public Board twin() {
    //     // int[][] blocks = new int[n][n];
    //     char[] blocks = new char[totalLen];
    //     int firstIndex = -1, secondIndex = -1;
    //     char c;

    //     for (int i = 0; i < totalLen; ++i) {
    //         c = tiles[i];
    //         boolean isNotZero = i != indexOfZero;

    //         // just convert
    //         if (secondIndex != -1) {
    //             // blocks[getY(i)][getX(i)] = Character.getNumericValue(c);
    //             blocks[i] = c;
    //         }
    //         else if (firstIndex > -1 && isNotZero) {
    //             secondIndex = 0;  // so will always do conversion after then
    //             char prevC = tiles[firstIndex];
    //             // blocks[getY(i)][getX(i)] = Character.getNumericValue(prevC);
    //             // blocks[getY(a)][getX(a)] = Character.getNumericValue(c);

    //             blocks[firstIndex] = c;
    //             blocks[i] = prevC;
    //         }
    //         // original case, begin without '0'
    //         else if (firstIndex == secondIndex && isNotZero) {
    //             firstIndex = i;
    //         }
    //         // original case, begin with '0'
    //         else {
    //             // blocks[0][0] = 0;
    //             blocks[0] = cZero;
    //         }
    //     }

    //     return new Board(blocks);
    // }

    public Board twin() {
        char[] blocks = tiles.clone();
        int x = 0, y = 1;

        if      (blocks[x] == cZero) x = y + 1;
        else if (blocks[y] == cZero) y = y + 1;

        char c = blocks[x];
        blocks[x] = blocks[y];
        blocks[y] = c;

        return new Board(blocks);
    }

    private int getX(int num) { return num % n; }
    private int getY(int num) { return num / n; }

    private Board movedZero(int to) {
        char[] blocks = tiles.clone();
        blocks[indexOfZero] = blocks[to];
        blocks[to] = cZero;
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
