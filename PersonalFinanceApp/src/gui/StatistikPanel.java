package gui;

import model.*;
import service.TransaksiService;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistikPanel extends JPanel {
    private TransaksiService transaksiService;
    private JPanel chartPanel;
    private JTextArea statsTextArea;

    public StatistikPanel(TransaksiService transaksiService) {
        this.transaksiService = transaksiService;
        setLayout(new BorderLayout(10, 10));

        // Panel atas: Chart visual
        add(createChartPanel(), BorderLayout.CENTER);

        // Panel bawah: Detail statistik
        add(createStatsPanel(), BorderLayout.SOUTH);

        // Panel kanan: Control buttons
        add(createControlPanel(), BorderLayout.EAST);

        // Load data awal
        updateStatistics();
    }

    private JPanel createChartPanel() {
        JPanel panel = UIHelper.createStyledPanel("Visualisasi Statistik");
        panel.setLayout(new GridLayout(2, 2, 15, 15));

        // Card 1: Total Pemasukan vs Pengeluaran
        panel.add(createComparisonCard());

        // Card 2: Pengeluaran per Kategori
        panel.add(createCategoryCard());

        // Card 3: Saldo Trend
        panel.add(createSaldoCard());

        // Card 4: Ringkasan
        panel.add(createSummaryCard());

        chartPanel = panel;
        return panel;
    }

    private JPanel createComparisonCard() {
        JPanel card = UIHelper.createCardPanel("Pemasukan vs Pengeluaran", UIHelper.PRIMARY_COLOR);
        card.setLayout(new BorderLayout());

        // Simple bar chart menggunakan JLabels
        JPanel barPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        barPanel.setBackground(Color.WHITE);

        double pemasukan = transaksiService.hitungTotalPemasukan();
        double pengeluaran = transaksiService.hitungTotalPengeluaran();
        double max = Math.max(pemasukan, pengeluaran);

        // Bar Pemasukan
        JPanel pemasukanBar = createBar("Pemasukan", pemasukan, max, UIHelper.SUCCESS_COLOR);
        barPanel.add(pemasukanBar);

        // Bar Pengeluaran
        JPanel pengeluaranBar = createBar("Pengeluaran", pengeluaran, max, UIHelper.DANGER_COLOR);
        barPanel.add(pengeluaranBar);

        card.add(barPanel, BorderLayout.CENTER);

        // Info text
        DecimalFormat df = new DecimalFormat("#,##0.00");
        JLabel infoLabel = new JLabel(
                "<html><center>Pemasukan: Rp" + df.format(pemasukan) +
                        "<br>Pengeluaran: Rp" + df.format(pengeluaran) +
                        "<br>Selisih: Rp" + df.format(pemasukan - pengeluaran) + "</center></html>",
                SwingConstants.CENTER
        );
        infoLabel.setFont(UIHelper.SMALL_FONT);
        card.add(infoLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCategoryCard() {
        JPanel card = UIHelper.createCardPanel("Pengeluaran per Kategori", UIHelper.WARNING_COLOR);
        card.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(UIHelper.MONOSPACE_FONT);
        textArea.setBackground(Color.WHITE);

        // Hitung pengeluaran per kategori
        Map<String, Double> kategoriMap = new HashMap<>();
        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();

        for (Transaksi t : transaksiList) {
            if (t instanceof Pengeluaran) {
                String kategori = t.getKategori();
                double nominal = t.getNominal();
                kategoriMap.put(kategori, kategoriMap.getOrDefault(kategori, 0.0) + nominal);
            }
        }

        // Tampilkan dalam text area
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        if (kategoriMap.isEmpty()) {
            sb.append("Belum ada data pengeluaran");
        } else {
            for (Map.Entry<String, Double> entry : kategoriMap.entrySet()) {
                sb.append(String.format("%-15s Rp%15s\n",
                        entry.getKey(),
                        df.format(entry.getValue())));
            }
        }

        textArea.setText(sb.toString());
        card.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return card;
    }

    private JPanel createSaldoCard() {
        JPanel card = UIHelper.createCardPanel("Trend Saldo", UIHelper.SUCCESS_COLOR);
        card.setLayout(new BorderLayout());

        double saldoAwal = transaksiService.getSaldo().getSaldoAwal();
        double saldoSekarang = transaksiService.getSaldo().getSaldoSekarang();
        double perubahan = saldoSekarang - saldoAwal;

        JPanel saldoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        saldoPanel.setBackground(Color.WHITE);

        DecimalFormat df = new DecimalFormat("#,##0.00");

        saldoPanel.add(createInfoLabel("Saldo Awal", saldoAwal));
        saldoPanel.add(createInfoLabel("Saldo Sekarang", saldoSekarang));
        saldoPanel.add(createInfoLabel("Perubahan", perubahan));

        card.add(saldoPanel, BorderLayout.CENTER);

        // Status
        JLabel statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(UIHelper.NORMAL_FONT);

        if (perubahan > 0) {
            statusLabel.setText("MENINGKAT");
            statusLabel.setForeground(UIHelper.SUCCESS_COLOR);
        } else if (perubahan < 0) {
            statusLabel.setText("MENURUN");
            statusLabel.setForeground(UIHelper.DANGER_COLOR);
        } else {
            statusLabel.setText("STABIL");
            statusLabel.setForeground(UIHelper.WARNING_COLOR);
        }

        card.add(statusLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createSummaryCard() {
        JPanel card = UIHelper.createCardPanel("Ringkasan", UIHelper.DARK_COLOR);
        card.setLayout(new BorderLayout());

        JPanel summaryPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        summaryPanel.setBackground(Color.WHITE);

        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();
        int totalTransaksi = transaksiList.size();
        double pemasukan = transaksiService.hitungTotalPemasukan();
        double pengeluaran = transaksiService.hitungTotalPengeluaran();
        double rataPemasukan = totalTransaksi > 0 ? pemasukan / countPemasukan(transaksiList) : 0;
        double rataPengeluaran = totalTransaksi > 0 ? pengeluaran / countPengeluaran(transaksiList) : 0;

        summaryPanel.add(createInfoLabel("Total Transaksi", totalTransaksi));
        summaryPanel.add(createInfoLabel("Rata² Pemasukan", rataPemasukan));
        summaryPanel.add(createInfoLabel("Rata² Pengeluaran", rataPengeluaran));
        summaryPanel.add(createInfoLabel("Transaksi/Hari", calculateAveragePerDay(transaksiList)));

        card.add(summaryPanel, BorderLayout.CENTER);

        // Kategori paling boros
        String kategoriBoros = getMostExpensiveCategory();
        JLabel borosLabel = new JLabel("Kategori Boros: " + kategoriBoros);
        borosLabel.setFont(UIHelper.SMALL_FONT);
        borosLabel.setHorizontalAlignment(SwingConstants.CENTER);
        borosLabel.setForeground(UIHelper.DANGER_COLOR);
        card.add(borosLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Detail Statistik"));

        statsTextArea = new JTextArea(8, 50);
        UIHelper.setTextAreaStyle(statsTextArea);

        JScrollPane scrollPane = new JScrollPane(statsTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(new TitledBorder("Kontrol"));
        panel.setPreferredSize(new Dimension(150, 0));

        JButton refreshBtn = new JButton("Refresh");
        UIHelper.setPrimaryButtonStyle(refreshBtn);
        refreshBtn.addActionListener(e -> updateStatistics());

        JButton exportBtn = new JButton("Export Data");
        UIHelper.setSuccessButtonStyle(exportBtn);
        exportBtn.addActionListener(e -> exportStatistics());

        JButton printBtn = new JButton("Cetak");
        UIHelper.setWarningButtonStyle(printBtn);
        printBtn.addActionListener(e -> printStatistics());

        JButton helpBtn = new JButton("Bantuan");
        UIHelper.setPrimaryButtonStyle(helpBtn);
        helpBtn.addActionListener(e -> showHelp());

        panel.add(refreshBtn);
        panel.add(exportBtn);
        panel.add(printBtn);
        panel.add(helpBtn);

        return panel;
    }

    // ========== HELPER METHODS ==========

    private JPanel createBar(String label, double value, double maxValue, Color color) {
        JPanel barPanel = new JPanel(new BorderLayout());
        barPanel.setBackground(Color.WHITE);

        // Bar container
        JPanel barContainer = new JPanel(new BorderLayout());
        barContainer.setBackground(Color.LIGHT_GRAY);
        barContainer.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Actual bar (height proportional to value)
        int barHeight = maxValue > 0 ? (int)((value / maxValue) * 100) : 0;
        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(50, Math.max(10, barHeight)));

        barContainer.add(bar, BorderLayout.SOUTH);

        // Label
        DecimalFormat df = new DecimalFormat("#,##0.00");
        JLabel valueLabel = new JLabel("Rp" + df.format(value));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(UIHelper.SMALL_FONT);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(UIHelper.SMALL_FONT);

        barPanel.add(nameLabel, BorderLayout.NORTH);
        barPanel.add(barContainer, BorderLayout.CENTER);
        barPanel.add(valueLabel, BorderLayout.SOUTH);

        return barPanel;
    }

    private JLabel createInfoLabel(String label, double value) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        JLabel infoLabel = new JLabel(
                "<html><b>" + label + ":</b> Rp" + df.format(value) + "</html>"
        );
        infoLabel.setFont(UIHelper.SMALL_FONT);
        return infoLabel;
    }

    private JLabel createInfoLabel(String label, int value) {
        JLabel infoLabel = new JLabel(
                "<html><b>" + label + ":</b> " + value + "</html>"
        );
        infoLabel.setFont(UIHelper.SMALL_FONT);
        return infoLabel;
    }

    private int countPemasukan(List<Transaksi> transaksiList) {
        int count = 0;
        for (Transaksi t : transaksiList) {
            if (t instanceof Pemasukan) count++;
        }
        return count;
    }

    private int countPengeluaran(List<Transaksi> transaksiList) {
        int count = 0;
        for (Transaksi t : transaksiList) {
            if (t instanceof Pengeluaran) count++;
        }
        return count;
    }

    private double calculateAveragePerDay(List<Transaksi> transaksiList) {
        if (transaksiList.isEmpty()) return 0;

        // Hitung rentang tanggal
        // Sederhana: bagi jumlah transaksi dengan 30 (anggap 1 bulan)
        return transaksiList.size() / 30.0;
    }

    private String getMostExpensiveCategory() {
        Map<String, Double> kategoriMap = new HashMap<>();
        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();

        for (Transaksi t : transaksiList) {
            if (t instanceof Pengeluaran) {
                String kategori = t.getKategori();
                kategoriMap.put(kategori, kategoriMap.getOrDefault(kategori, 0.0) + t.getNominal());
            }
        }

        if (kategoriMap.isEmpty()) {
            return "Belum ada data";
        }

        String mostExpensive = "";
        double maxAmount = 0;

        for (Map.Entry<String, Double> entry : kategoriMap.entrySet()) {
            if (entry.getValue() > maxAmount) {
                maxAmount = entry.getValue();
                mostExpensive = entry.getKey();
            }
        }

        return mostExpensive + " (Rp" + new DecimalFormat("#,##0.00").format(maxAmount) + ")";
    }

    private void updateStatistics() {
        // Refresh semua card
        chartPanel.removeAll();
        chartPanel.add(createComparisonCard());
        chartPanel.add(createCategoryCard());
        chartPanel.add(createSaldoCard());
        chartPanel.add(createSummaryCard());

        // Refresh text area
        updateStatsTextArea();

        // Revalidate dan repaint
        chartPanel.revalidate();
        chartPanel.repaint();

        UIHelper.showSuccessDialog(this, "Statistik diperbarui!");
    }

    private void updateStatsTextArea() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();

        sb.append("=== STATISTIK KEUANGAN ===\n\n");
        sb.append("Total Transaksi: ").append(transaksiList.size()).append("\n");
        sb.append("Total Pemasukan: Rp").append(df.format(transaksiService.hitungTotalPemasukan())).append("\n");
        sb.append("Total Pengeluaran: Rp").append(df.format(transaksiService.hitungTotalPengeluaran())).append("\n");
        sb.append("Saldo Sekarang: Rp").append(df.format(transaksiService.getSaldo().getSaldoSekarang())).append("\n\n");

        sb.append("=== RATA-RATA ===\n");
        int pemasukanCount = countPemasukan(transaksiList);
        int pengeluaranCount = countPengeluaran(transaksiList);

        if (pemasukanCount > 0) {
            sb.append("Rata-rata Pemasukan: Rp")
                    .append(df.format(transaksiService.hitungTotalPemasukan() / pemasukanCount))
                    .append("\n");
        }

        if (pengeluaranCount > 0) {
            sb.append("Rata-rata Pengeluaran: Rp")
                    .append(df.format(transaksiService.hitungTotalPengeluaran() / pengeluaranCount))
                    .append("\n");
        }

        sb.append("\n=== KATEGORI PALING BOROS ===\n");
        sb.append(getMostExpensiveCategory()).append("\n");

        statsTextArea.setText(sb.toString());
    }

    private void exportStatistics() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Statistik");
        fileChooser.setSelectedFile(new java.io.File("statistik_keuangan.txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                writer.write(statsTextArea.getText());
                UIHelper.showSuccessDialog(this, "Statistik berhasil diexport ke:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                UIHelper.showErrorDialog(this, "Error export: " + e.getMessage());
            }
        }
    }

    private void printStatistics() {
        try {
            statsTextArea.print();
            UIHelper.showSuccessDialog(this, "Statistik berhasil dikirim ke printer!");
        } catch (Exception e) {
            UIHelper.showErrorDialog(this, "Error printing: " + e.getMessage());
        }
    }

    private void showHelp() {
        String helpText =
                "📊 BANTUAN STATISTIK\n\n" +
                        "1. Refresh: Memperbarui data statistik\n" +
                        "2. Export: Simpan statistik ke file teks\n" +
                        "3. Cetak: Kirim statistik ke printer\n" +
                        "4. Kartu Statistik:\n" +
                        "   - Perbandingan pemasukan/pengeluaran\n" +
                        "   - Pengeluaran per kategori\n" +
                        "   - Trend saldo dari waktu ke waktu\n" +
                        "   - Ringkasan lengkap\n\n" +
                        "Statistik diperbarui otomatis saat ada transaksi baru.";

        JTextArea helpArea = new JTextArea(helpText);
        helpArea.setEditable(false);
        helpArea.setFont(UIHelper.MONOSPACE_FONT);
        UIHelper.setTextAreaStyle(helpArea);

        JScrollPane scrollPane = new JScrollPane(helpArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Bantuan Statistik", JOptionPane.INFORMATION_MESSAGE);
    }
}