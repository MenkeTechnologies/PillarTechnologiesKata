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

    private static void getDataInteractively(HashMap<String, String> personnelData, HashMap<String, String> timesData) {

        try (Scanner scanner = new Scanner(System.in)) {
            loopUntilValid(personnelData, scanner, BABYSITTER_NAME);

            loopUntilValid(personnelData, scanner, JOB_NAME);

            System.out.println("Please enter the times.  Format is h:mm am/pm");

            loopUntilValid(timesData, scanner, STARTING_TIME);

            loopUntilValid(timesData, scanner, BED_TIME);

            loopUntilValid(timesData, scanner, ENDING_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getDataNonInteractively(HashMap<String, String> personnelData, HashMap<String, String> timesData) {
        personnelData.put(BABYSITTER_NAME, "Jane Doe");
        personnelData.put(JOB_NAME, "James' House");
        timesData.put(STARTING_TIME, "8:00 pm");
        timesData.put(BED_TIME, "11:00 pm");
        timesData.put(ENDING_TIME, "3:00 am");
    }

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
    static String sanitizeTimes(String possibleTime) throws Exception {

        StringBuilder sb = new StringBuilder();

        possibleTime.chars().filter(c -> c != ' ').mapToObj(c -> (char) c).forEachOrdered(sb::append);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mma");

        //will throw exception if time is invalid which will lead to next iteration of while loop
        LocalTime localTime = LocalTime.parse(sb.toString().toUpperCase(), dtf);

        Integer validhour = localTime.getHour();
        Integer validMinutes = localTime.getMinute();

        Integer formattedHour = validhour;

        if (validhour == 0){
            formattedHour = 12;
        }

        if (validhour > 12){
            formattedHour-=12;
        }


        String time = formattedHour + ":" + String.format("%02d", validMinutes) + " " + (validhour < 12 ? "am" : "pm");

        return time;

    }

    static HashMap<String, HashMap<String, String>> getUserData() {
        HashMap<String, String> personnelData = new HashMap<>();
        HashMap<String, String> timesData = new HashMap<>();

//        getDataInteractively(personnelData, timesData);

        getDataNonInteractively(personnelData, timesData);

        HashMap<String, HashMap<String, String>> allData = new HashMap<>();

        allData.put("personnelData", personnelData);
        allData.put("timesData", timesData);

        return allData;
    }
}
