import java.util.Arrays;

public class AllSchedules {
    Schedule[] allSchedules;
    Job[] jobs;

    public AllSchedules(Job[] jobs){
        this.jobs = jobs;
        this.allSchedules = scheduler(jobs);

    }


    /**
     * Takes an array of Jobs and creates all possible first level schedules
     * @param jobs
     * @return
     */
    public Schedule[] scheduler(Job[] jobs){
        Schedule[] schedules = new Schedule[jobs.length];
        for(int i=0; i<jobs.length; i++) {
            Schedule newSchedule = new Schedule(new Job[] {jobs[i]} ,jobs);
            schedules[i] = newSchedule;
        }
        return schedules;
    }

    //TODO: Move initialBranch() and cutArray() to sub problem
    //TODO: Make branchSchedules a linked list
    public Schedule[] initialBranch(){
        Schedule[] branchSchedules = new Schedule[allSchedules.length];
        Arrays.fill(branchSchedules,null);
        branchSchedules[0] =  allSchedules[0];
        int k = 0;
        for(int i=1; i<allSchedules.length;i++){
            if(branchSchedules[0].objFunctionValue > allSchedules[i].objFunctionValue) {
                Arrays.fill(branchSchedules,null);
                k = 0;
                branchSchedules[k] = allSchedules[i];
            } else if(branchSchedules[0].objFunctionValue == allSchedules[i].objFunctionValue){
                branchSchedules[k+1] = allSchedules[i];
                k++;
            }
        }
        return cutArray(branchSchedules, k+1);
    }

    private Schedule[] cutArray(Schedule[] branchSchedules, int number) {
        Schedule[] branchSchedulesFinal = new Schedule[number];
        for (int i = 0; i < allSchedules.length; i++) {
            if (branchSchedules[i] != null) {
                branchSchedulesFinal[i] = branchSchedules[i];
            }
        }
        return branchSchedulesFinal;
    }


    /**
     * Prints the different first level schedules and their maximum lateness
     */
    public static void printBranchSchedules(Schedule[] schedules){
        for(Schedule schedule: schedules){
            String[] jobArray = new String[schedule.length];
            for(int i=0; i<schedule.schedule.length; i++){
                jobArray[i] = schedule.schedule[i].getName();
            }
            System.out.print(Arrays.toString(jobArray));
            System.out.println(" Maximum Lateness: " + schedule.objFunctionValue);
        }
    }

    public void printSchedules(){
        for(Schedule schedule: allSchedules){
            String[] jobArray = new String[schedule.length];
            for(int i=0; i<schedule.schedule.length; i++){
                jobArray[i] = schedule.schedule[i].getName();
            }
            System.out.print(Arrays.toString(jobArray));
            System.out.println(" Maximum Lateness: " + schedule.objFunctionValue);
        }
    }
}
