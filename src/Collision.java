// Boş satır açıklaması // Çarpışma hesaplayan yardımcı sınıf
public class Collision { // Statik çarpışma fonksiyonlarını içeren sınıf
    // Dikdörtgen tabanlı çarpışma kontrolü // Her iki varlığın konum ve boyutları kullanılır
    public static boolean checkRectIntersection(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) { // checkRectIntersection başlangıcı
        boolean overlapX = x1 < x2 + w2 && x1 + w1 > x2; // X ekseninde üst üste gelme kontrolü
        boolean overlapY = y1 < y2 + h2 && y1 + h1 > y2; // Y ekseninde üst üste gelme kontrolü
        return overlapX && overlapY; // Her iki eksen de kesişiyorsa çarpışma vardır
    } // checkRectIntersection sonu
    // Mermi ve düşman çarpışmasını kontrol eden metod // Bullet ve Enemy nesnelerini kullanır
    public static boolean checkBulletEnemyCollision(Bullet bullet, Enemy enemy) { // checkBulletEnemyCollision başlangıcı
        return checkRectIntersection(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight(), enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight()); // Dikdörtgen kesişim sonucu döndürülür
    } // checkBulletEnemyCollision sonu
    // Düşman ve oyuncu çarpışmasını kontrol eden metod // Enemy ve Player nesnelerini kullanır
    public static boolean checkEnemyPlayerCollision(Enemy enemy, Player player) { // checkEnemyPlayerCollision başlangıcı
        return checkRectIntersection(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight(), player.getX(), player.getY(), player.getWidth(), player.getHeight()); // Kesişim sonucu döndürülür
    } // checkEnemyPlayerCollision sonu
} // Collision sınıfı sonu
