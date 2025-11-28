package com.saglik.model;

import com.saglik.exception.DataValidationException;
import com.saglik.service.AnalizYapabilir;

import java.time.LocalDate;

/**
 * Uyku süresini tutan kayıt.
 */
public class UykuKaydi extends GunlukKayit implements AnalizYapabilir {
    private double saat;
    private Double derinlikSkoru;
    private String ruhHali;
    private boolean telefonKullanim;

    public UykuKaydi(LocalDate tarih, String aciklama, int oncelik, boolean saglikli, double saat, Double derinlikSkoru, String ruhHali, boolean telefonKullanim) {
        super(tarih, aciklama, oncelik, saglikli);
        setSaat(saat);
        this.derinlikSkoru = derinlikSkoru;
        this.ruhHali = ruhHali;
        this.telefonKullanim = telefonKullanim;
    }

    public UykuKaydi() {
        this(LocalDate.now(), "Uyku", 2, true, 7.5, 85.5, "İyi", false);
    }

    @Override
    public String turBilgisi() {
        return "Uyku";
    }

    @Override
    public double miktarGetir() {
        return saat;
    }

    @Override
    public String raporOlustur() {
        return "Uyku: " + saat + " saat derinlik:" + derinlikSkoru + " ruh hali:" + ruhHali;
    }

    @Override
    public String analizYap() {
        String durum = saat >= 7 ? "Yeterli" : "Yetersiz";
        return durum + " uyku - telefon kullanımı:" + telefonKullanim;
    }

    @Override
    public String getAnalizBasligi() {
        return "Uyku Analizi";
    }

    public double getSaat() {
        return saat;
    }

    public void setSaat(double saat) {
        if (saat < 0 || saat > 24) {
            throw new DataValidationException("Uyku süresi 0-24 saat olmalıdır");
        }
        this.saat = saat;
    }

    public Double getDerinlikSkoru() {
        return derinlikSkoru;
    }

    public void setDerinlikSkoru(Double derinlikSkoru) {
        this.derinlikSkoru = derinlikSkoru;
    }

    public String getRuhHali() {
        return ruhHali;
    }

    public void setRuhHali(String ruhHali) {
        this.ruhHali = ruhHali;
    }

    public boolean isTelefonKullanim() {
        return telefonKullanim;
    }

    public void setTelefonKullanim(boolean telefonKullanim) {
        this.telefonKullanim = telefonKullanim;
    }
}
