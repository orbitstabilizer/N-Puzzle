
package project;

import java.util.Arrays;
import java.util.Stack;


/**
 * 
 * @author Yusuf Akin
 *
 */
public class Puzzle {
	/**
	 * the board of the puzzle
	 */
	private final int[][] tiles;
	/**
	 * the size of the puzzle
	 */
	private final int dim;
	/**
	 * x coordinate of the blank tile
	 */
	private int blankX;
	/**
	 * y coordinate of the blank tile
	 */
	private int blankY;

	/**
	 * @param tiles the board of the puzzle
	 */
	public Puzzle(int[][] tiles) {
		this.dim = tiles.length;
		this.tiles = copy(tiles);


	}

	/**
	 * using the inversion count and position of the blank tile, determines if the puzzle is solvable
	 * @return if the puzzle is solvable
	 */
	public boolean isSolvable() {
		int N = dim;
		int inversions = 0;
		// count the number of inversions in the board, excluding the blank tile
		for (int i = 0; i < N * N - 1; i++){
			for (int j = i + 1; j < N * N; j++){
				// i variable scans the board from top to bottom and left to right
				// i/N is the row number of the tile
				// i%N is the column number of the tile
				// ex. if i = 5 && N = 3, i/N = 1, i%N = 2 => the tile is in row 2, column 3
				if (tiles[j/N][j%N]!=0 && tiles[i/N][i%N]!=0 && tiles[i/N][i%N] > tiles[j/N][j%N])
					inversions++;
			}
		}
		// if the board dimension is even,
		if (N%2 == 0) {
			// if the number of inversions is odd
			// and the blank tile is at an even row counting from the bottom,
			// the puzzle is solvable
			if (inversions%2== 1 && (N-blankY)%2==0) {
				return true;
			}
			// if the number of inversions is even
			// and the blank tile is at an odd row counting from the bottom,
			// the puzzle is solvable
			else return inversions % 2 == 0 && (N - blankY) % 2 == 1;
		}
		// if the board dimension is odd,
		else {
			// and if the number of inversions is even,
			// the puzzle is solvable
			return inversions % 2 == 0;

		}

		// in all other cases, the puzzle is unsolvable

	}

	/**
	 *
	 * @return string representation of the board
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(tiles.length).append("\n");
		for (int[] tile : tiles) {
			for (int i : tile) {
				str.append(" ").append(i);
			}
			str.append("\n");
		}
		return str.toString();

	}

	/**
	 *
	 * @return the dimension of the puzzle
	 */
	public int dimension() {
		return this.dim;
	}


	/**
	 *
	 * @return sum of Manhattan distances between tiles and goal
	 */
	public int h() {
		int tmp = 0;
		for (int r = 0 ;r< dimension() ;r++) {
			for (int c = 0 ;c< dimension(); c++) {
				int num = this.tiles[r][c];
				if (num == 0) continue; // skip blank tile
				tmp+= Math.abs((num-1)%dim-c) +  Math.abs((num-1)/dim-r);
			}

		}
		return tmp;
	}

	/**
	 * if manhattan distance is 0, then the puzzle is at the goal state
	 * @return if the puzzle is in the goal state
	 */
	public boolean isCompleted() {
		return h() == 0;

	}


	/**
	 * keeps the possible directions to move the blank tile
	 */
	private enum Direction{
		l(0,-1),
		r(0,1),
		u(-1,0),
		d(1,0);
		

		final int x;
		final int y;
		Direction(int x, int y){
			this.x = x;
			this.y = y;
		}
	}

	/**
	 *
	 * @return the adjacent states of the current state, in the order of right, left, up, down, - if possible
	 */
	public Iterable<Puzzle> getAdjacents() {
		Stack<Puzzle> mvs = new Stack<>();
		for(var move : Direction.values()) {
			int nx = move.x + blankX;
			int ny = move.y + blankY;
			if (nx>=0 && nx <dim && ny >= 0 && ny<dim) {
				int tmp = tiles[ny][nx];
				tiles[ny][nx] = tiles[blankY][blankX];
				tiles[blankY][blankX] = tmp;
				mvs.add(new Puzzle(tiles));
				tiles[blankY][blankX] = tiles[ny][nx];
				tiles[ny][nx] = tmp;
			}
		}
		return mvs;
	}

	/**
	 *
	 * @param source the board to be copied
	 * @return a copy of the source board
	 */
	private int[][] copy(int[][] source) {
		int[][] tmp = new int[source.length][source[0].length];
		for (int r = 0 ;r< dimension() ;r++) {
			for (int c = 0 ;c< dimension(); c++) {
				if (source[r][c] == 0) {
					blankX = c;
					blankY = r;
				}
				tmp[r][c] = source[r][c];
			}
		}

		return tmp;

	}

	/**
	 * this method is used to prevent unnecessary moves in the search
	 * @param o the object to be compared
	 * @return if the two objects are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Puzzle puzzle = (Puzzle) o;
		return Arrays.deepEquals(tiles, puzzle.tiles);
	}
}

