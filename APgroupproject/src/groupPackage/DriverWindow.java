package groupPackage;
import javax.swing.*;
import java.awt.*;

public class DriverWindow extends JFrame {
    public DriverWindow(User user) {
        super("Driver Window");
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

