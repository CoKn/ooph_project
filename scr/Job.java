// package scr;
// import Sorting.Sortable;

/**
 * Class representing a Job for the Single machine scheduling problem
 *
 */
public class Job implements Sortable {

    private double dueDate;
    private double lengthPeriod;
    private double releaseDate;
    private String name;

    public String getName() {
        return name;
    }

    public Job(String name, double dueDate, double length, double releaseDate) {
        this.name = name;
        this.dueDate = dueDate;
        this.lengthPeriod = length;
        this.releaseDate = releaseDate;
    }

    public double getDueDate() {
        return dueDate;
    }

    public double getLengthPeriod() {
        return lengthPeriod;
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
    protected double calculateLateness(double startDate){
        if(checkReleaseDate(startDate)) return startDate + lengthPeriod - dueDate;
        else return releaseDate + lengthPeriod - dueDate;
    }

    /**
     * checks if Job can start at specified starting date, or if it's before its releaseDate
     *
     *
     * @param startDate
     * @return true if Job can be started
     */
    protected boolean checkReleaseDate(double startDate){
        return (startDate > releaseDate);
    }

    @Override
    public double sortValue() {
        return lengthPeriod;
    }
}
