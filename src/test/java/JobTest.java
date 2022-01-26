import org.junit.Assert;
import org.junit.Test;

public class JobTest {

    @Test
    public void testCalculateLatenessPositive() {
        double startDate = 2;
        Job job = new Job("A", 2, 1, 1);
        Assert.assertEquals(1, job.calculateLateness(startDate),0);
    }

    @Test
    public void testCalculateLatenessNegative() {
        double startDate = 0;
        Job job = new Job("A", 3, 1, 1);
        Assert.assertEquals(-1, job.calculateLateness(startDate),0);
    }

    @Test
    public void testCalculateLatenessZero() {
        double startDate = 2;
        Job job = new Job("A", 3, 1, 1);
        Assert.assertEquals(0, job.calculateLateness(startDate),0);
    }

    @Test
    public void testCheckReleaseDateGreaterStartDate() {
        double startDate = 2;
        Job job = new Job("A", 2, 1, 1);
        Assert.assertTrue(job.checkReleaseDate(startDate));
    }

    @Test
    public void testCheckReleaseDateEqualsStartDate() {
        double startDate = 2;
        Job job = new Job("A", 3, 1, 2);
        Assert.assertTrue(job.checkReleaseDate(startDate));
    }

    @Test
    public void testCheckReleaseDateSmallerStartDate() {
        double startDate = 1;
        Job job = new Job("A", 3, 1, 2);
        Assert.assertFalse(job.checkReleaseDate(startDate));
    }


    @Test
    public void testSortValueLengthPeriod() {
       String str = "lengthPeriod";
       Job job = new Job("A", 2, 1, 0);
        Assert.assertEquals(1, job.sortValue(str), 0);
    }

    @Test
    public void testSortValueDueDate() {
        String str = "dueDate";
        Job job = new Job("A", 2, 1, 0);
        Assert.assertEquals(2, job.sortValue(str), 0);
    }

    @Test
    public void testSortValueExceptionEmptyString() {
        String str = "";
        Job job = new Job("A", 2, 1, 0);
        Assert.assertThrows(IllegalStateException.class,
                () -> {
                    Assert.assertEquals(2, job.sortValue(str), 0);
                });
    }
}