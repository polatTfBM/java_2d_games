// Bu satır kurala uygun açıklama sağlıyor // Dosya içinde boş satır bırakmıyoruz
public class Player { // Oyuncu gemisini temsil eden sınıf
    private int x; // Oyuncunun x konumu
    private int y; // Oyuncunun y konumu
    private final int width; // Oyuncu genişliği
    private final int height; // Oyuncu yüksekliği
    private final int speed; // Oyuncu hareket hızı
    private final int bulletWidth; // Mermi genişliği sabiti
    private final int bulletHeight; // Mermi yüksekliği sabiti
    private final int bulletSpeed; // Mermi hızı sabiti
    private long lastShotTime; // Son ateş zamanı
    private final long shotCooldown; // İki atış arasındaki minimum süre
    // Yapıcı metod oyuncuyu başlangıç değerleriyle kurar // Parametreler boyut ve hız içerir
    public Player(int x, int y, int width, int height, int speed) { // Player yapıcısı
        this.x = x; // X konumunu atıyoruz
        this.y = y; // Y konumunu atıyoruz
        this.width = width; // Genişlik alanını atıyoruz
        this.height = height; // Yükseklik alanını atıyoruz
        this.speed = speed; // Hız alanını atıyoruz
        this.bulletWidth = 6; // Mermi genişliğini sabitliyoruz
        this.bulletHeight = 14; // Mermi yüksekliğini sabitliyoruz
        this.bulletSpeed = 8; // Mermi hızını sabitliyoruz
        this.lastShotTime = 0; // Son ateş zamanını başlatıyoruz
        this.shotCooldown = 180; // 180 ms bekleme ile ateş hızını sınırlıyoruz
    } // Yapıcı metod sonu
    // Oyuncuyu verilen deltaX ve deltaY kadar hareket ettirir // Panel sınırları dışına çıkmasını engeller
    public void move(int dx, int dy, int maxWidth, int maxHeight) { // Hareket metodu başlangıcı
        x += dx; // X konumunu değiştiriyoruz
        y += dy; // Y konumunu değiştiriyoruz
        if (x < 0) { // Sol sınır kontrolü
            x = 0; // Sol sınırda tutuyoruz
        } // if sonu
        if (x + width > maxWidth) { // Sağ sınır kontrolü
            x = maxWidth - width; // Sağ sınırı aşmasını engelliyoruz
        } // if sonu
        if (y < 0) { // Üst sınır kontrolü
            y = 0; // Üste çıkmasını engelliyoruz
        } // if sonu
        if (y + height > maxHeight) { // Alt sınır kontrolü
            y = maxHeight - height; // Alta inmesini engelliyoruz
        } // if sonu
    } // move metodu sonu
    // Ateş etme metodudur // Cooldown'a göre yeni mermi üretir
    public Bullet shoot() { // shoot metodu başlangıcı
        long currentTime = System.currentTimeMillis(); // Şimdiki zamanı alıyoruz
        if (currentTime - lastShotTime < shotCooldown) { // Eğer bekleme süresi dolmadıysa
            return null; // Yeni mermi üretmiyoruz
        } // if sonu
        lastShotTime = currentTime; // Son atış zamanını güncelliyoruz
        int bulletX = x + width / 2 - bulletWidth / 2; // Merminin x konumunu ortalıyoruz
        int bulletY = y - bulletHeight; // Mermiyi oyuncunun üstünde başlatıyoruz
        return new Bullet(bulletX, bulletY, bulletWidth, bulletHeight, -bulletSpeed); // Yukarı yönlü mermi oluşturup döndürüyoruz
    } // shoot metodu sonu
    // Oyuncunun başlangıç konumuna dönmesini sağlayan metod // Restart sırasında kullanılır
    public void resetPosition(int startX, int startY) { // resetPosition başlangıcı
        this.x = startX; // X konumunu resetliyoruz
        this.y = startY; // Y konumunu resetliyoruz
        this.lastShotTime = 0; // Ateş zamanını sıfırlıyoruz
    } // resetPosition sonu
    // Getter metodları // Diğer sınıflar pozisyon ve boyut bilgisine ihtiyaç duyar
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
    public int getSpeed() { // Hız getter
        return speed; // Hız değerini döndürüyoruz
    } // getSpeed sonu
} // Player sınıfı sonu
