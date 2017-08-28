import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 * Generates collection of integers from sampling a uniform distribution.
 * 
 * @author jkcchan
 */
public class DataGeneratorLettisia {
	/** Program name. */
	protected static final String progName = "IntegerGenerator";
	private static final String wordFile = "words_alpha.txt";

	/** Random generator to use. */
	static Random mRandGen;

	private static ArrayList<String> words = new ArrayList<>();

	public DataGeneratorLettisia() {
		mRandGen = new Random(System.currentTimeMillis());
		readWordList();
	}
	
	public static void main(String[] args) {
		int poolSize = 1000; // guaranteed number of distinct nodes in the
								// structure
		int finalSize = 5000; // actual final size of all files
		int extras = poolSize/4; // number of extra unseen words for the testing files
		String fileStub = "1000nodeinput2"; // name for all initial data files
		int numTrials = 1;// number of sets of data sets to create
		mRandGen = new Random(System.currentTimeMillis()); // Pick any seed to
															// get the same
															// output each time
		readWordList(); // grab the dictionary from the file

		// create data files for initial data structures
		ArrayList<ArrayList<String>> allSamples = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < numTrials; i++) {
			String fileName = fileStub + Integer.toString(i + 1) + ".txt";
			allSamples.add(createStartingDataSets(poolSize, finalSize, extras, fileName));
		}

		// create a testing file with various proportions of add, remove one and
		// search in that order
		double[][] props = { 
				{ 1, 0, 0 }, 
				{ 0, 1, 0 }, 
				{ 0, 0, 1 }, 
				{ 0.5, 0, 0.5 }, 
				{ 0.5, 0.5, 0 }, 
				{ 0, 0.5, 0.5 },
				{ 0.75, 0.25, 0 }, 
				{ .25, .75, 0 }, 
				{ .375, .375, .25 }, 
				{ .125, .125, .75 }, 
				{ .25, .25, .5 } };
		
			for (int i = 0; i < props.length; i++) {
			createTestingSets(allSamples.get(0), finalSize, extras, props[i],
					"Test" + Integer.toString(i + 1) + "_" + Integer.toString(finalSize) + "_A_"
							+ Double.toString(props[i][0]) + "_RO_" + Double.toString(props[i][1]) + "_S_"
							+ Double.toString(props[i][2]) + ".txt");
		}
	}	// end of main()
	
	/**
	 * Generate one sample, using sampling with replacement.
	 */
	public static int sampleWithReplacement(int mStartOfRange, int mEndOfRange) {
		return mRandGen.nextInt(mEndOfRange - mStartOfRange) + mStartOfRange;
	} // end of sampleWithReplacement()

	/**
	 * Generate 'sampleSize' number of samples, using sampling with replacement.
	 * 
	 * @param sampleSize
	 *            Number of samples to generate.
	 */
	public static int[] sampleWithReplacement(int sampleSize, int mStartOfRange, int mEndOfRange) {
		int[] samples = new int[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			samples[i] = sampleWithReplacement(mStartOfRange, mEndOfRange);
		}

		return samples;
	} // end of sampleWithReplacement()

	/**
	 * Sample without replacement, using "Algorithm R" by Jeffrey Vitter, in
	 * paper "Random sampling without a reservoir". This algorithm has O(size of
	 * range) time complexity.
	 * 
	 * @param sampleSize
	 *            Number of samples to generate.
	 * @throws IllegalArgumentException
	 *             When sampleSize is greater than the valid integer range.
	 */
	public static int[] sampleWithOutReplacement(int sampleSize, int mStartOfRange, int mEndOfRange)
			throws IllegalArgumentException {
		int populationSize = mEndOfRange - mStartOfRange + 1;

		if (sampleSize > populationSize) {
			throw new IllegalArgumentException(
					"SampleSize cannot be greater than populationSize for sampling without replacement.");
		}

		int[] samples = new int[sampleSize];
		// fill it with initial values in the range
		for (int i = 0; i < sampleSize; i++) {
			samples[i] = i + mStartOfRange;
		}

		// replace
		for (int j = sampleSize; j < populationSize; j++) {
			int t = mRandGen.nextInt(j + 1);
			if (t < sampleSize) {
				samples[t] = j + mStartOfRange;
			}
		}

		return samples;
	} // end of sampleWithOutReplacement()

	public static String[] allA(int length) {
		String[] array = new String[length];
		for (int i = 0; i < length; i++) {
			array[i] = "A";
		}
		return array;
	}

	/**
	 * Sum(propAROS) = 1
	 * 
	 * @param length
	 * @param propAROS
	 *            int[] each value should be between 0-1 and represents the
	 *            proportion of that command in return set. The three values
	 *            should add up to 1
	 */
	public static String[] randomAROS(int length, double[] propAROS) {
		Random rand = new Random(System.currentTimeMillis());
		String[] array = new String[length];

		for (int i = 0; i < length; i++) {
			double randNum = rand.nextDouble();
			if (randNum < propAROS[0]) {
				array[i] = "A";
			} else if (randNum < propAROS[0] + propAROS[1]) {
				array[i] = "RO";
			} else {
				array[i] = "S";
			}
		}
		return array;
	}

	public static ArrayList<String> generateSamples(ArrayList<String> list, String samplingType, int sampleSize) {
		int[] samples = null;
		switch (samplingType) {
		// sampling with replacement
		case "with":
			samples = sampleWithReplacement(sampleSize, 0, list.size() - 1);
			break;
		// sampling without replacement
		case "without":
			samples = sampleWithOutReplacement(sampleSize, 0, list.size() - 1);
			break;
		default:
			System.err.println(samplingType + " is an unknown sampling type.");
		}

		ArrayList<String> result = new ArrayList<>(samples.length);
		for (int i = 0; i < samples.length; i++) {
			result.add(list.get(samples[i]));
		}
		return result;
	}

	public static void readWordList() {
		try {
			Scanner in = new Scanner(new File(wordFile));
			while (in.hasNextLine()) {
				words.add(in.nextLine());
			}
			System.out.println("Size of Words: " + words.size());
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("cannot find words_alpha.txt");
			System.exit(0);
		}
	}

	public static void writeDataFile(ArrayList<String> samples, String[] commands, String outFilename) {
		try {
			PrintWriter outFile = new PrintWriter(outFilename);

			if (samples.size() != commands.length) {
				outFile.close();
				System.err.println("Sample size does not match length of command array.");
				System.exit(0);
			}

			for (int i = 0; i < samples.size(); i++) {
				outFile.println(commands[i] + " " + samples.get(i));
			}

			// outFile.println("P");
			outFile.close();
		} catch (FileNotFoundException e) {
			System.err.println("cannot find words_alpha.txt");
			System.exit(0);
		}
	}

	public static ArrayList<String> createStartingDataSets(int poolSize, int finalSize, int extras,
			String outFilename) {
		ArrayList<String> samples = generateSamples(words, "without", poolSize + extras);
		ArrayList<String> originals = new ArrayList<>();
		originals.addAll(samples);
		for (int i = 0; i < extras; i++) {
			samples.remove(0); // takes out all the extras
		}
		samples.addAll(generateSamples(samples, "with", finalSize - poolSize));
		String[] commands = allA(samples.size());

		writeDataFile(samples, commands, outFilename);
		Collections.sort(samples);
		writeDataFile(samples, commands, "sorted" + outFilename);
		Collections.reverse(samples);
		writeDataFile(samples, commands, "reverse" + outFilename);
		System.out.println("Sample size: " + samples.size());
		return originals;
	}

	public static void createTestingSets(ArrayList<String> samples, int finalSize, int extras, double[] propAROS,
			String outFilename) {
		ArrayList<String> tester = new ArrayList<>();
		tester.addAll(samples);
		// tester.addAll(generateSamples(words, "without", extras));
		tester.addAll(generateSamples(tester, "with", finalSize - tester.size()));
		String[] commands = randomAROS(tester.size(), propAROS);
		Collections.shuffle(tester);
		writeDataFile(tester, commands, outFilename);
		System.out.println("Tester size: " + tester.size());
	}


} // end of class DataGenerator