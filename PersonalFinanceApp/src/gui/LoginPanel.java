package gui;

import service.AuthService;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private AuthService authService;
    private JPasswordField passField;
    private JCheckBox showPasswordCheck;

    public LoginPanel(MainFrame mainFrame, AuthService authService) {
        this.mainFrame = mainFrame;
        this.authService = authService;

        setLayout(new GridBagLayout());
        setBackground(UIHelper.BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Panel login box - ukuran lebih besar
        JPanel loginBox = UIHelper.createCardPanel("Personal Finance Manager", UIHelper.PRIMARY_COLOR);
        loginBox.setPreferredSize(new Dimension(800, 600)); // Diperbesar untuk muat checkbox

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(UIHelper.TITLE_FONT);
        titleLabel.setForeground(UIHelper.DARK_COLOR);

        // Username - FIELD DIPERLEBAR
        JLabel userLabel = new JLabel("Username:");
        UIHelper.setFormLabelStyle(userLabel);
        JTextField userField = new JTextField(); // HAPUS parameter 25, biarkan kosong
        UIHelper.setFormFieldStyle(userField, 300); // Width 300 pixel

        // Password - FIELD DIPERLEBAR
        JLabel passLabel = new JLabel("Password:");
        UIHelper.setFormLabelStyle(passLabel);
        passField = new JPasswordField(); // HAPUS parameter 25, biarkan kosong
        UIHelper.setFormFieldStyle(passField, 300); // Width 300 pixel
        passField.setEchoChar('•'); // Set bullet character

        // Checkbox untuk show password
        showPasswordCheck = new JCheckBox("Tampilkan password");
        UIHelper.setCheckboxStyle(showPasswordCheck);
        showPasswordCheck.setFocusPainted(false);
        showPasswordCheck.addActionListener(e -> togglePasswordVisibility());

        // Buttons - juga diperlebar
        JButton loginButton = new JButton("Masuk");
        UIHelper.setPrimaryButtonStyle(loginButton);
        loginButton.setPreferredSize(new Dimension(250, 40)); // Button lebih besar

        JButton registerButton = new JButton("Daftar Baru");
        UIHelper.setSuccessButtonStyle(registerButton);
        registerButton.setPreferredSize(new Dimension(250, 40));

        JButton guestButton = new JButton("Masuk sebagai Tamu");
        UIHelper.setWarningButtonStyle(guestButton);
        guestButton.setPreferredSize(new Dimension(250, 40));

        // ========== LAYOUT ==========
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 30, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(titleLabel, gbc);

        // Username Label
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 15);
        gbc.anchor = GridBagConstraints.LINE_END;
        contentPanel.add(userLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPanel.add(userField, gbc);

        // Password Label
        gbc.gridx = 0;  // PASTIKAN KEMBALI KE 0
        gbc.gridy = 2;  // ROW BERIKUTNYA
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 15);  // Padding kanan untuk align dengan field
        gbc.anchor = GridBagConstraints.LINE_END;
        contentPanel.add(passLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // PASTIKAN ADA INI
        contentPanel.add(passField, gbc);

        // Checkbox Tampilkan Password
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        contentPanel.add(showPasswordCheck, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(loginButton, gbc);

        // Register Button
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 10, 0);
        contentPanel.add(registerButton, gbc);

        // Guest Button
        gbc.gridy = 6;
        contentPanel.add(guestButton, gbc);

//        // Info Label
//        JLabel infoLabel = new JLabel(
//                "<html><div style='text-align: center;'>" +
//                        "<font size='3'><b>Untuk demo:</b></font><br>" +
//                        "👤 <b>Username:</b> admin<br>" +
//                        "🔑 <b>Password:</b> admin123" +
//                        "</div></html>"
//        );
//        infoLabel.setFont(UIHelper.SMALL_FONT);
//        infoLabel.setForeground(Color.GRAY);
//        gbc.gridy = 7;
//        gbc.insets = new Insets(20, 0, 20, 0);
//        contentPanel.add(infoLabel, gbc);

        // ========== ACTION LISTENERS ==========

        // Login Button Action
        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            // Validasi input
            if (username.isEmpty()) {
                UIHelper.showErrorDialog(this, "Username harus diisi!");
                userField.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                UIHelper.showErrorDialog(this, "Password harus diisi!");
                passField.requestFocus();
                return;
            }

            // Coba login
            if (authService.login(username, password)) {
                UIHelper.showSuccessDialog(this,
                        "Login berhasil!\nSelamat datang, " +
                                authService.getCurrentUser().getNama());
                mainFrame.showDashboard();
            } else {
                UIHelper.showErrorDialog(this,
                        "Login gagal!\nUsername atau password salah.");
                passField.setText("");
                passField.requestFocus();
            }
        });

        // Register Button Action
        registerButton.addActionListener(e -> showRegisterDialog());

        // Guest Button Action
        guestButton.addActionListener(e -> {
            int confirm = UIHelper.showConfirmDialog(this,
                    "Masuk sebagai tamu?\nFitur mungkin terbatas.");

            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.showDashboard();
            }
        });

        // Enter Key Support
        userField.addActionListener(e -> passField.requestFocus());
        passField.addActionListener(e -> loginButton.doClick());

        loginBox.add(contentPanel, BorderLayout.CENTER);
        add(loginBox);
    }

    // Method untuk toggle password visibility
    private void togglePasswordVisibility() {
        if (showPasswordCheck.isSelected()) {
            // Tampilkan karakter asli
            passField.setEchoChar((char) 0);
            showPasswordCheck.setText("Sembunyikan password");
        } else {
            // Kembali ke mode password (bullet)
            passField.setEchoChar('•');
            showPasswordCheck.setText("Tampilkan password");
        }
    }

    // Method untuk dialog registrasi
    private void showRegisterDialog() {
        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JPasswordField confirmPassField = new JPasswordField(20);
        JTextField namaField = new JTextField(20);

        // Panel dengan styling
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.WHITE);

        // Labels dengan styling
        JLabel userLabel = new JLabel("Username:");
        UIHelper.setFormLabelStyle(userLabel);

        JLabel passLabel = new JLabel("Password:");
        UIHelper.setFormLabelStyle(passLabel);

        JLabel confirmLabel = new JLabel("Konfirmasi:");
        UIHelper.setFormLabelStyle(confirmLabel);

        JLabel namaLabel = new JLabel("Nama Lengkap:");
        UIHelper.setFormLabelStyle(namaLabel);

        // Field styling
        UIHelper.setFormFieldStyle(userField, 200);
        UIHelper.setFormFieldStyle(passField, 200);
        passField.setEchoChar('•');

        UIHelper.setFormFieldStyle(confirmPassField, 200);
        confirmPassField.setEchoChar('•');

        UIHelper.setFormFieldStyle(namaField, 200);

        // Tambahkan ke panel
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(confirmLabel);
        panel.add(confirmPassField);
        panel.add(namaLabel);
        panel.add(namaField);

        // Dialog options
        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "📝 Registrasi Pengguna Baru",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());
            String nama = namaField.getText().trim();

            // Validasi
            StringBuilder errors = new StringBuilder();

            if (username.isEmpty()) errors.append("• Username harus diisi\n");
            if (password.isEmpty()) errors.append("• Password harus diisi\n");
            if (nama.isEmpty()) errors.append("• Nama lengkap harus diisi\n");

            if (!password.equals(confirmPass)) {
                errors.append("• Password dan konfirmasi tidak sama\n");
            }

            if (password.length() < 6) {
                errors.append("• Password minimal 6 karakter\n");
            }

            if (errors.length() > 0) {
                UIHelper.showErrorDialog(this, "Kesalahan:\n" + errors.toString());
                return;
            }

            // Coba registrasi
            if (authService.register(username, password, nama)) {
                UIHelper.showSuccessDialog(this,
                        "Registrasi berhasil!\nSilakan login dengan akun baru Anda.");
            } else {
                UIHelper.showErrorDialog(this,
                        "Registrasi gagal!\nUsername sudah digunakan.");
            }
        }
    }

    // Reset form (optional)
    public void resetForm() {
        // Method ini bisa dipanggil saat logout
        if (showPasswordCheck != null) {
            showPasswordCheck.setSelected(false);
            togglePasswordVisibility();
        }
    }
}