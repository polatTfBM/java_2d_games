package com.saglik.service;

/**
 * Rapor oluşturma yeteneği sunan nesneler için arayüz.
 */
public interface Raporlanabilir {
    /**
     * Detaylı rapor üretir.
     * @return rapor
     */
    String raporOlustur();
}
