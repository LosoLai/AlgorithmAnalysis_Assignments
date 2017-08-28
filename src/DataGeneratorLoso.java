
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

public class DataGeneratorLoso {
	/** Program name. */
	protected static final String progName = "IntegerGenerator";
	private static final String wordFile = "words_alpha.txt";

	/** Random generator to use. */
	static Random mRandGen;

	private static ArrayList<String> words = new ArrayList<>();

	public DataGeneratorLoso() {
		mRandGen = new Random(System.currentTimeMillis());
		readWordList();
	}

	public static void main(String[] args) {
		// guaranteed number of distinct nodes in the initial structure
		int poolSize = 100000;

		// Size of initial structure after repetitions are added
		int initialSize = poolSize*5;

		// final size of command lists
		int finalSize = 50000;

		// number of extra unseen words for the testing files
		// poolSize /4 means 80% known and 20% unknown
		int extras = Math.round(poolSize / 4);

		// name for all initial data files
		String fileStub = Integer.toString(poolSize) + "nodeinput";

		String[] dataOrder = { "random", "order", "reverse" };

		// Pick any seed to get the same output each time
		mRandGen = new Random(System.currentTimeMillis());
		readWordList(); // grab the dictionary from the file

		// create data files for initial data structures
		ArrayList<ArrayList<String>> allSamples = new ArrayList<ArrayList<String>>();
		String fileName = fileStub + ".txt";
		createStartingDataSets(allSamples, poolSize, initialSize, extras, fileName);

		for (int i = 0; i < allSamples.size(); i++) {
			// create a testing file with various proportions of add, remove one
			// and
			// search in that order
			double[][] props = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }, { 0.5, 0, 0.5 }, { 0.5, 0.5, 0 },
					{ 0, 0.5, 0.5 }, { 0.75, 0.25, 0 }, { .25, .75, 0 }, { .375, .375, .25 }, { .125, .125, .75 },
					{ .25, .25, .5 } };

			ArrayList<String> sample = allSamples.get(i);

			for (int j = 0; j < props.length; j++) {
				String testingCase = "FixData" + Integer.toString(poolSize) + "_OpSize"
						+ Integer.toString(finalSize) + "_Test" + Integer.toString(j + 1) + "_" + dataOrder[i] + ".txt";
				createTestingSets(sample, finalSize, props[j], testingCase);
			}
		}
	} // end of main()

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
				String command = commands[i] + " " + samples.get(i);
				outFile.println(command);
			}

			// outFile.println("P");
			outFile.close();
		} catch (FileNotFoundException e) {
			System.err.println("cannot find words_alpha.txt");
			System.exit(0);
		}
	}

	public static void createStartingDataSets(ArrayList<ArrayList<String>> samplePool, int poolSize, int finalSize,
			int extras, String outFilename) {
		ArrayList<String> samples = generateSamples(words, "without", Math.max(poolSize + extras, words.size()));
		ArrayList<String> originals = new ArrayList<>();
		originals.addAll(samples);
		while (samples.size() > finalSize) {
			samples.remove(0); // takes out all the extras
		}
		samples.addAll(generateSamples(samples, "with", finalSize - poolSize));
		String[] commands = allA(samples.size());
		// random
		ArrayList<String> random = new ArrayList<>(samples);
		writeDataFile(samples, commands, "random" + outFilename);
		samplePool.add(random);
		// sorted
		Collections.sort(samples);
		ArrayList<String> sorted = new ArrayList<>(samples);
		writeDataFile(samples, commands, "sorted" + outFilename);
		samplePool.add(sorted);
		// reverse
		Collections.reverse(samples);
		ArrayList<String> reverse = new ArrayList<>(samples);
		writeDataFile(samples, commands, "reverse" + outFilename);
		System.out.println("Sample size: " + samples.size());
		samplePool.add(reverse);
	}

	public static void createTestingSets(ArrayList<String> samples, int finalSize, double[] propAROS,
			String outFilename) {
		ArrayList<String> tester = new ArrayList<>();
		if (finalSize <= samples.size()) {
			tester.addAll(generateSamples(samples, "with", finalSize));
		} else {
			tester.addAll(samples);
			// tester.addAll(generateSamples(words, "without", extras));
			tester.addAll(generateSamples(tester, "with", finalSize - tester.size()));
		}
		String[] commands = randomAROS(tester.size(), propAROS);
		// Collections.shuffle(tester);
		writeDataFile(tester, commands, outFilename);
		System.out.println("Tester size: " + tester.size());
	}

} // end of class DataGenerator
