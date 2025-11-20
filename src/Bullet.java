// Boş satır açıklaması // Mermi sınıfı dosyasının başlangıcı
public class Bullet { // Mermi davranışını temsil eden sınıf
    private int x; // Merminin x konumu
    private int y; // Merminin y konumu
    private final int width; // Mermi genişliği
    private final int height; // Mermi yüksekliği
    private final int speed; // Mermi hızı (y yönünde)
    // Yapıcı metod mermiyi başlangıç konumu ve hızla oluşturur // Sorumluluğu sadece veri atamasıdır
    public Bullet(int x, int y, int width, int height, int speed) { // Bullet yapıcısı
        this.x = x; // X konumunu atıyoruz
        this.y = y; // Y konumunu atıyoruz
        this.width = width; // Genişlik alanını atıyoruz
        this.height = height; // Yükseklik alanını atıyoruz
        this.speed = speed; // Hız alanını atıyoruz
    } // Yapıcı metod sonu
    // Her karede mermiyi hareket ettiren metod // Yukarı veya aşağı yönde ilerler
    public void update() { // update başlangıcı
        y += speed; // Y konumunu hız kadar güncelliyoruz
    } // update sonu
    // Mermi ekran sınırları içinde mi kontrol eden metod // Ekran dışında ise temizlenir
    public boolean isOnScreen(int screenHeight) { // isOnScreen başlangıcı
        return y + height >= 0 && y <= screenHeight; // Merminin görünür alanda olup olmadığını döndürüyoruz
    } // isOnScreen sonu
    // Getter metodlar // Diğer sınıflar konum bilgisine ihtiyaç duyuyor
    public int getX() { // X getter
        return x; // X değerini döndürüyoruz
    } // getX sonu
    public int getY() { // Y getter
        return y; // Y değerini döndürüyoruz
    } // getY sonu
    public int getWidth() { // Genişlik getter
        return width; // Genişlik değerini döndürüyoruz
    } // getWidth sonu
    public int getHeight() { // Yükseklik getter
        return height; // Yükseklik değerini döndürüyoruz
    } // getHeight sonu
} // Bullet sınıfı sonu
