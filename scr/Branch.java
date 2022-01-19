import javax.swing.*;
import java.util.ArrayList;


public class Branch {

    static GT createTree(Job[] allJobs){
        ArrayList<Job> scheduledSequence = new ArrayList<>();
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        return new GT(schedule);
    }

    //TODO: Fix function -> create deep copies
    public static void branch(GT tree){
        GT.Node node = tree.getRoot();
        double optimum = node.getData().objFunctionValue;

        if (node.getData().objFunctionValue == optimum) {

            ArrayList<Job> scheduledSequenceReference = deepCopyListArray(node.getData().scheduledSequence);

            for(int i=node.getData().scheduledSequence.size(); i<node.getData().allJobs.length; i++){

                ArrayList<Job> scheduledSequence = deepCopyListArray(node.getData().scheduledSequence);

                for(Job job: node.getData().allJobs) {

                    if(!scheduledSequenceReference.contains(job)){
                        scheduledSequence.add(job);
                        scheduledSequenceReference.add(job);
                        System.out.println(scheduledSequenceReference.size());
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

    private static ArrayList<Job> deepCopyListArray(ArrayList<Job> jobs){
        ArrayList<Job> sequence = new ArrayList<>();
        for(Job job : jobs) {
            sequence.add(job.clone());
        }
        return sequence;
    }
}
