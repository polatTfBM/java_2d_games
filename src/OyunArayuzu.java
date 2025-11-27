import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OyunArayuzu extends JFrame {
    private Oyuncu oyuncu;
    private GunlukRapor aktifRapor;
    private final Map<LocalDate, GunlukRapor> raporlar = new HashMap<>();

    private final JTextField oyuncuAdiField = new JTextField("Oyuncu");
    private final JTextField tarihField = new JTextField(LocalDate.now().toString());
    private final JTextField yiyecekAdField = new JTextField();
    private final JTextField kaloriField = new JTextField();
    private final JTextField saglikField = new JTextField();
    private final JCheckBox meyveMiCheck = new JCheckBox("Meyve", true);
    private final JCheckBox mevsimindeCheck = new JCheckBox("Mevsiminde");
    private final JCheckBox sebzeLifliCheck = new JCheckBox("Lifli");
    private final JTextField kosuSureField = new JTextField();
    private final JTextField kosuMesafeField = new JTextField();
    private final JTextField kiloField = new JTextField();
    private final JTextField suField = new JTextField();
    private final JTextField dosyaField = new JTextField("rapor.txt");
    private final JTextArea ciktiAlani = new JTextArea();

    public OyunArayuzu() {
        super("Sağlık ve Beslenme Eğitici Oyunu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(null);
        ciktiAlani.setEditable(false);
        ciktiAlani.setLineWrap(true);
        ciktiAlani.setWrapStyleWord(true);
        add(new JScrollPane(ciktiAlani), BorderLayout.CENTER);
        add(olusturFormPanel(), BorderLayout.WEST);
        appendCikti("Arayüz hazır. Oyuncu adı girip başlatın.");
    }

    private JPanel olusturFormPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Oyuncu adı:"));
        panel.add(oyuncuAdiField);
        JButton oyuncuBaslat = new JButton("Oyuncuyu Başlat");
        oyuncuBaslat.addActionListener(e -> oyuncuyuBaslat());
        panel.add(oyuncuBaslat);

        panel.add(new JLabel("Gün tarihi (YYYY-MM-DD):"));
        panel.add(tarihField);
        JButton gunBaslat = new JButton("Yeni Gün Başlat");
        gunBaslat.addActionListener(e -> yeniGunBaslat());
        panel.add(gunBaslat);

        panel.add(new JLabel("Yiyecek adı:"));
        panel.add(yiyecekAdField);
        panel.add(new JLabel("Kalori:"));
        panel.add(kaloriField);
        panel.add(new JLabel("Sağlık değeri (1-100):"));
        panel.add(saglikField);
        panel.add(meyveMiCheck);
        panel.add(mevsimindeCheck);
        panel.add(sebzeLifliCheck);
        JButton yiyecekEkle = new JButton("Yiyecek Ekle");
        yiyecekEkle.addActionListener(e -> yiyecekEkle());
        panel.add(yiyecekEkle);

        panel.add(new JLabel("Koşu süresi (dk):"));
        panel.add(kosuSureField);
        panel.add(new JLabel("Mesafe (km):"));
        panel.add(kosuMesafeField);
        panel.add(new JLabel("Kilo (kg):"));
        panel.add(kiloField);
        JButton aktiviteEkle = new JButton("Koşu Ekle");
        aktiviteEkle.addActionListener(e -> aktiviteEkle());
        panel.add(aktiviteEkle);

        panel.add(new JLabel("Su miktarı (ml):"));
        panel.add(suField);
        JButton suIcButton = new JButton("Su Ekle");
        suIcButton.addActionListener(e -> suIc());
        panel.add(suIcButton);

        JButton raporGoster = new JButton("Raporu Göster");
        raporGoster.addActionListener(e -> raporGoruntule());
        panel.add(raporGoster);

        panel.add(new JLabel("Dosya yolu:"));
        panel.add(dosyaField);
        JButton kaydet = new JButton("Raporu Kaydet");
        kaydet.addActionListener(e -> raporuKaydet());
        panel.add(kaydet);
        JButton yukle = new JButton("Dosyadan Yükle");
        yukle.addActionListener(e -> raporYukle());
        panel.add(yukle);

        return panel;
    }

    private void oyuncuyuBaslat() {
        String ad = oyuncuAdiField.getText().trim();
        try {
            oyuncu = new Oyuncu(ad.isEmpty() ? "Oyuncu" : ad);
            appendCikti("Oyuncu hazır: " + oyuncu.getAd());
        } catch (SaglikDegeriGecersizException e) {
            appendCikti("Oyuncu oluşturulamadı: " + e.getMessage());
        }
    }

    private void yeniGunBaslat() {
        if (oyuncu == null) {
            appendCikti("Önce oyuncuyu başlatın.");
            return;
        }
        try {
            LocalDate tarih = LocalDate.parse(tarihField.getText().trim());
            aktifRapor = new GunlukRapor(tarih);
            raporlar.put(tarih, aktifRapor);
            appendCikti("Yeni gün başladı: " + tarih);
            if (aktifRapor.gelecekGunMu()) {
                appendCikti("Uyarı: gelecekte bir gün seçtiniz.");
            }
        } catch (Exception e) {
            aktifRapor = new GunlukRapor(LocalDate.now());
            raporlar.put(aktifRapor.getTarih(), aktifRapor);
            appendCikti("Tarih hatalı, bugüne ayarlandı.");
        }
    }

    private void yiyecekEkle() {
        if (aktifRapor == null || oyuncu == null) {
            appendCikti("Önce oyuncuyu ve günü başlatın.");
            return;
        }
        try {
            String ad = yiyecekAdField.getText().trim();
            double kalori = Double.parseDouble(kaloriField.getText().trim());
            int saglik = Integer.parseInt(saglikField.getText().trim());
            Yiyecek yiyecek;
            if (meyveMiCheck.isSelected()) {
                yiyecek = new Meyve(ad, kalori, saglik, mevsimindeCheck.isSelected(), "Renkli");
            } else {
                yiyecek = new Sebze(ad, kalori, saglik, sebzeLifliCheck.isSelected(), true);
            }
            yiyecek.tuket(oyuncu);
            aktifRapor.yiyecekEkle(yiyecek);
            raporlar.put(aktifRapor.getTarih(), aktifRapor);
            List<? extends Yiyecek> tumYiyecekler = new ArrayList<>(aktifRapor.getYiyecekler());
            appendCikti("Yiyecek eklendi. Toplam: " + tumYiyecekler.size());
        } catch (SaglikDegeriGecersizException | NumberFormatException ex) {
            appendCikti("Yiyecek eklenemedi: " + ex.getMessage());
        }
    }

    private void aktiviteEkle() {
        if (aktifRapor == null || oyuncu == null) {
            appendCikti("Önce oyuncuyu ve günü başlatın.");
            return;
        }
        try {
            int sure = Integer.parseInt(kosuSureField.getText().trim());
            double mesafe = Double.parseDouble(kosuMesafeField.getText().trim());
            double kilo = Double.parseDouble(kiloField.getText().trim());
            Aktivite kosu = new Kosu("Koşu", sure, kilo, mesafe);
            aktifRapor.aktiviteEkle(kosu);
            oyuncu.setToplamPuan(oyuncu.getToplamPuan() + kosu.puanHesapla());
            appendCikti("Aktivite eklendi: " + kosu);
        } catch (NumberFormatException e) {
            appendCikti("Geçersiz sayı girdiniz: " + e.getMessage());
        } catch (AktiviteSuresiGecersizException e) {
            appendCikti("Aktivite eklenemedi: " + e.getMessage());
        }
    }

    private void suIc() {
        if (aktifRapor == null || oyuncu == null) {
            appendCikti("Önce oyuncuyu ve günü başlatın.");
            return;
        }
        try {
            int su = Integer.parseInt(suField.getText().trim());
            oyuncu.setSuMiktari(oyuncu.getSuMiktari() + su);
            aktifRapor.setIcilenSuMl(oyuncu.getSuMiktari());
            appendCikti("Su eklendi, toplam: " + oyuncu.getSuMiktari() + " ml");
        } catch (NumberFormatException e) {
            appendCikti("Sayı formatı hatası: " + e.getMessage());
        } catch (SaglikDegeriGecersizException e) {
            appendCikti("Su miktarı hatası: " + e.getMessage());
        }
    }

    private void raporGoruntule() {
        if (aktifRapor == null) {
            appendCikti("Henüz bir rapor yok.");
            return;
        }
        try {
            appendCikti(aktifRapor.raporuHazirla());
            if (aktifRapor.gecmisGunMu()) {
                appendCikti("Bu rapor geçmiş bir güne ait.");
            }
        } catch (Exception e) {
            appendCikti("Rapor hazırlanamadı: " + e.getMessage());
        }
    }

    private void raporuKaydet() {
        if (aktifRapor == null) {
            appendCikti("Kaydedilecek rapor yok.");
            return;
        }
        try {
            DosyaIslemleri.raporuKaydet(dosyaField.getText().trim(), aktifRapor.raporuHazirla());
            appendCikti("Rapor kaydedildi: " + dosyaField.getText());
        } catch (Exception e) {
            appendCikti("Dosyaya yazılamadı: " + e.getMessage());
        }
    }

    private void raporYukle() {
        List<String> satirlar = DosyaIslemleri.raporlariOku(dosyaField.getText().trim());
        if (satirlar.isEmpty()) {
            appendCikti("Dosyada rapor bulunamadı.");
        } else {
            appendCikti("Dosyadaki raporlar:");
            for (String s : satirlar) {
                appendCikti(s);
            }
            appendCikti("Yükleme zamanı: " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
        }
    }

    private void appendCikti(String mesaj) {
        ciktiAlani.append(mesaj + "\n");
    }
}
