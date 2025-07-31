package com.example.utils;

import com.example.models.Course;
import com.example.models.Student;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static Student currentStudent;
    private static List<Course> availableCourses = new ArrayList<>();
    private static final String STUDENTS_FILE = "data/students.txt";
    private static final String COURSES_FILE = "data/courses.txt";

    public static void initializeData() {
        // Create data directory if it doesn't exist
        new File("data").mkdirs();

        // Initialize available courses with more detailed information
        availableCourses.clear();
        availableCourses.add(new Course("CS101", "Introduction to Java Programming", "Dr. Sarah Smith", 3, "Mon/Wed 10:00-11:30 AM", "Computer Science"));
        availableCourses.add(new Course("CS201", "Database Management Systems", "Dr. Michael Johnson", 3, "Tue/Thu 2:00-3:30 PM", "Computer Science"));
        availableCourses.add(new Course("CS301", "Python Programming", "Dr. Emily Brown", 3, "Mon/Wed 4:00-5:30 PM", "Computer Science"));
        availableCourses.add(new Course("CS401", "Web Development", "Dr. David Davis", 3, "Tue/Thu 10:00-11:30 AM", "Computer Science"));
        availableCourses.add(new Course("CS501", "Data Structures & Algorithms", "Dr. Jennifer Wilson", 4, "Mon/Wed/Fri 9:00-10:00 AM", "Computer Science"));
        availableCourses.add(new Course("CS601", "Software Engineering", "Dr. Carlos Garcia", 3, "Tue/Thu 12:00-1:30 PM", "Computer Science"));
        availableCourses.add(new Course("CS701", "Mobile App Development", "Dr. Lisa Martinez", 3, "Wed/Fri 2:00-3:30 PM", "Computer Science"));
        availableCourses.add(new Course("CS801", "Machine Learning", "Dr. Robert Taylor", 4, "Mon/Thu 4:00-5:30 PM", "Computer Science"));
        availableCourses.add(new Course("CS901", "Cybersecurity Fundamentals", "Dr. Amanda Lee", 3, "Tue/Fri 10:00-11:30 AM", "Computer Science"));
        availableCourses.add(new Course("CS1001", "Cloud Computing", "Dr. Kevin Wang", 3, "Wed/Thu 12:00-1:30 PM", "Computer Science"));

        // Initialize current student
        currentStudent = new Student("ST2025001", "John Doe", "john.doe@university.edu", "Computer Science", "6th Semester");

        // Load saved data
        loadData();
    }

    public static boolean validateLogin(String username, String password) {
        // Enhanced login validation
        return ("student".equals(username.toLowerCase()) && "password".equals(password)) ||
                ("admin".equals(username.toLowerCase()) && "admin123".equals(password)) ||
                ("demo".equals(username.toLowerCase()) && "demo".equals(password));
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static List<Course> getAvailableCourses() {
        return new ArrayList<>(availableCourses);
    }

    public static List<Course> getUnregisteredCourses() {
        List<Course> unregistered = new ArrayList<>();
        for (Course course : availableCourses) {
            if (!currentStudent.isRegisteredFor(course)) {
                unregistered.add(course);
            }
        }
        return unregistered;
    }

    public static void saveData() {
        try {
            // Ensure data directory exists
            new File("data").mkdirs();

            // Save student data
            try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
                // Save student basic info
                writer.println("STUDENT_INFO:" + currentStudent.getStudentId() + "," +
                        currentStudent.getName() + "," +
                        currentStudent.getEmail() + "," +
                        currentStudent.getProgram() + "," +
                        currentStudent.getSemester());

                // Save registered courses
                writer.println("REGISTERED_COURSES:");
                for (Course course : currentStudent.getRegisteredCourses()) {
                    writer.println(course.getCourseId());
                }
            }

            System.out.println("Data saved successfully to " + STUDENTS_FILE);

        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadData() {
        try {
            File file = new File(STUDENTS_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    boolean readingCourses = false;

                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("STUDENT_INFO:")) {
                            String[] parts = line.substring(13).split(",");
                            if (parts.length >= 5) {
                                currentStudent.setStudentId(parts[0]);
                                currentStudent.setName(parts[1]);
                                currentStudent.setEmail(parts[2]);
                                currentStudent.setProgram(parts[3]);
                                currentStudent.setSemester(parts[4]);
                            }
                        } else if (line.equals("REGISTERED_COURSES:")) {
                            readingCourses = true;
                        } else if (readingCourses && !line.trim().isEmpty()) {
                            Course course = findCourseById(line.trim());
                            if (course != null) {
                                currentStudent.addCourse(course);
                            }
                        }
                    }
                }
                System.out.println("Data loaded successfully from " + STUDENTS_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private static Course findCourseById(String courseId) {
        return availableCourses.stream()
                .filter(course -> course.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);
    }

    public static void resetStudentData() {
        currentStudent.getRegisteredCourses().clear();
        currentStudent.setName("John Doe");
        currentStudent.setEmail("john.doe@university.edu");
        currentStudent.setProgram("Computer Science");
        currentStudent.setSemester("6th Semester");
        saveData();
    }
}
