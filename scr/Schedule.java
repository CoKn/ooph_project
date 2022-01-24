/**
 * Class representing the schedule of a B&B Subproblem
 * when the fixed sequence and all jobs available are provided,
 * this class schedules the unfixed sequence based on earliest release date
 * checks if a schedule is feasible
 * calculates the maximum lateness
 */

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
        this.objFunctionValue = calculateObjFuncValue(scheduledSequence);  // calculates lower bound for schedule
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

//    /**
//     * Checks if a job in the unscheduled sequence has already appeared in the scheduled part
//     * @param job  job of the unscheduled sequence to be checked in the scheduled part
//     * @return  true if the value is not in the scheduled part
//     */
//    public boolean checkValue(Job job) {
//        for (Job value : scheduledSequence) {
//            if (job == value) return false;
//        }
//        return true;
//    }


/*
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

     */

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
     * helps to print the name of the job sequence on the console
     * @return the name of the Job sequence in our schedule and its objective function value
     */
    //TODO: können wir hier noch dazu schreiben, ob die Lösung feasible ist?
    //TODO: was ist genau der Unterschied zwischen diesen beiden Funktionen?
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

    /**
     * To calculate the maxLateness of a job Sequence, this function takes either the scheduled or the unscheduled
     * part of a sequence
     * @param sequence LinkedList<Job></Job> either fixed part of the schedule, or unscheduled part
     * @param isUnscheduled boolean, indicates if preemption is allowed when calculating the maxLateness
     * @param startingPoint double, indicated the starting point of the sequence - for unscheduled part, the startingPoint
     *                      is derived by the end of the scheduled sequence
     * @param maxLateness   double, indicated the maxlateness of the fixed sequence, so only higher values in the
     *                      unscheduled sequence can be added
     * @return double []    to save both the starting point and the maxLateness or the subsequence
     */
    public double [] calculatePartialLateness(LinkedList<Job> sequence, boolean isUnscheduled, double startingPoint, double maxLateness) {
        double timeToSubstract;
        feasibleSolution = true;
        double [] toReturn = new double [2];
        for (int i = 0; i < sequence.size(); i++) {
            //set new value for maxLateness, if it exceeds the old value
            if (sequence.get(i).calculateLateness(startingPoint) > maxLateness) {
                maxLateness = sequence.get(i).calculateLateness(startingPoint);
            }
            //checks if the job can start directly after to previous job has ended
            if (sequence.get(i).checkReleaseDate(startingPoint)) {
                startingPoint += sequence.get(i).getRemainingPeriod();
            }
            // otherwise, the difference between releaseDate and startingPoint plus the job's
            // length in periods is added
            else {
                // calculate the time between the previous job
                // finishing and the release date of the next in sequence
                timeToSubstract = (sequence.get(i).getReleaseDate() - startingPoint);
                // refresh the startingPoint for the next job in the sequence in the next iteration
                startingPoint += timeToSubstract + sequence.get(i).getRemainingPeriod();

                // Here the preemption logic is happening
                // check if we are in the unscheduled part of our schedule (no preemption allowed in fixed part)
                // check if were latest at the second to last index of out unscheduled sequence
                // check if the next job in the sequence can start at the current starting point
                if (isUnscheduled && i != sequence.size() - 1 && sequence.get(i+1).getReleaseDate() > startingPoint) {
                    //TODO: besser schreiben, Prozess dynamisch anpassen

                    // the period length of the job that is preempted is shortened, so when its scheduled,
                    // so it and continue exactly where it left of
                    // if only the successor job fits into gap between precessor and current job
                    if(sequence.get(i+1).getLengthPeriod() - timeToSubstract > 0) {
                        sequence.get(i + 1).setRemainingPeriod(sequence.get(i + 1).getLengthPeriod() - timeToSubstract);

                        // if more than the direct successor fits into gap, also the job scheduled 2 places behind current
                        // job is added in the gap
                    } else if (i != sequence.size() - 2){
                        timeToSubstract = -(sequence.get(i+1).getLengthPeriod() - timeToSubstract);
                        sequence.get(i+1).setRemainingPeriod(0);
                        sequence.get(i+2).setRemainingPeriod(sequence.get(i+2).getRemainingPeriod()- timeToSubstract);
                    }

                    //since the schedule is created with preemption, it is not a feasible solution anymore
                    feasibleSolution = false;

                }
            }
            // reset the old period length, so when the job is scheduled in another branch,
            // the period length is at its original level again
            sequence.get(i).setRemainingPeriod(sequence.get(i).getLengthPeriod());
        }
        // both the startingPoint, and the maxLateness have to be returned when the maxlateness
        // for the unscheduled sequence is being calculated
        toReturn[0] = startingPoint;
        toReturn[1] = maxLateness;

        return toReturn;
    }

    /**
     * Calculates the objective function value for the whole schedule
     * @param scheduled the part of the schedule that is fixed
     * @return double, the maxLateness
     */
    public double calculateObjFuncValue(LinkedList<Job> scheduled){
        // first the maxlateness of the fixed sequence has to be calculated
        double [] toReturn = calculatePartialLateness(scheduled, false, 0, 0);
        // the maxlateness of the unscheduled sequence is calculated
        // happens in two different function calls since the unscheduled sequence allows preemption
        double [] secondReturn = calculatePartialLateness(createUnscheduledSequence(scheduled), true, toReturn[0], toReturn[1]);
        // the maxlateness of the schedule as a whole is returned
        return secondReturn[1];
    }

    /*
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
        double [] secondReturn = calculatePartialLateness(createUnscheduledSequence(scheduled), true, toReturn[0], toReturn[1]);
        return secondReturn[1];
    }

     */
}

