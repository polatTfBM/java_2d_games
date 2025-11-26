public class Hesaplayici {
    private Hesaplayici() {
    }

    public static int kaloriToPuan(double kalori) {
        return (int) Math.max(1, kalori / 10);
    }

    public static int kaloriToPuan(double kalori, int bonus) {
        return kaloriToPuan(kalori) + bonus;
    }

    public static int kaloriToPuan(double kalori, int bonus, boolean ceza) {
        int puan = kaloriToPuan(kalori, bonus);
        return ceza ? puan / 2 : puan;
    }

    public static int toplamPuan(int yiyecekPuan, int aktivitePuan) {
        return yiyecekPuan + aktivitePuan;
    }

    public static int toplamPuan(int yiyecekPuan, int aktivitePuan, int suPuan) {
        return yiyecekPuan + aktivitePuan + suPuan;
    }
}
