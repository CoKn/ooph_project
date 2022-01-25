/**
 * Class representing a Job for the Single machine scheduling problem
 *
 */
public class Job implements Sortable, Cloneable {

    private final double dueDate;
    private final double lengthPeriod;
    private final double releaseDate;
    private final String name;
    private double remainingPeriod;


    public Job(String name, double dueDate, double length, double releaseDate) {
        this.name = name;
        this.dueDate = dueDate;
        this.lengthPeriod = length;
        this.releaseDate = releaseDate;
        this.remainingPeriod = length;
    }

//    public double getDueDate() {
//        return dueDate;
//    }

    public double getLengthPeriod() {
        return lengthPeriod;
    }

    public double getReleaseDate() {
        return releaseDate;
    }

    public String getName() {
        return name;
    }

//    public void setLengthPeriod(double lengthPeriod) {
//        this.lengthPeriod = lengthPeriod;
//    }

    public void setRemainingPeriod(double remainingPeriod) {
        this.remainingPeriod = remainingPeriod;
    }

    public double getRemainingPeriod() {
        return remainingPeriod;
    }

    /**
     * calculates the lateness of the Job, given a specified starting date*
     * @param startDate, when the job can start
     * @return how late the Job is
     */
    protected double calculateLateness(double startDate){
        if(checkReleaseDate(startDate)) return startDate + lengthPeriod - dueDate;
        else return releaseDate + lengthPeriod - dueDate;
    }

    /**
     * checks if Job can start at specified starting date, or if it's before its releaseDate
     * @param startDate indicates when a job could start based on precessor
     * @return true if Job can be started
     */
    protected boolean checkReleaseDate(double startDate){
        return (startDate >= releaseDate);
    }

    @Override
    public double sortValue(String attribute) {
        return switch (attribute) {
            case "lengthPeriod" -> lengthPeriod;
            case "dueDate" -> dueDate;
            default -> throw new IllegalStateException("Unexpected value: " + attribute);
        };
    }

    @Override
    public Job clone() {
        try {
            Job clone = (Job) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
