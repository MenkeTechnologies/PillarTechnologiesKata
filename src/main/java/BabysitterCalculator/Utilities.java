package BabysitterCalculator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

import static BabysitterCalculator.Keys.*;

/**
 * Created by jacobmenke on 6/9/17.
 */
public class Utilities {
    /**
     * Get all necessary data in while loop structure, looping until valid
     * @param personnelData
     * @param timesData
     * @return
     */
    private static void getDataInteractively(HashMap<String, String> personnelData, HashMap<String, String> timesData) {

        Scanner scanner = new Scanner(System.in);

        loopUntilValid(personnelData, scanner, BABYSITTER_NAME);

        loopUntilValid(personnelData, scanner, JOB_NAME);

        System.out.println("Please enter the times.  Format is h:mm am/pm");

        loopUntilValid(timesData, scanner, STARTING_TIME);

        loopUntilValid(timesData, scanner, BED_TIME);

        loopUntilValid(timesData, scanner, ENDING_TIME);
    }


    /**
     * For quick testing purposes, hard code data into the two main hash maps
     * @param personnelData
     * @param timesData
     */
    private static void getDataNonInteractively(HashMap<String, String> personnelData, HashMap<String, String> timesData) {
        personnelData.put(BABYSITTER_NAME, "Jane Doe");
        personnelData.put(JOB_NAME, "James' House");
        timesData.put(STARTING_TIME, "8:35 pm");
        timesData.put(BED_TIME, "12:05 am");
        timesData.put(ENDING_TIME, "2:00 am");
    }

    /**
     * Build up the prompts and invalid messages
     * @param dataMap
     * @param scanner
     * @param dataDescriptor controls switch statement
     */
    private static void loopUntilValid(HashMap<String, String> dataMap, Scanner scanner, String dataDescriptor) {
        String prompt = "Please enter the ";
        String invalidMessage = "Invalid ";
        String key = dataDescriptor;

        switch (dataDescriptor) {
            case BABYSITTER_NAME:
                prompt += "babysitter's name.";
                invalidMessage += "babysitter's name.";

                break;
            case JOB_NAME:
                prompt += "job's name.";
                invalidMessage += "job name.";

                break;
            case STARTING_TIME:
                prompt += "starting time.";
                invalidMessage += "starting time.";

                break;
            case BED_TIME:
                prompt += "bedtime.";
                invalidMessage += "bedtime.";

                break;
            case ENDING_TIME:
                prompt += "ending time.";
                invalidMessage += "ending time.";
                break;
            default:
                break;
        }

        while (true) {

            System.out.println(prompt);

            String userInput = scanner.nextLine().trim();

            //only checking for some character in babysitter name and job name
            if (!userInput.equals("")) {

                //check for valid time
                if (key.contains("Time")) {

                    try {
                        String validTime = Utilities.sanitizeTimes(userInput);
                        userInput = validTime;
                    } catch (Exception e) {
                        System.out.println(invalidMessage);
                        continue;
                    }
                }

                dataMap.put(key, userInput);
                break;
            }
            System.out.println(invalidMessage);
        }

        for (int i = 0; i < 50; i++) {
            System.out.print("_");
        }
        System.out.println();
    }

    /**
     * Check if the starting, bed and ending times are valid
     * @param possibleTime
     * @return properly formatted and valid time
     * @throws Exception if invalid time
     */

    static String sanitizeTimes(String possibleTime) throws Exception {

        StringBuilder sb = new StringBuilder();

        possibleTime.chars().filter(c -> c != ' ').mapToObj(c -> (char) c).forEachOrdered(sb::append);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mma");

        //will throw exception if time is invalid which will lead to next iteration of while loop
        LocalTime localTime = LocalTime.parse(sb.toString().toUpperCase(), dtf);

        Integer validhour = localTime.getHour();
        Integer validMinutes = localTime.getMinute();

        Integer formattedHour = validhour;

        if (validhour == 0) {
            formattedHour = 12;
        }

        if (validhour > 12) {
            formattedHour -= 12;
        }

        String time = formattedHour + ":" + String.format("%02d", validMinutes) + " " + (validhour < 12 ? "am" : "pm");

        return time;
    }

    /**
     * Generate the main HashMap
     * @param type interactive or noninteractive data input
     * @return main HashMap containing two Hash Maps
     */
    static HashMap<String, HashMap<String, String>> getUserData(String type) {
        HashMap<String, String> personnelData = new HashMap<>();
        HashMap<String, String> timesData = new HashMap<>();

        if (type.equals("interactive")) {
            getDataInteractively(personnelData, timesData);
        } else {
            getDataNonInteractively(personnelData, timesData);
        }

        HashMap<String, HashMap<String, String>> allData = new HashMap<>();

        //nest HashMaps inside main HashMap
        allData.put("personnelData", personnelData);
        allData.put("timesData", timesData);

        return allData;
    }
}
