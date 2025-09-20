package registration.View;

import registration.model.Subject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentDashboardPanel extends JPanel { // คลาสหน้าจอหลักของนักเรียน
    private JLabel welcomeLabel; 
    private JList<Subject> availableCoursesList; // listแสดงวิชาที่ลงทะเบียนได้
    private JList<Subject> registeredCoursesList; // listแสดงวิชาที่ลงทะเบียนแล้ว
    private DefaultListModel<Subject> availableCoursesModel; // ตัวจัดการข้อมูลของlistวิชาที่ลงได้
    private DefaultListModel<Subject> registeredCoursesModel; // ตัวจัดการข้อมูลของlistวิชาที่ลงแล้ว
    private JTextArea courseDetailsArea; // พื้นที่แสดงรายละเอียดวิชา
    private JButton registerButton; // ปุ่มลงทะเบียน
    private JButton logoutButton; // ปุ่มออกจากระบบ

    public StudentDashboardPanel() { // สร้างหน้าจอ
        setLayout(new BorderLayout(10, 10)); // กำหนดการจัดวางหลัก

        welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER); // สร้างป้ายข้อความ
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16)); // กำหนดฟอนต์
        add(welcomeLabel, BorderLayout.NORTH); // เพิ่มป้ายข้อความไว้ด้านบน

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // สร้างตัวแบ่งหน้าจอซ้ายขวา
        splitPane.setResizeWeight(0.5); // กำหนดให้แบ่งครึ่งเท่าๆ กัน
        add(splitPane, BorderLayout.CENTER); // เพิ่มตัวแบ่งไว้ตรงกลาง

        availableCoursesModel = new DefaultListModel<>(); // สร้างตัวจัดการข้อมูล
        availableCoursesList = new JList<>(availableCoursesModel); // สร้างlist
        JPanel leftPanel = new JPanel(new BorderLayout()); // สร้างพาเนลฝั่งซ้าย
        leftPanel.add(new JLabel("Available Courses", SwingConstants.CENTER), BorderLayout.NORTH); // เพิ่มหัวข้อ
        leftPanel.add(new JScrollPane(availableCoursesList), BorderLayout.CENTER); // เพิ่มlistพร้อมแถบเลื่อน
        splitPane.setLeftComponent(leftPanel); // กำหนดเป็นส่วนซ้าย

        registeredCoursesModel = new DefaultListModel<>(); // สร้างตัวจัดการข้อมูล
        registeredCoursesList = new JList<>(registeredCoursesModel); // สร้างlist
        JPanel rightPanel = new JPanel(new BorderLayout()); // สร้างพาเนลฝั่งขวา
        rightPanel.add(new JLabel("My Registered Courses", SwingConstants.CENTER), BorderLayout.NORTH); // เพิ่มหัวข้อ
        rightPanel.add(new JScrollPane(registeredCoursesList), BorderLayout.CENTER); // เพิ่มlistพร้อมแถบเลื่อน
        splitPane.setRightComponent(rightPanel); // กำหนดเป็นส่วนขวา

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10)); // สร้างพาเนลด้านล่าง
        courseDetailsArea = new JTextArea(5, 30); // สร้างพื้นที่แสดงข้อความ
        courseDetailsArea.setEditable(false); // ตั้งค่าให้แก้ไขไม่ได้
        courseDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // กำหนดฟอนต์
        courseDetailsArea.setBorder(BorderFactory.createTitledBorder("Selected Course Details")); // เพิ่มเส้นขอบและหัวข้อ
        bottomPanel.add(new JScrollPane(courseDetailsArea), BorderLayout.CENTER); // เพิ่มพื้นที่แสดงข้อความ

        JPanel buttonPanel = new JPanel(new FlowLayout()); // สร้างพาเนลสำหรับวางปุ่ม
        registerButton = new JButton("Register Selected Course"); // สร้างปุ่มลงทะเบียน
        logoutButton = new JButton("Logout"); // สร้างปุ่มออกจากระบบ
        buttonPanel.add(registerButton); // เพิ่มปุ่ม
        buttonPanel.add(logoutButton); // เพิ่มปุ่ม
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH); // เพิ่มพาเนลปุ่มไว้ด้านล่างสุด

        add(bottomPanel, BorderLayout.SOUTH); // เพิ่มพาเนลด้านล่างทั้งหมดเข้าหน้าจอ
    }

    public void setWelcomeMessage(String name) { // กำหนดข้อความต้อนรับ
        welcomeLabel.setText("Welcome, " + name + "!");
    }

    public void setAvailableCourses(List<Subject> subjects) { // กำหนดข้อมูลวิชาที่ลงทะเบียนได้
        availableCoursesModel.clear(); // ล้างข้อมูลเก่า
        for (Subject subject : subjects) { // วนลูปเพิ่มข้อมูลใหม่
            availableCoursesModel.addElement(subject);
        }
    }

    public void setRegisteredCourses(List<Subject> subjects) { // กำหนดข้อมูลวิชาที่ลงทะเบียนแล้ว
        registeredCoursesModel.clear(); // ล้างข้อมูลเก่า
        for (Subject subject : subjects) { // วนลูปเพิ่มข้อมูลใหม่
            registeredCoursesModel.addElement(subject);
        }
    }
    
    public Subject getSelectedAvailableCourse() { // ดึงวิชาที่ถูกเลือก
        return availableCoursesList.getSelectedValue();
    }
    
    public JList<Subject> getAvailableCoursesList() { // ดึงlistวิชา
        return availableCoursesList;
    }

    public void setCourseDetails(Subject subject) { // แสดงรายละเอียดวิชา
        if (subject == null) { // ถ้าไม่ได้เลือกวิชา
            courseDetailsArea.setText(""); // ให้แสดงค่าว่าง
            return;
        }
        String capacity = subject.getMaxCapacity() == -1 ? "Unlimited" : String.valueOf(subject.getMaxCapacity()); // แปลงข้อมูลจำนวนรับ
        String details = String.format("ID: %s\nName: %s\nInstructor: %s\nCredits: %d\nCapacity: %d/%s\nPrerequisite: %s", // สร้างข้อความ
            subject.getSubjectId(),
            subject.getSubjectName(),
            subject.getInstructorName(),
            subject.getCredits(),
            subject.getCurrentEnrollment(),
            capacity,
            subject.getPrerequisiteSubjectId() == null ? "None" : subject.getPrerequisiteSubjectId()
        );
        courseDetailsArea.setText(details); // แสดงข้อความ
    }
    
    public void addRegisterListener(ActionListener listener) { // เชื่อมการทำงานปุ่มลงทะเบียน
        registerButton.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) { // เชื่อมการทำงานปุ่มออกจากระบบ
        logoutButton.addActionListener(listener);
    }
}