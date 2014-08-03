/**
 * Created by rshen on 7/31/2014.
 */
public class Board {
    private int[][] board;

    public Board(int[][] blocks) {
       if (blocks == null) {
           throw new IllegalArgumentException("Input is null");
       }
       if (blocks[0].length != blocks.length) {
           throw new IllegalArgumentException("This is not a square");
       }
       if (blocks.length < 2 || blocks.length > 128) {
           throw new IllegalArgumentException("Board size is either too small or too large");
       }

       board = new int[blocks.length][blocks.length];

       for (int i = 0; i < blocks.length; i++) {
           for (int j = 0; j < blocks.length; j++) {
               board[i][j] = blocks[i][j];
           }
       }
    }

    private int[][] getBoard() { return board; }

    public int dimension() {
        return board.length;
    }

    public int hamming() {
        int hammingSize = 0;
        int correctNumber = 1;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] != 0 && board[i][j] != correctNumber) {
                    hammingSize++;
                }
                correctNumber++;
            }
        }
        return hammingSize;
    }

    public int manhattan() {
        int manhattanSize = 0;
        int currentNumber = 1;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] != 0 && board[i][j] != currentNumber) {
                    int currentValue = board[i][j];
                    int goalRow = (currentValue - 1) / dimension();
                    int goalColumn = (currentValue - 1) % dimension();
                    int distance = Math.abs(goalRow - i) + Math.abs(goalColumn - j);
                    manhattanSize += distance;
                }
                currentNumber++;
            }
        }
        return manhattanSize;
    }

    public boolean isGoal() {
        int correctNumber = 1;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] != 0 && board[i][j] != correctNumber) {
                    return false;
                }
                correctNumber++;
            }
        }
        return true;
    }

    public Board twin() {
        Board twinBoard = null;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (j < dimension() - 1 && board[i][j] != 0 && board[i][j + 1] != 0) {
                    exchColumn(j, j + 1, i, board);
                    twinBoard = new Board(board);
                    exchColumn(j, j + 1, i, board);
                    return twinBoard;
                }
            }
        }
        return twinBoard;
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (!y.getClass().equals(this.getClass())) {
            return false;
        }
        if (((Board) y).dimension() != dimension()) {
            return false;
        }
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] != ((Board) y).getBoard()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> listOfNeighbor = new Stack<Board>();
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) {
                    if (i - 1 >= 0) {
                        exchRow(i, i - 1, j, board);
                        Board n = new Board(board);
                        listOfNeighbor.push(n);
                        exchRow(i, i - 1, j, board);
                    }
                    if (i + 1 < dimension()) {
                        exchRow(i, i + 1, j, board);
                        Board n = new Board(board);
                        listOfNeighbor.push(n);
                        exchRow(i, i + 1, j, board);
                    }
                    if (j - 1 >= 0) {
                        exchColumn(j, j - 1, i, board);
                        Board n = new Board(board);
                        listOfNeighbor.push(n);
                        exchColumn(j, j - 1, i, board);
                    }
                    if (j + 1 < dimension()) {
                        exchColumn(j, j + 1, i, board);
                        Board n = new Board(board);
                        listOfNeighbor.push(n);
                        exchColumn(j, j + 1, i, board);
                    }
                    return listOfNeighbor;
                }
            }
        }
        return listOfNeighbor;
    }

    private void exchColumn(int first, int second, int row, int[][] blocks) {
        int temp = blocks[row][first];
        blocks[row][first] = blocks[row][second];
        blocks[row][second] = temp;
    }

    private void exchRow(int first, int second, int column, int[][] blocks) {
        int temp = blocks[first][column];
        blocks[first][column] = blocks[second][column];
        blocks[second][column] = temp;
    }

    public String toString() {
        StringBuilder outPut = new StringBuilder();
        outPut.append(dimension());
        outPut.append("\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                outPut.append(String.format("%2d ", board[i][j]));
            }
            outPut.append("\n");
        }
        return outPut.toString();
    }

    public static void main(String[] args) {
//        readFromFileTest(args[0]);
//        neighborsTest();
//        manhattanSizeTest();
//        hammingSizeTest();
//        equalTest();
//        twinTest();
//        isGoalTest();
//        testToString();
    }

    private static void readFromFileTest(String arg) {
        In in = new In(arg);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial.hamming());
    }

    private static void neighborsTest() {
        int[][] p1 = new int[][]{{2, 0, 3}, {1, 5, 6}, {7, 8, 4}};
        int[][] p2 = new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 8, 5}};
        Board sut1 = new Board(p1);
        Board sut2 = new Board(p2);

        Stack<Board> result1 = (Stack<Board>) sut1.neighbors();
        while (!result1.isEmpty()) {
            StdOut.println(result1.pop().toString());
        }

        Stack<Board> result2 = (Stack<Board>) sut2.neighbors();
        while (!result2.isEmpty()) {
            StdOut.println(result1.pop().toString());
        }
    }

    private static void manhattanSizeTest() {
        int[][] p1 = new int[][]{{2, 0, 3}, {1, 5, 6}, {7, 8, 4}};
        int[][] p2 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board sut1 = new Board(p1);
        Board sut2 = new Board(p2);

        System.out.println(sut1.manhattan());
        StdOut.println(sut2.manhattan());
    }

    private static void hammingSizeTest() {
        int[][] p1 = new int[][]{{2, 0, 3}, {1, 5, 6}, {7, 8, 4}};
        int[][] p2 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board sut1 = new Board(p1);
        Board sut2 = new Board(p2);

        System.out.println(sut1.hamming());
        StdOut.println(sut2.hamming());
    }

    private static void equalTest() {
        int[][] test = new int[][]{{2, 0, 3}, {1, 5, 6}, {7, 8, 4}};
        int[][] p1 = new int[][]{{2, 0, 3}, {1, 5, 6}, {7, 8, 4}};
        int[][] p2 = new int[][]{{2, 0, 3}, {1, 5, 8}, {7, 6, 4}};
        Board sut = new Board(test);

        System.out.println(sut.equals(new Board(p1)));
        System.out.println(sut.equals(new Board(p2)));
    }

    private static void twinTest() {
        int[][] test = new int[][]{{2, 0, 3}, {1, 5, 6}, {7, 8, 4}};
        Board sut = new Board(test);
        Board twinBoard = sut.twin();
        System.out.println(twinBoard.toString());
        StdOut.println(sut.toString());
    }

    private static void isGoalTest() {
        int[][] test1 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board sut1 = new Board(test1);
        System.out.println(sut1.isGoal());

        int[][] test2 = new int[][]{{1, 2, 3}, {0, 5, 6}, {7, 8, 4}};
        Board sut2 = new Board(test2);
        System.out.println(sut2.isGoal());
    }


    private static void testToString() {
        int[][] test = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board sut = new Board(test);
        String result = sut.toString();
        System.out.print(result);
    }
}
