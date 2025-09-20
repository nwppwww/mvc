package registration.model;


public class Registration {
    private String studentId;
    private String subjectId;
    private String grade;

    public Registration(String studentId, String subjectId, String grade) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.grade = grade.isEmpty() ? null : grade;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getSubjectId() { return subjectId; }
    public String getGrade() { return grade; }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String toCsvString() {
        return String.join(",", studentId, subjectId, grade == null ? "" : grade);
    }
}