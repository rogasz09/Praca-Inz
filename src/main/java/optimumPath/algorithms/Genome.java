package optimumPath.algorithms;

import java.util.ArrayList;
import java.util.Random;

public class Genome {
	private ArrayList<Integer> bits;
	private double fitness;

	public Genome() {
		Initialize ();
	}

	public Genome(int numBits) {
		Initialize ();

		for (int i = 0; i < numBits; i++) {
			Random rnd = new Random();

			bits.add(rnd.nextInt(1));
		}
	}

	private void Initialize() {
		fitness = 0;
		bits = new ArrayList<Integer>();
	}
	
	/************************
	 * Getters Setters
	 ************************/

	public ArrayList<Integer> getBits() {
		return bits;
	}

	public void setBits(ArrayList<Integer> bits) {
		this.bits = bits;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
}
