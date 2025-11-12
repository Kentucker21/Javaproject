package groupPackage;


import java.io.*;
import java.util.*;

public class UserDatabase {
    private static final String FILE_PATH = "users.txt";

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            // ignore — first time use
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNextId(List<User> users) {
        return users.isEmpty() ? 1 : users.get(users.size() - 1).getId() + 1;
    }
}

