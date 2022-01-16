// package scr;

/**
 *
 */
public class Schedule {

    Job[] scheduledSequence;
    double objFunctionValue;
    Job[] allJobs;
    Job[] schedule;
    int length;


    public Schedule(Job[] scheduledSequence, Job[] allJobs) {
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
    public Job[] createSchedule(Job[] scheduledSequence) {
        Job[] schedule = new Job[4];
        Job[] unscheduled = createUnscheduledSequence(scheduledSequence);
        SortingAlgorithm.selectionSort(unscheduled, true);

        // add the already scheduled Jobs to the schedule
        int j = 0;
        for(int i = 0; i < scheduledSequence.length; i++){
            schedule[i] = scheduledSequence[i];
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




    public Job[] createUnscheduledSequence(Job[] scheduledSequence){
        Job[] unscheduled = new Job[4-scheduledSequence.length];
        int k = 0;
        for (Job allJob : allJobs) {
            if (checkSequence(allJob, scheduledSequence)) {
                unscheduled[k] = allJob;
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

    public boolean checkSequence(Job job, Job[] scheduledSequence) {
        for (Job value : scheduledSequence) {
            if (job.getName().equals(value.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * calculate the completion Dates for a given job sequence
    */
    public static double [] calculateCompletionDate(Job[] jobs) {
        double [] completionDate = new double[jobs.length];
        for (int i = 0; i < jobs.length; i++) {
            if(i==0){
                completionDate[i] = jobs[i].getReleaseDate() + jobs[i].getLengthPeriod();
            } else {
                if(completionDate[i-1] >= jobs[i].getReleaseDate()){
                    completionDate[i] = completionDate[i-1] + jobs[i].getLengthPeriod();
                } else {
                    completionDate[i] = jobs[i].getReleaseDate() + jobs[i].getLengthPeriod();
                }
            }
        }
        return completionDate;
    }

    /**
     * Get max due date of a given set of jobs
     */
    public double getMaxDueDate(){
        double maxDueDate = this.allJobs[0].getDueDate();
        for(int i=1; i<this.allJobs.length; i++){
            if(this.allJobs[i].getDueDate() > maxDueDate){
                maxDueDate = this.allJobs[i].getDueDate();
            }
        }
        return maxDueDate;
    }


    /**
     * Get min release date of a given set of jobs
     */
    public double getMinReleaseDate(){
        double minReleaseDate = this.allJobs[0].getReleaseDate();
        for(int i=1; i<this.allJobs.length; i++){
            if(this.allJobs[i].getReleaseDate() < minReleaseDate){
                minReleaseDate = this.allJobs[i].getReleaseDate();
            }
        }
        return minReleaseDate;
    }

}






