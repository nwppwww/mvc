package registration.controller;

import registration.model.*;
import registration.View.AdminDashboardPanel; // Import aDDMIN PANEL
import registration.View.LoginPanel;
import registration.View.MainFrame;
import registration.View.StudentDashboardPanel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppController {
    // Model
     private final DatabaseService dbService; // ตัวจัดการไฟล์ CSV
    private final RegistrationService regService; // ตัวจัดการกฎการลงทะเบียน
    private List<Student> students; // เก็บรายชื่อนักเรียนทั้งหมด
    private List<Subject> subjects; // เก็บรายวิชาทั้งหมด
    private List<Registration> registrations; // เก็บข้อมูลการลงทะเบียนทั้งหมด
    private Student currentStudent; // เก็บข้อมูลนักเรียนที่ล็อกอินอยู่

    // View
     private MainFrame mainFrame; // หน้าต่างหลักของโปรแกรม
    private LoginPanel loginPanel; // หน้าจอ Login
    private StudentDashboardPanel studentDashboardPanel; // หน้าจอของนักเรียน
    private AdminDashboardPanel adminDashboardPanel; // หน้าจอของแอดมิน
    
    public AppController() { //สร้างอ็อบเจกต์ Service ที่ต้องใช้
        this.dbService = new DatabaseService();
        this.regService = new RegistrationService();
    }

    public void start() {
        //  โหลดข้อมูลทั้งหมดจากไฟล์ CSV มาเก็บใน List
        this.students = dbService.loadStudents();
        this.subjects = dbService.loadSubjects();
        this.registrations = dbService.loadRegistrations();

        // สร้างอ็อบเจกต์ของหน้าจอต่างๆ
        this.mainFrame = new MainFrame();
        this.loginPanel = new LoginPanel();
        this.studentDashboardPanel = new StudentDashboardPanel();
        this.adminDashboardPanel = new AdminDashboardPanel(); // สร้าง instance ของ Admin View

        // Add panels to the frame
        mainFrame.addPanel(loginPanel, "LOGIN");
        mainFrame.addPanel(studentDashboardPanel, "STUDENT_DASHBOARD");
        mainFrame.addPanel(adminDashboardPanel, "ADMIN_DASHBOARD"); // เพิ่ม Admin panel เข้าไปใน frame

        // Attach event listeners
        attachListeners();

        // Show the initial panel and make the frame visible
        mainFrame.switchToPanel("LOGIN");
        mainFrame.setVisible(true);
    }

    private void attachListeners() {
        // --- Common Listeners ---
        loginPanel.addLoginListener(e -> handleLogin());

        // --- Student Listeners ---
        studentDashboardPanel.addRegisterListener(e -> handleRegistration());
        studentDashboardPanel.addLogoutListener(e -> handleLogout());
        studentDashboardPanel.getAvailableCoursesList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Subject selected = studentDashboardPanel.getSelectedAvailableCourse();
                studentDashboardPanel.setCourseDetails(selected);
            }
        });

        // --- Admin Listeners ---
        adminDashboardPanel.getStudentList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateAdminRegistrationsView();
            }
        });
        adminDashboardPanel.addSaveGradeListener(e -> handleSaveGrade());
        adminDashboardPanel.addLogoutListener(e -> handleLogout());
    }

    private void handleLogin() {
        String userId = loginPanel.getStudentId();
        
        // --- Admin Login ---
        if ("admin".equalsIgnoreCase(userId)) {
            updateAdminDashboard();
            mainFrame.switchToPanel("ADMIN_DASHBOARD");
            return;
        }

        // --- Student Login ---
        Optional<Student> studentOpt = students.stream()
                .filter(s -> s.getStudentId().equals(userId))
                .findFirst();

        if (studentOpt.isPresent()) {
            currentStudent = studentOpt.get();
            updateStudentDashboard();
            mainFrame.switchToPanel("STUDENT_DASHBOARD");
        } else {
            mainFrame.showErrorMessage("Login Failed. User ID not found.");
        }
    }

    private void handleLogout() {
        currentStudent = null;
        mainFrame.switchToPanel("LOGIN");
    }

    // --- Student Methods ---

    private void handleRegistration() {
        Subject selectedSubject = studentDashboardPanel.getSelectedAvailableCourse();
        if (selectedSubject == null) {
            mainFrame.showErrorMessage("Please select a course to register.");
            return;
        }
        String result = regService.registerStudentForSubject(currentStudent, selectedSubject, registrations);
        if (result.startsWith("Registration successful")) {
            mainFrame.showInfoMessage(result);
            dbService.saveSubjects(subjects);
            dbService.saveRegistrations(registrations);
            updateStudentDashboard();
        } else {
            mainFrame.showErrorMessage(result);
        }
    }

    private void updateStudentDashboard() {
        if (currentStudent == null) return;
        studentDashboardPanel.setWelcomeMessage(currentStudent.getFirstName());
        List<String> registeredSubjectIds = registrations.stream()
                .filter(r -> r.getStudentId().equals(currentStudent.getStudentId()))
                .map(Registration::getSubjectId)
                .collect(Collectors.toList());
        List<Subject> availableSubjects = subjects.stream()
                .filter(s -> !registeredSubjectIds.contains(s.getSubjectId()))
                .collect(Collectors.toList());
        List<Subject> registeredSubjects = subjects.stream()
                .filter(s -> registeredSubjectIds.contains(s.getSubjectId()))
                .collect(Collectors.toList());
        studentDashboardPanel.setAvailableCourses(availableSubjects);
        studentDashboardPanel.setRegisteredCourses(registeredSubjects);
        studentDashboardPanel.setCourseDetails(null);
    }

    // --- Admin Methods ---

    private void updateAdminDashboard() {
        adminDashboardPanel.setStudents(students);
        updateAdminRegistrationsView(); // Initially show no registrations selected
    }

    private void updateAdminRegistrationsView() {
        Student selectedStudent = adminDashboardPanel.getSelectedStudent();
        if (selectedStudent == null) {
            adminDashboardPanel.setRegistrations(List.of(), Map.of()); // Clear list if no student selected
            return;
        }
        // Get all registrations for the selected student
        List<Registration> studentRegs = registrations.stream()
                .filter(r -> r.getStudentId().equals(selectedStudent.getStudentId()))
                .collect(Collectors.toList());

        // Create a map of SubjectID -> SubjectName for easy lookup in the view
        Map<String, String> subjectNameMap = subjects.stream()
                .collect(Collectors.toMap(Subject::getSubjectId, Subject::getSubjectName));

        adminDashboardPanel.setRegistrations(studentRegs, subjectNameMap);
    }
    
    private void handleSaveGrade() {
        Registration selectedReg = adminDashboardPanel.getSelectedRegistration();
        if (selectedReg == null) {
            mainFrame.showErrorMessage("Please select a student and a registered course.");
            return;
        }
        String grade = adminDashboardPanel.getGradeInput().trim().toUpperCase();
        if (grade.isEmpty()) {
            mainFrame.showErrorMessage("Please enter a grade.");
            return;
        }
        
        // Update model
        selectedReg.setGrade(grade);
        
        // Save to CSV
        dbService.saveRegistrations(registrations);
        
        mainFrame.showInfoMessage("Grade updated successfully!");
        
        // Refresh the view to show the new grade
        updateAdminRegistrationsView();
    }
}