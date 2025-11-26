public class Oyuncu {
    private String ad;
    private double toplamKalori;
    private int toplamPuan;
    private int suMiktari;

    public Oyuncu(String ad) throws SaglikDegeriGecersizException {
        setAd(ad);
        this.toplamKalori = 0;
        this.toplamPuan = 0;
        this.suMiktari = 0;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) throws SaglikDegeriGecersizException {
        if (ad == null || ad.isBlank()) {
            throw new SaglikDegeriGecersizException("Oyuncu adı boş olamaz");
        }
        this.ad = ad;
    }

    public double getToplamKalori() {
        return toplamKalori;
    }

    public void setToplamKalori(double toplamKalori) {
        this.toplamKalori = toplamKalori;
    }

    public int getToplamPuan() {
        return toplamPuan;
    }

    public void setToplamPuan(int toplamPuan) {
        this.toplamPuan = toplamPuan;
    }

    public int getSuMiktari() {
        return suMiktari;
    }

    public void setSuMiktari(int suMiktari) throws SaglikDegeriGecersizException {
        if (suMiktari < 0) {
            throw new SaglikDegeriGecersizException("Su miktarı negatif olamaz");
        }
        this.suMiktari = suMiktari;
    }
}
