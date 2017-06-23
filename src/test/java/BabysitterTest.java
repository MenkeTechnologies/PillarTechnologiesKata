/**
 * Created by jacobmenke on 6/9/17.
 */

import BabysitterCalculator.BabySitter;
import BabysitterCalculator.BabysittingJob;
import junit.framework.*;
import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.*;
import static BabysitterCalculator.HourlyRates.*;
import static BabysitterCalculator.BabySitter.formatMoney;

public class BabysitterTest {
    @Test
    public void validHoursEndingAfterMidnight() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:00 pm", "10:00 pm", "1:00 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));
    }

    @Test
    public void validHoursEndingBeforeMidnight() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:00 pm", "10:00 pm", "11:00 pm");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));
    }

    @Test
    public void InvalidHoursStarting() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "3:00 pm", "10:00 pm", "1:00 am");

        assertNotEquals("yes", babySitter.proposeJob(babysittingJob));
    }

    @Test
    public void InvalidHoursEnding() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "10:00 pm", "5:00 am");

        assertNotEquals("yes", babySitter.proposeJob(babysittingJob));
    }

    @Test
    public void InvalidBedtimeAfterEndingTime() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "4:00 am", "3:00 am");

        assertNotEquals("yes", babySitter.proposeJob(babysittingJob));
    }

    @Test
    public void InvalidBedtimeBeforeStartingTime() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "4:00 pm", "3:00 am");

        assertNotEquals("yes", babySitter.proposeJob(babysittingJob));
    }

    @Test
    public void InvalidStartingTime() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 m", "4:00 am", "3:00 am");

        assertEquals(false, babysittingJob.isValid());
    }

    @Test
    public void InvalidBedTime() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "13:00 am", "3:00 am");

        assertEquals(false, babysittingJob.isValid());
    }

    @Test
    public void InvalidEndingTime() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "4:00 am", "37:00 am");

        assertEquals(false, babysittingJob.isValid());
    }

    @Test
    public void InvalidHoursStartingEnding() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "3:00 pm", "10:00 pm", "5:00 am");

        assertNotEquals("yes", babySitter.proposeJob(babysittingJob));
    }

    @Test
    public void AllInvalidHours() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "3:00 pm", "10:00 am", "5:00 am");

        assertNotEquals("yes", babySitter.proposeJob(babysittingJob));
    }


    @Test
    public void wageCalculationEndingBeforeMidnight() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:00 pm", "10:00 pm", "11:00 pm");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 2.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 1 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 0;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void wageCalculationEndingAfterMidnight() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:00 pm", "10:00 pm", "1:00 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 2.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 2 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 1;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void wageCalculationEndingBeforeMidnightFractional() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:15 pm", "10:00 pm", "11:00 pm");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 1.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 1 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 0;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void wageCalculationEndingAfterMidnightFractional() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:15 pm", "10:00 pm", "12:45 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 1.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 2 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 0;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void wageCalculationEndingAfterMidnightAllFractional() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:15 pm", "10:15 pm", "12:45 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 2.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 1 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 0;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void wageCalculationEndingAfterMidnightAllFractionalLateEnd() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "8:05 pm", "11:15 pm", "3:45 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 3.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 0 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 3;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void wageCalculationStartingTimeAtMidnight() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "12:00 am", "1:00 am", "3:45 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 1.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 0 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 2;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void wageCalculationStartingTimeAfterMidnight() {

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "12:15 am", "1:00 am", "3:45 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 0.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 0 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 2;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void MaximumHoursWageCalculation(){

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "12:00 am", "4:00 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 7.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 0 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 4;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void MaximumHoursBedtimeBeforeMidnightWageCalculation(){

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "11:00 pm", "4:00 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 6.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 1 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 4;

        assertEquals(formatMoney(expectedPay), pay);
    }

    @Test
    public void MaximumHoursBedtimeAfterMidnightWageCalculation(){

        BabySitter babySitter = new BabySitter("Jane Doe");

        BabysittingJob babysittingJob = new BabysittingJob("test", "5:00 pm", "1:00 am", "4:00 am");

        assertEquals("yes", babySitter.proposeJob(babysittingJob));

        babySitter.calculateHours(babysittingJob);

        String pay = babySitter.calculatePay(babysittingJob);

        Double expectedPay = HOURLY_RATE_FROM_START_TO_BEDTIME * 8.0 + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT * 0 + HOURLY_RATE_FROM_MIDNIGHT_TO_END * 3;

        assertEquals(formatMoney(expectedPay), pay);
    }

}
