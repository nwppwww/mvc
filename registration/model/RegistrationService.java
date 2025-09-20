package registration.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class RegistrationService {

    /**
     * Attempts to register a student for a subject.
     * @return A string message indicating success or the reason for failure.
     */
    public String registerStudentForSubject(Student student, Subject subject, List<Registration> registrations) {
        // Rule: Student must be at least 15 years old.
        if (Period.between(student.getBirthDate(), LocalDate.now()).getYears() < 15) {
            return "Error: Student must be at least 15 years old.";
        }

        // Rule: Check prerequisite
        if (subject.getPrerequisiteSubjectId() != null) {
            boolean prerequisiteMet = registrations.stream()
                .filter(r -> r.getStudentId().equals(student.getStudentId()))
                .filter(r -> r.getSubjectId().equals(subject.getPrerequisiteSubjectId()))
                .anyMatch(r -> r.getGrade() != null && !r.getGrade().isEmpty()); // Must have a grade

            if (!prerequisiteMet) {
                return "Error: Prerequisite subject " + subject.getPrerequisiteSubjectId() + " has not been passed.";
            }
        }

        // Rule: Check if capacity is full
        if (subject.isFull()) {
            return "Error: Subject " + subject.getSubjectName() + " is full.";
        }

        // Model Logic: The checks for registration with and without max capacity are handled by isFull()
        subject.incrementEnrollment();
        registrations.add(new Registration(student.getStudentId(), subject.getSubjectId(), ""));
        
        return "Registration successful for subject: " + subject.getSubjectName();
    }
}