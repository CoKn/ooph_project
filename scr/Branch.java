import java.util.ArrayList;

public class Branch {

    static GT createTree(Job[] allJobs){
        ArrayList<Job> scheduledSequence = new ArrayList<>();
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        return new GT(schedule);
    }

    public static void branch(GT tree){
        GT.Node node = tree.getRoot();
        double optimum = node.getData().objFunctionValue;

        if (node.getData().objFunctionValue == optimum) {

            ArrayList<Job> scheduledSequenceReference = node.getData().scheduledSequence;

            for(int i=0; i<node.getData().allJobs.length; i++){ //node.getData().scheduledSequence.size()

                ArrayList<Job> scheduledSequence = node.getData().scheduledSequence;

                for(Job job: node.getData().allJobs) {

                    if(!scheduledSequenceReference.contains(job)){
                        scheduledSequence.add(job);
                        // scheduledSequenceReference.add(job);
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
}
