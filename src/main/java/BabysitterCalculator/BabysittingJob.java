package BabysitterCalculator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by jacobmenke on 4/27/17.
 */


class HourlyRates {
    public static final Integer HOURLY_RATE_FROM_START_TO_BEDTIME = 12;
    public static final Integer HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT = 8;

    public static final Integer HOURLY_RATE_FROM_MIDNIGHT_TO_END= 16;

}

class Keys {
    //hash keys
    public static final String BABYSITTER_NAME = "babysitterName";
    public static final String JOB_NAME = "jobName";
    public static final String STARTING_TIME = "startingTime";
    public static final String BED_TIME = "bedTime";
    public static final String ENDING_TIME = "endingTime";

}


public class BabysittingJob {
    private String jobName;
    private Double startingTime;
    private Double endingTime;
    private Double bedTime;

    public BabysittingJob(String jobName, String startingTime, String bedTime, String endingTime) {
        this.jobName = jobName;

        //convert string time to Double
        this.startingTime = formatStringTimeToDouble(startingTime);

        this.endingTime = formatStringTimeToDouble(endingTime);
        this.bedTime = formatStringTimeToDouble(bedTime);
    }

    private Double formatStringTimeToDouble(String genericTime) {

        DateTimeFormatter dtf =DateTimeFormatter.ofPattern("h:mm a");


        LocalTime localTime = null;
        //parsing requires AM or PM
        try {
            localTime = LocalTime.parse(genericTime.toUpperCase(), dtf);
        } catch (Exception e) {
            System.out.println("Invalid time: " + genericTime);
            System.exit(0);
        }
        return (double) localTime.getHour() + (double) localTime.getMinute() / 60;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Double getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Double startingTime) {
        this.startingTime = startingTime;
    }

    public Double getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Double endingTime) {
        this.endingTime = endingTime;
    }

    public Double getBedTime() {
        return bedTime;
    }

    public void setBedTime(Double bedTime) {
        this.bedTime = bedTime;
    }
}
