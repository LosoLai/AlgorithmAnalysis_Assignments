import java.io.*;
import java.util.*;

/**
 * 5 type of data structures Performance Analysis
 * 
 * @author jefcha
 * modified by Loso 21.08.2017
 */
public class PerformanceAnalysis
{
    protected static final String progName = "PerformanceAnalysis";

    /** Different modes that program can run. */
    /*public enum Mode {
      	BUB,
      	SELECT
    }*/

    
    /**
     * Print usage information.
     * 
     * @param progName Program name.
     */
    protected static void printUsage(String progName) {
      	System.err.println("USAGE: " + progName + " [data stracture] [input file]");
      	System.err.println("  data structure [linkedlist, sortedlinkedlist, bst, hash]");
      	System.err.println("EXAMPLE: " + progName + " hash random.txt");
    } // end of printUsage()


    /**
     * Main method.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            // not enough arguments
            if (args.length != 2) {
                printUsage(progName);
                System.exit(1);
            }
            
            // data structure to be used
            String dataUsed = args[0];
            // input file to be processed
            String fileName = null;
    		if (args.length == 2) {
    			fileName = args[1];
    		}

            // figure out which data structure we are using    		
            Multiset<String> implementType = null;
            switch(dataUsed) {
	            case "linkedlist":
	            	implementType = new LinkedListMultiset<String>();
					break;
				case "sortedlinkedlist":
					implementType = new SortedLinkedListMultiset<String>();
					break;
				case "bst":
					implementType = new BstMultiset<String>();
					break;
				case "hash":
					implementType = new HashMultiset<String>();
					break;
				case "baltree":
					implementType = new BalTreeMultiset<String>();
					break;
				default:
					System.err.println("Unknown implmementation type.");
                    printUsage(progName);
                    System.exit(1);
            }
            
            // construct in and output streams/writers/readers, then process each operation.
            long startTime = 0;
            long endTime = 0;
            
    		try {
    			FileReader in = new FileReader(new File(fileName));
    		    BufferedReader inReader = new BufferedReader(in);
    			PrintWriter searchOutWriter = new PrintWriter(System.out, true);
    			
    			//test 
    			//String line = inReader.readLine();
    			startTime = System.nanoTime();
    			// do the experiment (depends on text file)
    			// process the operations
    			MultisetTester.processOperations(inReader, searchOutWriter, implementType);
    			
    			endTime = System.nanoTime();
    		} catch (IOException e) {
    			System.err.println(e.getMessage());
    		}
            

            
            double timeElapsed = (endTime - startTime) / Math.pow(10, 9);
            System.out.println("Time elapsed (secs): " + timeElapsed);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            printUsage(progName);
        }

    } // end of main()
    	
} // end of class SortDemo1





