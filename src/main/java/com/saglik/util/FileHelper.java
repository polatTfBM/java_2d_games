package com.saglik.util;

import com.saglik.exception.DosyaIslemException;
import com.saglik.model.GunlukKayit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Dosya okuma/yazma işlemleri için yardımcı sınıf.
 */
public final class FileHelper {
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String DATA_PATH = "data" + SEPARATOR + "kayitlar.txt";

    private FileHelper() {
    }

    public static void yaz(List<GunlukKayit> kayitlar) throws DosyaIslemException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_PATH, false))) {
            writer.write("#Kayitlar " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            writer.newLine();
            for (GunlukKayit kayit : kayitlar) {
                writer.write(kayit.turBilgisi() + ";" + kayit.formatliTarih() + ";" + kayit.raporOlustur());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DosyaIslemException("Dosyaya yazarken hata", e);
        }
    }

    public static void ekLog(String metin) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(DATA_PATH, true));
            writer.write("LOG:" + metin);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Log yazılamadı: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Kapatma hatası" + e.getMessage());
                }
            }
        }
    }

    public static List<String> oku() throws DosyaIslemException {
        try {
            if (!Files.exists(Path.of(DATA_PATH))) {
                return List.of();
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH))) {
                return reader.lines().collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new DosyaIslemException("Dosya okunamadı", e);
        }
    }

    public static int sayfaSayisiTahmini() {
        int satir = 0;
        try (Scanner scanner = new Scanner(new File(DATA_PATH))) {
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                satir++;
            }
        } catch (FileNotFoundException e) {
            satir = 0;
        }
        return satir;
    }
}
