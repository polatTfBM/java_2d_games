import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OyunYoneticisi {
    private final Scanner scanner = new Scanner(System.in);
    private GunlukRapor aktifRapor;
    private Oyuncu oyuncu;
    private final Map<LocalDate, GunlukRapor> raporlar = new HashMap<>();

    public void baslat() {
        try {
            System.out.print("Oyuncu adınızı girin: ");
            String ad = scanner.nextLine();
            oyuncu = new Oyuncu(ad);
        } catch (SaglikDegeriGecersizException e) {
            System.out.println("Varsayılan ad kullanıldı: Oyuncu");
            try {
                oyuncu = new Oyuncu("Oyuncu");
            } catch (SaglikDegeriGecersizException ex) {
                System.out.println("Oyuncu oluşturulamadı. Çıkılıyor.");
                return;
            }
        }

        boolean devam = true;
        while (devam) {
            try {
                menuYazdir();
                String secim = scanner.nextLine();
                switch (secim) {
                    case "1" -> yeniGunBaslat();
                    case "2" -> yiyecekTuket();
                    case "3" -> aktiviteYap();
                    case "4" -> suIc();
                    case "5" -> raporGoruntule();
                    case "6" -> raporuKaydet();
                    case "7" -> raporYukle();
                    case "0" -> devam = false;
                    default -> System.out.println("Geçersiz seçim, tekrar deneyin.");
                }
            } catch (Exception e) {
                System.out.println("Beklenmeyen hata: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void menuYazdir() {
        System.out.println("\n--- Sağlık ve Beslenme Eğitici Oyunu ---");
        System.out.println("1) Yeni gün başlat");
        System.out.println("2) Yiyecek tüket");
        System.out.println("3) Aktivite yap");
        System.out.println("4) Su iç");
        System.out.println("5) Günlük raporu görüntüle");
        System.out.println("6) Raporu dosyaya kaydet");
        System.out.println("7) Rapor yükle");
        System.out.println("0) Çıkış");
        System.out.print("Seçiminiz: ");
    }

    private void yeniGunBaslat() {
        try {
            System.out.print("Gün tarihi (YYYY-MM-DD): ");
            String giris = scanner.nextLine();
            LocalDate tarih = LocalDate.parse(giris);
            aktifRapor = new GunlukRapor(tarih);
            raporlar.put(tarih, aktifRapor);
            Kutu<GunlukRapor> kutu = new Kutu<>(aktifRapor);
            System.out.println("Rapor kutuya alındı: " + kutu.getIcerik().getTarih());
            if (aktifRapor.gelecekGunMu()) {
                System.out.println("Uyarı: gelecekte bir gün başlatıldı.");
            }
            System.out.println("Yeni gün başladı: " + tarih);
        } catch (Exception e) {
            System.out.println("Tarih hatalı, bugün için rapor oluşturuluyor.");
            aktifRapor = new GunlukRapor(LocalDate.now());
            raporlar.put(aktifRapor.getTarih(), aktifRapor);
        }
    }

    private void yiyecekTuket() {
        if (aktifRapor == null) {
            System.out.println("Önce yeni bir gün başlatın.");
            return;
        }
        try {
            System.out.print("Meyve için 1, Sebze için 2 seçin: ");
            String secim = scanner.nextLine();
            System.out.print("Ad: ");
            String ad = scanner.nextLine();
            System.out.print("Kalori: ");
            double kalori = Double.parseDouble(scanner.nextLine());
            System.out.print("Sağlık değeri (1-100): ");
            int saglik = Integer.parseInt(scanner.nextLine());
            Yiyecek yiyecek;
            if ("1".equals(secim)) {
                yiyecek = new Meyve(ad, kalori, saglik, true, "Kırmızı");
            } else {
                yiyecek = new Sebze(ad, kalori, saglik, false, true);
            }
            yiyecek.tuket(oyuncu);
            aktifRapor.yiyecekEkle(yiyecek);
            raporlar.put(aktifRapor.getTarih(), aktifRapor);
            List<? extends Yiyecek> tumYiyecekler = new ArrayList<>(aktifRapor.getYiyecekler());
            listeleYiyecekler(tumYiyecekler);
        } catch (SaglikDegeriGecersizException | NumberFormatException e) {
            System.out.println("Yiyecek eklenemedi: " + e.getMessage());
        }
    }

    private void listeleYiyecekler(List<? extends Yiyecek> yiyecekler) {
        System.out.println("Yiyecek listesi (dinamik polimorfizm örneği):");
        for (Yiyecek y : yiyecekler) {
            System.out.println(" * " + y.toString());
        }
    }

    private void aktiviteYap() {
        if (aktifRapor == null) {
            System.out.println("Önce yeni bir gün başlatın.");
            return;
        }
        try {
            System.out.print("Koşu süresi (dk): ");
            int sure = Integer.parseInt(scanner.nextLine());
            System.out.print("Kilonuz (kg): ");
            double kilo = Double.parseDouble(scanner.nextLine());
            System.out.print("Mesafe (km): ");
            double mesafe = Double.parseDouble(scanner.nextLine());
            Aktivite kosu = new Kosu("Günlük koşu", sure, kilo, mesafe);
            aktifRapor.aktiviteEkle(kosu);
            oyuncu.setToplamPuan(oyuncu.getToplamPuan() + kosu.puanHesapla());
            System.out.println("Aktivite eklendi: " + kosu);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz sayı girdiniz: " + e.getMessage());
        } catch (AktiviteSuresiGecersizException e) {
            System.out.println("Aktivite eklenemedi: " + e.getMessage());
        }
    }

    private void suIc() {
        if (aktifRapor == null) {
            System.out.println("Önce yeni bir gün başlatın.");
            return;
        }
        try {
            System.out.print("İçilen su (ml): ");
            int su = Integer.parseInt(scanner.nextLine());
            oyuncu.setSuMiktari(oyuncu.getSuMiktari() + su);
            aktifRapor.setIcilenSuMl(oyuncu.getSuMiktari());
        } catch (NumberFormatException e) {
            System.out.println("Sayı formatı hatası: " + e.getMessage());
        } catch (SaglikDegeriGecersizException e) {
            System.out.println("Su miktarı hatası: " + e.getMessage());
        }
    }

    private void raporGoruntule() {
        if (aktifRapor == null) {
            System.out.println("Henüz bir rapor yok.");
            return;
        }
        try {
            System.out.println(aktifRapor.raporuHazirla());
            if (aktifRapor.gecmisGunMu()) {
                System.out.println("Bu rapor geçmiş bir güne ait.");
            }
        } catch (Exception e) {
            System.out.println("Rapor hazırlanamadı: " + e.getMessage());
        }
    }

    private void raporuKaydet() {
        if (aktifRapor == null) {
            System.out.println("Kaydedilecek rapor yok.");
            return;
        }
        System.out.print("Kaydedilecek dosya yolu: ");
        String dosya = scanner.nextLine();
        try {
            DosyaIslemleri.raporuKaydet(dosya, aktifRapor.raporuHazirla());
            System.out.println("Rapor kaydedildi: " + dosya);
        } catch (IOException e) {
            System.out.println("Dosyaya yazılamadı: " + e.getMessage());
        }
    }

    private void raporYukle() {
        System.out.print("Okunacak dosya yolu: ");
        String dosya = scanner.nextLine();
        List<String> satirlar = DosyaIslemleri.raporlariOku(dosya);
        if (satirlar.isEmpty()) {
            System.out.println("Dosyada rapor bulunamadı.");
        } else {
            System.out.println("Dosyadaki raporlar: ");
            for (String s : satirlar) {
                System.out.println(s);
            }
            System.out.println("Yükleme zamanı: " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
        }
    }
}
