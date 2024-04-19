package dev.makeev.training_diary_app;

import dev.makeev.training_diary_app.service.AppUI;

/**
 * The main class representing the training diary application.
 * @author Evgeniy Makeev
 * @version 2.3
 */
public class App {
    /**
     * The main method to start the application.
     *
     * @param args The command-line arguments.
     */    public static void main(String[] args) {
        AppUI application = new AppUI();
        application.loadDB();
        application.start();
    }
}
