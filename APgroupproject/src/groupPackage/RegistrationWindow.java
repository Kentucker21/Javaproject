package groupPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

public class RegistrationWindow extends JFrame {
    private JComboBox<String> roleCombo;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton confirmButton;

    public RegistrationWindow() {
        super("Registration Page");
        initializeComponents();
        setWindowProperties();
    }

    private void initializeComponents() {
        String[] roles = {"Customer", "Clerk", "Manager", "Driver"};
        roleCombo = new JComboBox<>(roles);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmButton = new JButton("Confirm");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(createField("Role:", roleCombo));
        panel.add(createField("Username:", usernameField));
        panel.add(createField("Password:", passwordField));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);

        confirmButton.addActionListener(e -> registerUser());
    }

    private JPanel createField(String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(80, 25));
        panel.add(lbl);
        panel.add(field);
        return panel;
    }

    private void setWindowProperties() {
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void registerUser() {
        String role = (String) roleCombo.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<User> users = UserDatabase.loadUsers();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int newId = UserDatabase.getNextId(users);

        
        User newUser = new User(newId, role, username, password);

        // DRIVER: vehicle capacity
        
        if (role.equals("Driver")) {
        	Random random=new Random();
           int capacity = (random.nextInt(5)+1)*60;

           

            newUser.setVehicleCapacity(capacity);
        }
        

        // Save updated user list
        users.add(newUser);
        UserDatabase.saveUsers(users);

        JOptionPane.showMessageDialog(this, "User info saved! Returning to login.", "Success", JOptionPane.INFORMATION_MESSAGE);

        new LoginWindow();
        this.dispose();
    }
}
