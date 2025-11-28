package com.saglik.util;

import java.util.Arrays;
import java.util.List;

/**
 * Basit hesaplamalar ve dönüştürmeler için yardımcı sınıf.
 */
public final class Hesaplayici {
    public static final double SU_HEDEF_LITRE = 2.0;
    public static int sayac = 0;

    private Hesaplayici() {
    }

    /**
     * Ortalama hesaplar.
     */
    public static <T extends Number> double ortalama(List<T> sayilar) {
        double toplam = 0;
        for (T s : sayilar) {
            toplam += s.doubleValue();
        }
        sayac++;
        return sayilar.isEmpty() ? 0 : toplam / sayilar.size();
    }

    /**
     * İki sayıyı toplayan overload.
     */
    public static int topla(int a, int b) {
        return a + b;
    }

    public static double topla(double a, double b, double c) {
        return a + b + c;
    }

    public static long topla(long a, long b, long c, long d) {
        return a + b + c + d;
    }

    /**
     * String işlem örnekleri: farklı metodlar kullanır.
     */
    public static String stringIslemleri(String ifade) {
        String sonuc = ifade.trim();
        sonuc = sonuc.toUpperCase();
        boolean basliyor = sonuc.startsWith("SA");
        boolean bitiyor = sonuc.endsWith("MI");
        sonuc = sonuc.replace(" ", "-");
        int index = sonuc.indexOf("A");
        String parca = sonuc.substring(Math.max(0, index), Math.min(sonuc.length(), index + 3));
        List<String> bolumler = Arrays.asList(sonuc.split("-"));
        boolean iceriyor = sonuc.contains("BESLENME");
        return sonuc + "|" + basliyor + "|" + bitiyor + "|" + parca + "|" + bolumler.size() + "|" + iceriyor;
    }

    /**
     * Basit bir mod alma ve diğer operatör örnekleri.
     */
    public static int operatorOrnekleri(int x, int y) {
        int carpim = x * y;
        int bolum = x / (y == 0 ? 1 : y);
        int fark = x - y;
        int toplam = x + y;
        int mod = x % (y == 0 ? 1 : y);
        return carpim + bolum + fark + toplam + mod;
    }
}
