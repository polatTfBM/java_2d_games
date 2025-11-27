import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GunlukRapor {
    private LocalDate tarih;
    private List<Yiyecek> yiyecekler;
    private List<Aktivite> aktiviteler;
    private int icilenSuMl;
    private LocalDateTime raporZamani;

    public GunlukRapor(LocalDate tarih) {
        this.tarih = tarih;
        this.yiyecekler = new ArrayList<>();
        this.aktiviteler = new ArrayList<>();
        this.icilenSuMl = 0;
        this.raporZamani = LocalDateTime.now();
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public List<Yiyecek> getYiyecekler() {
        return yiyecekler;
    }

    public void setYiyecekler(List<Yiyecek> yiyecekler) {
        this.yiyecekler = yiyecekler;
    }

    public List<Aktivite> getAktiviteler() {
        return aktiviteler;
    }

    public void setAktiviteler(List<Aktivite> aktiviteler) {
        this.aktiviteler = aktiviteler;
    }

    public int getIcilenSuMl() {
        return icilenSuMl;
    }

    public void setIcilenSuMl(int icilenSuMl) {
        this.icilenSuMl = icilenSuMl;
    }

    public void yiyecekEkle(Yiyecek yiyecek) {
        yiyecekler.add(yiyecek);
    }

    public void yiyecekSil(Yiyecek yiyecek) {
        yiyecekler.remove(yiyecek);
    }

    public boolean yiyecekVarMi(Yiyecek yiyecek) {
        return yiyecekler.contains(yiyecek);
    }

    public void yiyecekleriSirala() {
        Collections.sort(yiyecekler, Comparator.comparing(Yiyecek::getKalori));
    }

    public void aktiviteEkle(Aktivite aktivite) {
        aktiviteler.add(aktivite);
    }

    public void aktiviteSil(Aktivite aktivite) {
        aktiviteler.remove(aktivite);
    }

    public boolean aktiviteVarMi(Aktivite aktivite) {
        return aktiviteler.contains(aktivite);
    }

    public void aktiviteleriSirala() {
        aktiviteler.sort(Comparator.comparing(Aktivite::getSureDakika));
    }

    public double toplamKalori() {
        double kalori = 0;
        for (Yiyecek y : yiyecekler) {
            kalori += y.getKalori();
        }
        for (Aktivite a : aktiviteler) {
            kalori -= a.harcananKalori();
        }
        return kalori;
    }

    public int toplamPuan() {
        int puan = 0;
        for (Yiyecek y : yiyecekler) {
            puan += y.puanHesapla();
        }
        for (Aktivite a : aktiviteler) {
            puan += a.puanHesapla();
        }
        puan += Hesaplayici.kaloriToPuan(icilenSuMl / 100.0, 2, false);
        return puan;
    }

    public String raporuHazirla() {
        yiyecekleriSirala();
        aktiviteleriSirala();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        raporZamani = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append("Rapor Tarihi: ").append(tarih).append(" Oluşturulma: ").append(formatter.format(raporZamani)).append(System.lineSeparator());
        sb.append("Yiyecekler:").append(System.lineSeparator());
        for (Yiyecek y : yiyecekler) {
            sb.append(" - ").append(y).append(System.lineSeparator());
        }
        sb.append("Aktiviteler:").append(System.lineSeparator());
        for (Aktivite a : aktiviteler) {
            sb.append(" - ").append(a).append(" | Harcanan kalori: ").append(a.harcananKalori()).append(System.lineSeparator());
        }
        sb.append("İçilen su: ").append(icilenSuMl).append(" ml").append(System.lineSeparator());
        sb.append("Toplam puan: ").append(toplamPuan()).append(System.lineSeparator());
        return sb.toString();
    }

    public boolean gecmisGunMu() {
        return tarih.isBefore(LocalDate.now());
    }

    public boolean gelecekGunMu() {
        return tarih.isAfter(LocalDate.now());
    }
}
