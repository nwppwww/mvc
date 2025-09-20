package registration.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Student { // คลาสสำหรับเก็บข้อมูลนักเรียน
    private String studentId; // เก็บข้อมูลรหัสนักเรียน
    private String title; // เก็บข้อมูลคำนำหน้า
    private String firstName; // เก็บข้อมูลชื่อจริง
    private String lastName; // เก็บข้อมูลนามสกุล
    private LocalDate birthDate; // เก็บข้อมูลวันเกิด
    private String currentSchool; // เก็บข้อมูลโรงเรียน
    private String email; // เก็บข้อมูลอีเมล

    public Student(String studentId, String title, String firstName, String lastName, String birthDate, String currentSchool, String email) { // สร้างobjectนักเรียน
        this.studentId = studentId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE); // แปลงข้อความเป็นวันที่
        this.currentSchool = currentSchool;
        this.email = email;
    }

    public String getStudentId() { return studentId; } // ดึงรหัสนักเรียน
    public String getFirstName() { return firstName; } // ดึงชื่อจริง
    public String getLastName() { return lastName; } // ดึงนามสกุล
    public LocalDate getBirthDate() { return birthDate; } // ดึงวันเกิด
    
    @Override
    public String toString() { 
        return "Student ID: " + studentId + ", Name: " + title + " " + firstName + " " + lastName;
    }
    
    public String toCsvString() { // แปลงข้อมูลกลับเป็นรูปแบบCSV
        return String.join(",", studentId, title, firstName, lastName, birthDate.toString(), currentSchool, email);
    }
}