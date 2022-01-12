// package scr;

public class Subproblem {


    Job [] jobOrder;
    Job[] allJobs;
    Subproblem[] daughterProblems;
    Subproblem motherProblem;
    double objectiveFunctionValue;
    boolean active;
    int level;
    boolean isOptimum;
    Schedule schedule;

    //current optimum vielleicht in B&B algorithmus speichern
    double currentOptimum;


    public Subproblem(Job[] allJobs, Subproblem motherProblem, boolean active, int level) {
        this.allJobs = allJobs;
        this.motherProblem = motherProblem;
        this.active = active;
        this.level = level;
    }

    //to branch, the algorithm checks where to branch next
    //checks how many daughterProblems, according to level of motherproblem, array of subproblems is created
    public void branch(){
        //creates an empty array of length (4-1) - level
        daughterProblems = new Subproblem[(schedule.length - 1)  - level];
        int j = 0;
        int k = 0;
        Job [] jobsToAdd = createNextToSchedule();
        for(Subproblem problem : daughterProblems){
            problem = new Subproblem(allJobs, this, true, level + 1);
            problem.jobOrder = new Job[this.jobOrder.length +1];
            for(int i =0; i < jobOrder.length; i++){
                problem.jobOrder[i] = this.jobOrder[i];
                j ++;
            }
            problem.jobOrder[j] = jobsToAdd[k];
            k++;
        }
        active = false;

    }




    //TODO make this whole logic an Interface
    public Job[] createNextToSchedule(){
        Job[] unscheduled = new Job[4-jobOrder.length];
        int k = 0;
        for(int i =0; i < allJobs.length; i++){
            if (checkSequence(allJobs[i], jobOrder)) {
                unscheduled[k] = allJobs[i];
                k++;
            }
        }
        return unscheduled;
    }

    public boolean checkSequence(Job job, Job[] scheduledSequence) {
        for (Job value : scheduledSequence) {
            if (job.getName().equals(value.getName())) {
                return false;
            }
        }
        return true;
    }

    //TODO: implement different algorithm
    // Greedy: goes through all Subproblems and checks which one has the min max Lateness
    // If no subproblems to branch --> goes back to motherproblem and checks its subproblems
    public Subproblem nextToBranch(){
        if (active) {
            double daughterProblemMinLateness = daughterProblems[0].schedule.objFunctionValue;
            Subproblem nextToBranch = null;
            for (Subproblem subproblem : daughterProblems) {
                //if daughterProblem has min Latenessvalue, return this
                if(subproblem.schedule.objFunctionValue < daughterProblemMinLateness){
                    nextToBranch = this;
                }
            }
            return nextToBranch;
        } else {
            // climb up to motherproblem and check if daughterproblems are still active
            //TODO: what are we doing when all problems are branched already?
            return motherProblem.nextToBranch();
        }
    }


/*
    public boolean checkOptimum(){

    }

// make array of length 4, add parent fixed schedule, make the Schedule class
    public Job [] ruleForSequence(){

    }

 */

}