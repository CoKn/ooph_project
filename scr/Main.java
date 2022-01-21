// package scr;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Instant start = Instant.now();

        Job A = new Job("A",14, 6, 0);
        Job B = new Job("B", 17, 3, 1);
        Job C = new Job("C", 15,8, 5);
        Job D = new Job("D", 13, 5, 8);
        Job E = new Job("E", 13, 5, 8);
        Job F = new Job("F", 13, 5, 8);
        Job G = new Job("G", 13, 5, 8);

        Job[] allJobs = new Job[]{A, B, C, D};

        GenericTree tree = Branch.createTree(allJobs);

        Branch.loopBranch(tree);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        tree.display();

        System.out.println("Execution time: " + timeElapsed + " milliseconds");
    }


}