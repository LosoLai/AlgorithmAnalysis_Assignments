
import java.io.*;
import java.util.*;

/**
 * Framework to test the multiset implementations.
 * 
 * @author jkcchan
 */
public class MultiTester {
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

	public static void main(String[] args) {
		String[] startFileNames = new String[3];
		startFileNames[0] = "1000nodeinput1.txt";
		startFileNames[1] = "reverse1000nodeinput1.txt";
		startFileNames[2] = "sorted1000nodeinput1.txt";

		String[] testFileNames = new String[11];
		testFileNames[0] = "Test1_5000_A_1.0_RO_0.0_S_0.0.txt";
		testFileNames[1] = "Test2_5000_A_0.0_RO_1.0_S_0.0.txt";
		testFileNames[2] = "Test3_5000_A_0.0_RO_0.0_S_1.0.txt";
		testFileNames[3] = "Test4_5000_A_0.5_RO_0.0_S_0.5.txt";
		testFileNames[4] = "Test5_5000_A_0.5_RO_0.5_S_0.0.txt";
		testFileNames[5] = "Test6_5000_A_0.0_RO_0.5_S_0.5.txt";
		testFileNames[6] = "Test7_5000_A_0.75_RO_0.25_S_0.0.txt";
		testFileNames[7] = "Test8_5000_A_0.25_RO_0.75_S_0.0.txt";
		testFileNames[8] = "Test9_5000_A_0.375_RO_0.375_S_0.25.txt";
		testFileNames[9] = "Test10_5000_A_0.125_RO_0.125_S_0.75.txt";
		testFileNames[10] = "Test11_5000_A_0.25_RO_0.25_S_0.5.txt";

		HashMap<String, ArrayList<HashMap<String, Double>>> table = new HashMap<>();
		table.put(startFileNames[0], runTrials(startFileNames[0], testFileNames));
		table.put(startFileNames[1], runTrials(startFileNames[1], testFileNames));
		table.put(startFileNames[2], runTrials(startFileNames[2], testFileNames));

		try {
			PrintWriter out = new PrintWriter(new FileWriter("out_1000_node_5000_command.txt"), true);
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

	private static void printTable(ArrayList<HashMap<String, Double>> table, PrintWriter out) {
		Set<String> headings = table.get(0).keySet();
		for (String string : headings) {
			out.print(string + ", ");
		}
		out.println();

		Iterator<HashMap<String, Double>> it = table.iterator();
		while (it.hasNext()) {
			HashMap<String, Double> hashMap = (HashMap<String, Double>) it.next();

			for (String key : headings) {
				out.print(hashMap.get(key) + ", ");
			}
			out.println();
		}

	}

	public static ArrayList<HashMap<String, Double>> runTrials(String inFilename, String[] testFileNames) {
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Run tests
		ArrayList<HashMap<String, Double>> table = new ArrayList<>();
		for (Iterator<Multiset<String>> iterator = sets.iterator(); iterator.hasNext();) {
			Multiset<String> multiset = (Multiset<String>) iterator.next();
			table.add(runTests(testFileNames, multiset));
		}

		return table;
	}

	public static HashMap<String, Double> runTests(String[] inFilename, Multiset<String> multiset) {
		HashMap<String, Double> tableColumn = new HashMap<>();
		for (int i = 0; i < inFilename.length; i++) {
			tableColumn.put(inFilename[i], runTenTrials(inFilename[i], multiset));
		}
		return tableColumn;
	}

	public static double runTenTrials(String inFilename, Multiset<String> multiset) {
		double sum = 0;
		for (int i = 0; i < 50; i++) {
			sum += runOneTrial(inFilename, multiset);
		}
		return sum / 50;
	}

	public static double runOneTrial(String inFilename, Multiset<String> multiset) {
		long startTime = 0;
		long endTime = 0;

		// construct in and output streams/writers/readers, then process each
		// operation.
		try {
			Scanner inReader = new Scanner(new File(inFilename));

			// process the operations
			startTime = System.nanoTime();
			processOperations(inReader, multiset);
			endTime = System.nanoTime();

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

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		double timeElapsed = (endTime - startTime) / Math.pow(10, 9);
		System.out.println("Time elapsed (secs): " + timeElapsed);
		return timeElapsed;

	} // end of test()

} // end of class MultisetTester
