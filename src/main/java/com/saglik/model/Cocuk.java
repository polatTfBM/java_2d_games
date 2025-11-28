package com.saglik.model;

import com.saglik.exception.DataValidationException;

import java.time.LocalDate;

/**
 * Çocuk kullanıcıyı temsil eder.
 */
public class Cocuk extends Kullanici {
    private String ebeveynAdi;
    private double boy;
    private double kilo;
    private char cinsiyet;

    public Cocuk(String ad, String email, int yas, boolean aktif, LocalDate kayitTarihi, String ebeveynAdi, double boy, double kilo, char cinsiyet) {
        super(ad, email, yas, aktif, kayitTarihi);
        setEbeveynAdi(ebeveynAdi);
        setBoy(boy);
        setKilo(kilo);
        setCinsiyet(cinsiyet);
    }

    public Cocuk() {
        this("Çocuk", "cocuk@example.com", 10, true, LocalDate.now(), "Anne", 1.40, 35, 'K');
    }

    @Override
    public String getKategori() {
        return "Çocuk";
    }

    @Override
    public String ozelNot() {
        return "Büyüme döneminde yeterli uyku ve dengeli beslenme önemli.";
    }

    @Override
    public String analizYap() {
        double vki = kilo / (boy * boy);
        return "Çocuk VKİ: " + String.format("%.2f", vki);
    }

    @Override
    public String getAnalizBasligi() {
        return "Çocuk Sağlık Analizi";
    }

    @Override
    public String raporOlustur() {
        return getAd() + " için ebeveyn: " + ebeveynAdi + " cinsiyet:" + cinsiyet;
    }

    public String getEbeveynAdi() {
        return ebeveynAdi;
    }

    public void setEbeveynAdi(String ebeveynAdi) {
        if (ebeveynAdi == null || ebeveynAdi.isEmpty()) {
            throw new DataValidationException("Ebeveyn adı boş olamaz");
        }
        this.ebeveynAdi = ebeveynAdi;
    }

    public double getBoy() {
        return boy;
    }

    public void setBoy(double boy) {
        if (boy < 0.5 || boy > 2.5) {
            throw new DataValidationException("Boy 0.5-2.5 metre arasında olmalıdır");
        }
        this.boy = boy;
    }

    public double getKilo() {
        return kilo;
    }

    public void setKilo(double kilo) {
        if (kilo <= 0 || kilo > 200) {
            throw new DataValidationException("Kilo 0-200 arasında olmalıdır");
        }
        this.kilo = kilo;
    }

    public char getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(char cinsiyet) {
        if (cinsiyet != 'K' && cinsiyet != 'E') {
            throw new DataValidationException("Cinsiyet K veya E olmalıdır");
        }
        this.cinsiyet = cinsiyet;
    }
}
