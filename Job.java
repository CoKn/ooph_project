/**
 * Class representing a Job for the Single machine scheduling problem
 *
 */
public class Job {
    private double dueDate;
    private double length;
    private double releaseDate;

    public Job(double dueDate, double length, double releaseDate) {
        this.dueDate = dueDate;
        this.length = length;
        this.releaseDate = releaseDate;
    }

    public double getDueDate() {
        return dueDate;
    }

    public double getLength() {
        return length;
    }

    public double getReleaseDate() {
        return releaseDate;
    }

    /**
     * calculates the lateness of the Job, given a specified starting date
     *
     *
     * @param startDate
     * @return how late the Job is
     */
    private double calculateLateness(double startDate){
        return startDate + length - dueDate;
    }

    /**
     * checks if Job can start at specified starting date, or if it's before its releaseDate
     *
     *
     * @param startDate
     * @return true if Job can be started
     */
    private boolean checkReleaseDate(double startDate){
        return !(startDate < releaseDate);
    }
}
