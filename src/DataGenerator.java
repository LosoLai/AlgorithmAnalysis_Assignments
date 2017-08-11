import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Generates collection of integers from sampling a uniform distribution.
 * 
 * @author jkcchan
 */
public class DataGenerator {
	/** Program name. */
	protected static final String progName = "IntegerGenerator";
	private static final String wordFile = "words_alpha.txt";

	/** Start of integer range to generate values from. */
	protected int mStartOfRange;
	/** End of integer range to generate values from. */
	protected int mEndOfRange;
	/** Random generator to use. */
	Random mRandGen;

	/**
	 * Constructor.
	 * 
	 * @param startOfRange
	 *            Start of integer range to generate values.
	 * @param endOfRange
	 *            End of integer range to generate values.
	 * @throws IllegalArgumentException
	 *             If range of integers is inappropriate
	 */
	public DataGenerator(int startOfRange, int endOfRange) throws IllegalArgumentException {
		if (startOfRange < 0 || endOfRange < 0 || startOfRange > endOfRange) {
			throw new IllegalArgumentException("startOfRange or endOfRange is invalid.");
		}
		mStartOfRange = startOfRange;
		mEndOfRange = endOfRange;
		// use current time as seed
		mRandGen = new Random(System.currentTimeMillis());
	} // end of DataGenerator()

	/**
	 * Generate one sample, using sampling with replacement.
	 */
	public int sampleWithReplacement() {
		return mRandGen.nextInt(mEndOfRange - mStartOfRange) + mStartOfRange;
	} // end of sampleWithReplacement()

	/**
	 * Generate 'sampleSize' number of samples, using sampling with replacement.
	 * 
	 * @param sampleSize
	 *            Number of samples to generate.
	 */
	public int[] sampleWithReplacement(int sampleSize) {
		int[] samples = new int[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			samples[i] = sampleWithReplacement();
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
	public int[] sampleWithOutReplacement(int sampleSize) throws IllegalArgumentException {
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

	/**
	 * Error message.
	 */
	public static void usage() {
		System.err.println(progName
				+ ": <start of range to sample from> <end of range to sample from> <number of values to sample> <type of sampling> <out filename> [text or integer]");
		System.exit(1);
	} // end of usage()

	/**
	 * Main method.
	 */
	public static void main(String[] args) {

		// check correct number of command line arguments
		if (args.length < 5 || args.length > 6) {
			usage();
		}

		try {
			// integer range
			int startOfRange = Integer.parseInt(args[0]);
			int endOfRange = Integer.parseInt(args[1]);

			// number of values to sample
			int sampleSize = Integer.parseInt(args[2]);

			// type of sampling
			String samplingType = args[3];
			String outFilename = args[4];
			String text = args.length > 5 ? args[5] : "int";

			PrintWriter outFile = new PrintWriter(outFilename);

			DataGenerator gen = new DataGenerator(startOfRange, endOfRange);

			int[] samples = null;
			switch (samplingType) {
			// sampling with replacement
			case "with":
				samples = gen.sampleWithReplacement(sampleSize);
				break;
			// sampling without replacement
			case "without":
				samples = gen.sampleWithOutReplacement(sampleSize);
				break;
			default:
				System.err.println(samplingType + " is an unknown sampling type.");
				usage();
			}

			if (text.equals("text")) {
				try {
					Scanner in = new Scanner(new File(wordFile));

					ArrayList<String> words = new ArrayList<>();

					while (in.hasNextLine()) {
						words.add(in.nextLine());
					}

					System.out.println("Size of Words: " + words.size());

					in.close();

					for (int i = 0; i< samples.length; i++) {
						outFile.println("A " + words.get(samples[i]));
					}
					
					outFile.println("P");

				} catch (FileNotFoundException e) {
					System.err.println("cannot find words_alpha.txt");
					System.exit(0);
				}

			} else {
				// print integer samples to file
				if (samples != null) {
					for (int i = 0; i < samples.length; i++) {
						outFile.println(Integer.toString(samples[i]));
					}
				}
			}

			outFile.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			usage();
		}

	} // end of main()
} // end of class DataGenerator
