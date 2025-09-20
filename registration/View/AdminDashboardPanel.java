package registration.View;
import registration.model.Registration;
import registration.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboardPanel extends JPanel {
    private JList<Student> studentList;
    private JList<Registration> registrationList;
    private DefaultListModel<Student> studentListModel;
    private DefaultListModel<Registration> registrationListModel;
    private JTextField gradeField;
    private JButton saveGradeButton;
    private JButton logoutButton;

    public AdminDashboardPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Administrator Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // --- Split pane for students and their registrations ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.4);
        add(splitPane, BorderLayout.CENTER);

        // Student List Panel
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.add(new JLabel("All Students", SwingConstants.CENTER), BorderLayout.NORTH);
        studentPanel.add(new JScrollPane(studentList), BorderLayout.CENTER);
        splitPane.setLeftComponent(studentPanel);

        // Registration List Panel
        registrationListModel = new DefaultListModel<>();
        registrationList = new JList<>(registrationListModel);
        JPanel registrationPanel = new JPanel(new BorderLayout());
        registrationPanel.add(new JLabel("Selected Student's Registrations", SwingConstants.CENTER), BorderLayout.NORTH);
        registrationPanel.add(new JScrollPane(registrationList), BorderLayout.CENTER);
        splitPane.setRightComponent(registrationPanel);

        // --- Bottom panel for grading and actions ---
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Enter Grade:"));
        gradeField = new JTextField(5);
        bottomPanel.add(gradeField);
        saveGradeButton = new JButton("Save Grade");
        bottomPanel.add(saveGradeButton);
        logoutButton = new JButton("Logout");
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- Methods for Controller to use ---

    public JList<Student> getStudentList() {
        return studentList;
    }

    public Student getSelectedStudent() {
        return studentList.getSelectedValue();
    }

    public Registration getSelectedRegistration() {
        return registrationList.getSelectedValue();
    }

    public String getGradeInput() {
        return gradeField.getText();
    }

    public void setStudents(List<Student> students) {
        studentListModel.clear();
        students.forEach(studentListModel::addElement);
    }

    public void setRegistrations(List<Registration> registrations, java.util.Map<String, String> subjectNameMap) {
        registrationListModel.clear();
        // Custom cell renderer to show more details
        registrationList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Registration) {
                    Registration reg = (Registration) value;
                    String subjectName = subjectNameMap.getOrDefault(reg.getSubjectId(), "Unknown Subject");
                    String grade = (reg.getGrade() == null || reg.getGrade().isEmpty()) ? "Not Graded" : reg.getGrade();
                    setText(String.format("%s (%s) - Grade: %s", subjectName, reg.getSubjectId(), grade));
                }
                return c;
            }
        });
        registrations.forEach(registrationListModel::addElement);
    }
    
    public void addSaveGradeListener(ActionListener listener) {
        saveGradeButton.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
}