package gui;

import service.TransaksiService;
import service.AuthService;
import service.FileManager;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private TransaksiService transaksiService;
    private AuthService authService;
    private LoginPanel loginPanel; // Simpan reference

    public MainFrame() {
        super("Personal Finance Manager");

        // Setup frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Initialize services
        transaksiService = new TransaksiService(1000000);
        authService = new AuthService();

        // Load existing data
        FileManager.loadData(transaksiService);

        // Setup layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels
        loginPanel = new LoginPanel(this, authService); // Simpan reference
        DashboardPanel dashboardPanel = new DashboardPanel(this, transaksiService, authService);

        // Add to main panel
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");

        add(mainPanel);

        // Show login first
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showDashboard() {
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void showLogin() {
        // Reset login form saat kembali ke login
        if (loginPanel != null) {
            loginPanel.resetForm();
        }
        authService.logout();
        cardLayout.show(mainPanel, "LOGIN");
    }

    public TransaksiService getTransaksiService() {
        return transaksiService;
    }

    public AuthService getAuthService() {
        return authService;
    }
}