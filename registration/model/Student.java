package registration.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Student {
    private String studentId;
    private String title;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String currentSchool;
    private String email;

    public Student(String studentId, String title, String firstName, String lastName, String birthDate, String currentSchool, String email) {
        this.studentId = studentId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE); // yyyy-MM-dd
        this.currentSchool = currentSchool;
        this.email = email;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    
    @Override
    public String toString() {
        return "Student ID: " + studentId + ", Name: " + title + " " + firstName + " " + lastName;
    }
    
    public String toCsvString() {
        return String.join(",", studentId, title, firstName, lastName, birthDate.toString(), currentSchool, email);
    }
}