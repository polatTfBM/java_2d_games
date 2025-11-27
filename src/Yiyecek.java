public abstract class Yiyecek implements Tuketilebilir, Puanlanabilir {
    private String ad;
    private double kalori;
    private int saglikDegeri;

    public Yiyecek(String ad, double kalori, int saglikDegeri) throws SaglikDegeriGecersizException {
        setAd(ad);
        setKalori(kalori);
        setSaglikDegeri(saglikDegeri);
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) throws SaglikDegeriGecersizException {
        if (ad == null || ad.isBlank()) {
            throw new SaglikDegeriGecersizException("Yiyecek adı boş olamaz");
        }
        this.ad = ad;
    }

    public double getKalori() {
        return kalori;
    }

    public void setKalori(double kalori) throws SaglikDegeriGecersizException {
        if (kalori <= 0) {
            throw new SaglikDegeriGecersizException("Kalori değeri pozitif olmalıdır");
        }
        this.kalori = kalori;
    }

    public int getSaglikDegeri() {
        return saglikDegeri;
    }

    public void setSaglikDegeri(int saglikDegeri) throws SaglikDegeriGecersizException {
        if (saglikDegeri < 1 || saglikDegeri > 100) {
            throw new SaglikDegeriGecersizException("Sağlık değeri 1-100 aralığında olmalıdır");
        }
        this.saglikDegeri = saglikDegeri;
    }

    @Override
    public String toString() {
        return ad + " (" + kalori + " kcal, sağlık=" + saglikDegeri + ")";
    }
}
