package com.saglik.service;

/**
 * Analiz yapılabilen nesneler için sözleşme.
 */
public interface AnalizYapabilir {
    /**
     * Nesnenin durumunu analiz eder ve sonuç mesajı döner.
     * @return analiz sonucu
     */
    String analizYap();

    /**
     * Analize ait kısa başlık döner.
     * @return başlık
     */
    String getAnalizBasligi();
}
