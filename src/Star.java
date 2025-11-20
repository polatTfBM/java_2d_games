// Boş satır açıklaması // Arka plan yıldız sınıfı
public class Star { // Yıldız partikülünü temsil eden sınıf
    private int x; // Yıldızın x konumu
    private int y; // Yıldızın y konumu
    private final int size; // Yıldız boyutu
    private final int speed; // Yıldızın düşüş hızı
    private int lastScreenHeight; // Son güncellemede kullanılan ekran yüksekliği
    // Yapıcı metod yıldızın temel özelliklerini alır // Sadece veri ataması yapar
    public Star(int x, int y, int size, int speed) { // Star yapıcısı
        this.x = x; // X konumunu atıyoruz
        this.y = y; // Y konumunu atıyoruz
        this.size = size; // Boyut bilgisini atıyoruz
        this.speed = speed; // Hız bilgisini atıyoruz
        this.lastScreenHeight = 0; // Ekran yüksekliği bilgisi başlangıçta 0
    } // Yapıcı metod sonu
    // Her karede yıldızı aşağı hareket ettirir // Basit kayma efekti
    public void update(int screenHeight) { // update başlangıcı
        lastScreenHeight = screenHeight; // Güncel ekran yüksekliğini saklıyoruz
        y += speed; // Y konumunu hız kadar artırıyoruz
        if (y > screenHeight) { // Ekran altına geçti mi kontrol
            y = screenHeight + size; // Ekran dışına taşarak temizlenmeye hazır hale getiriyoruz
        } // if sonu
    } // update sonu
    // Yıldız ekran dışına çıktı mı kontrol eder // GamePanel kaldırma için kullanır
    public boolean isOffScreen() { // isOffScreen başlangıcı
        return y > lastScreenHeight; // Y konumu panel yüksekliğini aştığında yıldız kaldırılmalı
    } // isOffScreen sonu
    // Getter metodları // Çizim için konum ve boyut bilgisi sağlar
    public int getX() { // X getter
        return x; // X değerini döndürüyoruz
    } // getX sonu
    public int getY() { // Y getter
        return y; // Y değerini döndürüyoruz
    } // getY sonu
    public int getSize() { // Boyut getter
        return size; // Boyut değerini döndürüyoruz
    } // getSize sonu
} // Star sınıfı sonu
