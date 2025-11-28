package com.saglik.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Basit generic depo sınıfı.
 */
public class Depo<T> {
    private final List<T> liste = new ArrayList<>();

    public void ekle(T eleman) {
        liste.add(eleman);
    }

    public T getir(int index) {
        return liste.get(index);
    }

    public List<T> getListe() {
        return liste;
    }

    /**
     * Generic bir metot: kaynak listeden hedefe kopyalama.
     */
    public static <E> void kopyala(List<? extends E> kaynak, List<? super E> hedef) {
        for (E e : kaynak) {
            hedef.add(e);
        }
    }

    /**
     * Generic metot: ilk elemanı döner.
     */
    public static <K> K ilkiniGetir(List<K> liste) {
        return liste.isEmpty() ? null : liste.get(0);
    }
}
