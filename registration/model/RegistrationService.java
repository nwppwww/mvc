package registration.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class RegistrationService { // คลาสสำหรับจัดการกฎการลงทะเบียน
    public String registerStudentForSubject(Student student, Subject subject, List<Registration> registrations) { // ลงทะเบียน
        if (Period.between(student.getBirthDate(), LocalDate.now()).getYears() < 15) { // ตรวจสอบว่าอายุถึงสิบห้าปีหรือไม่
            return "Error: Student must be at least 15 years old."; // คืนค่าข้อความผิดพลาด
        }

        if (subject.getPrerequisiteSubjectId() != null) { // ตรวจสอบว่ามีวิชาบังคับก่อนหรือไม่
            boolean prerequisiteMet = registrations.stream() // ค้นหาในประวัติการลงทะเบียน
                .filter(r -> r.getStudentId().equals(student.getStudentId())) // กรองเฉพาะของนักเรียนคนนี้
                .filter(r -> r.getSubjectId().equals(subject.getPrerequisiteSubjectId())) // กรองเฉพาะวิชาบังคับก่อน
                .anyMatch(r -> r.getGrade() != null && !r.getGrade().isEmpty()); // ตรวจสอบว่าต้องมีเกรดแล้ว

            if (!prerequisiteMet) { // ถ้ายังไม่ผ่านวิชาบังคับก่อน
                return "Error: Prerequisite subject " + subject.getPrerequisiteSubjectId() + " has not been passed."; // คืนค่าข้อความผิดพลาด
            }
        }

        if (subject.isFull()) { // ตรวจสอบว่าวิชาเต็มหรือยัง
            return "Error: Subject " + subject.getSubjectName() + " is full."; // คืนค่าข้อความผิดพลาด
        }

        subject.incrementEnrollment(); // เพิ่มจำนวนคนลงทะเบียน
        registrations.add(new Registration(student.getStudentId(), subject.getSubjectId(), "")); // เพิ่มข้อมูลการลงทะเบียนใหม่
        
        return "Registration successful for subject: " + subject.getSubjectName(); // คืนค่าข้อความสำเร็จ
    }
}