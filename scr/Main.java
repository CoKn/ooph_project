// package scr;

public class Main {

    public static void main(String[] args) {

        Job A = new Job("A",10, 1, 8);
        Job B = new Job("B", 7, 3, 0);
        Job C = new Job("C", 11,5, 6);
        Job D = new Job("D", 4, 1, 3);

        Job[] allJobs = new Job[]{A, B, C, D};

        GenericTree tree = Branch.createTree(allJobs);
        Branch.branch(tree);

    }


}