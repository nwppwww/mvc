package registration.View;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame { // คลาสหน้าต่างหลักของโปรแกรม
    private CardLayout cardLayout; // ตัวจัดการการสลับหน้าจอ
    private JPanel mainPanel; // Panelหลักสำหรับวางหน้าจออื่น

    public MainFrame() { // สร้างหน้าต่าง
        setTitle(" Pre-Registration System"); // ตั้งชื่อหน้าต่าง
        setSize(800, 600); // กำหนดขนาดหน้าต่าง
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ตั้งค่าให้ปิดโปรแกรมเมื่อกดกากบาท
        setLocationRelativeTo(null); // แสดงหน้าต่างกลางจอ

        cardLayout = new CardLayout(); // สร้างตัวสลับหน้าจอ
        mainPanel = new JPanel(cardLayout); // สร้างPanelหลักและกำหนดตัวสลับ

        add(mainPanel); // เพิ่มPanelหลักลงในหน้าต่าง
    }

    public void addPanel(JPanel panel, String name) { // เพิ่มหน้าจอใหม่
        mainPanel.add(panel, name);
    }

    public void switchToPanel(String name) { // สลับไปแสดงหน้าจอที่ต้องการ
        cardLayout.show(mainPanel, name);
    }
    
    public void showErrorMessage(String message) { // แสดงกล่องข้อความผิดพลาด
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message) { // แสดงกล่องข้อความทั่วไป
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}