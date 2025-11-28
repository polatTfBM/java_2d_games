package com.saglik.core;

import com.saglik.exception.DosyaIslemException;
import com.saglik.model.*;
import com.saglik.service.AnalizYapabilir;
import com.saglik.util.Depo;
import com.saglik.util.FileHelper;
import com.saglik.util.Hesaplayici;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Konsol menüsünü yöneten ana sınıf.
 */
public class ApplicationRunner {
    private final Scanner scanner = new Scanner(System.in);
    private final List<GunlukKayit> kayitlar = new ArrayList<>();
    private final LinkedList<String> ogunListesi = new LinkedList<>();
    private final Map<LocalDate, Integer> kaloriHaritasi = new HashMap<>();
    private final Set<String> ozelNotlar = new TreeSet<>();
    private final List<Kullanici> kullanicilar = new ArrayList<>();

    public ApplicationRunner() {
        // Başlangıç verileri
        kullanicilar.add(new Yetiskin());
        kullanicilar.add(new Cocuk());
        ogunListesi.addAll(Arrays.asList("Kahvaltı", "Öğle", "Akşam"));
        kaloriHaritasi.put(LocalDate.now(), 1800);
    }

    /**
     * Uygulamayı başlatır.
     */
    public void baslat() {
        int secim;
        do {
            menuYazdir();
            while (!scanner.hasNextInt()) {
                System.out.println("Lütfen sayı giriniz.");
                scanner.next();
            }
            secim = scanner.nextInt();
            scanner.nextLine();
            switch (secim) {
                case 1 -> suEkle();
                case 2 -> kaloriEkle();
                case 3 -> ogunleriYonet();
                case 4 -> saglikAnalizi();
                case 5 -> uykuKaydet();
                case 6 -> raporOlustur();
                case 7 -> dosyaIslemleri();
                case 8 -> System.out.println("Çıkılıyor...");
                default -> System.out.println("Hatalı seçim");
            }
        } while (secim != 8);
    }

    private void menuYazdir() {
        System.out.println("=== Sağlık ve Beslenme Eğitim Uygulaması ===");
        System.out.println("1- Günlük su tüketimi ekle");
        System.out.println("2- Günlük kalori ekle");
        System.out.println("3- Öğün listesi yönet");
        System.out.println("4- Sağlık analizi yap");
        System.out.println("5- Uyku süresi kaydı");
        System.out.println("6- Rapor oluştur");
        System.out.println("7- Dosyaya kaydet / dosyadan yükle");
        System.out.println("8- Çıkış");
        System.out.print("Seçim: ");
    }

    private void suEkle() {
        try {
            System.out.print("İçilen su (L): ");
            double litre = scanner.nextDouble();
            scanner.nextLine();
            SuKaydi kayit = new SuKaydi(LocalDate.now(), "Su girişi", 2, true, litre, 8, "", true);
            kayitlar.add(kayit);
            ozelNotlar.add("Su eklendi:" + litre);
            System.out.println("Su kaydedildi. Hedef: " + Hesaplayici.SU_HEDEF_LITRE);
        } catch (InputMismatchException | com.saglik.exception.DataValidationException e) {
            System.out.println("Geçersiz veri: " + e.getMessage());
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Beklenmedik hata: " + e.getMessage());
        }
    }

    private void kaloriEkle() {
        try {
            System.out.print("Kalori: ");
            int kalori = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Öğün adı: ");
            String ogun = scanner.nextLine();
            KaloriKaydi kayit = new KaloriKaydi(LocalDate.now(), "Kalori girişi", 3, true, kalori, ogun, "", true);
            kayitlar.add(kayit);
            kaloriHaritasi.put(LocalDate.now(), kaloriHaritasi.getOrDefault(LocalDate.now(), 0) + kalori);
            System.out.println("Kalori kaydedildi.");
        } catch (InputMismatchException | com.saglik.exception.DataValidationException e) {
            System.out.println("Hatalı kalori girdiniz: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void ogunleriYonet() {
        System.out.println("1- Listele 2- Ekle 3- Sil 4- Ara");
        int secim = scanner.nextInt();
        scanner.nextLine();
        switch (secim) {
            case 1 -> ogunListesi.forEach(System.out::println);
            case 2 -> {
                System.out.print("Öğün adı: ");
                String yeni = scanner.nextLine();
                ogunListesi.add(yeni);
            }
            case 3 -> {
                System.out.print("Silinecek öğün: ");
                String sil = scanner.nextLine();
                ogunListesi.remove(sil);
            }
            case 4 -> {
                System.out.print("Aranacak: ");
                String ara = scanner.nextLine();
                System.out.println("Var mı? " + ogunListesi.contains(ara));
            }
            default -> System.out.println("Geçersiz.");
        }
        Collections.sort(ogunListesi);
        System.out.println("Sıralı öğünler: " + ogunListesi);
    }

    private void saglikAnalizi() {
        List<AnalizYapabilir> analizListesi = new ArrayList<>(kullanicilar);
        analizListesi.addAll(kayitlar.stream().filter(a -> a instanceof AnalizYapabilir).map(a -> (AnalizYapabilir) a).toList());
        for (AnalizYapabilir analiz : analizListesi) {
            System.out.println(analiz.getAnalizBasligi() + ": " + analiz.analizYap());
        }

        // Dinamik polimorfizm ve generics örneği
        Depo<GunlukKayit> depo = new Depo<>();
        Depo.kopyala(kayitlar, depo.getListe());
        System.out.println("Depodaki ilk kayıt: " + Depo.ilkiniGetir(depo.getListe()));

        // Operatör ve string işlemleri
        System.out.println("String işlemleri: " + Hesaplayici.stringIslemleri(" Sağlık Beslenme Eğitimi "));
        System.out.println("Operatör sonucu: " + Hesaplayici.operatorOrnekleri(5, 3));

        // Primitive örnekleri ve casting
        byte b = 1;
        short s = 2;
        int i = b + s; // implicit
        long l = i * 1000L;
        float f = 3.14f;
        double d = f; // implicit
        char c = 'A';
        boolean dogru = l > 0 && i != 0 || c == 'B';
        int explicit = (int) d; // explicit
        System.out.println("Primitive örnek: " + (b + s + i + l + explicit) + " boolean:" + dogru);

        // Diziler
        int[] kaloriler = {100, 200, 300};
        double[][] uykuMatrix = {{7.5, 6.0}, {8.0, 7.0}};
        for (int deger : kaloriler) {
            System.out.println("Kalori değeri: " + deger);
        }
        int satir = 0;
        while (satir < uykuMatrix.length) {
            int sutun = 0;
            do {
                System.out.println("Uyku saat:" + uykuMatrix[satir][sutun]);
                sutun++;
            } while (sutun < uykuMatrix[satir].length);
            satir++;
        }

        // Tarih işlemleri
        LocalDateTime simdi = LocalDateTime.now();
        LocalDateTime gelecek = simdi.plusDays(3);
        System.out.println("Bugün: " + simdi.format(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("3 gün sonrası: " + gelecek);
        System.out.println("Karşılaştırma: " + simdi.isBefore(gelecek));
    }

    private void uykuKaydet() {
        try {
            System.out.print("Uyku saati: ");
            double saat = scanner.nextDouble();
            scanner.nextLine();
            UykuKaydi kayit = new UykuKaydi(LocalDate.now(), "Uyku", 2, true, saat, 90.0, "İyi", false);
            kayitlar.add(kayit);
            System.out.println("Uyku eklendi.");
        } catch (InputMismatchException e) {
            System.out.println("Sayı girmelisiniz.");
            scanner.nextLine();
        }
    }

    private void raporOlustur() {
        System.out.println("--- Güncel Rapor ---");
        for (GunlukKayit kayit : kayitlar) {
            System.out.println(kayit.raporOlustur());
        }
        System.out.println("Öğünler: " + ogunListesi);
        System.out.println("Kalori Haritası: " + kaloriHaritasi);
        System.out.println("Özel Notlar: " + ozelNotlar);
    }

    private void dosyaIslemleri() {
        System.out.println("1- Kaydet 2- Yükle");
        int secim = scanner.nextInt();
        scanner.nextLine();
        if (secim == 1) {
            try {
                FileHelper.yaz(kayitlar);
                FileHelper.ekLog("Kaydedildi");
                System.out.println("Dosyaya kaydedildi.");
            } catch (DosyaIslemException e) {
                System.out.println("Dosya hatası: " + e.getMessage());
            }
        } else if (secim == 2) {
            try {
                List<String> satirlar = FileHelper.oku();
                for (String satir : satirlar) {
                    if (satir.startsWith("#")) continue;
                    System.out.println("Okundu: " + satir);
                }
            } catch (DosyaIslemException e) {
                System.out.println("Okuma hatası: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Beklenmedik hata: " + e.getMessage());
            }
        } else {
            System.out.println("Geçersiz seçim");
        }
    }
}
