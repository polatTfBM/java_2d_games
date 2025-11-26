public class Kosu extends FizikselAktivite {
    private double mesafe; // km

    public Kosu(String ad, int sureDakika, double agirlik, double mesafe) throws AktiviteSuresiGecersizException {
        super(ad, sureDakika, agirlik);
        setMesafe(mesafe);
    }

    public double getMesafe() {
        return mesafe;
    }

    public void setMesafe(double mesafe) throws AktiviteSuresiGecersizException {
        if (mesafe <= 0) {
            throw new AktiviteSuresiGecersizException("Mesafe pozitif olmalıdır");
        }
        this.mesafe = mesafe;
    }

    @Override
    public double harcananKalori() {
        return super.harcananKalori() + mesafe * 30;
    }

    @Override
    public String toString() {
        return "Koşu: " + getAd() + " (" + getSureDakika() + " dk, " + mesafe + " km)";
    }
}
