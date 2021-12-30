import Sorting.SortingAlgorithm;

/**
 *
 */
public class Schedule {

    Job[] unscheduledSequence;
    Job[] scheduledSequence;
    double objFunctionValue;

    public Schedule(Job[] unscheduledSequence, Job[] scheduledSequence, double objFunctionValue) {
        this.unscheduledSequence = unscheduledSequence;
        this.scheduledSequence = scheduledSequence;
        this.objFunctionValue = objFunctionValue;
    }

    /**
     * created a sequence of the scheduled part from the B&B algorithm and adds the other jobs
     * in ascending order after their duration in periods
     * Jobs cannot be added multiple times
     * @param  scheduledSequence takes the sequence of jobs that is given by the B&B algorithm
     * @return  the complete schedule including not scheduled jobs
     */
    public Job[] createSchedule(Job[] scheduledSequence) {
        Job[] schedule = new Job[4];
        SortingAlgorithm.selectionSort(unscheduledSequence, true);
        for (int i = 0; i < schedule.length; i++) {
            if (scheduledSequence[i] != null) {
                schedule[i] = scheduledSequence[i];
            } else {
                if(checkValue(unscheduledSequence[i])){
                    schedule[i] = unscheduledSequence[i];
                }
            }
        }
        return schedule;
    }

    /**
     * Checks if a job in the unscheduled sequence has already appeared in the scheduled part
     * @param job  job of the unscheduled sequence to be checked in the scheduled part
     * @return  true if the value is not in the scheduled part
     */
    public boolean checkValue(Job job) {
           for (int j = 0; j < scheduledSequence.length; j++) {
                if (job == scheduledSequence[j]) return false;
            }
        return true;
    }

    /**
     * Calculates the maxLateness of all jobs within a schedule
     * @param schedule  the schedule where the maxLateness has to be calculated
     * @return  the maxLateness within the schedule
     */
    public double calculateObjFuncValue(Job [] schedule){
        double maxLateness = 0;
        double startingPoint = 0;
        for(Job job : schedule){
            // check if the job's lateness if higher than the maxLateness
            if(job.calculateLateness(startingPoint)>maxLateness){
                maxLateness = job.calculateLateness(startingPoint);
            }
            // updates the startingPoint for the next job in the schedule

            // if the startingPoint is after the releaseDate, startingPoint is updated by adding the
                // period length of job
            if(job.checkReleaseDate(startingPoint))startingPoint += job.getLengthPeriod();
            //otherwise, the difference between releaseDate and startingPoint plus the job's
                // length in periods is added
            else startingPoint += (job.getReleaseDate()-startingPoint) + job.getLengthPeriod();

        }
        return maxLateness;
    }
}



