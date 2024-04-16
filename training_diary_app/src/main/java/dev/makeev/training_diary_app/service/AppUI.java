package dev.makeev.training_diary_app.service;

import dev.makeev.training_diary_app.dao.impl.TrainingOfUserDAOImpl;
import dev.makeev.training_diary_app.dao.impl.TypeOfTrainingDAOImpl;
import dev.makeev.training_diary_app.dao.impl.UserDAOImpl;
import dev.makeev.training_diary_app.dao.impl.UserLogEventDAOImpl;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.exceptions.LoginAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.NotAdminException;
import dev.makeev.training_diary_app.exceptions.TrainingNotFoundException;
import dev.makeev.training_diary_app.exceptions.TrainingOnDateAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.exceptions.VerificationException;
import dev.makeev.training_diary_app.in.Input;
import dev.makeev.training_diary_app.in.InputImpl;
import dev.makeev.training_diary_app.model.Statistic;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.model.User;
import dev.makeev.training_diary_app.out.Messages;
import dev.makeev.training_diary_app.out.OutputImpl;
import dev.makeev.training_diary_app.repository.impl.LogsRepositoryImpl;
import dev.makeev.training_diary_app.repository.impl.TrainingOfUserRepositoryImpl;
import dev.makeev.training_diary_app.repository.impl.TypeOfTrainingRepositoryImpl;
import dev.makeev.training_diary_app.repository.impl.UserRepositoryImpl;


import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class AppUI {
    private final UserService userService = new UserService(new UserDAOImpl(new UserRepositoryImpl()));

    private final AdminService adminService = new AdminService(new UserLogEventDAOImpl(new LogsRepositoryImpl()));

    private final TrainingsService trainingsService =
            new TrainingsService(new TrainingOfUserDAOImpl(new TrainingOfUserRepositoryImpl()),
                    new TypeOfTrainingDAOImpl(new TypeOfTrainingRepositoryImpl()));

    private final Input input = new InputImpl();
    private final Messages console = new Messages(new OutputImpl());
    private String loginOfCurrentUser = null;
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
    private boolean showStatisticMenu = false;

    {
        try {
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(1),
                    LocalDate.of(2024, 2, 2), 175.0, 153.5);
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(1),
                    LocalDate.of(2024, 1, 2), 215.0, 60.5);
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(1),
                    LocalDate.of(2024, 5, 2), 105.0, 600.5);
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(1),
                    LocalDate.of(2024, 8, 2), 155.0, 550.5);
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(1),
                    LocalDate.of(2024, 7, 2), 20.0, 70.5);

            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(2),
                    LocalDate.of(2024, 10, 2), 115.0, 1070.5);
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(2),
                    LocalDate.of(2024, 11, 2), 60.0, 365.5);
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(2),
                    LocalDate.of(2024, 4, 2), 175.1, 650.5);
            trainingsService.addTrainingOfUser("DemoUser", trainingsService.getTypeOfTrainingByIndex(2),
                    LocalDate.of(2024, 9, 2), 22.0, 75.5);
        } catch (EmptyException | TrainingOnDateAlreadyExistsException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


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
        while (loginOfCurrentUser == null) {
            console.loginMenu();
            String login = input.getString();
            console.passwordMessage();
            if (userOption == 1) {
                try {
                    userService.checkCredentials(login, input.getString());
                    console.print("Access is allowed!");
                    loginOfCurrentUser = login;
                    adminService.addEvent(loginOfCurrentUser,
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
                        loginOfCurrentUser = login;
                        adminService.addEvent(loginOfCurrentUser,
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
            console.greetingMessage(loginOfCurrentUser);
            console.userMenu();
            if (userOption < 0) {
                userOption = input.getInt(7);
            }
            switch (userOption) {
                case 1 -> //if "Add new training." was selected
                        showAddTrainingMenu = true;
                case 2 -> {//if "Edite training." was selected
                    showEditMenu = true;
                    showTrainingHistory();
                    }
                case 3 -> {//if "Delete training." was selected
                    showDeleteMenu = true;
                    showTrainingHistory();
                    }
                case 4 -> //if "Show trainings history." was selected
                        showTrainingHistory();
                case 5 -> {//if "Show statistic of trainings." was selected
                    showStatisticMenu = true;
                    showStatisticOfTrainingMenu();
                    }
                case 6 -> //if "Admin options" was selected
                        goToAdminOptions();
                case 7 -> //if "Log out" was selected
                        logOut();
                case 0 ->  //if "Exit" was selected
                        System.exit(0);
            }
            //if "Add new training" was selected
            if (showAddTrainingMenu) {
                showTrainingMenu(-1);
            }
            //if "Admin options" was selected
            if (showAdminMenu) {
                showAdminMenu();
            }
        }
    }

    private void showTrainingMenu(int indexForEdit) {
        String typeOfTraining = null;
        List<TypeOfTraining> listOfTypes = trainingsService.getAllTypesOfTraining();
        console.choiceTypeOfTrainingMessage();
        console.getTypesOfTraining(listOfTypes);
        console.choiceTypeMessage();
        userOption = input.getInt(2);

        switch (userOption) {
            case 1 -> {
                int index = input.getInt(listOfTypes.size());
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
                adminService.addEvent(loginOfCurrentUser,
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
                        loginOfCurrentUser, typeOfTraining, date, duration, caloriesBurned);
                console.addSuccessful();
                adminService.addEvent(loginOfCurrentUser,
                        "Added new training.");
            } else {
                editTraining(indexForEdit, loginOfCurrentUser, typeOfTraining,
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
        List<Training> trainingsList = trainingsService.getAllTrainingsForUser(loginOfCurrentUser);
        int trainingsListSize = trainingsList.size();
        console.printTrainingsForUser(trainingsList,
                loginOfCurrentUser);
        userOption = -1;
        adminService.addEvent(loginOfCurrentUser,
                "Trainings history was viewed.");

        if (showEditMenu) {
            if (trainingsList.isEmpty()) {
                showEditMenu = false;
                try {
                    adminService.addEvent(loginOfCurrentUser,
                            "Trying edit training.");
                    throw new EmptyException();
                } catch (EmptyException e) {
                    console.print(e.getMessage());
                }
            } else {
                console.printEditeMenu();
                userOption = input.getInt(3);
                switch (userOption) {
                    case 1 -> { //if "Edite base info of training." was selected
                        console.print("What training you want edite?");
                        int choice = input.getInt(trainingsListSize);
                        showTrainingMenu(choice);
                        }
                    case 2 -> { //if "Edite additional info of training." was selected
                        console.print("What training you want edite?");
                        int choice = input.getInt(trainingsListSize);
                        Training trainingForEdit = trainingsList.get(choice);
                        console.print("Enter info: ");
                        String info = input.getString();
                        console.print("Enter value: ");
                        Double value = input.getDouble();
                        trainingsService.editAdditionalInfo(trainingForEdit, info, value);
                        }
                    case 3 -> { //if "Back to User menu" was selected
                        userOption = -1;
                        showStatisticMenu = false;
                    }
                    case 0 ->  //if "Exit" was selected
                            System.exit(0);
                }

                console.editSuccessful();
                adminService.addEvent(loginOfCurrentUser,
                        "Edit training.");
                showEditMenu = false;
            }
        }

        if (showDeleteMenu) {
            if (trainingsList.isEmpty()) {
                showDeleteMenu = false;
                try {
                    adminService.addEvent(loginOfCurrentUser,
                            "Trying delete training.");
                    throw new EmptyException();
                } catch (EmptyException e) {
                    console.print(e.getMessage());
                }
            } else {
                console.print("What training name should I delete?");
                int indexForDelete = input.getInt(trainingsListSize);
                trainingsService.delete(indexForDelete, loginOfCurrentUser);
                console.deleteSuccessful();
                adminService.addEvent(loginOfCurrentUser,
                        "Delete training.");
                showDeleteMenu = false;
            }
        }
    }

    private void editTraining(int indexForEdit, String login, String typeOfTraining,
                              LocalDate date, double duration, double caloriesBurned)
            throws EmptyException, TrainingOnDateAlreadyExistsException, TrainingNotFoundException {
        trainingsService.edite(indexForEdit, login, typeOfTraining, date, duration, caloriesBurned);
    }

    private void showStatisticOfTrainingMenu() {
        while (showStatisticMenu) {
            console.statisticMenu();
            userOption = input.getInt(3);
            switch (userOption) {
                case 1 -> //if "Show statistic of trainings by duration." was selected
                        showStatisticOfTraining();
                case 2 ->  //if "Show statistic of trainings by calories burned." was selected
                        showStatisticOfTraining();
                case 3 -> { //if "Back to User menu" was selected
                    userOption = -1;
                    showStatisticMenu = false;
                }
                case 0 ->  //if "Exit" was selected
                        System.exit(0);
            }
        }
    }

    private void showStatisticOfTraining() {
        console.choiceTypeOfTrainingMessage();
        List<TypeOfTraining> listOfTypes = trainingsService.getAllTypesOfTraining();
        console.getTypesOfTraining(listOfTypes);
        int maxIndex = listOfTypes.size();
        int allType = maxIndex + 1;
        console.print(allType + ". All types.");
        int typeIndex = input.getInt(allType);
        if (typeIndex <= maxIndex) {
            try {
                String typeOfTraining = trainingsService.getTypeOfTrainingByIndex(typeIndex);
                console.print("Statistic for " + typeOfTraining + ":");
                List<Training> trainings =
                        trainingsService.getAllTrainingsForUserByTypeOfTraining(
                                loginOfCurrentUser, typeOfTraining);
                Statistic statistic = trainingsService.getStatistic(trainings, userOption);
                console.printStatistic(statistic, userOption);
                adminService.addEvent(loginOfCurrentUser,
                        "Viewed training statistics for " + typeOfTraining + ".");
            } catch (EmptyException e) {
                console.print(e.getMessage());
            }
        } else {
            console.print("Statistic for all types of training:");
            List<Training> trainings =
                    trainingsService.getAllTrainingsForUser(
                            loginOfCurrentUser);
            Statistic statistic = trainingsService.getStatistic(trainings, userOption);
            console.printStatistic(statistic, userOption);
            adminService.addEvent(loginOfCurrentUser,
                    "Viewed training statistics for all types of trainings.");
        }
    }

    private void goToAdminOptions() {
        try {
            if (userService.isAdmin(loginOfCurrentUser)) {
                showAdminMenu = true;
                userOption = -1;
                adminService.addEvent(loginOfCurrentUser,
                        "Enter in admin option.");
            } else {
                userOption = -1;
                adminService.addEvent(loginOfCurrentUser,
                        "Tried to entered in admin option.");
                throw new NotAdminException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
        } catch (NotAdminException e) {
            console.print(e.getMessage());
        }
    }
    private void showAdminMenu() {
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
                    adminService.addEvent(loginOfCurrentUser,
                            "Log was viewed.");
                }
                case 5 -> { //if "Back to User menu" was selected
                    userOption = -1;
                    showAdminMenu = false;
                    adminService.addEvent(loginOfCurrentUser,
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
            adminService.addEvent(loginOfCurrentUser,
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
                adminService.addEvent(loginOfCurrentUser,
                        "Training history for user was viewed.");
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
            adminService.addEvent(loginOfCurrentUser,
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
                adminService.addEvent(loginOfCurrentUser,
                        "Log for " + login + " was viewed.");
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
            adminService.addEvent(loginOfCurrentUser,
                    "Tried to view a log for user.");
        }

        userOption = -1;
        showLogForUser = false;
    }


    private void logOut() {
        adminService.addEvent(loginOfCurrentUser,
                "Logout.");
        loginOfCurrentUser = null;
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
