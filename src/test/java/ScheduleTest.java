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
    LinkedList<Job> scheduledSequence = new LinkedList<>();


    @Test
    public void testCreateUnscheduledSequenceAllJobs() {
        scheduledSequence.clear();
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        LinkedList<Job> unscheduledSequence = new LinkedList<>();
        unscheduledSequence.add(A);
        unscheduledSequence.add(B);
        unscheduledSequence.add(C);
        unscheduledSequence.add(D);
        Assert.assertEquals(unscheduledSequence, schedule.createUnscheduledSequence(scheduledSequence));
    }

    @Test
    public void testCreateUnscheduledSequenceOneJobs() {
        scheduledSequence.clear();
        scheduledSequence.add(B);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        LinkedList<Job> unscheduledSequence = new LinkedList<>();
        unscheduledSequence.add(A);
        unscheduledSequence.add(C);
        unscheduledSequence.add(D);
        Assert.assertEquals(unscheduledSequence, schedule.createUnscheduledSequence(scheduledSequence));
    }

    @Test
    public void testCreateUnscheduledSequenceNoJobs() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        scheduledSequence.add(C);
        scheduledSequence.add(D);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        LinkedList<Job> unscheduledSequence = new LinkedList<>();
        Assert.assertEquals(unscheduledSequence, schedule.createUnscheduledSequence(scheduledSequence));
    }

    @Test
    public void testCheckSequenceJobInScheduled() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertFalse(schedule.checkSequence(A, scheduledSequence));
    }

    @Test
    public void testCheckSequenceJobNotInScheduled() {
        scheduledSequence.clear();
        scheduledSequence.add(B);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertTrue(schedule.checkSequence(A, scheduledSequence));
    }


    @Test
    public void testCreateScheduleNoScheduled() {
        scheduledSequence.clear();
        Job[] scheduledJobs = new Job[]{D, A, C, B};
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(scheduledJobs, schedule.createSchedule(scheduledSequence));
    }

    @Test
    public void testCreateScheduleOneScheduled() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        Job[] scheduledJobs = new Job[]{A, D, C, B};
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(scheduledJobs, schedule.createSchedule(scheduledSequence));
    }

    @Test
    public void testCreateScheduleTwoScheduled() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        Job[] scheduledJobs = new Job[]{A, B, D, C};
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(scheduledJobs, schedule.createSchedule(scheduledSequence));
    }

    @Test
    public void testCalculateMaxLatenessScheduledNoJobs() {
        scheduledSequence.clear();
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(5.0, schedule.calculateObjFunctionValue(), 0.0);
    }

    @Test
    public void testCalculateMaxLatenessScheduledOneJob() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(5.0, schedule.calculateObjFunctionValue(), 0.0);
    }

    @Test
    public void testCalculateMaxLatenessScheduledTwoJobs() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(7, schedule.calculateObjFunctionValue(), 0.0);
    }

    @Test
    public void testCalculateMaxLatenessScheduledThreeJobs() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        scheduledSequence.add(C);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(9.0, schedule.calculateObjFunctionValue(), 0.0);
    }

    @Test
    public void testCalculateMaxLatenessScheduledFourJobs() {
        scheduledSequence.clear();
        scheduledSequence.add(A);
        scheduledSequence.add(B);
        scheduledSequence.add(C);
        scheduledSequence.add(D);
        Schedule schedule = new Schedule(scheduledSequence, allJobs);
        Assert.assertEquals(9.0, schedule.calculateObjFunctionValue(), 0.0);
    }
}
