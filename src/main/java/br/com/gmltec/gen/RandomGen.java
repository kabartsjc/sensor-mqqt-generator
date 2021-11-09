package br.com.gmltec.gen;

import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.GaussianGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class RandomGen {
	
	private MersenneTwisterRNG rng;
	private NumberGenerator<Double> generator;
	
	public RandomGen(double mean, double stdv) {
		rng = new MersenneTwisterRNG();
		generator = new GaussianGenerator(mean, stdv, rng);
	}
	
	
	public double nextSample() {
		return generator.nextValue();
	}
	
	
	
}
