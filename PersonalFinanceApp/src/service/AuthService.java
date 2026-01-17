package service;

import model.User;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final String USER_FILE = "users.dat";
    private User currentUser;
    private Map<String, User> users;

    public AuthService() {
        users = new HashMap<>();
        currentUser = null;
        loadUsers();

        // Add default user jika tidak ada user
        if (users.isEmpty()) {
            User defaultUser = new User("admin", "admin123", "Administrator");
            users.put("admin", defaultUser);
            saveUsers();
        }
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(String username, String password, String nama) {
        if (users.containsKey(username)) {
            return false; // Username sudah ada
        }

        User newUser = new User(username, password, nama);
        users.put(username, newUser);
        saveUsers();
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean updateProfile(String newNama, String newPassword) {
        if (currentUser != null) {
            currentUser.setNama(newNama);
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                currentUser.setPassword(newPassword);
            }
            saveUsers();
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File belum ada, akan dibuat saat save pertama
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
            users = new HashMap<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Untuk testing
//    public static void main(String[] args) {
//        AuthService auth = new AuthService();
//
//        // Test register
//        auth.register("testuser", "password123", "Test User");
//
//        // Test login
//        boolean success = auth.login("testuser", "password123");
//        System.out.println("Login successful: " + success);
//        System.out.println("Current user: " +
//                (auth.getCurrentUser() != null ? auth.getCurrentUser().getNama() : "null"));
//    }
}