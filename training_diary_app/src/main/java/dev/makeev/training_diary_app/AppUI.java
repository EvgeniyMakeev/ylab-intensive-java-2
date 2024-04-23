package dev.makeev.training_diary_app;

import dev.makeev.training_diary_app.dao.LogEventDAO;
import dev.makeev.training_diary_app.dao.impl.TrainingOfUserDAOImpl;
import dev.makeev.training_diary_app.dao.impl.TypeOfTrainingDAOImpl;
import dev.makeev.training_diary_app.dao.impl.UserDAOImpl;
import dev.makeev.training_diary_app.dao.impl.LogEventDAOImpl;
import dev.makeev.training_diary_app.exceptions.EmptyException;
import dev.makeev.training_diary_app.exceptions.LoginAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.NotAdminException;
import dev.makeev.training_diary_app.exceptions.TrainingOnDateAlreadyExistsException;
import dev.makeev.training_diary_app.exceptions.UserNotFoundException;
import dev.makeev.training_diary_app.exceptions.VerificationException;
import dev.makeev.training_diary_app.in.Input;
import dev.makeev.training_diary_app.in.InputImpl;
import dev.makeev.training_diary_app.model.Statistic;
import dev.makeev.training_diary_app.model.Training;
import dev.makeev.training_diary_app.model.TrainingOfUser;
import dev.makeev.training_diary_app.model.TypeOfTraining;
import dev.makeev.training_diary_app.model.User;
import dev.makeev.training_diary_app.out.Messages;
import dev.makeev.training_diary_app.out.OutputImpl;
import dev.makeev.training_diary_app.service.TrainingsService;
import dev.makeev.training_diary_app.service.UserService;
import dev.makeev.training_diary_app.utils.ConnectionManager;
import dev.makeev.training_diary_app.utils.ConnectionManagerImpl;
import dev.makeev.training_diary_app.utils.InitDB;


import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * {@code AppUI} is the main class responsible for the application's user interface.
 * It handles user input, displays messages, and interacts with the {@link UserService} and {@link TrainingsService}.
 */
public class AppUI {

    private final ConnectionManager connectionManager = new ConnectionManagerImpl();
    private final InitDB initDB = new InitDB(connectionManager);
    private final UserService userService = new UserService(new UserDAOImpl(connectionManager));
    private final LogEventDAO logEventDAO = new LogEventDAOImpl(connectionManager);

    private final TrainingsService trainingsService =
            new TrainingsService(new TrainingOfUserDAOImpl(connectionManager),
                    new TypeOfTrainingDAOImpl(connectionManager));

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

    /**
     * Initializes the database.
     */
    public void loadDB(){
        initDB.initDB();
    }

    /**
     * Starts the application.
     */
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

    /**
     * Displays the authorization menu and handles user input for authorization options.
     */
    private void authorizationMenu() {
        console.authorizationMenu();
        if (userOption < 0) {
            userOption = input.getInt(0, 2);
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

    /**
     * Displays the login menu and handles user login and registration.
     */
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
                    logEventDAO.addEvent(loginOfCurrentUser,
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
                        logEventDAO.addEvent(loginOfCurrentUser,
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
            if (input.getInt(1, 2) == 2) {
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

    /**
     * Displays the user menu and handles user options related to training management.
     */
    private void userMenu() {
        while (showUserMenu) {
            console.greetingMessage(loginOfCurrentUser);
            console.userMenu();
            if (userOption < 0) {
                userOption = input.getInt(0, 7);
            }
            switch (userOption) {
                case 1 -> //if "Add new training." was selected
                        showAddTrainingMenu = true;
                case 2 -> {//if "Edite training." was selected
                    showEditMenu = true;
                    showTrainingHistory();
                    showEditMenu = false;
                }
                case 3 -> {//if "Delete training." was selected
                    showDeleteMenu = true;
                    showTrainingHistory();
                    showDeleteMenu = false;
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

    /**
     * Displays the menu for adding or editing a training and handles the corresponding actions.
     *
     * @param indexForEdit the index of the training to edit, or -1 for adding a new training.
     */
    private void showTrainingMenu(int indexForEdit) {
        String typeOfTraining = "";
        Optional<TypeOfTraining> type = Optional.empty();
        List<TypeOfTraining> listOfTypes = trainingsService.getAllTypesOfTraining();
        console.choiceTypeOfTrainingMessage();
        console.showTypesOfTraining(listOfTypes);
        console.choiceTypeMessage();
        userOption = input.getInt(1, 2);

        switch (userOption) {
            case 1 -> {
                console.print("Choose type from list.");
                int index = input.getInt(1, listOfTypes.size()) - 1;
                type = Optional.of(listOfTypes.get(index));
                typeOfTraining = type.get().type();
            }
            case 2 -> {
                console.addTypeOfTrainingMessage();
                typeOfTraining = input.getString();
                if (trainingsService.getTypeOfTrainingByType(typeOfTraining).isEmpty()) {
                    trainingsService.addTypeOfTraining(typeOfTraining);
                    type = Optional.of(trainingsService.getTypeOfTrainingByType(typeOfTraining).get());
                    console.print("A new type of training was added.");
                    logEventDAO.addEvent(loginOfCurrentUser,
                            "Add a new type of training.");
                } else {
                    type = Optional.of(trainingsService.getTypeOfTrainingByType(typeOfTraining).get());
                }
            }
        }

        console.print("Enter information for new " + typeOfTraining);
        LocalDate date = getDate();

        console.setDurationMessage();
        double duration = input.getDouble();

        console.setCaloriesBurnedMessage();
        double caloriesBurned = input.getDouble();

        try {
            if (!showEditMenu) {
                trainingsService.addTrainingOfUser(loginOfCurrentUser, typeOfTraining, date, duration, caloriesBurned);
                console.addSuccessful();
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Added new training.");
            } else {
                long idOfTrainingForEdite =
                        trainingsService.getAllTrainingsForUser(loginOfCurrentUser)
                                .get(indexForEdit).training().id();
                TrainingOfUser newTrainingOfUser =
                        new TrainingOfUser(loginOfCurrentUser, typeOfTraining,
                                new Training(idOfTrainingForEdite, type.orElseThrow(EmptyException::new).id(),
                                        date, duration, caloriesBurned), null);
                trainingsService.editTraining(idOfTrainingForEdite, newTrainingOfUser);
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
        } catch (TrainingOnDateAlreadyExistsException e) {
            console.print(e.getMessage());
        } catch (EmptyException e) {
            throw new RuntimeException(e);
        }

        showAddTrainingMenu = false;
        userOption = -1;
    }

    /**
     * Displays the training history for the current user.
     */
    private void showTrainingHistory() {
        userOption = -1;
        List<TrainingOfUser> trainingsOfUserList = trainingsService.getAllTrainingsForUser(loginOfCurrentUser);
        if (trainingsOfUserList.isEmpty()) {
            console.print(new EmptyException().getMessage());
            logEventDAO.addEvent(loginOfCurrentUser,
                    "Trainings history was viewed.");
        } else {
            int trainingsListSize = trainingsOfUserList.size();
            console.printTrainingsForUser(trainingsOfUserList);
            if (showEditMenu) {
                console.printEditeMenu();
                userOption = input.getInt(0, 3);
                switch (userOption) {
                    case 1 -> { //if "Edite base info of training." was selected
                        console.print("What training you want edite?");
                        int indexOfTrainingForEdite = input.getInt(1, trainingsListSize) - 1;
                        showTrainingMenu(indexOfTrainingForEdite);
                    }
                    case 2 -> { //if "Edite additional info of training." was selected
                        console.print("What training you want edite?");
                        int indexOfTrainingForEdite = input.getInt(1, trainingsListSize) - 1;
                        long idOfTrainingForEdite =
                                trainingsOfUserList.get(indexOfTrainingForEdite).training().id();
                        console.print("Enter info: ");
                        String info = input.getString();
                        console.print("Enter value: ");
                        Double value = input.getDouble();
                        trainingsService.editAdditionalInfo(idOfTrainingForEdite, info, value);
                    }
                    case 3 -> { //if "Back to User menu" was selected
                        userOption = -1;
                        showStatisticMenu = false;
                    }
                    case 0 ->  //if "Exit" was selected
                            System.exit(0);
                }
                console.editSuccessful();
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Edit training.");
            } else if (showDeleteMenu) {
                console.print("What training name should I delete?");
                int indexForDelete = input.getInt(1, trainingsListSize) - 1;
                long idOfTrainingForDelete = trainingsOfUserList.get(indexForDelete).training().id();
                trainingsService.deleteTraining(idOfTrainingForDelete);
                console.deleteSuccessful();
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Delete training.");
            } else {
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Trainings history was viewed.");
            }
        }
    }

    /**
     * Displays the menu for viewing training statistics and handles the corresponding actions.
     */
    private void showStatisticOfTrainingMenu() {
        while (showStatisticMenu) {
            console.statisticMenu();
            userOption = input.getInt(0, 3);
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

    /**
     * Displays the training statistics based on the user's selection.
     */
    private void showStatisticOfTraining() {
        console.choiceTypeOfTrainingMessage();
        List<TypeOfTraining> listOfTypes = trainingsService.getAllTypesOfTraining();
        console.showTypesOfTraining(listOfTypes);
        int maxIndex = listOfTypes.size();
        int allType = maxIndex + 1;
        console.print(allType + ". All types.");
        int typeIndex = input.getInt(1, allType);
        if (typeIndex <= maxIndex) {
            try {
                long typeOfTrainingId = listOfTypes.get(typeIndex).id();
                String typeOfTraining = trainingsService.getTypeOfTrainingById(typeOfTrainingId).type();
                console.print("Statistic for " + typeOfTraining + ":");
                List<TrainingOfUser> trainingsOfUserList =
                        trainingsService.getAllTrainingsForUserByTypeOfTraining(
                                loginOfCurrentUser, typeOfTrainingId);
                Statistic statistic = trainingsService.getStatistic(trainingsOfUserList, userOption);
                console.printStatistic(statistic, userOption);
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Viewed training statistics for " + typeOfTraining + ".");
            } catch (EmptyException e) {
                console.print(e.getMessage());
            }
        } else {
            console.print("Statistic for all types of training:");
            List<TrainingOfUser> trainingsOfUserList =
                    trainingsService.getAllTrainingsForUser(loginOfCurrentUser);
            if (trainingsOfUserList.isEmpty()) {
                console.print(new EmptyException().getMessage());
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Trying view training statistics for all types of trainings.");
            } else {
                Statistic statistic = trainingsService.getStatistic(trainingsOfUserList, userOption);
                console.printStatistic(statistic, userOption);
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Viewed training statistics for all types of trainings.");
            }
        }
    }

    /**
     * Redirects to the admin options menu if the current user is an admin.
     */
    private void goToAdminOptions() {
        try {
            if (userService.isAdmin(loginOfCurrentUser)) {
                showAdminMenu = true;
                userOption = -1;
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Enter in admin option.");
            } else {
                userOption = -1;
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Tried to entered in admin option.");
                throw new NotAdminException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
        } catch (NotAdminException e) {
            console.print(e.getMessage());
        }
    }

    /**
     * Displays the admin options menu and handles admin-specific actions.
     */
    private void showAdminMenu() {
        while (showAdminMenu) {
            console.adminMenu();
            userOption = input.getInt(0, 5);
            switch (userOption) {
                case 1 -> //if "Show training history for all users." was selected
                        showTrainingHistoryForAllUsers();
                case 2 ->  //if "Show training history for user." was selected
                        showHistoryForUser = true;
                case 3 -> //if "Show log for user" was selected
                        showLogForUser = true;
                case 4 -> { //if "Show log" was selected
                    try {
                        console.printUserEvents(logEventDAO.getAllEvents(),
                                "Log for all users:\n");
                    } catch (EmptyException e) {
                        console.print(e.getMessage());
                    }
                    logEventDAO.addEvent(loginOfCurrentUser,
                            "Log was viewed.");
                }
                case 5 -> { //if "Back to User menu" was selected
                    userOption = -1;
                    showAdminMenu = false;
                    logEventDAO.addEvent(loginOfCurrentUser,
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

    /**
     * Displays the training history for all users.
     */
    private void showTrainingHistoryForAllUsers() {
            List<User> users = userService.getAll();
            if (users.size() < 2) {
                console.print(new EmptyException().getMessage());
            }
            console.print("History of trainings for all users:");
            for (User user : users) {
                List<TrainingOfUser> trainingOfUserList;
                trainingOfUserList = trainingsService.getAllTrainingsForUser(user.login());
                console.printTrainings(trainingOfUserList);
            }
            logEventDAO.addEvent(loginOfCurrentUser,
                    "History of trainings for all users was viewed.");
        userOption = -1;
    }

    /**
     * Displays the training history for a specific user.
     */
    private void showTrainingHistoryForUser() {
        console.loginMessage();
        String login = input.getString();
        try {
            if (userService.existByLogin(login)) {
                List<TrainingOfUser> listOfTrainings = trainingsService.getAllTrainingsForUser(login);
                if (listOfTrainings.isEmpty()){
                    console.print(new EmptyException().getMessage());
                } else {
                    console.printTrainingsForUser(
                            trainingsService.getAllTrainingsForUser(login));
                    logEventDAO.addEvent(loginOfCurrentUser,
                            "Training history for user was viewed.");
                }
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
            logEventDAO.addEvent(loginOfCurrentUser,
                    "Tried to view a training history for user.");
        }

        userOption = -1;
        showHistoryForUser = false;
    }

    /**
     * Displays the log events for a specific user.
     */
    private void logForUser() {
        console.loginMessage();
        String login = input.getString();
        try {
            if (userService.existByLogin(login)) {
                console.printUserEvents(logEventDAO.getAllEventsForUser(login),
                        "Log for " + login + ":\n");
                logEventDAO.addEvent(loginOfCurrentUser,
                        "Log for " + login + " was viewed.");
            } else {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            console.print(e.getMessage());
            logEventDAO.addEvent(loginOfCurrentUser,
                    "Tried to view a log for user.");
        } catch (EmptyException e) {
            throw new RuntimeException(e);
        }

        userOption = -1;
        showLogForUser = false;
    }

    /**
     * Logs out the current user.
     */
    private void logOut() {
        logEventDAO.addEvent(loginOfCurrentUser,
                "Logout.");
        loginOfCurrentUser = null;
        showAuthorizationMenu = true;
        showUserMenu = false;
        userOption = -1;
    }

    /**
     * Retrieves the date from the user.
     *
     * @return the selected date.
     */
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
        } while (month <= 0);

        console.setDayMessage();
        int day;
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        do {
            day = input.getInteger(2, 1, daysInMonth);
        } while (day <= 0);

        return LocalDate.of(year, month, day);
    }
}
