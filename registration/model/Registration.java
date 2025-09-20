package registration.model;

public class Registration { // คลาสสำหรับเก็บข้อมูลการลงทะเบียน
    private String studentId; // เก็บรหัสนักเรียนที่ลงทะเบียน
    private String subjectId; // เก็บรหัสวิชาที่ถูกลงทะเบียน
    private String grade; // เก็บเกรดที่ได้รับ

    public Registration(String studentId, String subjectId, String grade) { // สร้างobjectการลงทะเบียน
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.grade = grade.isEmpty() ? null : grade; // ถ้าเกรดว่างให้เก็บเป็นnull
    }

    public String getStudentId() { return studentId; } // ดึงรหัสนักเรียน
    public String getSubjectId() { return subjectId; } // ดึงรหัสวิชา
    public String getGrade() { return grade; } // ดึงเกรด
    public void setGrade(String grade) { // กำหนดค่าเกรด
        this.grade = grade;
    }

    public String toCsvString() { // แปลงข้อมูลกลับเป็นรูปแบบซีเอสวี
        return String.join(",", studentId, subjectId, grade == null ? "" : grade);
    }
}