package com.example.controllers;

import com.example.Main;
import com.example.utils.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button loginButton;

    @FXML
    private void initialize() {
        usernameField.requestFocus();

        // Add enter key support
        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleLogin());

        // Clear message when user starts typing
        usernameField.textProperty().addListener((obs, oldText, newText) -> clearMessage());
        passwordField.textProperty().addListener((obs, oldText, newText) -> clearMessage());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Input validation
        if (username.isEmpty()) {
            showMessage("⚠️ Please enter username", "error");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showMessage("⚠️ Please enter password", "error");
            passwordField.requestFocus();
            return;
        }

        // Disable login button during processing
        loginButton.setDisable(true);
        showMessage("🔄 Authenticating...", "info");

        // Simulate authentication delay for better UX
        new Thread(() -> {
            try {
                Thread.sleep(500);

                javafx.application.Platform.runLater(() -> {
                    if (DataManager.validateLogin(username, password)) {
                        showMessage("✅ Login successful! Redirecting...", "success");

                        // Add a small delay before redirect for better UX
                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                javafx.application.Platform.runLater(() -> {
                                    try {
                                        Main.showDashboard();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        showMessage("❌ Error loading dashboard", "error");
                                    }
                                });
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }).start();
                    } else {
                        showMessage("❌ Invalid credentials. Try: student/password", "error");
                        passwordField.clear();
                        passwordField.requestFocus();
                    }

                    loginButton.setDisable(false);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
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
            case "info":
                style = "-fx-text-fill: #3498db; -fx-font-weight: bold;";
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

    private void clearMessage() {
        messageLabel.setText("");
    }

    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText("Password Recovery");
        alert.setContentText("Demo Credentials:\n\n" +
                "Username: student\n" +
                "Password: password\n\n" +
                "Alternative:\n" +
                "Username: demo\n" +
                "Password: demo");
        alert.showAndWait();
    }
}
