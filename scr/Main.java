// package scr;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

/*
        for(int i=0; i<3; i++){
            Multithreading T = new Multithreading(i);
            T.start();
        }
*/
        // Job[] scheduled = new Job[2];
        // scheduled[0] = new Job("A",10, 1, 8);
        // scheduled[1] = new Job("B", 7, 3, 0);


        Job[] allJobs = new Job[4];
        allJobs[0] = new Job("A",10, 1, 8);
        allJobs[1] = new Job("B", 7, 3, 0);
        allJobs[2] = new Job("C", 11,5, 6);
        allJobs[3] = new Job("D", 4, 1, 3);



        AllSchedules allSchedules = new AllSchedules(allJobs);
        allSchedules.printSchedules();

        Schedule[] branchS = allSchedules.initialBranch();

        AllSchedules.printBranchSchedules(branchS);



        /*
        Schedule testSchedule = new Schedule(scheduled, allJobs);


        for(Job job : testSchedule.createSchedule(scheduled)) {
            System.out.println(job.getName());
        }


        System.out.println(testSchedule.objFunctionValue);


        Subproblem testSubproblem = new Subproblem(allJobs, null, true, 0);
        testSubproblem.branch();



        System.out.println(Arrays.toString(Schedule.calculateCompletionDate(allJobs)));

        Schedule scheduletest = new Schedule(allJobs, allJobs);

        System.out.println(scheduletest.getMaxDueDate());
        System.out.println(scheduletest.getMinReleaseDate());
        */

    }


}