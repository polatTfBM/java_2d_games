package com.saglik.model;

import com.saglik.service.AnalizYapabilir;
import com.saglik.util.Hesaplayici;

import java.time.LocalDate;

/**
 * Günlük su tüketim kaydı.
 */
public class SuKaydi extends GunlukKayit implements AnalizYapabilir {
    private double litre;
    private Integer hedefAdet; // wrapper örneği
    private String kayitNotu;
    private boolean hatirlatici;

    public SuKaydi(LocalDate tarih, String aciklama, int oncelik, boolean saglikli, double litre, Integer hedefAdet, String kayitNotu, boolean hatirlatici) {
        super(tarih, aciklama, oncelik, saglikli);
        this.litre = litre;
        this.hedefAdet = hedefAdet;
        this.kayitNotu = kayitNotu;
        this.hatirlatici = hatirlatici;
    }

    public SuKaydi() {
        this(LocalDate.now(), "Su", 2, true, 1.5, 8, "", true);
    }

    @Override
    public String turBilgisi() {
        return "Su";
    }

    @Override
    public double miktarGetir() {
        return litre;
    }

    @Override
    public String raporOlustur() {
        return "Su:" + litre + "L hedef:" + hedefAdet + " Hatırlatıcı:" + hatirlatici;
    }

    @Override
    public String analizYap() {
        double oran = litre / Hesaplayici.SU_HEDEF_LITRE;
        return oran >= 1 ? "Hedefe ulaşıldı" : "Hedefin " + String.format("%.0f", oran * 100) + "%'i tamamlandı";
    }

    @Override
    public String getAnalizBasligi() {
        return "Su Analizi";
    }

    public double getLitre() {
        return litre;
    }

    public void setLitre(double litre) {
        this.litre = litre;
    }

    public Integer getHedefAdet() {
        return hedefAdet;
    }

    public void setHedefAdet(Integer hedefAdet) {
        this.hedefAdet = hedefAdet;
    }

    public String getKayitNotu() {
        return kayitNotu;
    }

    public void setKayitNotu(String kayitNotu) {
        this.kayitNotu = kayitNotu;
    }

    public boolean isHatirlatici() {
        return hatirlatici;
    }

    public void setHatirlatici(boolean hatirlatici) {
        this.hatirlatici = hatirlatici;
    }
}
