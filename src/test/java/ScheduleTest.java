import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class ScheduleTest {


    Job A = new Job("A",14, 6, 0);
    Job B = new Job("B", 17, 3, 1);
    Job C = new Job("C", 15,8, 5);
    Job D = new Job("D", 13, 5, 8);

    Job[] allJobs = new Job[]{A, B, C, D};

    @Test
    public void testCalculateMaxLatenessScheduledOneJob() {
        LinkedList<Job> scheduledSequence = new LinkedList<>();
        scheduledSequence.add(A);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(-8.0, schedule.calculateMaxLatenessScheduled(), 0.0);
    }

    @Test
    public void testCalculateMaxLatenessScheduledTwoJobs() {
        LinkedList<Job> scheduledSequence = new LinkedList<>();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(-8.0, schedule.calculateMaxLatenessScheduled(), 0.0);
    }

    @Test
    public void testCalculateMaxLatenessScheduledThreeJobs() {
        LinkedList<Job> scheduledSequence = new LinkedList<>();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        scheduledSequence.add(C);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(2.0, schedule.calculateMaxLatenessScheduled(), 0.0);
    }

    @Test
    public void testCalculateMaxLatenessScheduledFourJobs() {
        LinkedList<Job> scheduledSequence = new LinkedList<>();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        scheduledSequence.add(C);
        scheduledSequence.add(D);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(9.0, schedule.calculateMaxLatenessScheduled(), 0.0);
    }


    @Test
    public void testUnscheduledScheduled(){
        LinkedList<Job> unScheduledSequence = new LinkedList<>();
        unScheduledSequence.add(D);
        unScheduledSequence.add(A);
        unScheduledSequence.add(C);
        unScheduledSequence.add(B);

        Schedule schedule = new Schedule(unScheduledSequence, allJobs);
        Assert.assertEquals(5.0, schedule.unscheduledScheduled(), 0.0);
    }

    @Test
    public void testUnscheduledScheduledADCB(){
        LinkedList<Job> unScheduledSequence = new LinkedList<>();
        unScheduledSequence.add(A);
        unScheduledSequence.add(D);
        unScheduledSequence.add(C);
        unScheduledSequence.add(B);

        Schedule schedule = new Schedule(unScheduledSequence, allJobs);
        Assert.assertEquals(5.0, schedule.unscheduledScheduled(), 0.0);
    }
}
