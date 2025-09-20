package registration.View;
import registration.model.Subject;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentDashboardPanel extends JPanel {
    private JLabel welcomeLabel;
    private JList<Subject> availableCoursesList;
    private JList<Subject> registeredCoursesList;
    private DefaultListModel<Subject> availableCoursesModel;
    private DefaultListModel<Subject> registeredCoursesModel;
    private JTextArea courseDetailsArea;
    private JButton registerButton;
    private JButton logoutButton;

    public StudentDashboardPanel() {
        setLayout(new BorderLayout(10, 10));

        welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        // --- Center Panel with two lists ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        availableCoursesModel = new DefaultListModel<>();
        availableCoursesList = new JList<>(availableCoursesModel);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Available Courses", SwingConstants.CENTER), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(availableCoursesList), BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        registeredCoursesModel = new DefaultListModel<>();
        registeredCoursesList = new JList<>(registeredCoursesModel);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("My Registered Courses", SwingConstants.CENTER), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(registeredCoursesList), BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        // --- Details and Buttons Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        courseDetailsArea = new JTextArea(5, 30);
        courseDetailsArea.setEditable(false);
        courseDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        courseDetailsArea.setBorder(BorderFactory.createTitledBorder("Selected Course Details"));
        bottomPanel.add(new JScrollPane(courseDetailsArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("Register Selected Course");
        logoutButton = new JButton("Logout");
        buttonPanel.add(registerButton);
        buttonPanel.add(logoutButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setWelcomeMessage(String name) {
        welcomeLabel.setText("Welcome, " + name + "!");
    }

    public void setAvailableCourses(List<Subject> subjects) {
        availableCoursesModel.clear();
        for (Subject subject : subjects) {
            availableCoursesModel.addElement(subject);
        }
    }

    public void setRegisteredCourses(List<Subject> subjects) {
        registeredCoursesModel.clear();
        for (Subject subject : subjects) {
            registeredCoursesModel.addElement(subject);
        }
    }
    
    public Subject getSelectedAvailableCourse() {
        return availableCoursesList.getSelectedValue();
    }
    
    public JList<Subject> getAvailableCoursesList() {
        return availableCoursesList;
    }

    public void setCourseDetails(Subject subject) {
        if (subject == null) {
            courseDetailsArea.setText("");
            return;
        }
        String capacity = subject.getMaxCapacity() == -1 ? "Unlimited" : String.valueOf(subject.getMaxCapacity());
        String details = String.format("ID: %s\nName: %s\nInstructor: %s\nCredits: %d\nCapacity: %d/%s\nPrerequisite: %s",
            subject.getSubjectId(),
            subject.getSubjectName(),
            subject.getInstructorName(),
            subject.getCredits(),
            subject.getCurrentEnrollment(),
            capacity,
            subject.getPrerequisiteSubjectId() == null ? "None" : subject.getPrerequisiteSubjectId()
        );
        courseDetailsArea.setText(details);
    }
    
    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
}