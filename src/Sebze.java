public class Sebze extends SaglikliYiyecek {
    private boolean yesilYaprakli;

    public Sebze(String ad, double kalori, int saglikDegeri, boolean lifli, boolean yesilYaprakli) throws SaglikDegeriGecersizException {
        super(ad, kalori, saglikDegeri, lifli);
        setYesilYaprakli(yesilYaprakli);
    }

    public boolean isYesilYaprakli() {
        return yesilYaprakli;
    }

    public void setYesilYaprakli(boolean yesilYaprakli) {
        this.yesilYaprakli = yesilYaprakli;
    }

    @Override
    public void tuket(Oyuncu oyuncu) {
        oyuncu.setToplamKalori(oyuncu.getToplamKalori() + getKalori());
        oyuncu.setToplamPuan(oyuncu.getToplamPuan() + puanHesapla());
    }

    @Override
    public int puanHesapla() {
        int temel = Hesaplayici.kaloriToPuan(getKalori());
        return yesilYaprakli ? temel + 5 : temel;
    }

    @Override
    public String toString() {
        return "Sebze: " + super.toString();
    }
}
