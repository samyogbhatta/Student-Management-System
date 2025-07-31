package com.example.controllers;

import com.example.Main;
import com.example.models.Student;
import com.example.utils.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

public class ProfileController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField programField;
    @FXML private TextField semesterField;
    @FXML private TextField studentIdField;
    @FXML private Label messageLabel;
    @FXML private VBox profileContainer;

    @FXML
    private void initialize() {
        // Add fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(600), profileContainer);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        loadStudentData();

        // Make student ID read-only
        studentIdField.setEditable(false);
        studentIdField.setStyle("-fx-background-color: #f8f9fa;");
    }

    private void loadStudentData() {
        Student student = DataManager.getCurrentStudent();
        studentIdField.setText(student.getStudentId());
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        programField.setText(student.getProgram());
        semesterField.setText(student.getSemester());
    }

    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) {
            return;
        }

        Student student = DataManager.getCurrentStudent();
        student.setName(nameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setProgram(programField.getText().trim());
        student.setSemester(semesterField.getText().trim());

        DataManager.saveData();

        showMessage("✅ Profile updated successfully!", "success");
    }

    private boolean validateInput() {
        // Name validation
        if (nameField.getText().trim().isEmpty()) {
            showMessage("⚠️ Name cannot be empty", "error");
            nameField.requestFocus();
            return false;
        }

        // Email validation
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showMessage("⚠️ Email cannot be empty", "error");
            emailField.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            showMessage("⚠️ Please enter a valid email address", "error");
            emailField.requestFocus();
            return false;
        }

        // Program validation
        if (programField.getText().trim().isEmpty()) {
            showMessage("⚠️ Program cannot be empty", "error");
            programField.requestFocus();
            return false;
        }

        // Semester validation
        if (semesterField.getText().trim().isEmpty()) {
            showMessage("⚠️ Semester cannot be empty", "error");
            semesterField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
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
    private void handleReset() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Reset Profile");
        confirmAlert.setHeaderText("Reset to Default Values");
        confirmAlert.setContentText("Are you sure you want to reset your profile to default values?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            DataManager.resetStudentData();
            loadStudentData();
            showMessage("🔄 Profile reset to default values", "success");
        }
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
