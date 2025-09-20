package registration.controller;

import registration.model.*;
import registration.View.AdminDashboardPanel;
import registration.View.LoginPanel;
import registration.View.MainFrame;
import registration.View.StudentDashboardPanel;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppController { // คลาสตัวควบคุมหลัก
    private final DatabaseService dbService; // ตัวจัดการไฟล์
    private final RegistrationService regService; // ตัวจัดการกฎการลงทะเบียน
    private List<Student> students; // ลิสต์เก็บข้อมูลนักเรียน
    private List<Subject> subjects; // ลิสต์เก็บข้อมูลวิชา
    private List<Registration> registrations; // ลิสต์เก็บข้อมูลการลงทะเบียน
    private Student currentStudent; // เก็บข้อมูลนักเรียนที่ล็อกอินอยู่

    private MainFrame mainFrame; // หน้าต่างหลัก
    private LoginPanel loginPanel; // หน้าจอลงชื่อเข้าใช้
    private StudentDashboardPanel studentDashboardPanel; // หน้าจอของนักเรียน
    private AdminDashboardPanel adminDashboardPanel; // หน้าจอของแอดมิน

    public AppController() { // สร้างตัวควบคุม
        this.dbService = new DatabaseService();
        this.regService = new RegistrationService();
    }

    public void start() { // เริ่มต้นการทำงาน
        this.students = dbService.loadStudents(); // โหลดข้อมูลนักเรียน
        this.subjects = dbService.loadSubjects(); // โหลดข้อมูลวิชา
        this.registrations = dbService.loadRegistrations(); // โหลดข้อมูลการลงทะเบียน

        this.mainFrame = new MainFrame(); // สร้างหน้าต่างหลัก
        this.loginPanel = new LoginPanel(); // สร้างหน้าจอลงชื่อเข้าใช้
        this.studentDashboardPanel = new StudentDashboardPanel(); // สร้างหน้าจอของนักเรียน
        this.adminDashboardPanel = new AdminDashboardPanel(); // สร้างหน้าจอของแอดมิน

        mainFrame.addPanel(loginPanel, "LOGIN"); // เพิ่มหน้าจอลงชื่อเข้าใช้
        mainFrame.addPanel(studentDashboardPanel, "STUDENT_DASHBOARD"); // เพิ่มหน้าจอของนักเรียน
        mainFrame.addPanel(adminDashboardPanel, "ADMIN_DASHBOARD"); // เพิ่มหน้าจอของแอดมิน

        attachListeners(); // เชื่อมต่อการทำงานของปุ่มต่างๆ

        mainFrame.switchToPanel("LOGIN"); // แสดงหน้าจอลงชื่อเข้าใช้เป็นหน้าแรก
        mainFrame.setVisible(true); // แสดงหน้าต่างโปรแกรม
    }

    private void attachListeners() { // เชื่อมต่อการทำงาน
        loginPanel.addLoginListener(e -> handleLogin()); // เมื่อปุ่มล็อกอินถูกกด

        studentDashboardPanel.addRegisterListener(e -> handleRegistration()); // เมื่อปุ่มลงทะเบียนถูกกด
        studentDashboardPanel.addLogoutListener(e -> handleLogout()); // เมื่อปุ่มออกจากระบบถูกกด
        studentDashboardPanel.getAvailableCoursesList().addListSelectionListener(e -> { // เมื่อมีการเลือกวิชาในลิสต์
            if (!e.getValueIsAdjusting()) { 
                Subject selected = studentDashboardPanel.getSelectedAvailableCourse(); // ดึงวิชาที่เลือก
                studentDashboardPanel.setCourseDetails(selected); // แสดงรายละเอียดวิชา
            }
        });

        adminDashboardPanel.getSubjectList().addListSelectionListener(e -> { // เมื่อแอดมินเลือกวิชาในลิสต์
            if (!e.getValueIsAdjusting()) {
                updateAdminRegistrationsForSubjectView(); // อัปเดตรายชื่อนักเรียน
            }
        });
        adminDashboardPanel.addSaveGradeListener(e -> handleSaveGrade()); // เมื่อปุ่มบันทึกเกรดถูกกด
        adminDashboardPanel.addLogoutListener(e -> handleLogout()); // เมื่อปุ่มออกจากระบบถูกกด
    }

    private void handleLogin() { // จัดการการลงชื่อเข้าใช้
        String userId = loginPanel.getStudentId(); // ดึงรหัสที่ผู้ใช้กรอก
        
        if ("admin".equalsIgnoreCase(userId)) { // ตรวจสอบว่าเป็นแอดมินหรือไม่
            updateAdminDashboard(); // เตรียมข้อมูลหน้าแอดมิน
            mainFrame.switchToPanel("ADMIN_DASHBOARD"); // สลับไปหน้าแอดมิน
            return;
        }

        Optional<Student> studentOpt = students.stream() // ค้นหานักเรียนจากรหัส
                .filter(s -> s.getStudentId().equals(userId))
                .findFirst();

        if (studentOpt.isPresent()) { // ถ้าเจอนักเรียน
            currentStudent = studentOpt.get(); // เก็บข้อมูลนักเรียนไว้
            updateStudentDashboard(); // เตรียมข้อมูลหน้านักเรียน
            mainFrame.switchToPanel("STUDENT_DASHBOARD"); // สลับไปหน้านักเรียน
        } else { // ถ้าไม่เจอ
            mainFrame.showErrorMessage("Login Failed. User ID not found."); // แสดงข้อความผิดพลาด
        }
    }

    private void handleLogout() { // จัดการการออกจากระบบ
        currentStudent = null; // ล้างข้อมูลนักเรียนที่ล็อกอินอยู่
        mainFrame.switchToPanel("LOGIN"); // กลับไปหน้าลงชื่อเข้าใช้
    }

    private void handleRegistration() { // จัดการการลงทะเบียน
        Subject selectedSubject = studentDashboardPanel.getSelectedAvailableCourse(); // ดึงวิชาที่เลือก
        if (selectedSubject == null) { // ถ้ายังไม่ได้เลือก
            mainFrame.showErrorMessage("Please select a course to register."); // แสดงข้อความเตือน
            return;
        }
        String result = regService.registerStudentForSubject(currentStudent, selectedSubject, registrations); // เรียกใช้บริการเพื่อลงทะเบียน
        
        if (result.startsWith("Registration successful")) { // ถ้าลงทะเบียนสำเร็จ
            mainFrame.showInfoMessage(result); // แสดงข้อความสำเร็จ
            dbService.saveSubjects(subjects); // บันทึกข้อมูลวิชา
            dbService.saveRegistrations(registrations); // บันทึกข้อมูลการลงทะเบียน
            updateStudentDashboard(); // อัปเดตหน้าจอ
        } else { // ถ้าไม่สำเร็จ
            mainFrame.showErrorMessage(result); // แสดงข้อความผิดพลาด
        }
    }

    private void updateStudentDashboard() { // อัปเดตข้อมูลหน้านักเรียน
        if (currentStudent == null) return; // ถ้าไม่มีใครล็อกอินให้ออก
        studentDashboardPanel.setWelcomeMessage(currentStudent.getFirstName()); // ตั้งข้อความต้อนรับ
        
        List<String> registeredSubjectIds = registrations.stream() // ค้นหารหัสวิชาที่ลงทะเบียนไปแล้ว
                .filter(r -> r.getStudentId().equals(currentStudent.getStudentId()))
                .map(Registration::getSubjectId)
                .collect(Collectors.toList());
        
        List<Subject> availableSubjects = subjects.stream() // กรองวิชาที่ยังไม่ได้ลงทะเบียน
                .filter(s -> !registeredSubjectIds.contains(s.getSubjectId()))
                .collect(Collectors.toList());
        
        List<Subject> registeredSubjects = subjects.stream() // กรองวิชาที่ลงทะเบียนไปแล้ว
                .filter(s -> registeredSubjectIds.contains(s.getSubjectId()))
                .collect(Collectors.toList());
        
        studentDashboardPanel.setAvailableCourses(availableSubjects); // ส่งข้อมูลวิชาที่ลงได้ไปแสดง
        studentDashboardPanel.setRegisteredCourses(registeredSubjects); // ส่งข้อมูลวิชาที่ลงแล้วไปแสดง
        studentDashboardPanel.setCourseDetails(null); // ล้างรายละเอียดวิชาที่แสดงอยู่
    }

    private void updateAdminDashboard() { // อัปเดตข้อมูลหน้าแอดมิน
        adminDashboardPanel.setSubjects(subjects); // ส่งรายชื่อวิชาทั้งหมดไปแสดง
        updateAdminRegistrationsForSubjectView(); // อัปเดตรายชื่อนักเรียน
    }

    private void updateAdminRegistrationsForSubjectView() { // อัปเดตรายชื่อนักเรียนในหน้าแอดมิน
        Subject selectedSubject = adminDashboardPanel.getSelectedSubject(); // ดึงวิชาที่แอดมินเลือก
        if (selectedSubject == null) { // ถ้ายังไม่ได้เลือก
            adminDashboardPanel.setRegistrationsForSubject(List.of(), Map.of()); // แสดงลิสต์ว่าง
            return;
        }
        
        List<Registration> subjectRegs = registrations.stream() // ค้นหาการลงทะเบียนของวิชานั้น
                .filter(r -> r.getSubjectId().equals(selectedSubject.getSubjectId()))
                .collect(Collectors.toList());

        Map<String, Student> studentMap = students.stream() // สร้างแผนที่สำหรับค้นหาข้อมูลนักเรียน
                .collect(Collectors.toMap(Student::getStudentId, student -> student));

        adminDashboardPanel.setRegistrationsForSubject(subjectRegs, studentMap); // ส่งข้อมูลไปแสดง
    }
    
    private void handleSaveGrade() { // จัดการการบันทึกเกรด
        Registration selectedReg = adminDashboardPanel.getSelectedRegistration(); // ดึงรายการที่เลือก
        if (selectedReg == null) { // ถ้ายังไม่ได้เลือก
            mainFrame.showErrorMessage("Please select a subject and then a student."); // แสดงข้อความเตือน
            return;
        }
        String grade = adminDashboardPanel.getGradeInput().trim().toUpperCase(); // ดึงเกรดที่กรอก
        if (grade.isEmpty()) { // ถ้าไม่ได้กรอก
            mainFrame.showErrorMessage("Please enter a grade."); // แสดงข้อความเตือน
            return;
        }
        
        selectedReg.setGrade(grade); // กำหนดเกรดในข้อมูล
        dbService.saveRegistrations(registrations); // บันทึกข้อมูลลงไฟล์
        mainFrame.showInfoMessage("Grade updated successfully!"); // แสดงข้อความสำเร็จ
        
        updateAdminRegistrationsForSubjectView(); // อัปเดตหน้าจอ
    }
}