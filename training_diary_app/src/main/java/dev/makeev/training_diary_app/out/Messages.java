package dev.makeev.training_diary_app.out;


import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.model.UserLogEvent;

import java.util.List;

public class Messages {
    private final Output<String> console = new OutputImpl();

    /**
     * Displays a welcome message to the user.
     */
    public void welcomeMessage() {
        console.output("Welcome!");
    }

    /**
     * Displays the authorization menu to the user.
     */
    public void authorizationMenu() {
        console.output("======= MAIN MENU =======\n" +
                "1. Log in.\n" +
                "2. Registration.\n\n" +
                "0. Exit");
    }
    public void userMenu() {
        console.output("=========== USER MENU ===========\n" +
                "1. Add new training.\n" +
                "2. Edite training.\n" +
                "3. Delete training.\n" +
                "4. Show trainings history.\n" +
                "5. Admin options.\n" +
                "6. Log out.\n\n" +
                "0. Exit");
    }
    public void loginMessage() {
        console.output("Enter Login: ");
    }
    public void passwordMessage() {
        console.output("Please enter password:");
    }
    public void tryOrBackMessage() {
        console.output("1. Try again.\n" +
                "2. Back.");
    }
    public void setYearMessage() {
        console.output("Please enter year (4 digits):");
    }
    public void setMonthMessage() {
        console.output("Please enter month (2 digits):");
    }
    public void setDayMessage() {
        console.output("Please enter day (2 digits):");
    }
    public void endMessage() {
        console.output("Goodbye!\n");
    }
    public void greetingMessage(String loginOfCurrentUser) {
        console.output("\nWelcome, " + loginOfCurrentUser + "!");
    }
    public void setDurationMessage() {
        console.output("Please enter duration:");
    }
    public void setCaloriesBurnedMessage() {
        console.output("Please enter calories burned:");
    }
    public void choiceTypeOfTrainingMessage() {
        console.output("What type of training?");
    }
    public void choiceTypeMessage() {
        console.output("1. Select from the list.\n" +
                "2. Add a new type.");
    }
    public void addTypeOfTrainingMessage() {
        console.output("Write the type of training: ");
    }

    public void getTypesOfTraining(List<TypeOfTraining> listOfTrainings) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 1;
        for (TypeOfTraining type : listOfTrainings) {
            stringBuilder.append(n).append(". ").append(type.type()).append("\n");
            n++;
        }
        console.output(stringBuilder.toString());
    }
    public void addSuccessful() {
        console.output("Trainings have been added.");
    }
    public void editSuccessful() {
        console.output("Training edited.");
    }
    public void deleteSuccessful() {
        console.output("Training deleted.");
    }

    public void adminMenu() {
        console.output("=========== ADMIN MENU ===========\n" +
                "1. Show training history for all users.\n" +
                "2. Show training history for user.\n" +
                "3. Show log for user.\n" +
                "4. Show log.\n" +
                "5. Back to User menu.\n\n" +
                "0. Exit");
    }
    public void loginMenu() {
        console.output("Enter your Login: ");
    }

    public void print(String s) {
        console.output(s);
    }

    public void printTrainingsForUser(List<Training> listOfTrainings, String login) {
        StringBuilder result = new StringBuilder("All trainings of " + login + ":\n");
        for (int i = 0; i < listOfTrainings.size(); i++) {
            Training training =  listOfTrainings.get(i);
                result.append(i + 1)
                        .append(" | ")
                        .append(training.date().getYear())
                        .append(" - ")
                        .append(training.date().getMonth())
                        .append(" - ")
                        .append(training.date().getDayOfMonth())
                        .append(" | ")
                        .append(training.type().type())
                        .append(" | ")
                        .append("Duration: ")
                        .append(training.duration())
                        .append(" | ")
                        .append("Calories are Burned: ")
                        .append(training.caloriesBurned())
                        .append("\n");
        }
        console.output(result.toString());
    }

    public void printTrainings(
            List<Training> listOfTrainings, String login) {
        StringBuilder result = new StringBuilder();
        for (Training training: listOfTrainings) {
            result.append(login)
                    .append(" | ")
                    .append(training.date().getYear())
                    .append(" - ")
                    .append(training.date().getMonth())
                    .append(" - ")
                    .append(training.date().getDayOfMonth())
                    .append(" | ")
                    .append(training.type().type())
                    .append(" | ")
                    .append("Duration: ")
                    .append(training.duration())
                    .append(" | ")
                    .append("Calories are Burned: ")
                    .append(training.caloriesBurned())
                    .append("\n");
        }
        console.output(result.toString());
    }

    public void printUserEvents(List<UserLogEvent> listOfUserEvent, String message) {
        StringBuilder result = new StringBuilder(message);
        for (UserLogEvent userLogEvent : listOfUserEvent) {
            result.append(userLogEvent.date())
                    .append(" | ")
                    .append(userLogEvent.login())
                    .append(" | ")
                    .append(userLogEvent.message())
                    .append("\n");
        }
        console.output(result.toString());
    }
}