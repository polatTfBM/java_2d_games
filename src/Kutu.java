public class Kutu<T> {
    private T icerik;

    public Kutu() {
    }

    public Kutu(T icerik) {
        this.icerik = icerik;
    }

    public T getIcerik() {
        return icerik;
    }

    public void setIcerik(T icerik) {
        this.icerik = icerik;
    }
}
