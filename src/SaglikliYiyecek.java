public abstract class SaglikliYiyecek extends Yiyecek {
    private boolean lifli;

    public SaglikliYiyecek(String ad, double kalori, int saglikDegeri, boolean lifli) throws SaglikDegeriGecersizException {
        super(ad, kalori, saglikDegeri);
        setLifli(lifli);
    }

    public boolean isLifli() {
        return lifli;
    }

    public void setLifli(boolean lifli) {
        this.lifli = lifli;
    }
}
