// Boş satır açıklaması // Düşman sınıfı başlıyor
public class Enemy { // Düşman davranışını temsil eden sınıf
    private int x; // Düşmanın x konumu
    private int y; // Düşmanın y konumu
    private final int width; // Düşman genişliği
    private final int height; // Düşman yüksekliği
    private final int speed; // Düşmanın düşüş hızı
    // Yapıcı metod düşmanın temel özelliklerini alır // Sadece veri ataması yapar
    public Enemy(int x, int y, int width, int height, int speed) { // Enemy yapıcısı
        this.x = x; // X konumunu atıyoruz
        this.y = y; // Y konumunu atıyoruz
        this.width = width; // Genişlik alanını atıyoruz
        this.height = height; // Yükseklik alanını atıyoruz
        this.speed = speed; // Hız alanını atıyoruz
    } // Yapıcı metod sonu
    // Düşman her karede aşağı hareket eder // Basit düşüş davranışı
    public void update() { // update başlangıcı
        y += speed; // Y konumunu hız kadar artırıyoruz
    } // update sonu
    // Getter ve setter metodlar // Diğer sınıfların erişimi için
    public int getX() { // X getter
        return x; // X değerini döndürüyoruz
    } // getX sonu
    public int getY() { // Y getter
        return y; // Y değerini döndürüyoruz
    } // getY sonu
    public void setY(int y) { // Y setter
        this.y = y; // Y konumunu dışarıdan güncelliyoruz
    } // setY sonu
    public int getWidth() { // Genişlik getter
        return width; // Genişlik değerini döndürüyoruz
    } // getWidth sonu
    public int getHeight() { // Yükseklik getter
        return height; // Yükseklik değerini döndürüyoruz
    } // getHeight sonu
} // Enemy sınıfı sonu
