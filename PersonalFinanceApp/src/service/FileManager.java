package service;

import model.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "data_keuangan.txt";

    public static void saveData(TransaksiService transaksiService) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println("SALDO_AWAL:" + transaksiService.getSaldo().getSaldoAwal());

            for (Transaksi t : transaksiService.getAllTransaksi()) {
                writer.println(t.toString());
            }

            System.out.println("Data saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    public static void loadData(TransaksiService transaksiService) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No data file found");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            List<Transaksi> loadedTransaksi = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("SALDO_AWAL:")) {
                    double saldoAwal = Double.parseDouble(line.substring(11));
                    transaksiService.getSaldo().setSaldoAwal(saldoAwal);
                } else if (line.startsWith("PEMASUKAN;")) {
                    String[] parts = line.split(";");
                    if (parts.length >= 5) {
                        Date tanggal = new SimpleDateFormat("dd-MM-yyyy").parse(parts[1]);
                        double nominal = Double.parseDouble(parts[3]);
                        loadedTransaksi.add(new Pemasukan(tanggal, parts[2], nominal, parts[4]));
                    }
                } else if (line.startsWith("PENGELUARAN;")) {
                    String[] parts = line.split(";");
                    if (parts.length >= 5) {
                        Date tanggal = new SimpleDateFormat("dd-MM-yyyy").parse(parts[1]);
                        double nominal = Double.parseDouble(parts[3]);
                        loadedTransaksi.add(new Pengeluaran(tanggal, parts[2], nominal, parts[4]));
                    }
                }
            }

            // Set transaksi ke service
            transaksiService.getAllTransaksi().clear();
            transaksiService.getAllTransaksi().addAll(loadedTransaksi);

        } catch (Exception e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }
}