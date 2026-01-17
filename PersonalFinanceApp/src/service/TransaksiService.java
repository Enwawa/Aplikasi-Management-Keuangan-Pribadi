package service;

import model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransaksiService {
    private final List<Transaksi> transaksiList;
    private final Saldo saldo;

    public TransaksiService(double saldoAwal) {
        this.transaksiList = new ArrayList<>();
        this.saldo = new Saldo(saldoAwal);
    }

    public void tambahPemasukan(Date tanggal, String kategori, double nominal, String keterangan) {
        Pemasukan pemasukan = new Pemasukan(tanggal, kategori, nominal, keterangan);
        transaksiList.add(pemasukan);
        saldo.updateSaldo(pemasukan);
    }

    public void tambahPengeluaran(Date tanggal, String kategori, double nominal, String keterangan) {
        Pengeluaran pengeluaran = new Pengeluaran(tanggal, kategori, nominal, keterangan);
        transaksiList.add(pengeluaran);
        saldo.updateSaldo(pengeluaran);
    }

    public void hapusTransaksi(int index) {
        if (index >= 0 && index < transaksiList.size()) {
            Transaksi transaksi = transaksiList.get(index);

            if (transaksi instanceof Pemasukan) {
                saldo.kurangiSaldo(transaksi.getNominal());
            } else {
                saldo.tambahSaldo(transaksi.getNominal());
            }

            transaksiList.remove(index);
        }
    }

    public double hitungTotalPemasukan() {
        double total = 0;
        for (Transaksi t : transaksiList) {
            if (t instanceof Pemasukan) {
                total += t.getNominal();
            }
        }
        return total;
    }

    public double hitungTotalPengeluaran() {
        double total = 0;
        for (Transaksi t : transaksiList) {
            if (t instanceof Pengeluaran) {
                total += t.getNominal();
            }
        }
        return total;
    }

    public List<Transaksi> getAllTransaksi() {
        return new ArrayList<>(transaksiList);
    }

    public Saldo getSaldo() {
        return saldo;
    }
}