public class Meyve extends SaglikliYiyecek {
    private String renk;

    public Meyve(String ad, double kalori, int saglikDegeri, boolean lifli, String renk) throws SaglikDegeriGecersizException {
        super(ad, kalori, saglikDegeri, lifli);
        setRenk(renk);
    }

    public String getRenk() {
        return renk;
    }

    public void setRenk(String renk) throws SaglikDegeriGecersizException {
        if (renk == null || renk.isBlank()) {
            throw new SaglikDegeriGecersizException("Renk boş olamaz");
        }
        this.renk = renk;
    }

    @Override
    public void tuket(Oyuncu oyuncu) {
        oyuncu.setToplamKalori(oyuncu.getToplamKalori() + getKalori());
        oyuncu.setToplamPuan(oyuncu.getToplamPuan() + puanHesapla());
    }

    @Override
    public int puanHesapla() {
        return Hesaplayici.kaloriToPuan(getKalori());
    }

    @Override
    public String toString() {
        return "Meyve: " + super.toString();
    }
}
