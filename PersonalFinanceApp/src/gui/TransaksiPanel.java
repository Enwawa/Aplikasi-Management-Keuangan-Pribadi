package gui;

import utils.UIHelper;
import model.*;
import service.TransaksiService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransaksiPanel extends JPanel {
    private TransaksiService transaksiService;
    private DashboardPanel dashboardPanel;
    private JTable transaksiTable;
    private DefaultTableModel tableModel;

    public TransaksiPanel(TransaksiService transaksiService, DashboardPanel dashboardPanel) {
        this.transaksiService = transaksiService;
        this.dashboardPanel = dashboardPanel;

        setLayout(new BorderLayout(10, 10));

        // Panel input (atas)
        add(createInputPanel(), BorderLayout.NORTH);

        // Panel tabel (tengah)
        add(createTablePanel(), BorderLayout.CENTER);

        // Panel aksi (bawah)
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = UIHelper.createFormPanel();

        GridBagConstraints gbc = UIHelper.createGridBagConstraints();

        panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Tambah Transaksi Baru"));

        // Komponen form
        JLabel jenisLabel = new JLabel("Jenis Transaksi:");
        JComboBox<String> jenisCombo = new JComboBox<>(new String[]{"Pemasukan", "Pengeluaran"});

        JLabel tanggalLabel = new JLabel("Tanggal:");
        JTextField tanggalField = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        JLabel kategoriLabel = new JLabel("Kategori:");
        JComboBox<String> kategoriCombo = new JComboBox<>();
        for (String kategori : Kategori.getKategoriDefault()) {
            kategoriCombo.addItem(kategori);
        }

        JLabel nominalLabel = new JLabel("Nominal (Rp):");
        JTextField nominalField = new JTextField();

        JLabel keteranganLabel = new JLabel("Keterangan:");
        JTextField keteranganField = new JTextField();

        JButton tambahButton = new JButton("➕ Tambah");
        tambahButton.addActionListener(e -> tambahTransaksi(
                jenisCombo, tanggalField, kategoriCombo, nominalField, keteranganField
        ));

        // Tambahkan ke panel
        panel.add(jenisLabel); panel.add(jenisCombo);
        panel.add(tanggalLabel); panel.add(tanggalField);
        panel.add(kategoriLabel); panel.add(kategoriCombo);
        panel.add(nominalLabel); panel.add(nominalField);
        panel.add(keteranganLabel); panel.add(keteranganField);
        panel.add(new JLabel()); panel.add(tambahButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabel
        String[] columns = {"No", "Tanggal", "Jenis", "Kategori", "Nominal", "Keterangan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transaksiTable = new JTable(tableModel);
        transaksiTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(transaksiTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshTable();

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.addActionListener(e -> refreshTable());

        JButton hapusButton = new JButton("🗑️ Hapus Terpilih");
        hapusButton.addActionListener(e -> hapusTransaksiTerpilih());

        panel.add(refreshButton);
        panel.add(hapusButton);

        return panel;
    }

    private void tambahTransaksi(JComboBox<String> jenisCombo, JTextField tanggalField,
                                 JComboBox<String> kategoriCombo, JTextField nominalField,
                                 JTextField keteranganField) {
        try {
            // Validasi
            if (nominalField.getText().trim().isEmpty()) {
                throw new InputException("Nominal harus diisi");
            }

            double nominal = Double.parseDouble(nominalField.getText());
            if (nominal <= 0) {
                throw new InputException("Nominal harus lebih dari 0");
            }

            // Parse data
            String jenis = (String) jenisCombo.getSelectedItem();
            Date tanggal = new SimpleDateFormat("dd-MM-yyyy").parse(tanggalField.getText());
            String kategori = (String) kategoriCombo.getSelectedItem();
            String keterangan = keteranganField.getText();

            // Tambah ke service
            if (jenis.equals("Pemasukan")) {
                transaksiService.tambahPemasukan(tanggal, kategori, nominal, keterangan);
            } else {
                transaksiService.tambahPengeluaran(tanggal, kategori, nominal, keterangan);
            }

            // Refresh tampilan
            refreshTable();
            dashboardPanel.updateSaldoDisplay();

            // Clear form
            nominalField.setText("");
            keteranganField.setText("");

            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusTransaksiTerpilih() {
        int selectedRow = transaksiTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus transaksi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                transaksiService.hapusTransaksi(selectedRow);
                refreshTable();
                dashboardPanel.updateSaldoDisplay();
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus!");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        int i = 1;
        for (Transaksi t : transaksiService.getAllTransaksi()) {
            String jenis = (t instanceof Pemasukan) ? "Pemasukan" : "Pengeluaran";
            tableModel.addRow(new Object[]{
                    i++,
                    t.getFormattedTanggal(),
                    jenis,
                    t.getKategori(),
                    String.format("Rp%,.2f", t.getNominal()),
                    t.getKeterangan()
            });
        }
    }
}