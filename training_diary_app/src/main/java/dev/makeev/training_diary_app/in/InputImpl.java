package dev.makeev.training_diary_app.in;

import java.util.Scanner;

/**
 * Implementation of the {@link Input} interface for handling user input in a console application.
 */
public class InputImpl implements Input {

    /**
     * Service for input stream.
     */
    private final Scanner input = new Scanner(System.in);

    /**
     * Reads and returns an integer input from the user within the specified range.
     *
     * @param max The maximum allowed integer value.
     * @return The integer input from the user.
     */
    @Override
    public int getInt(int max) {
        //option that user entered
        String optionString;

        //option that will convert for the system
        int option = -1;

        boolean isValid;
        do {
            optionString = input.nextLine();

            isValid = optionString.matches("[0-9]+") && Integer.parseInt(optionString) <= max;

            if (isValid) {
                option = Integer.parseInt(optionString);
            } else {
                System.out.println("Enter only digits 0 - " + max);
            }
        } while (!isValid || optionString.isEmpty());
        return option;
    }

    /**
     * Reads and returns a string input from the user.
     *
     * @return The string input from the user.
     */
    @Override
    public String getString() {
        return input.nextLine();
    }

    /**
     * Reads and returns a double input from the user.
     *
     * @return The double input from the user.
     */
    @Override
    public Double getDouble() {
        String str = "";
        double result = -1;
        boolean scan = true;
        while (scan) {
            str = input.nextLine();
            if (str.matches("^\\d+(\\.\\d+)?$") && str != null && !str.isEmpty()) {
                result = Double.parseDouble(str);
            }
            if (result >= 0) {
                    scan = false;
            }
        }
        return result;
    }

    /**
     * Reads and returns an integer input from the user within the specified length and range.
     *
     * @param maxLength The maximum length of the input.
     * @param min       The minimum allowed integer value.
     * @param max       The maximum allowed integer value.
     * @return The integer input from the user.
     */
    @Override
    public Integer getInteger(int maxLength, int min, int max) {
        String str = "";
        int result = -1;
        boolean scan = true;
            while (scan) {
                str = input.nextLine();
                if (str.matches("[0-9]+") && str.length() <= maxLength) {
                    result = Integer.parseInt(str);
                    if (result >= min && result <= max) {
                        scan = false;
                    }
                }
            }
        return result;
    }

}
