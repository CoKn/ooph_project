// package scr;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Job A = new Job("A",10, 1, 8);
        Job B = new Job("B", 7, 3, 0);
        Job C = new Job("C", 11,5, 6);
        Job D = new Job("D", 4, 1, 3);

        Job[] allJobs = new Job[]{A, B, C, D};

        GenericTree tree = Branch.createTree(allJobs);

        // Branch.loopBranch(tree);

        ArrayList<GenericTree.Node> queueNodes = new ArrayList<>();
        queueNodes.add(tree.getRoot());
        GenericTree.Node optimalScheduleNode = queueNodes.get(0);

        optimalScheduleNode = Branch.branch(tree, tree.getRoot(), queueNodes, optimalScheduleNode);
        System.out.println(optimalScheduleNode.getData().displayJobs());

        optimalScheduleNode = Branch.branch(tree, optimalScheduleNode, queueNodes, optimalScheduleNode);
        System.out.println(optimalScheduleNode.getData().displayJobs());

        tree.display();

        //TODO: Error tree branch always the same node (problem maybe in the function that fixes the jobs in a schedule)

    }


}