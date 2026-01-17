package gui;

import service.AuthService;
import service.TransaksiService;
import service.FileManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private TransaksiService transaksiService;
    private JLabel saldoLabel;

    public DashboardPanel(MainFrame mainFrame, TransaksiService transaksiService, AuthService authService) {
        this.mainFrame = mainFrame;
        this.transaksiService = transaksiService;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabbedPane = getJTabbedPane(transaksiService);

        add(tabbedPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JTabbedPane getJTabbedPane(TransaksiService transaksiService) {
        JTabbedPane tabbedPane = new JTabbedPane();

        TransaksiPanel transaksiPanel = new TransaksiPanel(transaksiService, this);
        LaporanPanel laporanPanel = new LaporanPanel(transaksiService);
        StatistikPanel statistikPanel = new StatistikPanel(transaksiService);
        KategoriPanel kategoriPanel = new KategoriPanel();

        tabbedPane.addTab("Transaksi", transaksiPanel);
        tabbedPane.addTab("Laporan", laporanPanel);
        tabbedPane.addTab("Statistik", statistikPanel);
        tabbedPane.addTab("Kategori", kategoriPanel);
        return tabbedPane;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 152, 219));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Personal Finance Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);

        saldoLabel = new JLabel();
        updateSaldoDisplay();
        saldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        saldoLabel.setForeground(Color.BLACK);
        saldoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(saldoLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEtchedBorder());

        JButton saveButton = new JButton("💾 Simpan Data");
        saveButton.addActionListener(e -> {
            FileManager.saveData(transaksiService);
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        });

        JButton logoutButton = new JButton("🚪 Keluar");
        logoutButton.addActionListener(e -> System.exit(0));

        panel.add(saveButton);
        panel.add(logoutButton);

        return panel;
    }

    public void updateSaldoDisplay() {
        double saldo = transaksiService.getSaldo().getSaldoSekarang();
        saldoLabel.setText(String.format("Saldo: Rp%,.2f", saldo));
    }
}