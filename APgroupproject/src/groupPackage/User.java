package groupPackage;


import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String role;
    private String username;
    private String password;

    public User(int id, String role, String username, String password) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return id + " - " + role + " - " + username;
    }
}

