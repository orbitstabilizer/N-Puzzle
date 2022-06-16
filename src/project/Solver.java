
package project;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * Solver class.
 * @author Yusuf Akin
 */
public class Solver {
	/**
	 * the last state of the puzzle
	 */
	private PriorityObject winner;

	/**
	 * @param root the initial state of the puzzle
	 */
	public Solver(Puzzle root) {
		System.out.println("Starting the solver...");
		if (root == null)
			throw new IllegalArgumentException();
		if(root.isSolvable())
			solve(root);
		else {
			System.out.println("Board is unsolvable");
		}
		System.out.println("Solving is finished...");
	}

	/**
	 * solves the puzzle using the A* algorithm
	 * @param root the initial state of the puzzle
	 */
	private void solve(Puzzle root) {
		PriorityQueue<PriorityObject> queue = new PriorityQueue<>();
		queue.add(new PriorityObject(root, 0, null));
		while (!queue.isEmpty()) {
			PriorityObject tmp = queue.poll();
			if (tmp.board.isCompleted()) {
				this.winner = tmp;
				return;
			}
			for (Puzzle p : tmp.board.getAdjacents()) {
				if (tmp.prevObject ==null || !tmp.prevObject.board.equals(p) ) {
					queue.add(new PriorityObject(p, tmp.depth+1, tmp));
				}
			}
		}
	}

	/**
	 *
	 * @return the number of moves to solve the puzzle
	 */
	public int getMoves() {
		if (winner != null) {
			return winner.depth;
		}else {
			return -1;
		}
		

	}

	/**
	 *
	 * @return the sequence of intermediate board states in the path of the shortest solution
	 */
	public Iterable<Puzzle> getSolution() {
		Deque<Puzzle> solution = new ArrayDeque<>();
		PriorityObject tmp = winner;
		while (tmp != null) {
			solution.addFirst(tmp.board);
			tmp = tmp.prevObject;
		}
		return solution;
	}

	/**
	 * wrapper class for board with priority and previous board
	 * 	- priority = move count + manhattan distance
	 * 	- prevObject = previous board
	 * 	- board = current board
	 * 	- depth = move count
	 */
	private static class PriorityObject implements Comparable<PriorityObject> {
		int hash;
		int depth;
		private final PriorityObject prevObject ;
		private final int f;
		private final Puzzle board;
		public PriorityObject(Puzzle board, int g, PriorityObject prev) {
			this.hash = board.hashCode();
			this.board = board;
			this.prevObject = prev;
			this.f = g + board.h();
			this.depth = g;

		}
		public int compareTo(PriorityObject other) {
			return this.f - other.f;
		}

		@Override
		public int hashCode() {
			return this.hash;
		}
	}



	public static void main(String[] args) throws IOException {

		File input = new File("input.txt");// input file
		Scanner scanner  = new Scanner(input);
		int n = scanner.nextInt(); // dimension of the puzzle
		int[][] inputTiles = new int[n][n]; // input tiles

		// read input tiles
		for (int i= 0;i< n;i++)
			for(int j=0; j< n; j++)
				inputTiles[i][j] = scanner.nextInt();

		var initial = new Puzzle(inputTiles); // initial state of the puzzle
		System.out.println(initial); // print initial state
		scanner.close();

		File output = new File("output.txt");// output file
		output.createNewFile(); // create new file if it doesn't exist

		if (!initial.isSolvable()) { // if the puzzle is not solvable
			try (PrintStream write = new PrintStream(output)) {
				write.println("Board is unsolvable"); // print unsolvable
			}
			return;
		}
		Solver solver = new Solver(initial); // else solve the puzzle

		// print the solution
		try (PrintStream write = new PrintStream(output)) {
			// if solver.getMoves() == -1  puzzle is not solvable
			write.println("Minimum number of moves = " + solver.getMoves());

			for (Puzzle board : solver.getSolution())
				write.println(board);
		}

	}
}

