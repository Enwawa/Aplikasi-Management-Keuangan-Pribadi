package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Transaksi {
    protected Date tanggal;
    protected String kategori;
    protected double nominal;
    protected String keterangan;

    public Transaksi(Date tanggal, String kategori, double nominal, String keterangan) {
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.nominal = nominal;
        this.keterangan = keterangan;
    }

    // Abstract method untuk polymorphism
    public abstract double hitungSaldo(double saldoSekarang);

    // Getter dan Setter
    public Date getTanggal() { return tanggal; }

    public String getFormattedTanggal() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(tanggal);
    }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public double getNominal() { return nominal; }
    public void setNominal(double nominal) { this.nominal = nominal; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    @Override
    public String toString() {
        return getFormattedTanggal() + ";" + kategori + ";" + nominal + ";" + keterangan;
    }
}