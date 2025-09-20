package registration.View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField studentIdField;
    private JButton loginButton;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        studentIdField = new JTextField(15);
        add(studentIdField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginButton = new JButton("Login");
        add(loginButton, gbc);
    }

    public String getStudentId() {
        return studentIdField.getText();
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
}