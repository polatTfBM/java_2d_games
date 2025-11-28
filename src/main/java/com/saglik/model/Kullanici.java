package com.saglik.model;

import com.saglik.exception.DataValidationException;
import com.saglik.service.AnalizYapabilir;
import com.saglik.service.Raporlanabilir;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Sistemi kullanan bireylerin temel özelliklerini tanımlar.
 */
public abstract class Kullanici implements AnalizYapabilir, Raporlanabilir {
    private String ad;
    private String email;
    private int yas;
    private boolean aktif;
    private LocalDate kayitTarihi;

    /**
     * Tam parametreli kurucu.
     */
    public Kullanici(String ad, String email, int yas, boolean aktif, LocalDate kayitTarihi) {
        setAd(ad);
        setEmail(email);
        setYas(yas);
        this.aktif = aktif;
        setKayitTarihi(kayitTarihi);
    }

    /**
     * Varsayılan kurucu.
     */
    public Kullanici() {
        this("Bilinmiyor", "bilinmiyor@example.com", 18, true, LocalDate.now());
    }

    /**
     * Kullanıcı tipini döner.
     */
    public abstract String getKategori();

    /**
     * Kullanıcıya özel mesaj.
     */
    public abstract String ozelNot();

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        if (ad == null || ad.trim().length() < 2) {
            throw new DataValidationException("Ad en az 2 karakter olmalıdır");
        }
        this.ad = ad.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new DataValidationException("Geçersiz email");
        }
        this.email = email;
    }

    public int getYas() {
        return yas;
    }

    public void setYas(int yas) {
        if (yas <= 0 || yas > 120) {
            throw new DataValidationException("Yaş 1-120 arasında olmalıdır");
        }
        this.yas = yas;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public LocalDate getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDate kayitTarihi) {
        if (kayitTarihi == null || kayitTarihi.isAfter(LocalDate.now())) {
            throw new DataValidationException("Kayıt tarihi bugünden ileri olamaz");
        }
        this.kayitTarihi = kayitTarihi;
    }

    /**
     * Kullanıcıyı selamlar.
     */
    public String selamla() {
        return "Merhaba " + ad + "!";
    }

    /**
     * Kullanıcı verilerinin basit özetini verir.
     */
    public String ozetGoster() {
        return String.format("%s (%s) - Yaş: %d - Aktif: %s", ad, email, yas, aktif);
    }

    @Override
    public String toString() {
        return ozetGoster();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kullanici kullanici = (Kullanici) o;
        return yas == kullanici.yas && Objects.equals(email, kullanici.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, yas);
    }
}
