package com.example.models;

import java.util.Objects;

public class Course {
    private String courseId;
    private String courseName;
    private String instructor;
    private int credits;
    private String schedule;
    private String department;

    public Course() {}

    public Course(String courseId, String courseName, String instructor, int credits, String schedule, String department) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructor = instructor;
        this.credits = credits;
        this.schedule = schedule;
        this.department = department;
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return courseName + " (" + courseId + ")";
    }
}
