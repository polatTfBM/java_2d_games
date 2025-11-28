package com.saglik.model;

import com.saglik.exception.DataValidationException;
import com.saglik.service.AnalizYapabilir;

import java.time.LocalDate;

/**
 * Günlük kalori kayıtlarını tutar.
 */
public class KaloriKaydi extends GunlukKayit implements AnalizYapabilir {
    private int kalori;
    private String ogun;
    private String icerik;
    private boolean evYapimi;

    public KaloriKaydi(LocalDate tarih, String aciklama, int oncelik, boolean saglikli, int kalori, String ogun, String icerik, boolean evYapimi) {
        super(tarih, aciklama, oncelik, saglikli);
        setKalori(kalori);
        setOgun(ogun);
        this.icerik = icerik;
        this.evYapimi = evYapimi;
    }

    public KaloriKaydi() {
        this(LocalDate.now(), "Kalori", 3, true, 500, "Öğle", "Tavuk", true);
    }

    @Override
    public String turBilgisi() {
        return "Kalori";
    }

    @Override
    public double miktarGetir() {
        return kalori;
    }

    @Override
    public String raporOlustur() {
        return ogun + " kalori:" + kalori + " içerik:" + icerik;
    }

    @Override
    public String analizYap() {
        String durum;
        if (kalori < 400) {
            durum = "Düşük kalori";
        } else if (kalori > 800) {
            durum = "Yüksek kalori";
        } else {
            durum = "Dengeli";
        }
        return durum + " - " + ogun;
    }

    @Override
    public String getAnalizBasligi() {
        return "Kalori Analizi";
    }

    public int getKalori() {
        return kalori;
    }

    public void setKalori(int kalori) {
        if (kalori <= 0 || kalori > 5000) {
            throw new DataValidationException("Kalori 0-5000 arasında olmalıdır");
        }
        this.kalori = kalori;
    }

    public String getOgun() {
        return ogun;
    }

    public void setOgun(String ogun) {
        if (ogun == null || ogun.isBlank()) {
            throw new DataValidationException("Öğün boş olamaz");
        }
        this.ogun = ogun;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public boolean isEvYapimi() {
        return evYapimi;
    }

    public void setEvYapimi(boolean evYapimi) {
        this.evYapimi = evYapimi;
    }
}
