package optimumPath.algorithms;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
	public ArrayList<Genome> genomes;
	public ArrayList<Genome> lastGenerationGenomes;

	public int populationSize = 80;
	public double crossoverRate = 0.7f;
	public double mutationRate = 0.015f;
	public int chromosomeLength = 72;
	public int geneLength = 2;
	public int fittestGenome;
	public double bestFitnessScore;
	public double totalFitnessScore;
	public int generation;
	public MazeController mazeController;
	private boolean busy;
	private boolean elitism;
	
	public Random rnd = new Random();

	public GeneticAlgorithm(MazeController mazeController, int geneLength) {
		busy = false;
		elitism = true;
		genomes = new ArrayList<Genome>();
		lastGenerationGenomes = new ArrayList<Genome>();
		this.mazeController = mazeController;
		this.geneLength = geneLength;
	}

	public void Mutate(ArrayList<Integer> bits) {
		for (int i = 0; i < bits.size(); i++) {
			// czy zanegowaæ bit?
			
			if (rnd.nextDouble() < mutationRate) {
				// negacja bitu bit
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

	// metoda selekcji proporcjonalna (ko³o ruletki)
	public Genome rouletteWheelSelection() {
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
	
	// metoda selekcji turniejowa (wybieranych jest 5 osobników)
    public Genome tournamentSelection() {
        ArrayList<Genome> tournament = new ArrayList<Genome>();

        for (int i = 0; i < 5; i++) {
            int randomId = (int) (Math.random() * populationSize);
            tournament.add(genomes.get(randomId));
        }
        
        Genome fittestGenome = getFittestGenome(tournament);
        return fittestGenome;
    }
    
    public static Genome getFittestGenome(ArrayList<Genome> listGenomes) {
    	double bestFittest = 0.0;
    	int fittest = 0;
    	
    	for (int i = 0; i < listGenomes.size(); i++) {
    		if(bestFittest < listGenomes.get(i).getFitness()) {
    			bestFittest = listGenomes.get(i).getFitness();
    			fittest = i;
    		}
    	}
    	
    	return listGenomes.get(fittest);
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

				// czy chromosom znalaz³ wyjœcie?
				if (genomes.get(i).getFitness() == 1) {
					busy = false; // zakoñcz algorytm
					return;
				}
			}
		}
	}

	//---------------------------Decode-------------------------------------
	//
	//	zamienia listê bitów na listê kierunków (ints)
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
	//	zamiana liczby dwójkowej na dziesi¹tn¹
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

		if (!busy) return;
		
		int numberOfNewBabies = 0;

		ArrayList<Genome> newPopulation = new ArrayList<Genome>();
		
		if (elitism) {
            newPopulation.add(genomes.get(fittestGenome));
        }
        
		while (numberOfNewBabies < populationSize) {
			// wybór 2 rodziców
			Genome parent1 = this.tournamentSelection();
			Genome parent2 = this.tournamentSelection();
			Genome baby1 = new Genome();
			Genome baby2 = new Genome();
			Crossover(parent1.getBits(), parent2.getBits(), baby1.getBits(), baby2.getBits());
			Mutate(baby1.getBits());
			Mutate(baby2.getBits());
			newPopulation.add(baby1);
			newPopulation.add(baby2);

			numberOfNewBabies += 2;
		}
		
		if (elitism) {
			newPopulation.remove(newPopulation.size()-1);
		}

		// nadpisuje populacje now
		genomes = newPopulation;

		// zwiêkszenie licznika generacji
		generation++;
		
	}
	
	/************************
	 * Getters Setters
	 ************************/

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
	
	
}
