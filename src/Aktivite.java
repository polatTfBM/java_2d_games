public abstract class Aktivite implements Puanlanabilir {
    private String ad;
    private int sureDakika;

    public Aktivite(String ad, int sureDakika) throws AktiviteSuresiGecersizException {
        setAd(ad);
        setSureDakika(sureDakika);
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) throws AktiviteSuresiGecersizException {
        if (ad == null || ad.isBlank()) {
            throw new AktiviteSuresiGecersizException("Aktivite adı boş olamaz");
        }
        this.ad = ad;
    }

    public int getSureDakika() {
        return sureDakika;
    }

    public void setSureDakika(int sureDakika) throws AktiviteSuresiGecersizException {
        if (sureDakika <= 0 || sureDakika > 300) {
            throw new AktiviteSuresiGecersizException("Aktivite süresi 1-300 dk aralığında olmalıdır");
        }
        this.sureDakika = sureDakika;
    }

    public abstract double harcananKalori();
}
