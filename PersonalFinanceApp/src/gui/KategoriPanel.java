package gui;

import model.Kategori;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class KategoriPanel extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> kategoriList;

    public KategoriPanel() {
        setLayout(new BorderLayout(10, 10));

        // Panel kiri: daftar kategori
        add(createListPanel(), BorderLayout.WEST);

        // Panel kanan: form tambah kategori
        add(createFormPanel(), BorderLayout.CENTER);
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Kategori"));
        panel.setPreferredSize(new Dimension(200, 400));

        listModel = new DefaultListModel<>();
        refreshList();

        kategoriList = new JList<>(listModel);
        kategoriList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(kategoriList);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Kelola Kategori"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input kategori baru
        JLabel label = new JLabel("Nama Kategori Baru:");
        JTextField kategoriField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(kategoriField, gbc);

        // Tombol
        JButton tambahButton = new JButton("Tambah");
        tambahButton.addActionListener(e -> {
            String nama = kategoriField.getText().trim();
            if (!nama.isEmpty()) {
                Kategori.addKategori(nama);
                refreshList();
                kategoriField.setText("");
                JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!");
            }
        });

        JButton hapusButton = getJButton();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshList());

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(tambahButton, gbc);

        gbc.gridy = 2;
        panel.add(hapusButton, gbc);

        gbc.gridy = 3;
        panel.add(refreshButton, gbc);

        return panel;
    }

    private JButton getJButton() {
        JButton hapusButton = new JButton("Hapus Terpilih");
        hapusButton.addActionListener(e -> {
            String selected = kategoriList.getSelectedValue();
            if (selected != null && !isDefaultCategory(selected)) {
                Kategori.removeKategori(selected);
                refreshList();
                JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus!");
            } else if (isDefaultCategory(Objects.requireNonNull(selected))) {
                JOptionPane.showMessageDialog(this,
                        "Kategori default tidak bisa dihapus!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return hapusButton;
    }

    private void refreshList() {
        listModel.clear();
        List<String> kategori = Kategori.getKategoriDefault();
        for (String k : kategori) {
            listModel.addElement(k);
        }
    }

    private boolean isDefaultCategory(String kategori) {
        return kategori.equals("Makan") || kategori.equals("Transport") ||
                kategori.equals("Pendidikan") || kategori.equals("Hiburan") ||
                kategori.equals("Lainnya");
    }
}