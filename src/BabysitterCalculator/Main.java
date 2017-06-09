package BabysitterCalculator;

import java.util.HashMap;

import static BabysitterCalculator.Keys.*;

/**
 * Created by jacobmenke on 5/18/17.
 */
public class Main {
    public static void main(String[] args) {

        //main hash to store all user data
        HashMap<String, HashMap<String, String>> userData = Utilities.getUserData();

        //construct main objects babysitter and babysitting job
        BabySitter babySitter = new BabySitter(userData.get("personnelData").get(BABYSITTER_NAME));

        HashMap<String, String> timesData = new HashMap<>(userData.get("timesData"));

        BabysittingJob babysittingJob = new BabysittingJob(userData.get("personnelData").get(JOB_NAME), timesData.get(STARTING_TIME), timesData.get(BED_TIME), timesData.get(ENDING_TIME));

        String answer = babySitter.proposeJob(babysittingJob, timesData);

        if (answer.equals("yes")) {
            babySitter.says("I accept the job at '" + babysittingJob.getJobName() + "' because it has " +
                    "acceptable hours.");

            babySitter.says(String.format("I will start at %s, the children will go to bed at %s and I will finish at %s.",
                    userData.get("timesData").get(STARTING_TIME), userData.get("timesData").get(BED_TIME),
                    userData.get("timesData").get(ENDING_TIME)));

            String pay = babySitter.calculatePay(babysittingJob);

            babySitter.says("My calculated pay is " + pay + " for " + babySitter.calculateHours(babysittingJob).get("totalHours") + " hours of work.");
        } else {


            babySitter.says("I reject the job '" + babysittingJob.getJobName() + "' due to " +
                    "unacceptable hours, namely " + answer);


        }
    }


}
