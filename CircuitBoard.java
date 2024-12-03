import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail, mzabriskie
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O';	//capital 'o', an open position
	private final char CLOSED = 'X';//a blocked position
	private final char TRACE = 'T';	//part of the trace connecting 1 to 2
	private final char START = '1';	//the starting component
	private final char END = '2';	//the ending component
	private final String ALLOWED_CHARS = "OXT12"; //useful for validating with indexOf

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot open or read the file
	 * @throws InvalidFileFormatException for any file formatting or content issue
	 */
	public CircuitBoard(String filename) throws FileNotFoundException, InvalidFileFormatException {
		Scanner fileScan = new Scanner(new File(filename));

		// initialize rows and columns from input file
		ROWS = fileScan.nextInt();
		System.out.println("Num rows is " + ROWS);
		COLS = fileScan.nextInt();
		System.out.println("Num cols is " + COLS);
		fileScan.nextLine();
		board = new char[ROWS][COLS];
		int oneCount = 0;
		int twoCount = 0;

		// read each row and store each character in the char[][]
		for(int i = 0; i < ROWS; i++) {
			String line = fileScan.nextLine();
			System.out.println("Data in line " + i + " is " + line);
			Scanner fileLineScan = new Scanner(line);
			fileLineScan.useDelimiter("\\s");

			for (int j = 0; j < COLS; j++) {
				String nextChar = fileLineScan.next();
				char tempChar = nextChar.charAt(0);
				System.out.println("Char at column " + j + " is " + tempChar);
				if(tempChar == OPEN || tempChar == CLOSED || tempChar == START || tempChar == END) { // check elements are allowed on board
					board[i][j] = tempChar;
					if(tempChar == START) {
						oneCount++;
					}
					if(tempChar == END) {
						twoCount++;
					}
				} else {
					fileLineScan.close();
					throw new InvalidFileFormatException("File contains elements that aren't allowed in circuit board");
				}
			}
			//check there aren't more columns than there should be
			if(fileLineScan.hasNext()) {
				fileLineScan.close();
				throw new InvalidFileFormatException("File doesn't have correct format");
			}
			fileLineScan.close();
		}
		// check there aren't too many rows
		if (fileScan.hasNext() || oneCount != 1 || twoCount != 1) {
			fileScan.close();
			throw new InvalidFileFormatException("File doesn't have correct format");
		}

		fileScan.close();
	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** Utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
