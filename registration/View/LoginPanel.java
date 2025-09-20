package registration.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel { // คลาสหน้าจอสำหรับลงชื่อเข้าใช้
    private JTextField studentIdField; // ช่องกรอกรหัสนักเรียน
    private JButton loginButton; // ปุ่มลงชื่อเข้าใช้

    public LoginPanel() { // สร้างหน้าจอ
        setLayout(new GridBagLayout()); // กำหนดการจัดวาง
        GridBagConstraints gbc = new GridBagConstraints(); // ตัวช่วยกำหนดตำแหน่ง

        gbc.insets = new Insets(5, 5, 5, 5); // กำหนดระยะห่าง

        gbc.gridx = 0; // ตำแหน่งคอลัมน์
        gbc.gridy = 0; // ตำแหน่งแถว
        add(new JLabel("Student ID:"), gbc); // เพิ่มป้ายข้อความ

        gbc.gridx = 1; // ตำแหน่งคอลัมน์
        studentIdField = new JTextField(15); // สร้างช่องกรอกข้อความ
        add(studentIdField, gbc); // เพิ่มช่องกรอกข้อความ

        gbc.gridx = 1; // ตำแหน่งคอลัมน์
        gbc.gridy = 1; // ตำแหน่งแถว
        loginButton = new JButton("Login"); // สร้างปุ่ม
        add(loginButton, gbc); // เพิ่มปุ่ม
    }

    public String getStudentId() { // ดึงข้อความที่ผู้ใช้กรอก
        return studentIdField.getText();
    }

    public void addLoginListener(ActionListener listener) { // เชื่อมการทำงานของปุ่ม
        loginButton.addActionListener(listener);
    }
}