package com.saglik.model;

import com.saglik.exception.DataValidationException;
import com.saglik.service.Raporlanabilir;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Günlük kayıtlara temel sağlayan soyut sınıf.
 */
public abstract class GunlukKayit implements Raporlanabilir {
    private LocalDate tarih;
    private String aciklama;
    private int oncelik;
    private boolean saglikli;

    public GunlukKayit(LocalDate tarih, String aciklama, int oncelik, boolean saglikli) {
        setTarih(tarih);
        setAciklama(aciklama);
        setOncelik(oncelik);
        this.saglikli = saglikli;
    }

    public GunlukKayit() {
        this(LocalDate.now(), "Kayit", 1, true);
    }

    public abstract String turBilgisi();

    public abstract double miktarGetir();

    public LocalDate getTarih() {
        return tarih;
    }

    public void setTarih(LocalDate tarih) {
        if (tarih == null) {
            throw new DataValidationException("Tarih boş olamaz");
        }
        this.tarih = tarih;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        if (aciklama == null || aciklama.isBlank()) {
            throw new DataValidationException("Açıklama boş olamaz");
        }
        this.aciklama = aciklama;
    }

    public int getOncelik() {
        return oncelik;
    }

    public void setOncelik(int oncelik) {
        if (oncelik < 1 || oncelik > 5) {
            throw new DataValidationException("Öncelik 1-5 arasında olmalıdır");
        }
        this.oncelik = oncelik;
    }

    public boolean isSaglikli() {
        return saglikli;
    }

    public void setSaglikli(boolean saglikli) {
        this.saglikli = saglikli;
    }

    /**
     * Tarihi formatlanmış şekilde döner.
     */
    public String formatliTarih() {
        return tarih.format(DateTimeFormatter.ISO_DATE);
    }

    @Override
    public String toString() {
        return turBilgisi() + " - " + formatliTarih() + " - " + aciklama;
    }
}
