import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DosyaIslemleri {
    private DosyaIslemleri() {
    }

    public static void raporuKaydet(String dosyaYolu, String icerik) throws IOException {
        try (FileWriter writer = new FileWriter(dosyaYolu, true)) {
            writer.write(icerik);
            writer.write(System.lineSeparator());
        }
    }

    public static List<String> raporlariOku(String dosyaYolu) {
        List<String> satirlar = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(dosyaYolu));
            String satir;
            while ((satir = reader.readLine()) != null) {
                satirlar.add(satir);
            }
        } catch (IOException e) {
            System.err.println("Dosya okunamadı: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Dosya kapatılırken hata: " + e.getMessage());
                }
            }
        }
        return satirlar;
    }
}
