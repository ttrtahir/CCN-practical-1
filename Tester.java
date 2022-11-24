import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Tester {
    public static void main(String[] args) throws FileNotFoundException {

        int popSize = Integer.parseInt(args[0]);
        double mutationRate = Double.parseDouble(args[1]);
        int timesRunnedProgram = Integer.parseInt(args[2]);

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
