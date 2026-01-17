package model;

import java.util.Date;

public class Pengeluaran extends Transaksi {
    public Pengeluaran(Date tanggal, String kategori, double nominal, String keterangan) {
        super(tanggal, kategori, nominal, keterangan);
    }

    @Override
    public double hitungSaldo(double saldoSekarang) {
        return saldoSekarang - nominal;
    }

    @Override
    public String toString() {
        return "PENGELUARAN;" + super.toString();
    }
}