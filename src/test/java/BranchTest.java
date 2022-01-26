import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class BranchTest {

    Job A = new Job("A",14, 6, 0);
    Job B = new Job("B", 17, 3, 1);
    Job C = new Job("C", 15,8, 5);
    Job D = new Job("D", 13, 5, 8);

    Job[] allJobs = new Job[]{A, B, C, D};

    GenericTree tree = Branch.createTree(allJobs);

    @Test
    public void testLoopBranch() {
    }

    @Test
    public void testBranch() {
    }

    @Test
    public void testCheckForEqualInSequence() {
        LinkedList<Job> scheduledSequence = new LinkedList<>();
        scheduledSequence.add(A);
        Assert.assertTrue(Branch.checkForEqual(scheduledSequence, A));
    }

    @Test
    public void testCheckForEqualNotInSequence() {
        LinkedList<Job> scheduledSequence = new LinkedList<>();
        Assert.assertFalse(Branch.checkForEqual(scheduledSequence, B));
    }

    @Test
    public void testFindMinLateness() {
    }

    @Test
    public void testCheckUnfeasible() {
        Job[] jobs = new Job[]{A, B};

        GenericTree tree = Branch.createTree(jobs);
        Branch.loopBranch(tree);

        for(GenericTree.Node childNode: tree.getRoot().children)
            childNode.getData().feasibleSolution = false;

        double minLatenessUF = 0;
        GenericTree.Node parentNode = tree.getRoot();
        GenericTree.Node childNode = tree.getRoot().children.get(0);
        LinkedList<GenericTree.Node> localOptimumsUf = new LinkedList<>();
        LinkedList<GenericTree.Node> optimalScheduleNodes = new LinkedList<>();

        Assert.assertEquals(-8.0, Branch.checkUnfeasible(
                parentNode, childNode, localOptimumsUf, optimalScheduleNodes, minLatenessUF), 0);



    }

    @Test
    public void testCheckFeasible() {
        Job[] jobs = new Job[]{A, B};

        GenericTree tree = Branch.createTree(jobs);
        Branch.loopBranch(tree);

        for(GenericTree.Node childNode: tree.getRoot().children)
            childNode.getData().feasibleSolution = true;

        double minLatenessUF = 0;
        GenericTree.Node parentNode = tree.getRoot();
        GenericTree.Node childNode = tree.getRoot().children.get(0);
        LinkedList<GenericTree.Node> localOptimumsUf = new LinkedList<>();
        LinkedList<GenericTree.Node> optimalScheduleNodes = new LinkedList<>();

        Assert.assertEquals(-8.0, Branch.checkUnfeasible(
                parentNode, childNode, localOptimumsUf, optimalScheduleNodes, minLatenessUF), 0);
    }

    @Test
    public void testDeepCopyLinkedList() {
    }
}