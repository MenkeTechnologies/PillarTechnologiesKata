package BabysitterCalculator;

import java.text.NumberFormat;
import java.util.HashMap;

import static BabysitterCalculator.Keys.*;

/**
 * Represents a babysitter
 */
public class BabySitter {
    private String name;
    private BabysittingJob babysittingJob;

    /**
     * Constructor for babysitter
     *
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
     *
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
                // needed for calculations and comparisons
                //1 AM represented as 25
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

//                    check to make sure ending time is after bedtime
                if (babysittingJob.getStartingTime() <= babysittingJob.getBedTime() && babysittingJob.getBedTime() <= babysittingJob.getEndingTime()) {
//                    ending time is before midnight
                    return "yes";
                } else {
                    //the ending time is later than 4 am
                    return "the late ending time at " + timesData.get(ENDING_TIME) + ".";
                }
//                }
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
     *
     * @param message
     */
    public void says(String message) {
        System.out.println(name + " says, \"" + message + "\"");
    }

    /**
     * Floors fractional hours and prepares for rates calculations
     *
     * @param babysittingJob
     * @return Hashmap with hours breakdown
     */
    public HashMap<String, Integer> calculateHours(BabysittingJob babysittingJob) {

        if (babysittingJob != null) {

            Double hoursStartingToBedtime = babysittingJob.getBedTime() - babysittingJob.getStartingTime();

            Double hoursBedtimeToMidnight = 0.0;


            if (babysittingJob.getEndingTime() >= 24){
                //ending time is after midnight so calculate hours from bedtime to midnight
                hoursBedtimeToMidnight = 24 - babysittingJob.getBedTime();
            } else {
                //ending time before midnight so just calculate from bedtime to endingtime
                hoursBedtimeToMidnight = babysittingJob.getEndingTime() - babysittingJob.getBedTime();
            }

            //default to zero hours from midnight to end
            //keep value if ending time before or at midnight
            Double hoursMidnightToEnd = 0.0;

            //ending time is between midnight and 4am
            //dont need to subtract because ending time calculated from 0 or midnight
            if (babysittingJob.getEndingTime() > 24 && babysittingJob.getEndingTime() <= 28) {
                hoursMidnightToEnd = babysittingJob.getEndingTime();
            }

            //compensate for 24 hour compensation introduced earlier
            if (hoursMidnightToEnd >= 24) {
                hoursMidnightToEnd -= 24;
            }

            //remove fractions for calculations
            Integer hoursStartingToBedtimeFloored = removeFractional(hoursStartingToBedtime);
            Integer hoursBedtimeToMidnightFloored = removeFractional(hoursBedtimeToMidnight);
            Integer hoursMidnighttoEndFloored = removeFractional(hoursMidnightToEnd);
            //remove fractions for hours sum
            Integer hours = removeFractional(hoursStartingToBedtime) + removeFractional(hoursBedtimeToMidnight) + removeFractional(hoursMidnightToEnd);

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
     *
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
     *
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
     *
     * @param hoursStartingToBedtime double hour
     * @return integer version of hour
     */
    private Integer removeFractional(Double hoursStartingToBedtime) {
        return (int) Math.floor(hoursStartingToBedtime);
    }

    /**
     * Uses NumberFormat class to convert a double into currency
     *
     * @param money
     * @return properly formatted money string
     */
    public static String formatMoney(Double money) {

        return NumberFormat.getCurrencyInstance().format(money);
    }
}
