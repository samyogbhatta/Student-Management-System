package com.example.controllers;

import com.example.Main;
import com.example.models.Course;
import com.example.utils.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class CourseRegistrationController {
    @FXML private ListView<Course> availableCoursesListView;
    @FXML private Label messageLabel;
    @FXML private Label availableCountLabel;
    @FXML private VBox registrationContainer;

    @FXML
    private void initialize() {
        // Add fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(600), registrationContainer);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        loadAvailableCourses();
        setupListView();
    }

    private void loadAvailableCourses() {
        var unregisteredCourses = DataManager.getUnregisteredCourses();
        availableCoursesListView.setItems(FXCollections.observableArrayList(unregisteredCourses));
        availableCountLabel.setText(String.valueOf(unregisteredCourses.size()));
    }

    private void setupListView() {
        availableCoursesListView.setCellFactory(listView -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setGraphic(createCourseCard(course));
                    setText(null);
                    setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                }
            }
        });
    }

    private VBox createCourseCard(Course course) {
        VBox courseCard = new VBox(8);
        courseCard.setStyle("-fx-padding: 20; -fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-border-color: #e9ecef; -fx-border-radius: 12; -fx-border-width: 1; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Course name and ID
        HBox headerBox = new HBox(10);
        Label courseName = new Label(course.getCourseName());
        courseName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label courseId = new Label(course.getCourseId());
        courseId.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d; -fx-background-color: #ecf0f1; " +
                "-fx-padding: 4 8; -fx-background-radius: 10;");

        headerBox.getChildren().addAll(courseName, courseId);

        // Course details
        Label instructor = new Label("👨‍🏫 " + course.getInstructor());
        instructor.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label credits = new Label("📊 " + course.getCredits() + " Credits");
        credits.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label schedule = new Label("🕒 " + course.getSchedule());
        schedule.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label department = new Label("🏛️ " + course.getDepartment());
        department.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        courseCard.getChildren().addAll(headerBox, instructor, credits, schedule, department);

        return courseCard;
    }

    @FXML
    private void handleRegister() {
        Course selectedCourse = availableCoursesListView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showMessage("⚠️ Please select a course to register", "error");
            return;
        }

        // Check if student already has maximum credits (example: 20 credits max)
        int currentCredits = DataManager.getCurrentStudent().getTotalCredits();
        int newTotalCredits = currentCredits + selectedCourse.getCredits();

        if (newTotalCredits > 20) {
            showMessage("⚠️ Cannot register: Would exceed maximum credit limit (20)", "error");
            return;
        }

        // Register for the course
        DataManager.getCurrentStudent().addCourse(selectedCourse);
        DataManager.saveData();

        showMessage("✅ Successfully registered for: " + selectedCourse.getCourseName(), "success");

        // Refresh the list
        loadAvailableCourses();

        // Clear selection
        availableCoursesListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleViewDetails() {
        Course selectedCourse = availableCoursesListView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showMessage("⚠️ Please select a course to view details", "error");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Details");
        alert.setHeaderText(selectedCourse.getCourseName() + " (" + selectedCourse.getCourseId() + ")");
        alert.setContentText(
                "Instructor: " + selectedCourse.getInstructor() + "\n" +
                        "Department: " + selectedCourse.getDepartment() + "\n" +
                        "Credits: " + selectedCourse.getCredits() + "\n" +
                        "Schedule: " + selectedCourse.getSchedule() + "\n\n" +
                        "Prerequisites: None\n" +
                        "Description: Comprehensive course covering fundamental concepts and practical applications."
        );
        alert.showAndWait();
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        String style;

        switch (type) {
            case "error":
                style = "-fx-text-fill: #e74c3c; -fx-font-weight: bold;";
                break;
            case "success":
                style = "-fx-text-fill: #27ae60; -fx-font-weight: bold;";
                break;
            default:
                style = "-fx-text-fill: #34495e;";
                break;
        }

        messageLabel.setStyle(style);

        // Add fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(300), messageLabel);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    @FXML
    private void handleBack() {
        try {
            Main.showDashboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
