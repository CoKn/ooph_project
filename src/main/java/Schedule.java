

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
/**
 * Class representing the schedule of a B&B Subproblem
 */
public class Schedule {
    LinkedList<Job> scheduledSequence;
    double objFunctionValue;
    Job[] allJobs;
    Job[] schedule;
    int length;
    boolean feasibleSolution;

/**
 * Constructor
 */
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
            // if a job is not in the scheduled sequence, it will be added to unscheduled
            if (checkSequence(job, scheduledSequence)) {
                unscheduled.add(job);
            }
        }
        return unscheduled;
    }

    /**
     * checks if a job is already scheduled in the scheduledSequence
     * @param job
     * @param scheduledSequence
     * @return boolean --> Wrong if job is in scheduledSequence
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
        if (feasibleSolution) return Arrays.toString(schedule) + " " + this.objFunctionValue+ " f"; // + " feasible"
        else return Arrays.toString(schedule) + " " + this.objFunctionValue+ " uf"; //  + " unfeasible"
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

    /**
     * Calculates the objective function value of the whole schedule
     * (scheduledSequence & unscheduled)
     * @return maxLateness of the sequence
     */
    public double calculateObjFunctionValue() {

        feasibleSolution = true;

        double startingPoint = 0;
        double maxLateness = schedule[0].calculateLateness(startingPoint);
        for (int i = 0; i < schedule.length; i++) {
            //update the value for maxLateness, if the lateness of current Job
            // exceeds the old value
            if (schedule[i].calculateLateness(startingPoint) > maxLateness) {
                maxLateness = schedule[i].calculateLateness(startingPoint);
            }
            //checks if the job can start directly after to previous job has ended
            //if that is the case, the startingPoint is already updated by the job's
            //remaining periods
            if (schedule[i].checkReleaseDate(startingPoint)) {
                startingPoint += schedule[i].getRemainingPeriod();
            }
            // otherwise, if we are in the scheduledSequence part of the schedule,
            // the difference between releaseDate and startingPoint plus the job's
            // length in periods is added (since  no preemption is allowed)
            // index i checks in which part of the schedule we are
            if(!schedule[i].checkReleaseDate(startingPoint) && i < scheduledSequence.size()) {
                startingPoint += schedule[i].getRemainingPeriod() + schedule[i].getReleaseDate();
            }

            //Preemption Logic
            // only applies to unscheduled Sequence
            if(!schedule[i].checkReleaseDate(startingPoint) && i >= scheduledSequence.size()){
                startingPoint = preemption(startingPoint, i);
                startingPoint += schedule[i].getRemainingPeriod();
            }

        }
        // the values of the remainingPeriods have to be set to their original values
        // the preemption logic might have changed them
        for(Job job: schedule){
            job.setRemainingPeriod(job.getLengthPeriod());
        }
        return maxLateness;
    }


    /**
     * Preemption of jobs
     * @param startingPoint the starting point before preemption happens
     * @param i to indicate at which part of the schedule to start
     * @return startingPoint after preemption is done
     */
    public double preemption(double startingPoint, int i) {
        feasibleSolution = false;
        double timeToSubtract;
        // the "jap" between two consecutive jobs
        timeToSubtract = schedule[i].getReleaseDate() - startingPoint;
        int j = i;
        while (timeToSubtract > 0 && j < schedule.length - 1) {
            if (schedule[j+1].getReleaseDate() <= startingPoint) {

                // if "jap" if longer then the job that's places in it,
                // the time remaining in jap will be shortened
                if (timeToSubtract >= schedule[j+1].getRemainingPeriod()) {
                    timeToSubtract -= schedule[j+1].getRemainingPeriod();
                    schedule[j+1].setRemainingPeriod(0);
                    // falls die Lücke kleiner ist, als die Joblänge
                } else {
                    // the "jap" is filled
                    schedule[j+1].setRemainingPeriod(schedule[j+1].getRemainingPeriod() - timeToSubtract);
                    timeToSubtract = 0;
                }
            }
            // update the startingPoint after each iteration
            startingPoint += schedule[j+1].getLengthPeriod() - schedule[j+1].getRemainingPeriod();
            j++;
        }
        return startingPoint;
    }
}

