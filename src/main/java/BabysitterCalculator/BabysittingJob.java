package BabysitterCalculator;

import java.security.Key;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

class Keys {
    //hash keys for data Hashmaps
    public static final String BABYSITTER_NAME = "babysitterName";
    public static final String JOB_NAME = "jobName";
    public static final String STARTING_TIME = "startingTime";
    public static final String BED_TIME = "bedTime";
    public static final String ENDING_TIME = "endingTime";
}

/**
 * Represents a babysitting job
 */
public class BabysittingJob {
    private String jobName;
    private Double startingTime;
    private Double endingTime;
    private Double bedTime;
    HashMap<String, Integer> hoursMap = new HashMap<>();
    HashMap<String, String> timesData = new HashMap<>();
    private boolean valid = true;

    public boolean isValid() {
        return valid;
    }


    public HashMap<String, String> getTimesData() {
        return timesData;
    }

    /**
     * Constructor for tests
     *
     * @param jobName name of the babysitting job
     * @param startingTime starting time of job
     * @param bedTime bedtime of job
     * @param endingTime ending time of job
     */
    public BabysittingJob(String jobName, String startingTime, String bedTime, String endingTime) {
        this.jobName = jobName;
        this.startingTime = formatStringTimeToDouble(startingTime);
        this.endingTime = formatStringTimeToDouble(endingTime);
        this.bedTime = formatStringTimeToDouble(bedTime);

        this.timesData = new HashMap<>();
        timesData.put(Keys.STARTING_TIME, startingTime);
        timesData.put(Keys.ENDING_TIME, endingTime);
        timesData.put(Keys.BED_TIME, bedTime);
    }

    /**
     * @param jobName the babysitting job name
     * @param timesData Hashmap with all of the times
     */
    public BabysittingJob(String jobName, HashMap<String, String> timesData) {
        this.jobName = jobName;

        //convert string incoming time to Double
        this.startingTime = formatStringTimeToDouble(timesData.get(Keys.STARTING_TIME));
        this.endingTime = formatStringTimeToDouble(timesData.get(Keys.ENDING_TIME));
        this.bedTime = formatStringTimeToDouble(timesData.get(Keys.BED_TIME));

        this.timesData = new HashMap<>(timesData);
    }

    /**
     * add 24 hours to times for calculations
     */
    void compensateFor24Hours() {
        if (startingTime >= 0 && startingTime <= 12) {
            startingTime += 24;
        }

        if (bedTime >= 0 && bedTime <= 12) {
            bedTime += 24;
        }

        if (endingTime >= 0 && endingTime <= 12) {
            endingTime += 24;
        }
    }

    /**
     * Converts string into double representation of time
     *
     * @param genericTime possibly valid time in string form
     * @return double and fractional representation of time
     */
    private Double formatStringTimeToDouble(String genericTime) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");

        LocalTime localTime = null;
        //parsing requires AM or PM so need to uppercase string input
        try {
            localTime = LocalTime.parse(genericTime.toUpperCase(), dtf);
        } catch (Exception e) {
            //parsing threw exception
            System.out.println("Invalid time: " + genericTime + ".");
            valid = false;
            return -1.0;
        }

        //return time as double with fraction, fraction will be floored later
        return (double) localTime.getHour() + (double) localTime.getMinute() / 60;
    }

    //POJO accessors and mutators

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

    /**
     * Setter for hoursMap
     * Called by babysitter's calculateHours method
     *
     * @param hoursMap
     */
    public void setHoursMap(HashMap<String, Integer> hoursMap) {
        this.hoursMap = hoursMap;
    }

    /**
     * Getter for hoursMap
     *
     * @return hoursMap used for outputting hours
     */
    public HashMap<String, Integer> getHoursMap() {
        return hoursMap;
    }
}
