public class FizikselAktivite extends Aktivite {
    private double agirlik; // kg

    public FizikselAktivite(String ad, int sureDakika, double agirlik) throws AktiviteSuresiGecersizException {
        super(ad, sureDakika);
        setAgirlik(agirlik);
    }

    public double getAgirlik() {
        return agirlik;
    }

    public void setAgirlik(double agirlik) throws AktiviteSuresiGecersizException {
        if (agirlik <= 0) {
            throw new AktiviteSuresiGecersizException("Ağırlık pozitif olmalıdır");
        }
        this.agirlik = agirlik;
    }

    @Override
    public double harcananKalori() {
        return (agirlik * getSureDakika()) / 5.0;
    }

    @Override
    public int puanHesapla() {
        return Hesaplayici.kaloriToPuan(harcananKalori(), 10);
    }
}
