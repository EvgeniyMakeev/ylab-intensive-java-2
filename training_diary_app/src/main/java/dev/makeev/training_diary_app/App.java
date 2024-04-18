package dev.makeev.training_diary_app;

import dev.makeev.training_diary_app.service.AppUI;

public class App {
    public static void main(String[] args) {
        AppUI application = new AppUI();
        application.loadDB();
        application.start();
    }
}
