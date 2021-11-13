package br.com.gmltec.scenario;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.GaussianGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class RandomGen {

	public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String lower = upper.toLowerCase(Locale.ROOT);
	public static final String digits = "0123456789";
	public static final String alphanum = upper + lower + digits;

	private MersenneTwisterRNG rng;
	private NumberGenerator<Double> generator;

	public RandomGen(double mean, double stdv) {
		rng = new MersenneTwisterRNG();
		generator = new GaussianGenerator(mean, stdv, rng);
	}

	public double nextSample() {
		return generator.nextValue();
	}

	public static double generateDouble(double max_value, double min_value) {
		Random rand = new SecureRandom();
		double value = rand.nextDouble() * (max_value - min_value) + min_value;
		return value;
	}

	public static String generateRandomString(int string_size) {
		String rStr = upper + lower + digits;
		Random rand = new SecureRandom();
		char[] symbols = rStr.toCharArray();
		char[] buf = new char[7];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = symbols[rand.nextInt(symbols.length)];
		}

		return new String(buf);
	}

}
