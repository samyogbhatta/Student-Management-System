package com.example.models;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String program;
    private String semester;
    private List<Course> registeredCourses;

    public Student() {
        this.registeredCourses = new ArrayList<>();
    }

    public Student(String studentId, String name, String email, String program, String semester) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.program = program;
        this.semester = semester;
        this.registeredCourses = new ArrayList<>();
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public List<Course> getRegisteredCourses() { return registeredCourses; }
    public void setRegisteredCourses(List<Course> registeredCourses) { this.registeredCourses = registeredCourses; }

    public void addCourse(Course course) {
        if (!registeredCourses.contains(course)) {
            registeredCourses.add(course);
        }
    }

    public void removeCourse(Course course) {
        registeredCourses.remove(course);
    }

    public int getTotalCredits() {
        return registeredCourses.stream().mapToInt(Course::getCredits).sum();
    }

    public boolean isRegisteredFor(Course course) {
        return registeredCourses.contains(course);
    }
}
