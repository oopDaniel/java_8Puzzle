/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java Board
 *  Dependencies: Bag (or any other iterable type)
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

import edu.princeton.cs.algs4.Bag;


public class Board {
    private char[] tiles;
    private short n;
    private short totalLen;
    private short indexOfZero;

    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j)
     *
     */
    public Board(int[][] blocks) {
        n        = (short) blocks.length;
        totalLen = (short) (n * n);
        tiles    = new char[totalLen];

        for (short i = 0; i < n; i++)
            for (short j = 0; j < n; j++) {
                short index = (short) (i * n + j);
                tiles[ index ] = (char) blocks[i][j];
                if (blocks[i][j] == 0) indexOfZero = index;
            }

    }

    /**
     * Board dimension n
     *
     * @return integer of dimension
     */
    public int dimension() { return n; }


    /**
     * The number of blocks in the wrong position,
     * plus the number of moves made so far to get to the search node.
     * Intuitively, a search node with a small number of blocks in the wrong position is close to the goal,
     * and we prefer a search node that have been reached using a small number of moves.
     *
     * @return integer of hamming
     */
    public int hamming() {
        short count = 0;
        for (short i = 0; i < totalLen - 1; ++i) {
            if ((short) tiles[i] != i + 1) ++count;
        }
        return count;
    }

    /**
     * The sum of the Manhattan distances (sum of the vertical and horizontal distance)
     * from the blocks to their goal positions, plus the number of moves made so far
     * to get to the search node.
     *
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        short count = 0;
        for (int i = 0; i < totalLen; ++i) {
            if (i == indexOfZero || (short) tiles[i] == i + 1) continue;

            int targetPos = tiles[i] - 1;
            short x = (short) Math.abs(getX(i) - getX(targetPos));
            short y = (short) Math.abs(getY(i) - getY(targetPos));

            count += (x + y);
        }
        return count;
    }

    /**
     * Is this board the goal board?
     *
     * @return boolean of whether the board is the goad
     */
    public boolean isGoal() {
        for (short i = 0; i < totalLen; ++i) {
            if (i == totalLen - 1) break;
            if ((short) tiles[i] != i + 1) return false;
        }
        return true;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     *
     * @return twin Board of the original one
     */
    public Board twin() {
        int[][] blocks = get2dIntArr();

        int tmpA = blocks[0][0];
        int tmpB = blocks[0][1];

        if (tmpA == 0) {
            tmpA         = blocks[1][0];
            blocks[1][0] = tmpB;
            blocks[0][1] = tmpA;
        }
        else if (tmpB == 0) {
            blocks[0][0] = blocks[1][1];
            blocks[1][1] = tmpA;
        } else {
            blocks[0][0] = tmpB;
            blocks[0][1] = tmpA;
        }

        return new Board(blocks);
    }

    /**
     * does this board equal y?
     *
     * @param Board y - the other board to compare
     * @return boolean of thether this board equal to y
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (this.dimension() != that.dimension()) return false;
        for (short i = 0; i < totalLen; ++i) {
            if (this.tiles[i] != that.tiles[i]) return false;
        }
        return true;
    }

    /**
     * all neighboring boards
     *
     * @return an Iterable<Board> of all neighboring boards
     */
    public Iterable<Board> neighbors() {
        Bag<Board> neighbors = new Bag<Board>();
        short x = (short) getX(indexOfZero);
        short y = (short) getY(indexOfZero);

        if (x > 0)     neighbors.add(movedZero(y * n + x - 1));
        if (x < n - 1) neighbors.add(movedZero(y * n + x + 1));
        if (y > 0)     neighbors.add(movedZero((y - 1) * n + x));
        if (y < n - 1) neighbors.add(movedZero((y + 1) * n + x));

        return neighbors;
    }

    /**
     * string representation of this board
     *
     * @return string expression of board
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (short i = 0; i < n; i++) {
            for (short j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) tiles[ i * n + j ]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * 1d index to 2d x-index
     *
     * @param integer of 1d index
     * @return integer of x-index
     */
    private int getX(int num) { return num % n; }

    /**
     * 1d index to 2d y-index
     *
     * @param integer of 1d index
     * @return integer of y-index
     */
    private int getY(int num) { return num / n; }

    /**
     * The new board after zero moved
     *
     * @param integer of index of where zero should move to
     * @return board after the movement
     */
    private Board movedZero(int to) {
        int[][] blocks = get2dIntArr();
        blocks[getY(indexOfZero)][getX(indexOfZero)] = blocks[getY(to)][getX(to)];
        blocks[getY(to)][getX(to)] = 0;
        return new Board(blocks);
    }

    /**
     * The original 2-d integer array for construction.
     * Recalculate it to reduce space
     *
     * @return original integer array
     */
    private int[][] get2dIntArr() {
        int[][] blocks = new int[n][n];
        for (short i = 0; i < n; ++i)
            for (short j = 0; j < n; ++j)
                blocks[i][j] = (int) tiles[ i * n + j ];
        return blocks;
    }
}
