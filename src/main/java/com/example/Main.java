package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import com.example.utils.DataManager;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Student Course Registration System");
        primaryStage.setResizable(false);

        // Initialize data
        DataManager.initializeData();

        // Load login scene
        showLogin();

        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            handleExit();
        });
    }

    public static void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load(), 480, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showDashboard() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/dashboard.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 750);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showProfile() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/profile.fxml"));
        Scene scene = new Scene(loader.load(), 750, 700);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showCourseRegistration() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/courseRegistration.fxml"));
        Scene scene = new Scene(loader.load(), 850, 700);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showViewCourses() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/viewCourses.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("All data will be saved automatically.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            DataManager.saveData();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
