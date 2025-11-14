package groupPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LoginWindow extends JFrame {
    JLabel roleLabel;
    JComboBox<String> role;
    JLabel userLabel;
    JTextField userName;
    JLabel passwordLabel;
    JPasswordField password;
    JButton loginButton;
    JButton registerButton;

    public LoginWindow() {
        super("Login Page");
        initializeComponents();
        AddComptoPanel();
        ActivateButtons(); 
        setWindowProperties();
    }

    private void setWindowProperties() {
        this.setSize(500, 220);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    private void initializeComponents() {
        String[] roles = {"Customer", "Clerk", "Manager", "Driver"};
        roleLabel = new JLabel("Role");
        role = new JComboBox<>(roles);
        userLabel = new JLabel("Username");
        userName = new JTextField();
        passwordLabel = new JLabel("Password");
        password = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
    }

    private void AddComptoPanel() {
        JPanel buttonHolder = new JPanel();
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));

        buttonHolder.add(loginButton);
        buttonHolder.add(registerButton);

        this.setLayout(new BorderLayout());
        JLabel title = new JLabel("Welcome to ANONSHIP - Sign in below", SwingConstants.CENTER);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);
        add(buttonHolder, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel ComboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        ComboPanel.add(roleLabel);
        ComboPanel.add(role);
        mainPanel.add(ComboPanel);
        mainPanel.add(createLabelFieldPair(userLabel, userName));
        mainPanel.add(createLabelFieldPair(passwordLabel, password));

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createLabelFieldPair(JLabel label, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        label.setPreferredSize(new Dimension(80, 25));
        textField.setPreferredSize(new Dimension(250, 35));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private void ActivateButtons() {
        registerButton.addActionListener(e -> {
            new RegistrationWindow();
            this.dispose();
        });

        loginButton.addActionListener(e -> authenticate());
    }

    
    private void authenticate() {
        String selectedRole = (String) role.getSelectedItem();
        String username = userName.getText().trim();
        String pass = new String(password.getPassword());

        List<User> users = UserDatabase.loadUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) &&
                u.getPassword().equals(pass) &&
                u.getRole().equals(selectedRole)) {

                JOptionPane.showMessageDialog(this, "Login successful!");

                this.dispose();
                openRoleWindow(u);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void openRoleWindow(User user) {
        switch (user.getRole()) {
            case "Customer" -> new CustomerWindow(user);
            case "Clerk" -> new ClerkWindow(user);
            case "Manager" -> new ManagerWindow(user);
            case "Driver" -> new DriverWindow(user);
        
            
        }
    }
}
