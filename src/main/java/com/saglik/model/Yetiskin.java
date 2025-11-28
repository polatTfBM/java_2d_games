package com.saglik.model;

import java.time.LocalDate;

/**
 * Yetişkin kullanıcıyı temsil eder.
 */
public class Yetiskin extends Kullanici {
    private String meslek;
    private double boy;
    private double kilo;
    private boolean sporYapiyor;

    public Yetiskin(String ad, String email, int yas, boolean aktif, LocalDate kayitTarihi, String meslek, double boy, double kilo, boolean sporYapiyor) {
        super(ad, email, yas, aktif, kayitTarihi);
        this.meslek = meslek;
        this.boy = boy;
        this.kilo = kilo;
        this.sporYapiyor = sporYapiyor;
    }

    public Yetiskin() {
        this("Bilinmiyor", "yetiskin@example.com", 30, true, LocalDate.now(), "Mühendis", 1.75, 70, true);
    }

    @Override
    public String getKategori() {
        return "Yetişkin";
    }

    @Override
    public String ozelNot() {
        return "Çalışma temposuna uygun esnek egzersiz planları önerilir.";
    }

    @Override
    public String analizYap() {
        double vki = kilo / (boy * boy);
        String durum = vki < 18.5 ? "Düşük kilolu" : vki > 25 ? "Fazla kilolu" : "İdeal";
        return "VKİ: " + String.format("%.2f", vki) + " - " + durum;
    }

    @Override
    public String getAnalizBasligi() {
        return "Yetişkin Sağlık Analizi";
    }

    @Override
    public String raporOlustur() {
        return String.join(" ", getKategori(), getAd(), "- Meslek:", meslek, "Spor:", String.valueOf(sporYapiyor));
    }

    public String getMeslek() {
        return meslek;
    }

    public void setMeslek(String meslek) {
        this.meslek = meslek;
    }

    public double getBoy() {
        return boy;
    }

    public void setBoy(double boy) {
        this.boy = boy;
    }

    public double getKilo() {
        return kilo;
    }

    public void setKilo(double kilo) {
        this.kilo = kilo;
    }

    public boolean isSporYapiyor() {
        return sporYapiyor;
    }

    public void setSporYapiyor(boolean sporYapiyor) {
        this.sporYapiyor = sporYapiyor;
    }
}
