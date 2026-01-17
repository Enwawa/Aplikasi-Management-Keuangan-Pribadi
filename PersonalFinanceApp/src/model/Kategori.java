package model;

import java.util.ArrayList;
import java.util.List;

public class Kategori {
    private static List<String> kategoriList = new ArrayList<>();

    static {
        kategoriList.add("Makan");
        kategoriList.add("Transport");
        kategoriList.add("Pendidikan");
        kategoriList.add("Hiburan");
        kategoriList.add("Lainnya");
    }

    public static List<String> getKategoriDefault() {
        return new ArrayList<>(kategoriList);
    }

    public static void addKategori(String kategori) {
        kategoriList.add(kategori);
    }

    public static boolean removeKategori(String kategori) {
        return kategoriList.remove(kategori);
    }
}