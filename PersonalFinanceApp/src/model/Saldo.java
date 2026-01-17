package model;

public class Saldo {
    private double saldoAwal;
    private double saldoSekarang;

    public Saldo(double saldoAwal) {
        this.saldoAwal = saldoAwal;
        this.saldoSekarang = saldoAwal;
    }

    public void updateSaldo(Transaksi transaksi) {
        saldoSekarang = transaksi.hitungSaldo(saldoSekarang);
    }

    public void tambahSaldo(double jumlah) {
        saldoSekarang += jumlah;
    }

    public void kurangiSaldo(double jumlah) {
        if (saldoSekarang >= jumlah) {
            saldoSekarang -= jumlah;
        }
    }

    public double getSaldoAwal() { return saldoAwal; }
    public double getSaldoSekarang() { return saldoSekarang; }

    public void setSaldoAwal(double saldoAwal) {
        this.saldoAwal = saldoAwal;
        this.saldoSekarang = saldoAwal;
    }
}