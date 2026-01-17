package utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UIHelper {

    // === COLOR PALETTE ===
    public static final Color PRIMARY_COLOR = new Color(52, 152, 219);     // Biru muda
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);     // Hijau terang
    public static final Color WARNING_COLOR = new Color(241, 196, 15);     // Kuning
    public static final Color DANGER_COLOR = new Color(231, 76, 60);       // Merah
    public static final Color DARK_COLOR = new Color(44, 62, 80);          // Biru tua
    public static final Color LIGHT_COLOR = new Color(236, 240, 241);      // Abu-abu muda
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250); // Off-white
    public static final Color TEXT_COLOR = new Color(33, 33, 33);          // Hitam keabu-abuan (soft black)

    // === FONTS ===
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font MONOSPACE_FONT = new Font("Consolas", Font.PLAIN, 13);

    // === BORDERS ===
    public static final Border DEFAULT_PADDING = new EmptyBorder(10, 10, 10, 10);
    public static final Border PANEL_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
    );

    // === BUTTON STYLES ===
    public static void setPrimaryButtonStyle(JButton button) {
        setButtonStyle(button, PRIMARY_COLOR, TEXT_COLOR); // Text hitam untuk kontras
    }

    public static void setSuccessButtonStyle(JButton button) {
        setButtonStyle(button, SUCCESS_COLOR, TEXT_COLOR); // Text hitam
    }

    public static void setDangerButtonStyle(JButton button) {
        setButtonStyle(button, DANGER_COLOR, Color.WHITE); // Merah dengan text putih (karena merah gelap)
    }

    public static void setWarningButtonStyle(JButton button) {
        setButtonStyle(button, WARNING_COLOR, TEXT_COLOR); // Text hitam
    }

    // Versi custom untuk dark background
    public static void setDarkButtonStyle(JButton button) {
        setButtonStyle(button, DARK_COLOR, Color.WHITE); // Background gelap, text putih
    }

    public static void setButtonStyle(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(backgroundColor.darker(), 1),
                new EmptyBorder(8, 16, 8, 16)
        ));
        button.setFont(NORMAL_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect - sedikit lebih gelap
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    // === PANEL STYLES ===
    public static JPanel createStyledPanel(String title) {
        return createStyledPanel(title, BACKGROUND_COLOR);
    }

    public static JPanel createStyledPanel(String title, Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        title,
                        0, 0,
                        HEADER_FONT,
                        DARK_COLOR
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }

    public static JPanel createCardPanel(String title, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(HEADER_FONT);
            titleLabel.setForeground(TEXT_COLOR); // Gunakan TEXT_COLOR
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(titleLabel, BorderLayout.NORTH);
        }

        return panel;
    }

    // === FORM STYLES ===
    public static void setFormLabelStyle(JLabel label) {
        label.setFont(NORMAL_FONT);
        label.setForeground(TEXT_COLOR);
    }

    public static void setFormFieldStyle(JTextField field, int width) {
        field.setFont(NORMAL_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_COLOR, 1),
                new EmptyBorder(8, 15, 8, 15)
        ));
        field.setPreferredSize(new Dimension(width, 35)); // Height 35 pixel (dari 300)
    }

    public static void setFormComboStyle(JComboBox<?> comboBox, int width) {
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_COLOR, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
        comboBox.setPreferredSize(new Dimension(width, 30)); // Width bisa disesuaikan
    }

    // === TABLE STYLES ===
    public static void setTableStyle(JTable table) {
        table.setFont(NORMAL_FONT);
        table.setForeground(TEXT_COLOR); // Tambahkan ini
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE); // Tetap putih saat selected

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(DARK_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
    }

    // === DIALOG HELPERS ===
    public static void showSuccessDialog(Component parent, String message) {
        showDialog(parent, "Sukses", message, JOptionPane.INFORMATION_MESSAGE, SUCCESS_COLOR);
    }

    public static void showErrorDialog(Component parent, String message) {
        showDialog(parent, "Error", message, JOptionPane.ERROR_MESSAGE, DANGER_COLOR);
    }

    public static void showWarningDialog(Component parent, String message) {
        showDialog(parent, "Peringatan", message, JOptionPane.WARNING_MESSAGE, WARNING_COLOR);
    }

    public static void showInfoDialog(Component parent, String message) {
        showDialog(parent, "Informasi", message, JOptionPane.INFORMATION_MESSAGE, PRIMARY_COLOR);
    }

    private static void showDialog(Component parent, String title, String message, int messageType, Color color) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);

        JOptionPane.showMessageDialog(
                parent,
                "<html><body style='width: 300px; padding: 10px; color: #212121;'>" + message,
                title,
                messageType
        );
    }

    public static int showConfirmDialog(Component parent, String message) {
        Object[] options = {"Ya", "Tidak"};
        return JOptionPane.showOptionDialog(
                parent,
                "<html><body style='width: 300px; padding: 10px; color: #212121;'>" + message,
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );
    }

    // === MISC HELPERS ===
    public static JLabel createIconLabel(String text, String iconText) {
        JLabel label = new JLabel(iconText + " " + text);
        label.setFont(NORMAL_FONT);
        label.setForeground(TEXT_COLOR); // Tambahkan ini
        return label;
    }

    public static JButton createIconButton(String text, String iconText) {
        JButton button = new JButton( iconText + " " + text);
        setPrimaryButtonStyle(button);
        return button;
    }

    public static void centerWindow(Window window) {
        window.setLocationRelativeTo(null);
    }

    public static void setApplicationIcon(JFrame frame) {
        // Anda bisa tambahkan icon aplikasi di sini
        // frame.setIconImage(new ImageIcon("icon.png").getImage());
    }

    // === FORM LAYOUT HELPERS ===
    public static GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    public static GridBagConstraints createGridBagConstraints(int gridx, int gridy) {
        GridBagConstraints gbc = createGridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        return gbc;
    }

    public static JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(PANEL_BORDER);
        return panel;
    }

    // === TEXT AREA STYLES ===
    public static void setTextAreaStyle(JTextArea textArea) {
        textArea.setFont(MONOSPACE_FONT);
        textArea.setForeground(TEXT_COLOR); // Tambahkan ini
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(248, 248, 248));
    }

    // === PROGRESS BAR STYLE ===
    public static void setProgressBarStyle(JProgressBar progressBar) {
        progressBar.setStringPainted(true);
        progressBar.setFont(SMALL_FONT);
        progressBar.setForeground(PRIMARY_COLOR);
        progressBar.setBackground(Color.WHITE);
        progressBar.setString("Memproses...");
    }

    // === LOADING DIALOG ===
    public static JDialog createLoadingDialog(JFrame parent, String message) {
        JDialog dialog = new JDialog(parent, "Memproses", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        UIHelper.centerWindow(dialog);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(NORMAL_FONT);
        label.setForeground(TEXT_COLOR); // Tambahkan ini

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        setProgressBarStyle(progressBar);

        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setResizable(false);

        return dialog;
    }

    // === NEW: UTILITY UNTUK KONTRAST CHECK ===
    public static boolean isDarkColor(Color color) {
        // Hitung brightness berdasarkan formula WCAG
        double brightness = (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
        return brightness < 128; // Jika < 128, warna tergolong gelap
    }

    public static Color getContrastTextColor(Color backgroundColor) {
        // Return putih jika background gelap, hitam jika background terang
        return isDarkColor(backgroundColor) ? Color.WHITE : TEXT_COLOR;
    }

    public static void setCheckboxStyle(JCheckBox checkbox) {
        checkbox.setFont(SMALL_FONT);
        checkbox.setForeground(DARK_COLOR);
        checkbox.setBackground(Color.WHITE);
        checkbox.setFocusPainted(false);
        checkbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}