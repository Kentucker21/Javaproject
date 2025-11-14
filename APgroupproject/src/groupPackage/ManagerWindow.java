package groupPackage;


import javax.swing.*;
import java.awt.*;

public class ManagerWindow extends JFrame {
    public ManagerWindow(User user) {
        super("Manager Window");
        JLabel label = new JLabel("Welcome, " + user.getUsername() + " (ID: " + user.getId() + ")", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
        
        setWindowProperties();
        
    }
    
    
    
    private void setWindowProperties() {
    	setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

