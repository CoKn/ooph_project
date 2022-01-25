/**
 * Class representing the schedule of a B&B Subproblem
 * when the fixed sequence and all jobs available are provided,
 * this class schedules the unfixed sequence based on earliest release date
 * checks if a schedule is feasible
 * calculates the maximum lateness
 */

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
        this.objFunctionValue = calculateObjFunctionValue();  // calculates lower bound for schedule
        this.length = schedule.length;
    }

    /**
     * created a sequence of the scheduled part from the B&B algorithm and adds the other jobs
     * based on earliest release date
     * Jobs cannot be added multiple times
     * @param  scheduledSequence takes the sequence of jobs that is given by the B&B algorithm
     * @return  the complete schedule including not scheduled jobs
     */
    public Job[] createSchedule(LinkedList<Job> scheduledSequence) {
        Job[] schedule = new Job[allJobs.length];
        Job[] unscheduled = createUnscheduledSequence(scheduledSequence).toArray(new Job[0]);
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
     * created the Job sequence that is not fixed by Branch and Bound (unscheduled sequence)
     * @param scheduledSequence LinkedList<Job> the sequence that is fixed
     * @return unscheduled sequence LinkedList<Job>
     */
    public LinkedList<Job> createUnscheduledSequence(LinkedList<Job> scheduledSequence){
        LinkedList<Job> unscheduled = new LinkedList<>();
        for (Job job : allJobs) {
            if (checkSequence(job, scheduledSequence)) {
                unscheduled.add(job);
            }
        }
        return unscheduled;
    }

    /**
     * checks if a job is already scheduled in the fixed sequence
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

    /**
     * Displays jobs names stored in an array, functionValue and feasible/unfeasible
     * @return the name of the Job sequence in our schedule and its objective function value
     */
    public String displayJobsArray(){
        String[] schedule = new String[this.schedule.length];
        for(int i=0; i< schedule.length; i++){
            schedule[i] = this.schedule[i].getName();
        }
        if (feasibleSolution) return Arrays.toString(schedule) + " " + this.objFunctionValue; // + " feasible"
        else return Arrays.toString(schedule) + " " + this.objFunctionValue; //  + " unfeasible"
    }

    /**
     * Displays jobs names stored in an array, functionValue and feasible/unfeasible
     * @return the job name as string
     */
    public String displayJobList(){
        String[] schedule = new String[scheduledSequence.size()];
        for(int i=0; i< scheduledSequence.size(); i++){
            schedule[i] = scheduledSequence.get(i).getName();
        }
        if (feasibleSolution) return Arrays.toString(schedule) + " " + this.objFunctionValue + " feasible"; // + " feasible"
        else return Arrays.toString(schedule) + " " + this.objFunctionValue + " unfeasible"; //  + " unfeasible"
    }


    public double calculateMaxLatenessScheduled() {

        double timeToSubtract;
        feasibleSolution = true;
        double[] toReturn = new double[2];
        double startingPoint = 0;
        double maxLateness = scheduledSequence.get(0).calculateLateness(startingPoint);
        for (Job job : scheduledSequence) {
            //set new value for maxLateness, if it exceeds the old value
            if (job.calculateLateness(startingPoint) > maxLateness) {
                maxLateness = job.calculateLateness(startingPoint);
            }
            //checks if the job can start directly after to previous job has ended
            if (job.checkReleaseDate(startingPoint)) {
                startingPoint += job.getRemainingPeriod();
            }
            // otherwise, the difference between releaseDate and startingPoint plus the job's
            // length in periods is added
            else {
                if (job.calculateLateness(job.getReleaseDate()) > maxLateness) {
                    maxLateness = job.calculateLateness(job.getReleaseDate());
                }
            }
        }
        return maxLateness;
    }

    public double calculateObjFunctionValue() {

        feasibleSolution = true;

        double startingPoint = 0;
        double maxLateness = schedule[0].calculateLateness(startingPoint);
        for (int i = 0; i < schedule.length; i++) {
            //set new value for maxLateness, if it exceeds the old value
            if (schedule[i].calculateLateness(startingPoint) > maxLateness) {
                maxLateness = schedule[i].calculateLateness(startingPoint);
            }
            //checks if the job can start directly after to previous job has ended
            if (schedule[i].checkReleaseDate(startingPoint)) {
                startingPoint += schedule[i].getRemainingPeriod();
            }
            // otherwise, the difference between releaseDate and startingPoint plus the job's
            // length in periods is added
            if(!schedule[i].checkReleaseDate(startingPoint) && i < scheduledSequence.size()) {
                startingPoint += schedule[i].getRemainingPeriod() + schedule[i].getReleaseDate();
            }

            //Preemption Logic
            if(!schedule[i].checkReleaseDate(startingPoint) && i >= scheduledSequence.size()){
                startingPoint = preemption(startingPoint, i);
                startingPoint += schedule[i].getRemainingPeriod();
            }

        }
        for(Job job: schedule){
            job.setRemainingPeriod(job.getLengthPeriod());
        }
        return maxLateness;
    }


    public double preemption(double startingPoint, int i) {
        feasibleSolution = false;
        double timeToSubtract;
        timeToSubtract = schedule[i].getReleaseDate() - startingPoint;
        int j = i;
        while (timeToSubtract > 0 && j < schedule.length - 1) {
            if (schedule[j+1].getReleaseDate() <= startingPoint) {

                // falls die Lücke größer ist als die Joblänge, die e füllen soll
                if (timeToSubtract >= schedule[j+1].getRemainingPeriod()) {
                    timeToSubtract -= schedule[j+1].getRemainingPeriod();
                    schedule[j+1].setRemainingPeriod(0);
                    // falls die Lücke kleiner ist, als die Joblänge
                } else {
                    schedule[j+1].setRemainingPeriod(schedule[j+1].getRemainingPeriod() - timeToSubtract);
                    timeToSubtract = 0;
                }
            }
            startingPoint += schedule[j+1].getLengthPeriod() - schedule[j+1].getRemainingPeriod();
            j++;
        }
        return startingPoint;
    }
}

