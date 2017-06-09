package PillarKata;

import java.text.NumberFormat;
import java.util.HashMap;

import static PillarKata.Keys.*;

/**
 * Created by jacobmenke on 4/27/17.
 */


public class BabySitter {
    private String name;
    private BabysittingJob babysittingJob;

    public BabySitter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BabysittingJob getBabysittingJob() {
        return babysittingJob;
    }

    public String proposeJob(BabysittingJob babysittingJob, HashMap<String, String> timesData) {

        String reason = "";

        if (babysittingJob.getStartingTime() >= 17){

            if (babysittingJob.getEndingTime() <= 4) {
                this.babysittingJob = babysittingJob;
                return "yes";

            } else {
                return "the late ending time at " + timesData.get(ENDING_TIME) + ".";

            }


        } else {
            reason += "the early starting time at " + timesData.get(STARTING_TIME);
            if (babysittingJob.getEndingTime() <= 4){
                reason += ".";

            } else {
                reason += ", not to mention the late ending hours at " + timesData.get(ENDING_TIME) + ".";


            }
        }

        return reason;



    }

    public void says(String message) {

        System.out.println(name + " says, \"" + message + "\"");
    }

    public HashMap<String, Long> calculateHours(BabysittingJob babysittingJob) {

        if (babysittingJob != null) {

            Double hoursStartingToBedtime = babysittingJob.getBedTime() - babysittingJob.getStartingTime();

            //midnight is at 24 hours
            Double hoursBedtimeToMidnight = 24 - babysittingJob.getBedTime();

            //dont need to subtract because calculated from 0 or midnight
            Double hoursMidnighttoEnd = babysittingJob.getEndingTime();

            long hoursStartingToBedtimeLong = removeFractional(hoursStartingToBedtime);
            long hoursBedtimeToMidnightLong = removeFractional(hoursBedtimeToMidnight);
            long hoursMidnighttoEndLong = removeFractional(hoursMidnighttoEnd);

            Long hours = removeFractional(hoursStartingToBedtime) + removeFractional(hoursBedtimeToMidnight) + removeFractional(hoursMidnighttoEnd);

            HashMap<String, Long> hourMap = new HashMap<>();
            hourMap.put("hoursStartingToBedtimeLong", hoursStartingToBedtimeLong);
            hourMap.put("hoursBedtimeToMidnightLong", hoursBedtimeToMidnightLong);
            hourMap.put("hoursMidnighttoEndLong", hoursMidnighttoEndLong);
            hourMap.put("totalHours", hours);

            return hourMap;
        }
        return null;
    }

    public String calculatePay(BabysittingJob babysittingJob) {

        if (babysittingJob != null) {

            HashMap<String, Long> hoursMap = calculateHours(babysittingJob);

            Double pay = (double) calculatePay(hoursMap.get("hoursStartingToBedtimeLong"), hoursMap.get("hoursBedtimeToMidnightLong"), hoursMap.get("hoursMidnighttoEndLong"));

            //no fractional hours
            return formatMoney(pay);
        }

        return null;
    }

    private long calculatePay(long hoursStartingToBedtimeLong, long hoursBedtimeToMidnightLong, long hoursMidnighttoEndLong) {
        return hoursStartingToBedtimeLong * HourlyRates.HOURLY_RATE_FROM_START_TO_BEDTIME
                + hoursBedtimeToMidnightLong * HourlyRates.HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT
                + hoursMidnighttoEndLong * HourlyRates.HOURLY_RATE_FROM_MIDNIGHT_TO_END;
    }

    private Long removeFractional(Double hoursStartingToBedtime) {

        return Math.round(hoursStartingToBedtime);
    }

    public String formatMoney(Double money) {

        return NumberFormat.getCurrencyInstance().format(money);
    }
}
