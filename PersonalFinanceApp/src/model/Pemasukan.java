package model;

import java.util.Date;

public class Pemasukan extends Transaksi {
    public Pemasukan(Date tanggal, String kategori, double nominal, String keterangan) {
        super(tanggal, kategori, nominal, keterangan);
    }

    @Override
    public double hitungSaldo(double saldoSekarang) {
        return saldoSekarang + nominal;
    }

    @Override
    public String toString() {
        return "PEMASUKAN;" + super.toString();
    }
}