package BabysitterCalculator;

import java.text.NumberFormat;
import java.util.HashMap;

import static BabysitterCalculator.Keys.*;

/**
 *  Represents a babysitter
 */
public class BabySitter {
    private String name;
    private BabysittingJob babysittingJob;

    /**
     * Constructor for babysitter
     * @param name
     */
    public BabySitter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BabysittingJob getBabysittingJob() {
        return babysittingJob;
    }

    /**
     * Babysitter decides whether to accept job or not.  If the job is invalid
     * method will return reason for rejection
     * @param babysittingJob the job in question
     * @return babysitter's answer
     */

    public String proposeJob(BabysittingJob babysittingJob) {

        HashMap<String, String> timesData = babysittingJob.getTimesData();
        String reason = "";

        //check for valid starting and ending times
        // times are doubles
        if (babysittingJob.getStartingTime() >= 17) {

            if (babysittingJob.getEndingTime() <= 4) {
                //turn midnight into 24 and hours after midnight to +24 if necessary
                // needed for calculations
                babysittingJob.compensateFor24Hours();

                //check that bedtime is between starting and ending time
                if (babysittingJob.getStartingTime() <= babysittingJob.getBedTime() && babysittingJob.getBedTime() <= babysittingJob.getEndingTime()) {
                    this.babysittingJob = babysittingJob;
                    //job is valid
                    return "yes";
                } else {
                    //bedtime not between start and end
                    return "an inappropriate bedtime at " + timesData.get(BED_TIME) + ".";
                }
            } else {
                //ending time is too late or before midnight in this block

                if (babysittingJob.getEndingTime() < 12){
                    //the ending time is later than 4 am and less than 12pm the following day
                    return "the late ending time at " + timesData.get(ENDING_TIME) + ".";
                } else {
                    //ending time is before midnight
                    //check to make sure ending time is after bedtime
                    if (babysittingJob.getStartingTime() <= babysittingJob.getBedTime() && babysittingJob.getBedTime() <= babysittingJob.getEndingTime()){
                        return "yes";
                    }

                }


            }
        } else {
            //starting time is earlier than 5pm
            reason += "the early starting time at " + timesData.get(STARTING_TIME);
            if (babysittingJob.getEndingTime() <= 4) {
                reason += ".";
            } else {
                //if both start and end times are invalid
                reason += ", not to mention the late ending hours at " + timesData.get(ENDING_TIME) + ".";
            }
        }
        //error message outputted to user
        return reason;
    }

    /**
     * Add double quotes and print babysitter's name
     * @param message
     */

    public void says(String message) {
        System.out.println(name + " says, \"" + message + "\"");
    }

    /**
     * Floors fractional hours and prepares for rates calculations
     * @param babysittingJob
     * @return Hashmap with hours breakdown
     */

    public HashMap<String, Integer> calculateHours(BabysittingJob babysittingJob) {

        if (babysittingJob != null) {

            Double hoursStartingToBedtime = babysittingJob.getBedTime() - babysittingJob.getStartingTime();

            //midnight is at 24 hours
            Double hoursBedtimeToMidnight = 24 - babysittingJob.getBedTime();

            if (hoursBedtimeToMidnight < 0) hoursBedtimeToMidnight = 0.0;

            //dont need to subtract because calculated from 0 or midnight
            Double hoursMidnighttoEnd = babysittingJob.getEndingTime();

            if (hoursMidnighttoEnd >= 24) {
                hoursMidnighttoEnd -= 24;
            }

            //remove fractions for calculations
            Integer hoursStartingToBedtimeFloored = removeFractional(hoursStartingToBedtime);
            Integer hoursBedtimeToMidnightFloored = removeFractional(hoursBedtimeToMidnight);
            Integer hoursMidnighttoEndFloored = removeFractional(hoursMidnighttoEnd);
            //remove fractions for hours sum
            Integer hours = removeFractional(hoursStartingToBedtime) + removeFractional(hoursBedtimeToMidnight) + removeFractional(hoursMidnighttoEnd);

            //store data in hashmap for readable retrieval later
            HashMap<String, Integer> hourMap = new HashMap<>();
            hourMap.put("hoursStartingToBedtimeFloored", hoursStartingToBedtimeFloored);
            hourMap.put("hoursBedtimeToMidnightFloored", hoursBedtimeToMidnightFloored);
            hourMap.put("hoursMidnightToEndFloored", hoursMidnighttoEndFloored);
            hourMap.put("totalHours", hours);

            babysittingJob.setHoursMap(hourMap);

            return hourMap;
        }
        return null;
    }

    /**
     * Takes the hours data from the hoursMap and calculates the total cost of the job
     * @param babysittingJob
     * @return string representation of total cost of job
     */
    public String calculatePay(BabysittingJob babysittingJob) {

        if (babysittingJob != null) {

            HashMap<String, Integer> hoursMap = babysittingJob.getHoursMap();

            Double pay = (double) calculatePay(hoursMap.get("hoursStartingToBedtimeFloored"), hoursMap.get("hoursBedtimeToMidnightFloored"), hoursMap.get("hoursMidnightToEndFloored"));

            //no fractional hours
            return formatMoney(pay);
        }

        return null;
    }

    /**
     * Calculates the total pay based on the rate constants
     * @param hoursStartingToBedtimeFloored
     * @param hoursBedtimeToMidnightFloored
     * @param hoursMidnighttoEndFloored
     * @return the total cost of the babysitting job
     */

    private int calculatePay(int hoursStartingToBedtimeFloored, int hoursBedtimeToMidnightFloored, int hoursMidnighttoEndFloored) {
        return hoursStartingToBedtimeFloored * HourlyRates.HOURLY_RATE_FROM_START_TO_BEDTIME
                + hoursBedtimeToMidnightFloored * HourlyRates.HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT
                + hoursMidnighttoEndFloored * HourlyRates.HOURLY_RATE_FROM_MIDNIGHT_TO_END;
    }

    /**
     * Floor the fractional hours and cast to int
     * @param hoursStartingToBedtime double hour
     * @return integer version of hour
     */

    private Integer removeFractional(Double hoursStartingToBedtime) {
        return (int) Math.floor(hoursStartingToBedtime);
    }

    /**
     * Uses NumberFormat class to convert a double into currency
     * @param money
     * @return properly formatted money string
     */

    public String formatMoney(Double money) {

        return NumberFormat.getCurrencyInstance().format(money);
    }
}
