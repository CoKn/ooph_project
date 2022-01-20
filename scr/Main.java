// package scr;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Job A = new Job("A",14, 6, 0);
        Job B = new Job("B", 17, 3, 1);
        Job C = new Job("C", 15,8, 5);
        Job D = new Job("D", 13, 5, 8);

        Job[] allJobs = new Job[]{A, B, C, D};

        GenericTree tree = Branch.createTree(allJobs);

        Branch.loopBranch(tree);

        //TODO: Check tree display function
        tree.display();
    }


}