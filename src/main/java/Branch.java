import java.util.*;

/**
 * Class to perform Branching Logic
 */
public class Branch {

    /**
     * Create a Tree based on a set of jobs
     * @param allJobs
     * @return
     */
    static GenericTree createTree(Job[] allJobs){
        LinkedList<Job> scheduledSequence = new LinkedList<>();
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        return new GenericTree(schedule);
    }


    /**
     *
     * @param tree
     */
    public static void loopBranch(GenericTree tree){
        LinkedList<GenericTree.Node> queueNodes = new LinkedList<>();
        queueNodes.add(tree.getRoot());
        LinkedList<GenericTree.Node> optimalScheduleNodes = deepCopyListArrayNodes(queueNodes);

        while (queueNodes.size()>0){
            branch(tree, queueNodes.get(queueNodes.size()-1), queueNodes, optimalScheduleNodes);
        }

        StringBuilder str = new StringBuilder("The optimal schedules are: ");
        for(GenericTree.Node node: optimalScheduleNodes){
            str.append(node.getData().displayJobsArray()).append(", ");
        }
        System.out.println(str);
    }

    /**
     * Branch a Node
     * @param tree to which the nodes belong which should be branched
     * @param parentNode where the branching starts
     * @param queueNodes the nodes which will be branched on the future
     * @param optimalScheduleNodes are all feasible nodes with an optimal objective function value
     */
    public static void branch(GenericTree tree,
                              GenericTree.Node parentNode,
                              LinkedList<GenericTree.Node> queueNodes,
                              LinkedList<GenericTree.Node> optimalScheduleNodes){

        LinkedList<Job> scheduledSequenceReference = deepCopyLinkedList(parentNode.getData().scheduledSequence);

        for(int i=parentNode.getData().scheduledSequence.size(); i<parentNode.getData().allJobs.length; i++){

            LinkedList<Job> scheduledSequence = deepCopyLinkedList(parentNode.getData().scheduledSequence);

            for(Job job: parentNode.getData().allJobs) {

                if(!checkForEqual(scheduledSequenceReference, job)){
                    scheduledSequence.add(job);
                    scheduledSequenceReference.add(job);
                    break;
                }

            }

            Schedule schedule = new Schedule(deepCopyLinkedList(scheduledSequence), parentNode.getData().allJobs);

            tree.addNode(schedule, parentNode);

            scheduledSequence.clear();
        }

        queueNodes.remove(parentNode);

        findMinLateness(parentNode, queueNodes, optimalScheduleNodes);

    }

    /**
     * Checks if a job is already in a sequence
     * @param scheduledSequenceReference the sequence to be compared to
     * @param job the jobs that have to be compared
     * @return
     */
    public static Boolean checkForEqual(LinkedList<Job> scheduledSequenceReference, Job job){
        for (Job value : scheduledSequenceReference) {
            if (value.getName().equals(job.getName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Finds the Schedule with the min Lateness on a horizontal level
     * @param parentNode the subproblems' parent node
     */
    private static void findMinLateness(GenericTree.Node parentNode,
                                        LinkedList<GenericTree.Node> queueNodes,
                                        LinkedList<GenericTree.Node> optimalScheduleNodes){

        double minLatenessUF = 0;
        double minLatenessF = 0;
        LinkedList<GenericTree.Node> localOptimumsUf = new LinkedList<>();
        LinkedList<GenericTree.Node> localOptimumsF = new LinkedList<>();

        for(GenericTree.Node childNode: parentNode.children) {
            if (!childNode.getData().feasibleSolution) {
                minLatenessUF = checkUnfeasible(parentNode, childNode, localOptimumsUf,
                        optimalScheduleNodes, minLatenessUF);

            } else {
                minLatenessF = checkFeasible(childNode, localOptimumsF, minLatenessF);
            }
        }
        queueNodes.addAll(localOptimumsUf);

        if (localOptimumsF.size()>0) {
            optimalScheduleNodes.clear();
            optimalScheduleNodes.addAll(localOptimumsF);
        }
    }

    /**
     * Checks an Unfeasible solution for the min Lateness and adds the min Latenss node to the queue Nodes for next
     * Branching
     * @param parentNode the subproblem's parent node
     * @param childNode unfeasible childnodes
     * @param localOptimumsUf list of all unfeasible local optimums
     * @param optimalScheduleNodes clears this list, if a higher obj function value is found
     * @param minLatenessUF the minimal value of an infeasible problem
     * @return minLatenessUF where to branch next
     */
    public static double checkUnfeasible(GenericTree.Node parentNode,
                                         GenericTree.Node childNode,
                                         LinkedList<GenericTree.Node> localOptimumsUf,
                                         LinkedList<GenericTree.Node> optimalScheduleNodes,
                                         double minLatenessUF) {

        if (localOptimumsUf.size() == 0) {
            minLatenessUF = parentNode.children.get(0).getData().objFunctionValue;
            localOptimumsUf.add(childNode);

        } else if (childNode.getData().objFunctionValue < minLatenessUF) {
            minLatenessUF = childNode.getData().objFunctionValue;
            optimalScheduleNodes.clear();
            localOptimumsUf.clear();
            localOptimumsUf.add(childNode);

        } else if (childNode.getData().objFunctionValue == minLatenessUF) {
            minLatenessUF = childNode.getData().objFunctionValue;
            localOptimumsUf.add(childNode);
        }

        return minLatenessUF;
    }


    /**
     * Checks a Feasible solution for the min Lateness
     * @param childNode
     * @param localOptimumsF
     * @param minLatenessF
     * @return the min Lateness
     */
    public static double checkFeasible(GenericTree.Node childNode,
                                        LinkedList<GenericTree.Node> localOptimumsF,
                                        double minLatenessF) {

        if (localOptimumsF.size() == 0){
            minLatenessF = childNode.getData().objFunctionValue;
            localOptimumsF.add(childNode);
        } else if (childNode.getData().objFunctionValue < minLatenessF){
            minLatenessF = childNode.getData().objFunctionValue;
            localOptimumsF.clear();
            localOptimumsF.add(childNode);
        } else if (childNode.getData().objFunctionValue == minLatenessF) {
            localOptimumsF.add(childNode);
        }

        return minLatenessF;
    }

    /**
     * Creates a deep copy of a LinkedList<Job>
     * @param jobs
     * @return sequence deep copy of jobs linkedList
     */
    private static LinkedList<Job> deepCopyLinkedList(LinkedList<Job> jobs){
        LinkedList<Job> sequence = new LinkedList<>();
        for(Job job : jobs) {
            sequence.add(job.clone());
        }
        return sequence;
    }



    /**
     * Creates a deep copy of a LinkedList<Nodes>
     * @param nodes
     * @return newNodes deep copy of nodes linkedList
     */
    private static LinkedList<GenericTree.Node> deepCopyListArrayNodes(LinkedList<GenericTree.Node> nodes){
        LinkedList<GenericTree.Node> newNodes = new LinkedList<>();
        for(GenericTree.Node node: nodes) newNodes.add(node.clone());
        return newNodes;
    }
}
