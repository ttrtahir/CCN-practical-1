import java.util.Random;

/**
 * Designed to answer this question: How many generations may need to get "HELLO
 * WORLD" from randomly initialized
 * eleven character long strings by using crossover (sometimes not) and
 * mutations with given amount of population size, mutation rate and if its
 * crossoverable or not?
 * <p>
 * Prints the last generation and generation amount needed to get the target.
 * 
 * @author Yusuf Tahir Aydin
 * @author Batu Ozogul
 */

public class Practical {

	static final String TARGET = "HELLO WORLD";
	static char[] alphabet = new char[27];

	int popSize;
	double mutationRate;
	int generationCounter;
	boolean isCrossover;
	Individual[] population;

	/**
	 * Can be used to calculate the solution for a sample by running only this file
	 * with appropriate args
	 * 
	 * @param args popSize mutationRate isCrossover
	 */

	public static void main(String[] args) {

		int popSize = Integer.parseInt(args[0]);
		double mutationRate = Double.parseDouble(args[1]);
		boolean isCrossover = true;
		try {
			if (args[2].equals("0") || args[2].equalsIgnoreCase("N"))
				isCrossover = false;
		} catch (Exception e) {
			isCrossover = true;
		}

		Practical p = new Practical(popSize, mutationRate, isCrossover);
		p.calculate();
		printIndividuals(p.population);
		System.out.println("Generation amount to get \"Hello World\" is: " + p.generationCounter);
	}

	/**
	 * Constructor to be used by Tester.java
	 * 
	 * @param popSize      population size
	 * @param mutationRate mutation rate
	 * @param isCrossover  specifies if we want the population is able to crossover
	 */
	public Practical(int popSize, double mutationRate, boolean isCrossover) {
		this.popSize = popSize;
		this.mutationRate = mutationRate;
		this.isCrossover = isCrossover;
	}

	/**
	 * Calculates the generation amount needed for specific fields that is declared
	 * in the constructor
	 */
	public void calculate() {

		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet[c - 'A'] = c;
		}
		alphabet[26] = ' ';
		Random generator = new Random(System.currentTimeMillis());
		population = new Individual[popSize];

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

			if (isCrossover) { // meiosis like reproduction
				for (int i = 0; i < popSize / 2; i++) {
					// the fittest is crossing over with the second most fittest etc.
					// but also the least fit ones crossing over too, to increase the chance of
					// generating a required gene that are not yet acquired by the fittest ones by
					// using mutation chance, without having risk the of decreasing its fitness
					int index1 = i * 2;
					int index2 = i * 2 + 1;
					crossover(population[index1], population[index2], mutationRate);
				}
			} else { // mitosis like reproduction
				for (Individual individual : population) {
					mitosis(individual, mutationRate);
				}
			}
		} while (true);
	}

	/**
	 * Prints all individuals in a population
	 * 
	 * @param population Individual array
	 */
	public static void printIndividuals(Individual[] population) {
		for (int i = 0; i < population.length; i++) {
			System.out.println(population[i].genoToPhenotype());
		}
	}

	/**
	 * Calculates and sets the fitness for each individual by comparing them with
	 * the target so that for each matching gene (letter) the Individuals fitness
	 * increases
	 * <p>
	 * After the calculation fitness value divided by 11, so each individuals
	 * fitness is between 0 and 1 (1 is the fittest, 0 is the least fit)
	 * 
	 * @param population
	 */
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
			// between 0 and 1
			double currentFitnessDouble = (double) currentFitness / 11;
			individual.setFitness(currentFitnessDouble);
		}
	}

	/**
	 * Checks if the fittest Individual has the fitness of 1 (if 1 than it is equal
	 * to our target)
	 * 
	 * @param population
	 * @return true, if the fittest Individual has the value 1, false otherwise
	 */
	public static boolean targetChecker(Individual[] population) {
		double epsilon = 0.001;
		return Math.abs(population[0].getFitness() - 1) < epsilon;
	}

	/**
	 * Sorts the individuals by their fitness.
	 * <p>
	 * It rearrange the population.
	 * 
	 * @param population Individual array
	 */
	public static void sortIndividualsByFitness(Individual[] population) {
		HeapSort.sort(population);
	}

	/**
	 * Method used to simulate meiosis like gene exchange.
	 * <p>
	 * There are three possibilities exist for each gene transfer sequence:
	 * <p>
	 * 1- No gene transfer nor mutation, which has no effect.
	 * <p>
	 * 2- Gene transfer but no mutation.
	 * <p>
	 * 3- Mutation while transfering genes: 40% of the time only changes the first
	 * chromosome's gene, 40% of the time only changes the second chromosome's gene,
	 * 20% of the time changes both genes.
	 * 
	 * @param individual1
	 * @param individual2
	 * @param mutationRate
	 */
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

			if (outcome < genKeepChance) { // no crossing over nor mutation
				continue;
			} else if (outcome < genKeepChance + genExchangeChance) { // chromosomes exchange genes
				char tempGene = chromosome1[currentGene];
				chromosome1[currentGene] = chromosome2[currentGene];
				chromosome2[currentGene] = tempGene;
			} else { // mutation
				// 40% in the 1st, 40% in the 2nd, 20% in both genes possible mutation in the
				// particular gene
				double mutationTargetOutcome = random.nextDouble();
				char tempGene = chromosome1[currentGene];

				if (mutationTargetOutcome < 0.4) {
					chromosome1[currentGene] = alphabet[random.nextInt(alphabet.length)];
					chromosome2[currentGene] = tempGene;
				} else if (mutationTargetOutcome < 0.8) {
					chromosome1[currentGene] = chromosome2[currentGene];
					chromosome2[currentGene] = alphabet[random.nextInt(alphabet.length)];
				} else {
					chromosome1[currentGene] = alphabet[random.nextInt(alphabet.length)];
					chromosome2[currentGene] = alphabet[random.nextInt(alphabet.length)];
				}
			}
		}

	}

	/**
	 * Method that imitates the process of mitosis without increasing the size of
	 * the population.
	 * Separeting each gene has a small amount of mutation rate
	 * 
	 * @param individual   The individual that wants to be reproduced with mitosis
	 * @param mutationRate Mutation rate that is responsible for the only possible
	 *                     source of changes in the gene in that method of
	 *                     reproduction
	 */
	public static void mitosis(Individual individual, double mutationRate) {
		char[] chromosome = individual.getChromosome();
		int chromosomeLength = chromosome.length;
		Random random = new Random();

		for (int currentGene = 0; currentGene < chromosomeLength; currentGene++) {
			double outcome = random.nextDouble();
			double genKeepChance = 1.0 - mutationRate;

			if (outcome < genKeepChance)
				continue;
			else {
				chromosome[currentGene] = alphabet[random.nextInt(alphabet.length)];
			}
		}
	}

}
