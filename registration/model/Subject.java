package registration.model;

public class Subject { // คลาสสำหรับเก็บข้อมูลรายวิชา
    private String subjectId; // เก็บข้อมูลรหัสวิชา
    private String subjectName; // เก็บข้อมูลชื่อวิชา
    private int credits; // เก็บข้อมูลหน่วยกิต
    private String instructorName; // เก็บข้อมูลชื่อผู้สอน
    private String prerequisiteSubjectId; // เก็บข้อมูลวิชาบังคับก่อน
    private int maxCapacity; // เก็บข้อมูลจำนวนรับสูงสุด
    private int currentEnrollment; // เก็บข้อมูลจำนวนคนลงทะเบียน

    public Subject(String subjectId, String subjectName, int credits, String instructorName, String prerequisiteSubjectId, int maxCapacity, int currentEnrollment) { // สร้างobjectวิชา
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.credits = credits;
        this.instructorName = instructorName;
        this.prerequisiteSubjectId = prerequisiteSubjectId.isEmpty() ? null : prerequisiteSubjectId; // ถ้าไม่มีวิชาบังคับก่อนให้เป็นnull
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = currentEnrollment;
    }

    public String getSubjectId() { return subjectId; } // ดึงรหัสวิชา
    public String getSubjectName() { return subjectName; } // ดึงชื่อวิชา
    public int getCredits() { return credits; } // ดึงหน่วยกิต
    public String getInstructorName() { return instructorName; } // ดึงชื่อผู้สอน
    public String getPrerequisiteSubjectId() { return prerequisiteSubjectId; } // ดึงวิชาบังคับก่อน
    public int getMaxCapacity() { return maxCapacity; } // ดึงจำนวนรับสูงสุด
    public int getCurrentEnrollment() { return currentEnrollment; } // ดึงจำนวนคนลงทะเบียน
    public void setCurrentEnrollment(int currentEnrollment) { this.currentEnrollment = currentEnrollment; } // กำหนดจำนวนคนลงทะเบียน

    public boolean isFull() { // ตรวจสอบว่าวิชาเต็มหรือยัง
        if (maxCapacity == -1) { // ถ้าจำนวนรับไม่จำกัด
            return false; // คืนค่าว่าไม่เต็ม
        }
        return currentEnrollment >= maxCapacity; // คืนค่าผลการเปรียบเทียบ
    }
    
    public void incrementEnrollment() { // เพิ่มจำนวนคนลงทะเบียน
        this.currentEnrollment++;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", subjectId, subjectName);
    }
    
    public String toCsvString() { // แปลงข้อมูลกลับเป็นรูปแบบCSV
        return String.join(",", subjectId, subjectName, String.valueOf(credits), instructorName, prerequisiteSubjectId == null ? "" : prerequisiteSubjectId, String.valueOf(maxCapacity), String.valueOf(currentEnrollment));
    }
}