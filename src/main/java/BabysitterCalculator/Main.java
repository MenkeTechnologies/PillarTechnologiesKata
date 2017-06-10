package BabysitterCalculator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static BabysitterCalculator.Keys.*;
import static BabysitterCalculator.HourlyRates.*;

/**
 * Created by jacobmenke on 5/18/17.
 */
public class Main {

    public static void main(String[] args) {

        //main hash to store all user data

        String interactionType = "interactive";

        //do while loop to ensure at least one iteration if noninteractive
        do {

            //main data structure, contains babysitter and job data in one hash and times data in another hash
            HashMap<String, HashMap<String, String>> userData = Utilities.getUserData(interactionType);

            //construct main objects babysitter and babysitting job
            BabySitter babySitter = new BabySitter(userData.get("personnelData").get(BABYSITTER_NAME));

            HashMap<String, String> timesData = new HashMap<>(userData.get("timesData"));

            BabysittingJob babysittingJob = new BabysittingJob(userData.get("personnelData").get(JOB_NAME), timesData.get(STARTING_TIME), timesData.get(BED_TIME), timesData.get(ENDING_TIME));

            //does babysitter decide to accept job?
            String answer = babySitter.proposeJob(babysittingJob, timesData);

            if (answer.equals("yes")) {
                babySitter.says("I accept the job at '" + babysittingJob.getJobName() + "' because it has " +
                        "acceptable hours.");

                babySitter.says(String.format("I will start at %s, the children will go to bed at %s and I will finish at %s.",
                        userData.get("timesData").get(STARTING_TIME), userData.get("timesData").get(BED_TIME),
                        userData.get("timesData").get(ENDING_TIME)));

                //babysitter sets the hours map for the babysitting job
                babySitter.calculateHours(babysittingJob);

                //and calculates the total pay
                String pay = babySitter.calculatePay(babysittingJob);

                //extra newline for readability
                System.out.println();

                //print out floored (nonfractional) hours in tabular format
                //hoursMap property in BabysittingJob object has all the hours
                babySitter.says(String.format("My breakdown is: \n\t%s hours from start to bedtime at $" + HOURLY_RATE_FROM_START_TO_BEDTIME + "/hr," +
                                "\n\t%s hours from bedtime to midnight at $" + HOURLY_RATE_FROM_BEDTIME_TO_MIDNIGHT + "/hr,\n\t%s hours from midnight to end at $" + HOURLY_RATE_FROM_MIDNIGHT_TO_END + "/hr.", babysittingJob.getHoursMap().get("hoursStartingToBedtimeFloored"),
                        babysittingJob.getHoursMap().get("hoursBedtimeToMidnightFloored"), babysittingJob.getHoursMap().get("hoursMidnightToEndFloored")));
                //extra newline for readability
                System.out.println();

                babySitter.says("According to my calculations, the pay should be " + pay + " for " + babysittingJob.getHoursMap().get("totalHours") + " hours of work disregarding fractional hours that is.");
            } else {

                babySitter.says("I reject the job '" + babysittingJob.getJobName() + "' due to " +
                        "unacceptable hours, namely " + answer);
            }

            if (interactionType.equals("interactive")) {
                System.out.println("Another go?");

                String goAgain = new Scanner(System.in).nextLine();

                if (goAgain.equalsIgnoreCase("n") || goAgain.equalsIgnoreCase("no")) {
                    break;
                }
            }


        } while (interactionType.equals("interactive"));
    }
}
