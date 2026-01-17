package model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String nama;

    public User(String username, String password, String nama) {
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    // Getter dan Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", nama='" + nama + '\'' +
                '}';
    }

    // Untuk file CSV jika perlu
    public String toCSV() {
        return username + "," + password + "," + nama;
    }

    // Untuk debug
    public void printInfo() {
        System.out.println("=== User Info ===");
        System.out.println("Username: " + username);
        System.out.println("Nama: " + nama);
        System.out.println("=================");
    }
}