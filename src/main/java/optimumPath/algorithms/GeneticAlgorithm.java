package optimumPath.algorithms;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
	public ArrayList<Genome> genomes;
	public ArrayList<Genome> lastGenerationGenomes;

	public int populationSize = 80;
	public double crossoverRate = 0.7f;
	public double mutationRate = 0.001f;
	public int chromosomeLength = 72;
	public int geneLength = 2;
	public int fittestGenome;
	public double bestFitnessScore;
	public double totalFitnessScore;
	public int generation;
	public MazeController mazeController;
	public MazeController mazeControllerDisplay;
	public boolean busy;
	
	public Random rnd = new Random();

	public GeneticAlgorithm(MazeController mazeController, int geneLength) {
		busy = false;
		genomes = new ArrayList<Genome>();
		lastGenerationGenomes = new ArrayList<Genome>();
		this.mazeController = mazeController;
		this.geneLength = geneLength;
	}

	public void Mutate(ArrayList<Integer> bits) {
		for (int i = 0; i < bits.size(); i++) {
			// flip this bit?
			
			if (rnd.nextDouble() < mutationRate) {
				// flip the bit
				bits.set(i, bits.get(i) == 0 ? 1 : 0);
			}
		}
	}

	public void Crossover(ArrayList<Integer> mom, ArrayList<Integer> dad, ArrayList<Integer> baby1, ArrayList<Integer> baby2) {
		if (rnd.nextDouble() > crossoverRate || mom == dad) {
			baby1.addAll(mom);
			baby2.addAll(dad);

			return;
		}

		int crossoverPoint = rnd.nextInt(chromosomeLength - 1);

		for (int i = 0; i < crossoverPoint; i++) {
			baby1.add(mom.get(i));
			baby2.add(dad.get(i));
		}
			
		for (int i = crossoverPoint; i < mom.size(); i++) {
			baby1.add(dad.get(i));
			baby2.add(mom.get(i));
		}
	}

	public Genome RouletteWheelSelection() {
		double slice = rnd.nextDouble() * totalFitnessScore;
		double total = 0;
		int selectedGenome = 0;

		for (int i = 0; i < populationSize; i++) {
			total += genomes.get(i).getFitness();

			if (total > slice) {
				selectedGenome = i;
				break;
			}
		}
		return genomes.get(selectedGenome);
	}

	public void UpdateFitnessScores() {
		fittestGenome = 0;
		bestFitnessScore = 0;
		totalFitnessScore = 0;

		for (int i = 0; i < populationSize; i++) {
			ArrayList<Integer> directions = Decode(genomes.get(i).getBits());

			genomes.get(i).setFitness(mazeController.TestRoute(directions));

			totalFitnessScore += genomes.get(i).getFitness();

			if (genomes.get(i).getFitness() > bestFitnessScore) {
				bestFitnessScore = genomes.get(i).getFitness();
				fittestGenome = i;

				// Has chromosome found the exit?
				if (genomes.get(i).getFitness() == 1) {
					busy = false; // stop the run
					return;
				}
			}
		}
	}

	//---------------------------Decode-------------------------------------
	//
	//	decodes a List of bits into a List of directions (ints)
	//
	//	0=North, 1=South, 2=East, 3=West
	//-----------------------------------------------------------------------
	public ArrayList<Integer> Decode(ArrayList<Integer> bits) {
		ArrayList<Integer> directions = new ArrayList<Integer>();

		for (int geneIndex = 0; geneIndex < bits.size(); geneIndex += geneLength) {
			ArrayList<Integer> gene = new ArrayList<Integer>();

			for (int bitIndex = 0; bitIndex < geneLength; bitIndex++) {
				gene.add(bits.get(geneIndex + bitIndex));
			}

			directions.add(GeneToInt(gene));
		}
		return directions;
	}

	//-------------------------------GeneToInt-------------------------------
	//	converts a List of bits into an integer
	//----------------------------------------------------------------------
	public int GeneToInt(ArrayList<Integer> gene) {
		int value = 0;
		int multiplier = 1;

		for (int i = gene.size(); i > 0; i--) {
			value += gene.get(i - 1) * multiplier;
			multiplier *= 2;
		}
		return value;
	}

	public void CreateStartPopulation() {
		genomes.clear();

		for (int i = 0; i < populationSize; i++) {
			Genome baby = new Genome(chromosomeLength);
			genomes.add(baby);
		}
	}

	public void Run() {
		CreateStartPopulation();
		busy = true;
	}

	public void Epoch() {
		if (!busy) return;
		UpdateFitnessScores ();

		if (!busy) {
			lastGenerationGenomes.clear();
			lastGenerationGenomes.addAll(genomes);
			return;
		}
		
		int numberOfNewBabies = 0;

		ArrayList<Genome> babies = new ArrayList<Genome> ();
		while (numberOfNewBabies < populationSize) {
			// select 2 parents
			Genome mom = RouletteWheelSelection ();
			Genome dad = RouletteWheelSelection ();
			Genome baby1 = new Genome();
			Genome baby2 = new Genome();
			Crossover(mom.getBits(), dad.getBits(), baby1.getBits(), baby2.getBits());
			Mutate(baby1.getBits());
			Mutate(baby2.getBits());
			babies.add(baby1);
			babies.add(baby2);

			numberOfNewBabies += 2;
		}

		// save last generation for display purposes
		lastGenerationGenomes.clear();
		lastGenerationGenomes.addAll(genomes);
		// overwrite population with babies
		genomes = babies;

		// increment the generation counter
		generation++;
		
	}

	public ArrayList<Genome> getGenomes() {
		return genomes;
	}

	public void setGenomes(ArrayList<Genome> genomes) {
		this.genomes = genomes;
	}

	public int getGeneLength() {
		return geneLength;
	}

	public void setGeneLength(int geneLength) {
		this.geneLength = geneLength;
	}

	public int getFittestGenome() {
		return fittestGenome;
	}

	public void setFittestGenome(int fittestGenome) {
		this.fittestGenome = fittestGenome;
	}

	public double getBestFitnessScore() {
		return bestFitnessScore;
	}

	public void setBestFitnessScore(double bestFitnessScore) {
		this.bestFitnessScore = bestFitnessScore;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	/************************
	 * Getters Setters
	 ************************/
	
	
}
