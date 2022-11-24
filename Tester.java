import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Tester {
    public static void main(String[] args) throws FileNotFoundException {

        int popSize = 100;
        double mutationRate = 0.01;
        int timesRunnedProgram = 1000;

        Practical p = new Practical(popSize, mutationRate);
        PrintWriter writer = new PrintWriter(
                "test" + "_p" + popSize + "_m" + mutationRate + "_t" + timesRunnedProgram + ".txt");
        writer.println("popSize: " + popSize + " mutationRate: " + mutationRate + " timesRunnedProgram: "
                + timesRunnedProgram);

        for (int i = 0; i < timesRunnedProgram; i++) {
            p.calculate();
            writer.println(p.generationCounter);
        }

        writer.close();
    }
}
