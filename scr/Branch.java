import java.util.ArrayList;


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


    /**
     * Branch a Node
     * @param tree
     */
    public static void branch(GenericTree tree){
        GenericTree.Node node = tree.getRoot();
        double optimum = node.getData().objFunctionValue;

        if (node.getData().objFunctionValue == optimum) {

            ArrayList<Job> scheduledSequenceReference = deepCopyListArray(node.getData().scheduledSequence);

            for(int i=node.getData().scheduledSequence.size(); i<node.getData().allJobs.length; i++){

                ArrayList<Job> scheduledSequence = deepCopyListArray(node.getData().scheduledSequence);

                for(Job job: node.getData().allJobs) {

                    if(!scheduledSequenceReference.contains(job)){
                        scheduledSequence.add(job);
                        scheduledSequenceReference.add(job);
                        break;
                    }

                }

                Schedule schedule = new Schedule(scheduledSequence, node.getData().allJobs);

                tree.addNode(schedule, node);

                scheduledSequence.clear();
            }

            tree.display();
        }

    }


    /**
     * Find the Schedule with the lowest Lateness on a horizontal level
     * @param schedule
     * @return
     */
    private Schedule findMaxLateness(Schedule schedule){
        return schedule;
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
}
