import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Schedule {
    ArrayList<Job> scheduledSequence;
    double objFunctionValue;
    Job[] allJobs;
    Job[] schedule;
    int length;


    public Schedule(ArrayList<Job> scheduledSequence, Job[] allJobs) {
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
    public Job[] createSchedule(ArrayList<Job> scheduledSequence) {
        Job[] schedule = new Job[allJobs.length];
        Job[] unscheduled = createUnscheduledSequence(scheduledSequence);
        SortingAlgorithm.selectionSort(unscheduled, true);

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


    public Job[] createUnscheduledSequence(ArrayList<Job> scheduledSequence){
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

    public boolean checkSequence(Job job, ArrayList<Job> scheduledSequence) {
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

    public static String displayJobSequence(ArrayList<Job> sequence){
        String[] schedule = new String[sequence.size()];
        for(int i=0; i< sequence.size(); i++){
            schedule[i] = sequence.get(i).getName();
        }
        return Arrays.toString(schedule);
    }
}
