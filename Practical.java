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

public class Practical {

	static final String TARGET = "HELLO WORLD";
	static char[] alphabet = new char[27];

	int popSize;
	double mutationRate;
	int generationCounter;

	/**
	 * @param args
	 */

	public Practical(int popSize, double mutationRate) {
		this.popSize = popSize;
		this.mutationRate = mutationRate;
	}

	public void calculate() {

		this.popSize = 100;
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

		// What does your population look like?
		// for (int i = 0; i < population.length; i++) {
		// System.out.println(population[i].genoToPhenotype());
		// }

		this.generationCounter = 0;

		do {
			generationCounter++;
			fitnessCalculator(population);
			if (targetChecker(population))
				break;
			sortIndividualsByFitness(population);

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

		// do your own cool GA here
		/**
		 * Some general programming remarks and hints:
		 * - A crucial point is to set each individual's fitness (by the setFitness()
		 * method) before sorting. When is an individual fit?
		 * How do you encode that into a double (between 0 and 1)?
		 * - Decide when to stop, that is: when the algorithm has converged. And make
		 * sure you terminate your loop when it does.
		 * - print the whole population after convergence and print the number of
		 * generations it took to converge.
		 * - print lots of output (especially if things go wrong).
		 * - work in an orderly and structured fashion (use tabs, use methods,..)
		 * - DONT'T make everything private. This will only complicate things. Keep
		 * variables local if possible
		 * - A common error are mistakes against pass-by-reference (this means that you
		 * pass the
		 * address of an object, not a copy of the object to the method). There is a
		 * deepclone method included in the
		 * Individual class.Use it!
		 * - You can compare your chromosome and your target string, using for eg.
		 * TARGET.charAt(i) == ...
		 * - Check your integers and doubles (eg. don't use ints for double divisions).
		 */
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
			individual.setFitness(currentFitness);
		}
	}

	public static boolean targetChecker(Individual[] population) {
		if (population[0].getFitness() == TARGET.length()) // HELLO WORLD length
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

}
