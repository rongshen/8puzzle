/**
 * Created by rshen on 8/2/2014.
 */
public class Solver {
    private MinPQ<SearchNode> queue = new MinPQ<SearchNode>();
    private MinPQ<SearchNode> twinQueue = new MinPQ<SearchNode>();

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int moves;
        SearchNode previousSearchNode;

        @Override
        public int compareTo(SearchNode o) {
//            if (board.hamming() < o.board.hamming()) {
//                return -1;
//            }
//            if (board.hamming() > o.board.hamming()) {
//                return 1;
//            }
//            return 0;
//
            if (board.manhattan() + moves < o.board.manhattan() + o.moves) {
                return -1;
            }
            if (board.manhattan() + moves > o.board.manhattan() + o.moves) {
                return 1;
            }
            return 0;
        }
    }

    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException();
        }

        SearchNode first = new SearchNode();
        first.board = initial;
        first.moves = 0;
        first.previousSearchNode = null;

        SearchNode twin = new SearchNode();
        twin.board = initial.twin();
        twin.moves = 0;
        twin.previousSearchNode = null;


        queue.insert(first);
        twinQueue.insert(twin);

        while (true) {

            if (queue.min().board.isGoal() || twinQueue.min().board.isGoal()) {
                return;
            } else {
                SearchNode currentNode = queue.delMin();
                SearchNode twinNode = twinQueue.delMin();

                for (Board n : currentNode.board.neighbors()) {
                    if (currentNode.previousSearchNode == null || !n.equals(currentNode.previousSearchNode.board)) {
                        SearchNode neighbor = new SearchNode();
                        neighbor.board = n;
                        neighbor.previousSearchNode = currentNode;
                        neighbor.moves = currentNode.moves + 1;
                        queue.insert(neighbor);
                    }
                }

                for (Board n : twinNode.board.neighbors()) {
                    if (currentNode.previousSearchNode == null || !n.equals(twinNode.previousSearchNode.board)) {
                        SearchNode neighbor = new SearchNode();
                        neighbor.board = n;
                        neighbor.previousSearchNode = twinNode;
                        neighbor.moves = twinNode.moves + 1;
                        twinQueue.insert(neighbor);
                    }
                }
            }
        }
    }

    public boolean isSolvable() {
        if (!queue.min().board.isGoal()) {
            return false;
        }
        return true;
    }

    public int moves() {
        int countOfMove = -1;
        if (isSolvable()) {
            SearchNode current = queue.min();
            while (current != null) {
                countOfMove++;
                current = current.previousSearchNode;
            }
        }
        return countOfMove;
    }

    public Iterable<Board> solution() {
        Stack<Board> result = new Stack<Board>();
        SearchNode current = queue.min();
        if (isSolvable()) {
            while (current != null) {
                result.push(current.board);
                current = current.previousSearchNode;
            }
            return result;
        }
        return null;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
            StdOut.println(solver.moves()); }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
