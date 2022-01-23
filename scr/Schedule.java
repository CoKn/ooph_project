import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class Schedule {
    LinkedList<Job> scheduledSequence;
    double objFunctionValue;
    Job[] allJobs;
    Job[] schedule;
    int length;
    boolean feasibleSolution;


    public Schedule(LinkedList<Job> scheduledSequence, Job[] allJobs) {
        this.scheduledSequence = scheduledSequence;
        this.allJobs = allJobs;
        this.schedule = createSchedule(scheduledSequence);
        this.objFunctionValue = calculateObjFuncValue();  // calculates lower bound for schedule
        this.length = schedule.length;
    }

    /**
     * created a sequence of the scheduled part from the B&B algorithm and adds the other jobs
     * in ascending order after their duration in periods
     * Jobs cannot be added multiple times
     * @param  scheduledSequence takes the sequence of jobs that is given by the B&B algorithm
     * @return  the complete schedule including not scheduled jobs
     */
    public Job[] createSchedule(LinkedList<Job> scheduledSequence) {
        Job[] schedule = new Job[allJobs.length];
        Job[] unscheduled = createUnscheduledSequence(scheduledSequence);
        SortingAlgorithm.selectionSort(unscheduled, true, "dueDate");

        // add the already scheduled Jobs to the schedule
        int j = 0;
        for(int i = 0; i < scheduledSequence.size(); i++){
            schedule[i] = scheduledSequence.get(i);
            j ++;
        }
        // add the missing unscheduled Jobs
        int k = 0;
        for(int i = j; i <schedule.length; i++){
            schedule[i] = unscheduled[k];
            k++;
        }
        return schedule;
    }

    /**
     * Checks if a job in the unscheduled sequence has already appeared in the scheduled part
     * @param job  job of the unscheduled sequence to be checked in the scheduled part
     * @return  true if the value is not in the scheduled part
     */
    public boolean checkValue(Job job) {
        for (Job value : scheduledSequence) {
            if (job == value) return false;
        }
        return true;
    }

    /**
     * Calculates the maxLateness of all jobs within a schedule
     * @return  the maxLateness within the schedule
     */
    public double calculateObjFuncValue(){
        double maxLateness = 0;
        double startingPoint = schedule[0].getReleaseDate();  // start with the schedule's first Job release date
        for(Job job : schedule){
            // check if the job's lateness if higher than the maxLateness
            if(job.calculateLateness(startingPoint)>maxLateness){
                maxLateness = job.calculateLateness(startingPoint);
            }
            // updates the startingPoint for the next job in the schedule

            // if the startingPoint is after the releaseDate, startingPoint is updated by adding the
            // period length of job
            else if(job.checkReleaseDate(startingPoint)) {
                startingPoint += job.getLengthPeriod();
            }
            //otherwise, the difference between releaseDate and startingPoint plus the job's
            // length in periods is added
            else startingPoint += (job.getReleaseDate()-startingPoint) + job.getLengthPeriod();

        }
        return maxLateness;
    }


    public Job[] createUnscheduledSequence(LinkedList<Job> scheduledSequence){
        Job[] unscheduled = new Job[allJobs.length-scheduledSequence.size()];
        int k = 0;
        for (Job job : allJobs) {
            if (checkSequence(job, scheduledSequence)) {
                unscheduled[k] = job;
                k++;
            }
        }
        return unscheduled;
    }

    /**
     * checks which Jobs are already scheduled
     * @param job
     * @param scheduledSequence
     * @return
     */

    public boolean checkSequence(Job job, LinkedList<Job> scheduledSequence) {
        for (Job scheduledJob : scheduledSequence) {
            if (Objects.equals(job.getName(), scheduledJob.getName())) {
                return false;
            }
        }
        return true;
    }

    public String displayJobs(){
        String[] schedule = new String[this.schedule.length];
        for(int i=0; i< schedule.length; i++){
            schedule[i] = this.schedule[i].getName();
        }
        return Arrays.toString(schedule) + " " + this.objFunctionValue;
    }

    public String displayJobSequence(){
        String[] schedule = new String[scheduledSequence.size()];
        for(int i=0; i< scheduledSequence.size(); i++){
            schedule[i] = scheduledSequence.get(i).getName();
        }
        return Arrays.toString(schedule);
    }


    public double [] calculatePartialLateness(Job[] sequence, boolean unscheduled, double startingPoint, double maxLateness) {
        double timeToSubstract;
        double [] toReturn = new double [2];
        for (int i = 0; i < sequence.length; i++) {
            //set new value for maxLateness, if it exceeds the old value
            if (sequence[i].calculateLateness(startingPoint) > maxLateness) {
                maxLateness = sequence[i].calculateLateness(startingPoint);
            }
            //checks if the job can start directly after to previous job has ended
            if (sequence[i].checkReleaseDate(startingPoint)) {
                startingPoint += sequence[i].getLengthPeriod();
            }
            //otherwise, the difference between releaseDate and startingPoint plus the job's
            // length in periods is added
            else {
                timeToSubstract = (sequence[i].getReleaseDate() - startingPoint);
                startingPoint += timeToSubstract + sequence[i].getLengthPeriod();

                if (unscheduled && i != sequence.length - 1 && sequence[i+1].getReleaseDate() > startingPoint) {
                    // TODO: anderen Mechanismus finden
                    sequence[i + 1].setLengthPeriod(sequence[i + 1].getLengthPeriod() - timeToSubstract);
                    feasibleSolution = false;
                }
            }
        }
        toReturn[0] = startingPoint;
        toReturn[1] = maxLateness;

        return toReturn;
    }
}

/*
package scr;

public class Schedule{

    Job[] scheduledSequence;
    Job[] allJobs;
    Job[] schedule;
    int length;
    boolean feasibleSolution;


    public Schedule(Job[] scheduledSequence, Job[] allJobs) {
        this.scheduledSequence = scheduledSequence;
        this.allJobs = allJobs;
        this.schedule = createSchedule(scheduledSequence);
        this.length = schedule.length;
    }

    public Job[] createSchedule(Job[] scheduledSequence) {

        if(scheduledSequence == null){
            return allJobs;
        }
        Job[] schedule = new Job[4];
        // create unscheduled sequence
        Job[] unscheduled = createUnscheduledSequence();

        // add the already scheduled Jobs to the schedule
        int j = 0;
        for(int i = 0; i < scheduledSequence.length; i++){
            schedule[i] = scheduledSequence[i];
            //System.out.println("Scheduled Part of the Sequence" + scheduledSequence[i].getName());
            j ++;
        }
        // add the missing unscheduled Jobs
        // checks if we have Job preemption; if so, feasibleSolution is set to false
        int k = 0;
        for (int i = j; i < schedule.length; i++){
            schedule[i] = unscheduled[k];
            //System.out.println("i" + i + "k" + k + "Unscheduled Part of the Sequence" + unscheduled[k].getName());
            k++;
        }

        return schedule;
    }

    public double [] calculatePartialLateness(Job[] sequence, boolean unscheduled, double startingPoint, double maxLateness) {
        double timeToSubstract;
        double [] toReturn = new double [2];
        for (int i = 0; i < sequence.length; i++) {
            //set new value for maxLateness, if it exceeds the old value
            if (sequence[i].calculateLateness(startingPoint) > maxLateness) {
                maxLateness = sequence[i].calculateLateness(startingPoint);
            }
            //checks if the job can start directly after to previous job has ended
            if (sequence[i].checkReleaseDate(startingPoint)) {
                startingPoint += sequence[i].getLengthPeriod();
            }
            //otherwise, the difference between releaseDate and startingPoint plus the job's
            // length in periods is added
            else {
                timeToSubstract = (sequence[i].getReleaseDate() - startingPoint);
                startingPoint += timeToSubstract + sequence[i].getLengthPeriod();

                if (unscheduled && i != sequence.length - 1 && sequence[i+1].getReleaseDate() > startingPoint) {
                    // TODO: anderen Mechanismus finden
                    sequence[i + 1].setLengthPeriod(sequence[i + 1].getLengthPeriod() - timeToSubstract);
                    feasibleSolution = false;
                }
            }
        }
        toReturn[0] = startingPoint;
        toReturn[1] = maxLateness;

        return toReturn;
    }

    public double calculateObjFunctionValue(Job[] scheduled){
        double [] toReturn = calculatePartialLateness(scheduled, false, 0, 0);
        double [] secondReturn = calculatePartialLateness(createUnscheduledSequence(), true, toReturn[0], toReturn[1]);
//        System.out.println("Lateness after scheduled : "+ toReturn[1] + "\n StartingPoint after scheduled : " + toReturn[0]);
//        System.out.println("Lateness after unscheduled : "+ secondReturn[1] + "\n StartingPoint after unscheduled : " + secondReturn[0]);
        return secondReturn[1];
    }

    public Job[] createUnscheduledSequence(){
        Job[] unscheduled = new Job[4 - scheduledSequence.length];
        int k = 0;
        for (int i = 0; i < allJobs.length; i++) {
            if (checkSequence(allJobs[i], scheduledSequence)) {
                unscheduled[k] = allJobs[i];
                k++;
            }
        }
        return unscheduled;
    }

    public boolean checkSequence(Job job, Job[] scheduledSequence) {
        for (int i = 0; i < scheduledSequence.length; i++) {
            if (job.getName() == scheduledSequence[i].getName()) {
                return false;
            }
        }
        return true;
    }

}

 */
