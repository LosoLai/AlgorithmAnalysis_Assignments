
import java.io.*;
import java.util.*;

/**
 * Framework to test the multiset implementations.
 * 
 * @author jkcchan
 * @author heavily modified by Lettisia George
 */
public class MultiTester {
	private static int numTrials = 10;

	/** Name of class, used in error messages. */
	protected static final String progName = "MultiTester";

	/** Standard outstream. */
	protected static final PrintStream outStream = System.out;

	/**
	 * Print help/usage message.
	 */
	public static void usage(String progName) {
		System.err.println(progName + ": <implementation> [inputfilename] [fileName to output search results to]");
		System.err.println("<implementation> = <linkedlist | sortedlinkedlist | bst| hash | baltree>");
		System.exit(1);
	} // end of usage

	/**
	 * Process the operation commands coming from inReader, and updates the
	 * multiset according to the operations.
	 * 
	 * @param inReader
	 *            Input reader where the operation commands are coming from.
	 * @param searchOutWriter
	 *            Where to output the results of search.
	 * @param multiset
	 *            The multiset which the operations are executed on.
	 * 
	 * @throws IOException
	 *             If there is an exception to do with I/O.
	 */
	public static void processOperations(Scanner inReader, Multiset<String> multiset) throws IOException {
		int lineNum = 1;
		boolean bQuit = false;

		// continue reading in commands until we either receive the quit signal
		// or there are no more input commands
		while (!bQuit && inReader.hasNextLine()) {
			String[] tokens = inReader.nextLine().split(" ");

			// check if there is at least an operation command
			if (tokens.length < 1) {
				System.err.println(lineNum + ": not enough tokens.");
				lineNum++;
				continue;
			}

			String command = tokens[0];
			// determine which operation to execute
			switch (command.toUpperCase()) {
			// add
			case "A":
				if (tokens.length == 2) {
					multiset.add(tokens[1]);
				} else {
					System.err.println(lineNum + ": not enough tokens.");
				}
				break;
			// search
			case "S":
				if (tokens.length == 2) {
					@SuppressWarnings("unused")
					int foundNumber = multiset.search(tokens[1]);
					// searchOutWriter.println(tokens[1] + " " + foundNumber);
				} else {
					// we print -1 to indicate error for automated testing
					// searchOutWriter.println(-1);
					// System.err.println(lineNum + ": not enough tokens.");
				}
				break;
			// remove one instance
			case "RO":
				if (tokens.length == 2) {
					multiset.removeOne(tokens[1]);
				} else {
					System.err.println(lineNum + ": not enough tokens.");
				}
				break;
			// remove all instances
			case "RA":
				if (tokens.length == 2) {
					multiset.removeAll(tokens[1]);
				} else {
					System.err.println(lineNum + ": not enough tokens.");
				}
				break;
			// print
			case "P":
				multiset.print(outStream);
				// multiset.print(searchOutWriter);
				break;
			// quit
			case "Q":
				bQuit = true;
				break;
			default:
				System.err.println(lineNum + ": Unknown command.");
			}

			lineNum++;
		}

	} // end of processOperations()

	/**
	 * Main class - where it all starts
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Here are all the parameters
		// Number of times to run each test
		numTrials = 10;
		// The files with all add commands to set up the data structures
		String[] startFileNames = new String[3];
		startFileNames[0] = "10000nodeinput1.txt";
		startFileNames[1] = "reverse10000nodeinput1.txt";
		startFileNames[2] = "sorted10000nodeinput1.txt";

		// The files with the lists of commands
		String[] testFileNames = new String[11];
		testFileNames[0] = "Test1_50000_A_1.0_RO_0.0_S_0.0.txt";
		testFileNames[1] = "Test2_50000_A_0.0_RO_1.0_S_0.0.txt";
		testFileNames[2] = "Test3_50000_A_0.0_RO_0.0_S_1.0.txt";
		testFileNames[3] = "Test4_50000_A_0.5_RO_0.0_S_0.5.txt";
		testFileNames[4] = "Test5_50000_A_0.5_RO_0.5_S_0.0.txt";
		testFileNames[5] = "Test6_50000_A_0.0_RO_0.5_S_0.5.txt";
		testFileNames[6] = "Test7_50000_A_0.75_RO_0.25_S_0.0.txt";
		testFileNames[7] = "Test8_50000_A_0.25_RO_0.75_S_0.0.txt";
		testFileNames[8] = "Test9_50000_A_0.375_RO_0.375_S_0.25.txt";
		testFileNames[9] = "Test10_50000_A_0.125_RO_0.125_S_0.75.txt";
		testFileNames[10] = "Test11_50000_A_0.25_RO_0.25_S_0.5.txt";

		HashMap<String, HashMap<String, HashMap<String, Double>>> table = new HashMap<>();
		table.put(startFileNames[0], runTrials(startFileNames[0], testFileNames));
		table.put(startFileNames[1], runTrials(startFileNames[1], testFileNames));
		table.put(startFileNames[2], runTrials(startFileNames[2], testFileNames));
		
		
		// Run the tests and print the results to a file
		try {
			PrintWriter out = new PrintWriter(new FileWriter("out_10000_node_50000_command.txt"), true);
			Iterator<String> it = table.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				out.println(key);
				printTable(table.get(key), out);
				out.println();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void printTable(HashMap<String, HashMap<String, Double>> table, PrintWriter out) {
		Set<String> keys = table.keySet();
		String[] skeys = new String[keys.size()];
		keys.toArray(skeys);
		HashMap<String, Double> first = table.get(skeys[0]);
		Set<String> headings = first.keySet();
		out.print(" , ");
		for (String string : headings) {
			out.print(string + ", ");
		}
		out.println();

		Iterator<String> it = table.keySet().iterator();
		while (it.hasNext()) {
			String ikey = (String) it.next();
			out.print(ikey + ", ");
			HashMap<String, Double> hashMap = table.get(ikey);
			for (String jkey : headings) {
				out.print(hashMap.get(jkey) + ", ");
			}
			out.println();
		}

	}

	public static HashMap<String, HashMap<String, Double>> runTrials(String inFilename, String[] testFileNames) {
		// Setup the multisets
		ArrayList<Multiset<String>> sets = new ArrayList<>();
		sets.add(new LinkedListMultiset<String>());
		sets.add(new HashMultiset<String>());
		sets.add(new BstMultiset<String>());
		sets.add(new BalTreeMultiset<String>());
		sets.add(new SortedLinkedListMultiset<String>());

		for (Iterator<Multiset<String>> iterator = sets.iterator(); iterator.hasNext();) {
			Multiset<String> multiset = (Multiset<String>) iterator.next();
			try {
				Scanner inReader = new Scanner(new File(inFilename));
				processOperations(inReader, multiset);
				inReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Run tests
		HashMap<String, HashMap<String, Double>> table = new HashMap<>();
		for (Iterator<Multiset<String>> iterator = sets.iterator(); iterator.hasNext();) {
			Multiset<String> multiset = (Multiset<String>) iterator.next();
			table.put(multiset.getClass().getSimpleName(), runTests(testFileNames, multiset));
		}

		return table;
	}

	public static HashMap<String, Double> runTests(String[] inFilename, Multiset<String> multiset) {
		HashMap<String, Double> tableColumn = new HashMap<>();
		for (int i = 0; i < inFilename.length; i++) {
			tableColumn.put(inFilename[i], runManyTrials(inFilename[i], multiset));
		}
		return tableColumn;
	}

	public static double runManyTrials(String inFilename, Multiset<String> multiset) {
		double sum = 0;
		for (int i = 0; i < numTrials; i++) {
			sum += runOneTrial(inFilename, multiset);
		}
		return sum / numTrials;
	}

	public static double runOneTrial(String inFilename, Multiset<String> multiset) {
		long startTime = 0;
		long endTime = 0;

		try {
			Scanner inReader = new Scanner(new File(inFilename));

			// process the operations
			startTime = System.nanoTime();
			processOperations(inReader, multiset);
			endTime = System.nanoTime();
			inReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		double timeElapsed = (endTime - startTime) / Math.pow(10, 9);
		System.out.println("Time elapsed (secs): " + timeElapsed);
		return timeElapsed;
	}

	/**
	 * Main method. Determines which implementation to test.
	 */
	public static double test(String[] args) {

		// check number of command line arguments
		if (args.length > 3 || args.length < 1) {
			System.err.println("Incorrect number of arguments.");
			usage(progName);
		}

		String implementationType = args[0];

		// String searchOutFilename = null;
		String inFilename = null;
		if (args.length == 2) {
			// searchOutFilename = args[1];
		} else if (args.length == 3) {
			// searchOutFilename = args[2];
			inFilename = args[1];
		}

		// determine which implementation to test
		Multiset<String> multiset = null;
		switch (implementationType) {
		case "linkedlist":
			multiset = new LinkedListMultiset<String>();
			break;
		case "sortedlinkedlist":
			multiset = new SortedLinkedListMultiset<String>();
			break;
		case "bst":
			multiset = new BstMultiset<String>();
			break;
		case "hash":
			multiset = new HashMultiset<String>();
			break;
		case "baltree":
			multiset = new BalTreeMultiset<String>();
			break;
		default:
			System.err.println("Unknown implmementation type.");
			usage(progName);
		}

		long startTime = 0;
		long endTime = 0;

		// construct in and output streams/writers/readers, then process each
		// operation.
		try {
			// BufferedReader inReader = new BufferedReader(new
			// InputStreamReader(System.in));
			Scanner inReader = new Scanner(new File(inFilename));
			// PrintWriter searchOutWriter = new PrintWriter(System.out, true);

			// if (searchOutFilename != null) {
			// searchOutWriter = new PrintWriter(new
			// FileWriter(searchOutFilename), true);
			// }
			// process the operations
			startTime = System.nanoTime();
			processOperations(inReader, multiset);
			endTime = System.nanoTime();
			inReader.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		double timeElapsed = (endTime - startTime) / Math.pow(10, 9);
		System.out.println("Time elapsed (secs): " + timeElapsed);
		return timeElapsed;

	} // end of test()

} // end of class MultisetTester
