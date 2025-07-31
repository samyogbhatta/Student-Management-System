package com.example.controllers;

import com.example.Main;
import com.example.models.Course;
import com.example.utils.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

public class ViewCoursesController {
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> courseIdColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> instructorColumn;
    @FXML private TableColumn<Course, Integer> creditsColumn;
    @FXML private TableColumn<Course, String> scheduleColumn;
    @FXML private TableColumn<Course, String> departmentColumn;
    @FXML private Label messageLabel;
    @FXML private Label totalCoursesLabel;
    @FXML private Label totalCreditsLabel;
    @FXML private Label currentSemesterLabel;
    @FXML private VBox coursesContainer;

    @FXML
    private void initialize() {
        // Add fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(600), coursesContainer);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        setupTableColumns();
        refreshTable();
        updateStats();
    }

    private void setupTableColumns() {
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        // Make columns resizable
        courseIdColumn.setPrefWidth(80);
        courseNameColumn.setPrefWidth(200);
        instructorColumn.setPrefWidth(150);
        creditsColumn.setPrefWidth(70);
        scheduleColumn.setPrefWidth(180);
        departmentColumn.setPrefWidth(120);

        // Style credits column to center align
        creditsColumn.setStyle("-fx-alignment: CENTER;");
    }

    private void refreshTable() {
        coursesTable.setItems(FXCollections.observableArrayList(
                DataManager.getCurrentStudent().getRegisteredCourses()
        ));
    }

    private void updateStats() {
        int totalCourses = DataManager.getCurrentStudent().getRegisteredCourses().size();
        int totalCredits = DataManager.getCurrentStudent().getTotalCredits();
        String semester = DataManager.getCurrentStudent().getSemester();

        totalCoursesLabel.setText(String.valueOf(totalCourses));
        totalCreditsLabel.setText(String.valueOf(totalCredits));
        currentSemesterLabel.setText(semester);
    }

    @FXML
    private void handleDelete() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showMessage("⚠️ Please select a course to unregister", "error");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Course Unregistration");
        confirmAlert.setHeaderText("Unregister from Course");
        confirmAlert.setContentText("Are you sure you want to unregister from:\n\n" +
                selectedCourse.getCourseName() + " (" + selectedCourse.getCourseId() + ")" +
                "\n\nThis action cannot be undone.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            DataManager.getCurrentStudent().removeCourse(selectedCourse);
            DataManager.saveData();

            showMessage("✅ Successfully unregistered from: " + selectedCourse.getCourseName(), "success");

            refreshTable();
            updateStats();
        }
    }

    @FXML
    private void handleViewDetails() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showMessage("⚠️ Please select a course to view details", "error");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Details");
        alert.setHeaderText(selectedCourse.getCourseName());
        alert.setContentText(
                "Course ID: " + selectedCourse.getCourseId() + "\n" +
                        "Instructor: " + selectedCourse.getInstructor() + "\n" +
                        "Department: " + selectedCourse.getDepartment() + "\n" +
                        "Credits: " + selectedCourse.getCredits() + "\n" +
                        "Schedule: " + selectedCourse.getSchedule() + "\n\n" +
                        "Registration Status: Enrolled\n" +
                        "Grade: In Progress"
        );
        alert.showAndWait();
    }

    @FXML
    private void handleExportList() {
        if (DataManager.getCurrentStudent().getRegisteredCourses().isEmpty()) {
            showMessage("⚠️ No courses to export", "error");
            return;
        }

        try {
            // Simple text export
            StringBuilder export = new StringBuilder();
            export.append("REGISTERED COURSES REPORT\n");
            export.append("Student: ").append(DataManager.getCurrentStudent().getName()).append("\n");
            export.append("Student ID: ").append(DataManager.getCurrentStudent().getStudentId()).append("\n");
            export.append("Semester: ").append(DataManager.getCurrentStudent().getSemester()).append("\n");
            export.append("Total Credits: ").append(DataManager.getCurrentStudent().getTotalCredits()).append("\n\n");

            export.append("COURSES:\n");
            for (Course course : DataManager.getCurrentStudent().getRegisteredCourses()) {
                export.append("- ").append(course.getCourseId()).append(": ").append(course.getCourseName())
                        .append(" (").append(course.getCredits()).append(" credits)\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Course List Export");
            alert.setHeaderText("Export Successful");
            alert.setContentText("Course list has been prepared for export:\n\n" + export.toString());
            alert.showAndWait();

        } catch (Exception e) {
            showMessage("❌ Error exporting course list", "error");
        }
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
