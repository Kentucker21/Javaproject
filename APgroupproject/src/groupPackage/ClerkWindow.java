package groupPackage;


import javax.swing.*;
import java.awt.*;

public class ClerkWindow extends JFrame {
	
	public User CurrentUser;
    public ClerkWindow(User user) {
        super("Clerk Window");
        this.CurrentUser=user;
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
