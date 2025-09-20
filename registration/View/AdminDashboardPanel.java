package registration.View;

import registration.model.Registration;
import registration.model.Student;
import registration.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class AdminDashboardPanel extends JPanel { // คลาสหน้าจอหลักของแอดมิน
    private JList<Subject> subjectList; // listแสดงรายวิชา
    private JList<Registration> registrationList; // listแสดงการลงทะเบียน
    private DefaultListModel<Subject> subjectListModel; // ตัวจัดการข้อมูลlistวิชา
    private DefaultListModel<Registration> registrationListModel; // ตัวจัดการข้อมูลlistการลงทะเบียน
    private JTextField gradeField; // ช่องกรอกเกรด
    private JButton saveGradeButton; // ปุ่มบันทึกเกรด
    private JButton logoutButton; // ปุ่มออกจากระบบ

    public AdminDashboardPanel() { // สร้างหน้าจอ
        setLayout(new BorderLayout(10, 10)); // กำหนดการจัดวางหลัก

        JLabel titleLabel = new JLabel("Administrator Dashboard - Grade Entry", SwingConstants.CENTER); // สร้างป้ายหัวข้อ
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); 
        add(titleLabel, BorderLayout.NORTH); // เพิ่มหัวข้อไว้ด้านบน

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // สร้างตัวแบ่งหน้าจอซ้ายขวา
        splitPane.setResizeWeight(0.4); // กำหนดขนาดเริ่มต้น
        add(splitPane, BorderLayout.CENTER); // เพิ่มตัวแบ่งไว้ตรงกลาง

        subjectListModel = new DefaultListModel<>(); // สร้างตัวจัดการข้อมูล
        subjectList = new JList<>(subjectListModel); // สร้างlist
        JPanel subjectPanel = new JPanel(new BorderLayout()); // สร้างPanelฝั่งซ้าย
        subjectPanel.add(new JLabel("1. Select a Subject", SwingConstants.CENTER), BorderLayout.NORTH); // เพิ่มหัวข้อ
        subjectPanel.add(new JScrollPane(subjectList), BorderLayout.CENTER); // เพิ่มlist
        splitPane.setLeftComponent(subjectPanel); // กำหนดเป็นส่วนซ้าย

        registrationListModel = new DefaultListModel<>(); // สร้างตัวจัดการข้อมูล
        registrationList = new JList<>(registrationListModel); // สร้างlist
        JPanel registrationPanel = new JPanel(new BorderLayout()); // สร้างPanelฝั่งขวา
        registrationPanel.add(new JLabel("2. Select a Student to Grade", SwingConstants.CENTER), BorderLayout.NORTH); // เพิ่มหัวข้อ
        registrationPanel.add(new JScrollPane(registrationList), BorderLayout.CENTER); // เพิ่มlist
        splitPane.setRightComponent(registrationPanel); // กำหนดเป็นส่วนขวา

        JPanel bottomPanel = new JPanel(new FlowLayout()); // สร้างPanelด้านล่าง
        bottomPanel.add(new JLabel("3. Enter Grade:")); // เพิ่มป้ายข้อความ
        gradeField = new JTextField(5); // สร้างช่องกรอกเกรด
        bottomPanel.add(gradeField); // เพิ่มช่องกรอก
        saveGradeButton = new JButton("Save Grade"); // สร้างปุ่มบันทึก
        bottomPanel.add(saveGradeButton); // เพิ่มปุ่ม
        logoutButton = new JButton("Logout"); // สร้างปุ่มออกจากระบบ
        bottomPanel.add(logoutButton); // เพิ่มปุ่ม
        add(bottomPanel, BorderLayout.SOUTH); // เพิ่มPanelด้านล่างเข้าหน้าจอ
    }

    public void setSubjects(List<Subject> subjects) { // กำหนดข้อมูลวิชา
        subjectListModel.clear(); // ล้างข้อมูลเก่า
        subjects.forEach(subjectListModel::addElement); // เพิ่มข้อมูลใหม่
    }
    
    public void setRegistrationsForSubject(List<Registration> registrations, Map<String, Student> studentMap) { // กำหนดข้อมูลการลงทะเบียน
        registrationListModel.clear(); // ล้างข้อมูลเก่า
        registrationList.setCellRenderer(new DefaultListCellRenderer() { // กำหนดรูปแบบการแสดงผลในlist
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Registration) { 
                    Registration reg = (Registration) value; // แปลงชนิดข้อมูล
                    Student student = studentMap.get(reg.getStudentId()); // ค้นหาข้อมูลนักเรียน
                    String studentName = (student != null) ? student.getFirstName() + " " + student.getLastName() : "Unknown Student"; // เตรียมชื่อนักเรียน
                    String grade = (reg.getGrade() == null || reg.getGrade().isEmpty()) ? "Not Graded" : reg.getGrade(); // เตรียมข้อมูลเกรด
                    setText(String.format("%s (%s) - Grade: %s", studentName, reg.getStudentId(), grade)); // กำหนดข้อความที่จะแสดง
                }
                return c; // คืนค่าComponentที่ปรับปรุงแล้ว
            }
        });
        registrations.forEach(registrationListModel::addElement); // เพิ่มข้อมูลใหม่
    }

    public JList<Subject> getSubjectList() { // ดึงlistวิชา
        return subjectList;
    }

    public Subject getSelectedSubject() { // ดึงวิชาที่ถูกเลือก
        return subjectList.getSelectedValue();
    }

    public Registration getSelectedRegistration() { // ดึงการลงทะเบียนที่ถูกเลือก
        return registrationList.getSelectedValue();
    }

    public String getGradeInput() { // ดึงเกรดที่ผู้ใช้กรอก
        return gradeField.getText();
    }
    
    public void addSaveGradeListener(ActionListener listener) { // เชื่อมการทำงานปุ่มบันทึก
        saveGradeButton.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) { // เชื่อมการทำงานปุ่มออกจากระบบ
        logoutButton.addActionListener(listener);
    }
}