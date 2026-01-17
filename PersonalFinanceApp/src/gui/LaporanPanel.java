package gui;

import model.Pemasukan;
import model.Transaksi;
import service.TransaksiService;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LaporanPanel extends JPanel {
    private final TransaksiService transaksiService;
    private final JTextArea reportArea;

    public LaporanPanel(TransaksiService transaksiService) {
        this.transaksiService = transaksiService;
        setLayout(new BorderLayout(10, 10));

        // Panel kontrol
        add(createControlPanel(), BorderLayout.NORTH);

        // Area laporan
        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Laporan"));

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton harianButton = new JButton("Laporan Harian");
        harianButton.addActionListener(e -> generateLaporanHarian());

        JButton bulananButton = new JButton("Laporan Bulanan");
        bulananButton.addActionListener(e -> generateLaporanBulanan());

        JButton ringkasanButton = new JButton("Ringkasan");
        ringkasanButton.addActionListener(e -> generateRingkasan());

        panel.add(harianButton);
        panel.add(bulananButton);
        panel.add(ringkasanButton);

        return panel;
    }

    private void generateLaporanHarian() {
        String dateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        StringBuilder sb = new StringBuilder();
        sb.append("LAPORAN HARIAN - ").append(dateStr).append("\n");
        sb.append("=".repeat(60)).append("\n\n");

        double totalPemasukan = 0;
        double totalPengeluaran = 0;
        boolean adaTransaksi = false;

        for (Transaksi t : transaksiService.getAllTransaksi()) {
            if (t.getFormattedTanggal().equals(dateStr)) {
                adaTransaksi = true;
                String jenis = (t instanceof Pemasukan) ? "[+]" : "[-]";
                sb.append(String.format("%s %-15s %-20s Rp%,12.2f\n",
                        jenis, t.getKategori(), t.getKeterangan(), t.getNominal()));

                if (t instanceof Pemasukan) {
                    totalPemasukan += t.getNominal();
                } else {
                    totalPengeluaran += t.getNominal();
                }
            }
        }

        if (!adaTransaksi) {
            sb.append("Tidak ada transaksi hari ini.\n");
        }

        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append(String.format("Total Pemasukan:  Rp%,.2f\n", totalPemasukan));
        sb.append(String.format("Total Pengeluaran: Rp%,.2f\n", totalPengeluaran));
        sb.append(String.format("Saldo Harian:     Rp%,.2f\n", totalPemasukan - totalPengeluaran));

        reportArea.setText(sb.toString());
    }

    private void generateLaporanBulanan() {
        // Implementasi laporan bulanan
        reportArea.setText("Laporan Bulanan - Fitur dalam pengembangan");
    }

    private void generateRingkasan() {
        double totalPemasukan = transaksiService.hitungTotalPemasukan();
        double totalPengeluaran = transaksiService.hitungTotalPengeluaran();
        double saldo = transaksiService.getSaldo().getSaldoSekarang();

        StringBuilder sb = new StringBuilder();
        sb.append("RINGKASAN KEUANGAN\n");
        sb.append("=".repeat(60)).append("\n\n");
        sb.append(String.format("Total Transaksi:  %d\n",
                transaksiService.getAllTransaksi().size()));
        sb.append(String.format("Total Pemasukan:  Rp%,.2f\n", totalPemasukan));
        sb.append(String.format("Total Pengeluaran: Rp%,.2f\n", totalPengeluaran));
        sb.append(String.format("Saldo Akhir:      Rp%,.2f\n", saldo));
        sb.append(String.format("Selisih:          Rp%,.2f\n",
                totalPemasukan - totalPengeluaran));

        if (totalPemasukan > totalPengeluaran) {
            sb.append("\nStatus: HEMAT\n");
        } else {
            sb.append("\nStatus: BOROS\n");
        }

        reportArea.setText(sb.toString());
    }
}