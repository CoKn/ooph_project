import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class Branch {

    /**
     * Create a Tree based on a set of jobs
     * @param allJobs
     * @return
     */
    static GenericTree createTree(Job[] allJobs){
        ArrayList<Job> scheduledSequence = new ArrayList<>();
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        return new GenericTree(schedule);
    }


    public static void loopBranch(GenericTree tree){
        ArrayList<GenericTree.Node> queueNodes = new ArrayList<>();
        queueNodes.add(tree.getRoot());
        GenericTree.Node optimalScheduleNode = queueNodes.get(0);

        while (queueNodes.size()>0){
            optimalScheduleNode = branch(tree, queueNodes.get(queueNodes.size()-1), queueNodes, optimalScheduleNode);
        }

        System.out.println("The optimal schedule is: " + optimalScheduleNode.getData().displayJobs());
    }

    //TODO: Check scheduleSequence
    /**
     * Branch a Node
     * @param tree
     */
    public static GenericTree.Node branch(GenericTree tree, GenericTree.Node parentNode, ArrayList<GenericTree.Node> queueNodes,
                              GenericTree.Node optimalSchedule){

        ArrayList<Job> scheduledSequenceReference = deepCopyListArray(parentNode.getData().scheduledSequence);

        for(int i=parentNode.getData().scheduledSequence.size(); i<parentNode.getData().allJobs.length; i++){

            ArrayList<Job> scheduledSequence = deepCopyListArray(parentNode.getData().scheduledSequence);

            for(Job job: parentNode.getData().allJobs) {

                if(!checkForEqual(scheduledSequenceReference, job)){
                    scheduledSequence.add(job);
                    scheduledSequenceReference.add(job);
                    break;
                }

            }

            /*
            for(Job job: parentNode.getData().allJobs) {
                if(!scheduledSequenceReference.contains(job)){
                    scheduledSequence.add(job);
                    scheduledSequenceReference.add(job);
                    break;
                }

            }

             */
            // System.out.println(Schedule.displayJobSequence(deepCopyListArray(scheduledSequence)));
            Schedule schedule = new Schedule(deepCopyListArray(scheduledSequence), parentNode.getData().allJobs);

            tree.addNode(schedule, parentNode);

            scheduledSequence.clear();
        }

        queueNodes.remove(parentNode);

        optimalSchedule = findMinLateness(parentNode, queueNodes, optimalSchedule);
        // System.out.println(optimalSchedule.getData().scheduledSequence.size());

        return optimalSchedule;

    }

    public static Boolean checkForEqual(ArrayList<Job> scheduledSequenceReference, Job job){
        for (Job value : scheduledSequenceReference) {
            if (value.getName().equals(job.getName())) {
                return true;
            }
        }
        return false;
    }



    //TODO: Change optimalScheduleNode to list
    /**
     * Find the Schedule with the min Lateness on a horizontal level
     * @param parentNode
     * @return
     */
    private static GenericTree.Node findMinLateness(GenericTree.Node parentNode, ArrayList<GenericTree.Node> queueNodes,
                                        GenericTree.Node optimalScheduleNode){
        double minLateness = parentNode.getData().objFunctionValue;
        ArrayList<GenericTree.Node> localOptimums = new ArrayList<>();

        for(GenericTree.Node childNode: parentNode.children){

            if(childNode.getData().objFunctionValue < minLateness){
                minLateness = childNode.getData().objFunctionValue;
                localOptimums.clear();
                localOptimums.add(childNode);

            } else if(childNode.getData().objFunctionValue == minLateness) {
                minLateness = childNode.getData().objFunctionValue;
                localOptimums.add(childNode);
            }

        }
        queueNodes.addAll(localOptimums);
        if (localOptimums.size()>0){
            return localOptimums.get(0);
        } else return optimalScheduleNode;
    }

    /**
     * Creates a deep copy of a ArrayList<Job>
     * @param jobs
     * @return
     */
    private static ArrayList<Job> deepCopyListArray(ArrayList<Job> jobs){
        ArrayList<Job> sequence = new ArrayList<>();
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
}
