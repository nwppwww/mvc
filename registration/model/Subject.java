package registration.model;

public class Subject {
    private String subjectId;
    private String subjectName;
    private int credits;
    private String instructorName;
    private String prerequisiteSubjectId; // Can be empty
    private int maxCapacity; // -1 for unlimited
    private int currentEnrollment;

    public Subject(String subjectId, String subjectName, int credits, String instructorName, String prerequisiteSubjectId, int maxCapacity, int currentEnrollment) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.credits = credits;
        this.instructorName = instructorName;
        this.prerequisiteSubjectId = prerequisiteSubjectId.isEmpty() ? null : prerequisiteSubjectId;
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = currentEnrollment;
    }

    // --- Getters and Setters ---
    public String getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public String getPrerequisiteSubjectId() { return prerequisiteSubjectId; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentEnrollment() { return currentEnrollment; }
    public void setCurrentEnrollment(int currentEnrollment) { this.currentEnrollment = currentEnrollment; }

    // ***** เพิ่ม 2 เมธอดนี้เข้าไป *****
    public int getCredits() { return credits; }
    public String getInstructorName() { return instructorName; }
    // *******************************

    public boolean isFull() {
        if (maxCapacity == -1) {
            return false; // Unlimited capacity
        }
        return currentEnrollment >= maxCapacity;
    }
    
    public void incrementEnrollment() {
        this.currentEnrollment++;
    }

    @Override
    public String toString() {
        // This method is used by the JList in the GUI to display the subject name.
        return String.format("[%s] %s", subjectId, subjectName);
    }
    
    public String toCsvString() {
        return String.join(",", subjectId, subjectName, String.valueOf(credits), instructorName, prerequisiteSubjectId == null ? "" : prerequisiteSubjectId, String.valueOf(maxCapacity), String.valueOf(currentEnrollment));
    }
}