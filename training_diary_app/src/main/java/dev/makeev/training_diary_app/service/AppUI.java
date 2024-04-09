package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.exceptions.*;
import dev.makeev.training_diary_app.in.Input;
import dev.makeev.training_diary_app.in.InputImpl;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.User;
import dev.makeev.training_diary_app.out.Messages;


import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class AppUI {
    private final UserService userService = new UserService();

    private final AdminService adminService = new AdminService();

    private final TrainingsService trainingsService = new TrainingsService();

    private final Input input = new InputImpl();

    private final Messages console = new Messages();

    private Optional<String> loginOfCurrentUser = Optional.empty();

    private int userOption = -1;

    private boolean appAreWorking = true;

    private boolean showAuthorizationMenu = true;
    private boolean showAdminMenu = false;
    private boolean showUserMenu = false;
    private boolean showAddTrainingMenu = false;
    private boolean showLoginMenu = false;
    private boolean showHistoryForUser = false;
    private boolean showLogForUser = false;
    private boolean showEditMenu = false;
    private boolean showDeleteMenu = false;


    public void start() {
        console.welcomeMessage();
        while (appAreWorking) {
            if (showAuthorizationMenu) {
                authorizationMenu();
            }
            //if option "Log in" was selected
            if (showLoginMenu) {
                loginMenu();
            }
            //if login as user
            if (showUserMenu) {
                userMenu();
            }
        }
        console.endMessage();
    }

    private void authorizationMenu() {
        console.authorizationMenu();
        if (userOption < 0) {
            userOption = input.getInt(2);
        }

        switch (userOption) {
            case 1, 2 -> { //if option "Log in" was selected
                showAuthorizationMenu = false;
                showLoginMenu = true;
            }
            //if option "Sign in" was selected
            case 0 -> appAreWorking = false;
        }
    }

    private void loginMenu() {
        boolean goBack = false;
        while (loginOfCurrentUser.isEmpty()) {
            console.loginMenu();
            String login = input.getString();
            console.passwordMessage();
            if (userOption == 1) {
                try {
                    userService.checkCredentials(login, input.getString());
                    console.print("Access is allowed!");
                    loginOfCurrentUser = Optional.of(login);
                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                            "Login.");
                    break;
                } catch (VerificationException e) {
                    console.print(e.getMessage());
                }
            } else {
                try {
                    if (!userService.existByLogin(login)) {
                        userService.addUser(login, input.getString());
                        console.print("Account was created!");
                        loginOfCurrentUser = Optional.of(login);
                        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                "Account was created and login.");
                        break;
                    } else {
                        throw new LoginAlreadyExistsException();
                    }
                } catch (LoginAlreadyExistsException e) {
                    console.print(e.getMessage());
                }
            }
            console.tryOrBackMessage();
            if (input.getInt(2) == 2) {
                goBack = true;
                break;
            }
        }
        if (goBack) {
            showAuthorizationMenu = true;
            showLoginMenu = false;
            userOption = -1;
        } else {
            showUserMenu = true;
            showLoginMenu = false;
            userOption = -1;
        }
    }

    private void userMenu() {
        while (showUserMenu) {
            console.greetingMessage(loginOfCurrentUser.orElseThrow());
            console.userMenu();
            if (userOption < 0) {
                userOption = input.getInt(6);
            }
            switch (userOption) {
                case 1 -> //if "Add new training." was selected
                        showAddTrainingMenu = true;
                case 2 -> {//if "Show trainings history." was selected
                    showEditMenu = true;
                    showTrainingHistory();
                    }
                case 3 -> {//if "Edite training." was selected
                    showDeleteMenu = true;
                    showTrainingHistory();
                    }
                case 4 ->  //if "Delete training." was selected
                        showTrainingHistory();
                case 5 -> //if "Admin options" was selected
                        goToAdminOptions();
                case 6 -> //if "Log out" was selected
                        logOut();
                case 0 ->  //if "Exit" was selected
                        System.exit(0);
            }
            //if "Add new training" was selected
            if (showAddTrainingMenu) {
                enterTrainingMenu(-1);
            }
            //if "Admin options" was selected
            if (showAdminMenu) {
                adminMenu();
            }
        }
    }

    private void enterTrainingMenu(int indexForEdit) {
        String typeOfTraining = null;
        console.choiceTypeOfTrainingMessage();
        console.getTypesOfTraining(trainingsService.getAllTypesOfTraining());
        console.choiceTypeMessage();
        userOption = input.getInt(2);

        switch (userOption) {
            case 1 -> {
                console.getTypesOfTraining(trainingsService.getAllTypesOfTraining());
                int index = input.getInt(trainingsService.getSizeOfListOfTypes());
                try {
                    typeOfTraining = trainingsService
                            .getTypeOfTrainingByIndex(index);
                } catch (EmptyException e) {
                    console.print(e.getMessage());
                }
            }
            case 2 -> {
                console.addTypeOfTrainingMessage();
                typeOfTraining = input.getString();
                trainingsService.addTypeOfTraining(typeOfTraining);
                console.print("A new type of training was added.");
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Add a new type of training.");
            }
        }

        LocalDate date = getDate();

        console.setDurationMessage();
        double duration = input.getDouble();

        console.setCaloriesBurnedMessage();
        double caloriesBurned = input.getDouble();

        try {
            if (!showEditMenu) {
                trainingsService.addTrainingOfUser(
                        loginOfCurrentUser.orElseThrow(), typeOfTraining, date, duration, caloriesBurned);
                console.addSuccessful();
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Added new training.");
            } else {
                editTraining(indexForEdit, loginOfCurrentUser.orElseThrow(), typeOfTraining,
                        date, duration, caloriesBurned);
            }
        } catch (EmptyException e) {
            console.print(e.getMessage());
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
        } catch (TrainingOnDateAlreadyExistsException e) {
            console.print(e.getMessage());
        } catch (TrainingNotFoundException e) {
            console.print(e.getMessage());
        }

        showAddTrainingMenu = false;
        userOption = -1;
    }

    private void showTrainingHistory() {
        List<Training> trainingsList = trainingsService.getAllTrainingsForUser(loginOfCurrentUser.orElseThrow());
        int trainingsListSize = trainingsList.size();
        console.printTrainingsForUser(trainingsList,
                loginOfCurrentUser.orElseThrow());
        userOption = -1;
        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                "Trainings history was viewed.");

        if (showEditMenu) {
            console.print("What workout name should I change?");
            enterTrainingMenu(input.getInt(trainingsListSize));
            console.editSuccessful();
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Edit training.");
            showEditMenu = false;
        }

        if (showDeleteMenu) {
            console.print("What training name should I delete?");
            trainingsService.delete(input.getInt(trainingsListSize), loginOfCurrentUser.orElseThrow());
            console.deleteSuccessful();
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Delete training.");
            showDeleteMenu = false;
        }
    }

    private void editTraining(int indexForEdit, String login, String typeOfTraining,
                              LocalDate date, double duration, double caloriesBurned)
            throws EmptyException, TrainingOnDateAlreadyExistsException, TrainingNotFoundException {
        trainingsService.edite(indexForEdit, login, typeOfTraining, date, duration, caloriesBurned);
    }
    private void goToAdminOptions() {
        try {
            if (userService.isAdmin(loginOfCurrentUser.orElseThrow())) {
                showAdminMenu = true;
                userOption = -1;
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Enter in admin option.");
            } else {
                userOption = -1;
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Tried to entered in admin option.");
                throw new NotAdminException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
        } catch (NotAdminException e) {
            console.print(e.getMessage());
        }
    }
    private void adminMenu() {
        while (showAdminMenu) {
            console.adminMenu();
            userOption = input.getInt(5);
            switch (userOption) {
                case 1 -> //if "Show training history for all users." was selected
                        showTrainingHistoryForAllUsers();
                case 2 ->  //if "Show training history for user." was selected
                        showHistoryForUser = true;
                case 3 -> //if "Show log for user" was selected
                        showLogForUser = true;
                case 4 -> { //if "Show log" was selected
                    console.printUserEvents(adminService.getAllEvents(),
                            "Log for all users:\n");
                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                            "Log was viewed.");
                }
                case 5 -> { //if "Back to User menu" was selected
                    userOption = -1;
                    showAdminMenu = false;
                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                            "Exit from admin options.");
                }
                case 0 ->  //if "Exit" was selected
                        System.exit(0);
            }
            if (showHistoryForUser) {
                showTrainingHistoryForUser();
            }
            if (showLogForUser) {
                logForUser();
            }
        }
    }

    private void showTrainingHistoryForAllUsers() {
        try {
            List<User> users = userService.getAll();
            if (users.size() < 2) {
                throw new EmptyException();
            }
            console.print("History of trainings for all users:");
            for (User user : users) {
                console.printTrainings(trainingsService.getAllTrainingsForUser(user.login()), user.login());
            }
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "History of trainings for all users was viewed.");
        } catch (EmptyException e) {
            console.print(e.getMessage());
        }
        userOption = -1;
    }

    private void showTrainingHistoryForUser() {
        console.loginMessage();
        String login = input.getString();
        try {
            if (userService.existByLogin(login)) {
                console.printTrainingsForUser(
                        trainingsService.getAllTrainingsForUser(login), login);
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Training history for user was viewed.");
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Tried to view a training history for user.");
        }

        userOption = -1;
        showHistoryForUser = false;
    }

    private void logForUser() {
        console.loginMessage();
        String login = input.getString();
        try {
            if (userService.existByLogin(login)) {
                console.printUserEvents(adminService.getAllEventsForUser(login),
                        "Log for " + login + ":\n");
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Log for " + login + " was viewed.");
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Tried to view a log for user.");
        }

        userOption = -1;
        showLogForUser = false;
    }


    private void logOut() {
        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                "Logout.");
        loginOfCurrentUser = Optional.empty();
        showAuthorizationMenu = true;
        showUserMenu = false;
        userOption = -1;
    }

    private LocalDate getDate() {
        console.setYearMessage();
        int year;
        do {
            year = input.getInteger(4, 0, 9999);
        } while (year < 0);

        console.setMonthMessage();
        int month;
        do {
            month = input.getInteger(2, 1, 12);
        } while (month <= 0 && month > 12);

        console.setDayMessage();
        int day;
        do {
            day = input.getInteger(2, 1, 12);
        } while (day <= 0 && day > YearMonth.of(year, month).lengthOfMonth());

        return LocalDate.of(year, month, day);
    }
}
