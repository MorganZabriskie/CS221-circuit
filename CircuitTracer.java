import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail, mzabriskie
 */
public class CircuitTracer {

	/** Launch the program. 
	 * 
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	private Storage<TraceState> stateStore;

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: $ java CircuitTracer [-s | -q] [-c | -g]");
		System.out.println("\t First argument:");
		System.out.println("\t\t -s --use a stack for storage");
		System.out.println("\t\t -q --use a queue for storage");
		System.out.println("\t Second argument:");
		System.out.println("\t\t -c --run program in console mode");
		System.out.println("\t\t -g --run program in GUI mode");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		//parse and validate command line args
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}
		if((!(args[0].equals("-s")) && !(args[0].equals("-q"))) || (!(args[1].equals("-c")) && !(args[1].equals("-g")))) {
			printUsage();
			return;
		}
		if (args[1].equals("-g")) {
			System.out.println("GUI mode not supported. Please run in console mode.");
			return;
		}

		// initialize storage to use stack or queue depending on input
		if(args[0].equals("-s")) {
			stateStore = Storage.getStackInstance();
		}
		if(args[0].equals("-q")) {
			stateStore = Storage.getQueueInstance();
		}

		//read in the CircuitBoard from the given file
		String fileName = args[2];
		try {
            CircuitBoard myBoard = new CircuitBoard(fileName);
			//System.out.println("Board is valid");
			//System.out.println(myBoard.toString());

			// run the search for best paths
			findPath(myBoard, stateStore);
			// TODO: output results to console or GUI, according to specified choice
        } catch (FileNotFoundException e) {
            System.out.println("File is not found");
        } catch (InvalidFileFormatException e) {
            System.out.println("InvalidFileFormat Exception. File is not in correct format. Please fix and try again.");
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException. File is not in correct format. Please fix and try again.");
        }	
	}

	//TODO: write method for search algorithm
	private void findPath(CircuitBoard startingBoard, Storage<TraceState> stateStore) {
		// initialize empty list
		List<TraceState> bestPaths = new ArrayList<TraceState>();

		// add TraceState object to stateStore for each open position adjacent to starting component
		int startRow = (int) startingBoard.getStartingPoint().getX();
		int startCol = (int) startingBoard.getStartingPoint().getY();
		//System.out.println("Starting row is " + startRow);
		//System.out.println("Starting col is: " + startCol);

		if (startingBoard.isOpen((startRow - 1), startCol)) {
			TraceState newPath = new TraceState(startingBoard, (startRow - 1), startCol);
			stateStore.store(newPath);
			//System.out.println("Created new path at " + (startRow - 1) + "," + startCol);
		}

		if (startingBoard.isOpen((startRow + 1), startCol)) {
			TraceState newPath = new TraceState(startingBoard, (startRow + 1), startCol);
			stateStore.store(newPath);
			//System.out.println("Created new path at " + (startRow + 1) + "," + startCol);
		}

		if (startingBoard.isOpen(startRow, (startCol - 1))) {
			TraceState newPath = new TraceState(startingBoard, startRow, (startCol - 1));
			stateStore.store(newPath);
			//System.out.println("Created new path at " + startRow + "," + (startCol - 1));
		}

		if (startingBoard.isOpen(startRow, (startCol + 1))) {
			TraceState newPath = new TraceState(startingBoard, startRow, (startCol + 1));
			stateStore.store(newPath);
			//System.out.println("Created new path at " + startRow + "," + (startCol + 1));
		}

		while(!stateStore.isEmpty()) {
			TraceState currentPath = stateStore.retrieve();
			//System.out.println("Current path is: ");
			//System.out.println(currentPath.getBoard());
			//System.out.println("Current path length is: " + currentPath.getPath().size());
			
			int currPathStartRow = currentPath.getRow();
			int currPathStartCol = currentPath.getCol();
			//System.out.println("Current path starting point is " + currPathStartRow + "," + currPathStartCol);
			
			if(currentPath.isSolution()) {
				//System.out.println("Current path is a solution!");
				//System.out.println("Path length is: " + currentPath.pathLength());
				if(bestPaths.isEmpty() || (currentPath.pathLength() == bestPaths.get(0).pathLength())) { 
					bestPaths.add(currentPath);
					//System.out.println("This path is as good as past paths, and has been added to bestPaths");
				} else if (currentPath.pathLength() < bestPaths.get(0).pathLength()) {
					bestPaths.clear();
					bestPaths.add(currentPath);
					//System.out.println("This is the new best path. bestPaths cleared and this path added");
				}
				//System.out.println("shortest path is " + bestPaths.get(0).pathLength());
				//System.out.println("bestPaths now contains: " + bestPaths.size() + " paths");
			} else {
				findOpenPaths(currentPath, currPathStartRow, currPathStartCol);
			}
			//System.out.println("-------------------");
			//System.out.println();
		}
		if(bestPaths.size() == 0) {
			System.out.println();
		} else {
			//System.out.println("Search is done. " + bestPaths.size() + " solution(s) found. Shortest path is " + bestPaths.get(0).pathLength());
			for(int i = 0; i < bestPaths.size(); i++) {
				System.out.println(bestPaths.get(i).toString());
			}
		}
	}
	
	private void findOpenPaths(TraceState currentPath, int startRow, int startCol) {
		if(currentPath.getBoard().isOpen((startRow - 1), startCol)) {
			TraceState newPath = new TraceState(currentPath, (startRow - 1), startCol);
			stateStore.store(newPath);
			//System.out.println("Created new path at " + (startRow - 1) + "," + startCol);
		}

		if (currentPath.getBoard().isOpen((startRow + 1), startCol)) {
			TraceState newPath = new TraceState(currentPath, (startRow + 1), startCol);
			stateStore.store(newPath);
			//System.out.println("Created new path at " + (startRow + 1) + "," + startCol);
		}

		if (currentPath.getBoard().isOpen(startRow, (startCol - 1))) {
			TraceState newPath = new TraceState(currentPath, startRow, (startCol - 1));
			stateStore.store(newPath);
			//System.out.println("Created new path at " + startRow + "," + (startCol -1));
		}

		if (currentPath.getBoard().isOpen(startRow, (startCol + 1))) {
			TraceState newPath = new TraceState(currentPath, startRow, (startCol + 1));
			stateStore.store(newPath);
			//System.out.println("Created new path at " + startRow + "," + (startCol + 1));
		}
	}
} // class CircuitTracer
