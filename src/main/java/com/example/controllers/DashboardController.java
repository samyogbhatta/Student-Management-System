package com.example.controllers;

import com.example.Main;
import com.example.utils.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label statsLabel;
    @FXML private Label registeredCountLabel;
    @FXML private Label totalCreditsLabel;
    @FXML private Label availableCountLabel;
    @FXML private PieChart courseChart;
    @FXML private VBox dashboardContainer;

    @FXML
    private void initialize() {
        // Add fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(800), dashboardContainer);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        updateDashboard();
    }

    private void updateDashboard() {
        String studentName = DataManager.getCurrentStudent().getName();
        welcomeLabel.setText("Welcome back, " + studentName + "!");

        int registeredCourses = DataManager.getCurrentStudent().getRegisteredCourses().size();
        int totalCredits = DataManager.getCurrentStudent().getTotalCredits();
        int availableCourses = DataManager.getUnregisteredCourses().size();
        int totalCourses = DataManager.getAvailableCourses().size();

        // Update statistics
        registeredCountLabel.setText(String.valueOf(registeredCourses));
        totalCreditsLabel.setText(String.valueOf(totalCredits));
        availableCountLabel.setText(String.valueOf(availableCourses));

        statsLabel.setText(String.format("You have registered for %d out of %d available courses",
                registeredCourses, totalCourses));

        // Update pie chart
        updateChart(registeredCourses, availableCourses);
    }

    private void updateChart(int registered, int available) {
        courseChart.getData().clear();

        if (registered > 0) {
            courseChart.getData().add(new PieChart.Data("Registered (" + registered + ")", registered));
        }
        if (available > 0) {
            courseChart.getData().add(new PieChart.Data("Available (" + available + ")", available));
        }

        // If no courses registered, show only available
        if (registered == 0 && available == 0) {
            courseChart.getData().add(new PieChart.Data("No Courses", 1));
        }

        // Customize chart colors
        courseChart.getData().forEach(data -> {
            if (data.getName().startsWith("Registered")) {
                data.getNode().setStyle("-fx-pie-color: #27ae60;");
            } else if (data.getName().startsWith("Available")) {
                data.getNode().setStyle("-fx-pie-color: #3498db;");
            }
        });
    }

    @FXML
    private void handleProfile() {
        try {
            Main.showProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterCourse() {
        try {
            Main.showCourseRegistration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewCourses() {
        try {
            Main.showViewCourses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            DataManager.saveData();
            Main.showLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        updateDashboard();
    }
}
