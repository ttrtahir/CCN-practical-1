import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Used to testing Practical.java with number of samples and extract the outcome
 * into a text file.
 * <p>
 * Must be runned with this arguments:
 * <p>
 * java Tester popSize mutationRate isCrossover sampleSize
 */
public class Tester {
    public static void main(String[] args) throws FileNotFoundException {

        int popSize = Integer.parseInt(args[0]);
        double mutationRate = Double.parseDouble(args[1]);
        boolean isCrossover = true;
        try {
            if (args[2].equals("0") || args[2].equalsIgnoreCase("N"))
                isCrossover = false;
        } catch (Exception e) {
            isCrossover = true;
        }
        int sampleSize = Integer.parseInt(args[3]);

        // java Tester popSize mutationRate isCrossover sampleSize

        Practical p = new Practical(popSize, mutationRate, isCrossover);
        PrintWriter writer = new PrintWriter(
                "test" + "_p" + popSize + "_m" + mutationRate + "_c"
                        + (isCrossover ? "Y" : "N") + "_ss" + sampleSize + ".txt");
        writer.println("popSize: " + popSize + " mutationRate: " + mutationRate + " sampleSize: "
                + sampleSize + " isCrossover: " + isCrossover);

        for (int i = 0; i < sampleSize; i++) {
            p.calculate();
            writer.println(p.generationCounter);
        }

        writer.close();
    }
}
