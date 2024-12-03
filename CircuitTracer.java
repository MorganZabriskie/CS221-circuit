import java.awt.Point;
import java.util.ArrayList;

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
		//TODO: parse and validate command line args - first validation provided
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}
		if((!(args[0] == "-s") || !(args[0] == "-q")) || (!(args[1] == "-c") || !(args[1] == "-g"))) {
			printUsage();
			return;
		}
		if (args[1] == "-g") {
			System.out.println("GUI mode not supported. Please run in console mode.");
			return;
		}
		//TODO: initialize the Storage to use either a stack or queue
		if(args[0] == "-s") {
			//call stack algorithm
		}

		if(args[0] == "-q") {
			//call queue algorithm
		}
		//TODO: read in the CircuitBoard from the given file
		//TODO: run the search for best paths
		//TODO: output results to console or GUI, according to specified choice
	}
	
} // class CircuitTracer
