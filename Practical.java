import java.util.Random;

/**
 * Some very basic stuff to get you started. It shows basically how each
 * chromosome is built.
 * 
 * @author Jo Stevens
 * @version 1.0, 14 Nov 2008
 * 
 * @author Alard Roebroeck
 * @version 1.1, 12 Dec 2012
 * 
 */

 //TO DO
 //mitoz ise clonlar mutasyon da kat

public class Practical {

	static final String TARGET = "HELLO WORLD";
	static char[] alphabet = new char[27];

	int popSize;
	double mutationRate;
	int generationCounter;
	boolean crossoverable;

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		int popSize = Integer.parseInt(args[0]);
        double mutationRate = Double.parseDouble(args[1]);

		Practical p = new Practical(popSize, mutationRate);
		p.calculate();
	}

	public Practical(int popSize, double mutationRate) {
		this.popSize = popSize;
		this.mutationRate = mutationRate;
		this.crossoverable = true;
	}
	public Practical(int popSize, double mutationRate, boolean crossoverable) {
		this.popSize = popSize;
		this.mutationRate = mutationRate;
		this.crossoverable = crossoverable;
	}

	public void calculate() {

		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet[c - 'A'] = c;
		}
		alphabet[26] = ' ';
		Random generator = new Random(System.currentTimeMillis());
		Individual[] population = new Individual[popSize];

		// we initialize the population (Generation 1) with random characters
		for (int i = 0; i < popSize; i++) {
			char[] tempChromosome = new char[TARGET.length()];
			for (int j = 0; j < TARGET.length(); j++) {
				tempChromosome[j] = alphabet[generator.nextInt(alphabet.length)]; // choose a random letter in the
																					// alphabet
			}
			population[i] = new Individual(tempChromosome);
		}

		this.generationCounter = 0;

		do {
			generationCounter++;
			fitnessCalculator(population);
			if (targetChecker(population))
				break;
			sortIndividualsByFitness(population);

			if(crossoverable){

			}
			else
				for (int i = 0; i < popSize / 2; i++) {
					// the fittest is crossing over with the second most fittest etc.
					// but also the least fit ones crossing over too, to increase the chance of
					// generating a required gene that are not yet acquired by the fittest ones by
					// using mutation chance, without having risk the of decreasing its fitness
					int index1 = i * 2;
					int index2 = i * 2 + 1;
					crossover(population[index1], population[index2], mutationRate);
				}
		} while (true);

		printIndividuals(population);
	}

	public void printIndividuals(Individual[] population){
		for (int i = 0; i < population.length; i++) {
			System.out.println(population[i].genoToPhenotype());
		}
	}

	public static void fitnessCalculator(Individual[] population) {
		for (Individual individual : population) {
			int currentFitness = 0;
			char[] individualChromosome = individual.getChromosome();
			char[] targetCharArray = TARGET.toCharArray();
			int chromosomeLength = individualChromosome.length;

			for (int currentGene = 0; currentGene < chromosomeLength; currentGene++) {
				if (individualChromosome[currentGene] == targetCharArray[currentGene])
					currentFitness++;
			}
			//between 0 and 1
			double currentFitnessDouble = (double)currentFitness / 11;
			individual.setFitness(currentFitnessDouble);
		}
	}

	public static boolean targetChecker(Individual[] population) {
		double epsilon = 0.001;
		if(Math.abs(population[0].getFitness() - 1) < epsilon)
			return true;
		return false;
	}

	public static void sortIndividualsByFitness(Individual[] population) {
		HeapSort.sort(population);
	}

	public static void crossover(Individual individual1, Individual individual2, double mutationRate) {
		char[] chromosome1 = individual1.getChromosome();
		char[] chromosome2 = individual2.getChromosome();
		int chromosomeLength = chromosome1.length;

		double nonMutationalCrossingOverPossibility = 1.0 - mutationRate;
		double genKeepChance = nonMutationalCrossingOverPossibility / 2;
		double genExchangeChance = nonMutationalCrossingOverPossibility / 2;

		Random random = new Random();

		for (int currentGene = 0; currentGene < chromosomeLength; currentGene++) {
			double outcome = random.nextDouble();

			if (outcome < genKeepChance) {
				// there is no crossing over at that gene
				continue;
			} else if (outcome < genKeepChance + genExchangeChance) { // which is cumulative genExchangechance, or
																		// nonMutationalCrossingoverPossibility
				// chromosomes exchange genes
				char tempGene = chromosome1[currentGene];
				chromosome1[currentGene] = chromosome2[currentGene];
				chromosome2[currentGene] = tempGene;
			} else {
				// mutation
				// 40% 1st, 40% 2nd, 20% both mutation possibility
				double mutationTargetOutcome = random.nextDouble();

				if (mutationTargetOutcome < 0.4) {
					chromosome1[currentGene] = alphabet[random.nextInt(alphabet.length)];
				} else if (mutationTargetOutcome < 0.8) {
					chromosome2[currentGene] = alphabet[random.nextInt(alphabet.length)];
				} else {
					chromosome1[currentGene] = alphabet[random.nextInt(alphabet.length)];
					chromosome2[currentGene] = alphabet[random.nextInt(alphabet.length)];
				}
			}
		}

	}

	//clone
	public static void mitosis(Individual individual, double mutationRate){
		Random random = new Random();

	}

}
