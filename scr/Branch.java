import java.util.*;


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


    public static void loopBranch(GenericTree tree){
        LinkedList<GenericTree.Node> queueNodes = new LinkedList<>();
        queueNodes.add(tree.getRoot());
        LinkedList<GenericTree.Node> optimalScheduleNodes = deepCopyListArrayNodes(queueNodes);

        while (queueNodes.size()>0){
            branch(tree, queueNodes.get(queueNodes.size()-1), queueNodes, optimalScheduleNodes);
        }

        StringBuilder str = new StringBuilder("The optimal schedules are: ");
        for(GenericTree.Node node: optimalScheduleNodes){
            str.append(node.getData().displayJobs()).append(", ");
        }
        System.out.println(str);
    }

    /**
     * Branch a Node
     * @param tree
     */
    public static void branch(GenericTree tree,
                              GenericTree.Node parentNode,
                              LinkedList<GenericTree.Node> queueNodes,
                              LinkedList<GenericTree.Node> optimalScheduleNodes){

        LinkedList<Job> scheduledSequenceReference = deepCopyListArray(parentNode.getData().scheduledSequence);

        for(int i=parentNode.getData().scheduledSequence.size(); i<parentNode.getData().allJobs.length; i++){

            LinkedList<Job> scheduledSequence = deepCopyListArray(parentNode.getData().scheduledSequence);

            for(Job job: parentNode.getData().allJobs) {

                if(!checkForEqual(scheduledSequenceReference, job)){
                    scheduledSequence.add(job);
                    scheduledSequenceReference.add(job);
                    break;
                }

            }
            // System.out.println(Schedule.displayJobSequence(deepCopyListArray(scheduledSequence)));
            Schedule schedule = new Schedule(deepCopyListArray(scheduledSequence), parentNode.getData().allJobs);

            tree.addNode(schedule, parentNode);

            scheduledSequence.clear();
        }

        queueNodes.remove(parentNode);

        findMinLateness(parentNode, queueNodes, optimalScheduleNodes);
        // System.out.println(optimalSchedule.getData().scheduledSequence.size());

    }

    public static Boolean checkForEqual(LinkedList<Job> scheduledSequenceReference, Job job){
        for (Job value : scheduledSequenceReference) {
            if (value.getName().equals(job.getName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Find the Schedule with the min Lateness on a horizontal level
     * @param parentNode
     * @return
     */
    private static void findMinLateness(GenericTree.Node parentNode,
                                        LinkedList<GenericTree.Node> queueNodes,
                                        LinkedList<GenericTree.Node> optimalScheduleNodes){

        double minLateness = parentNode.getData().objFunctionValue;
        LinkedList<GenericTree.Node> localOptimums = new LinkedList<>();

        for(GenericTree.Node childNode: parentNode.children){

            if(childNode.getData().objFunctionValue < minLateness){
                minLateness = childNode.getData().objFunctionValue;
                optimalScheduleNodes.clear();
                localOptimums.clear();
                localOptimums.add(childNode);

            } else if(childNode.getData().objFunctionValue == minLateness) {
                minLateness = childNode.getData().objFunctionValue;
                localOptimums.add(childNode);
            }

        }
        queueNodes.addAll(localOptimums);

        if (localOptimums.size()>0 &&
                localOptimums.get(0).getData().scheduledSequence.size() == localOptimums.get(0).getData().allJobs.length){
            optimalScheduleNodes.addAll(localOptimums);
        }
    }

    /**
     * Creates a deep copy of a ArrayList<Job>
     * @param jobs
     * @return
     */
    private static LinkedList<Job> deepCopyListArray(LinkedList<Job> jobs){
        LinkedList<Job> sequence = new LinkedList<>();
        for(Job job : jobs) {
            sequence.add(job.clone());
        }
        return sequence;
    }

    private static void printNode(ArrayList<GenericTree.Node> nodes){
        for(GenericTree.Node queueNode: nodes){
            System.out.println("\n" + queueNode.getData().displayJobs());
        }
    }

    private static LinkedList<GenericTree.Node> deepCopyListArrayNodes(LinkedList<GenericTree.Node> nodes){
        LinkedList<GenericTree.Node> newNodes = new LinkedList<>();
        for(GenericTree.Node node: nodes) newNodes.add(node.clone());
        return newNodes;
    }
}
