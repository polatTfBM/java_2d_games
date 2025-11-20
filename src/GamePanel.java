import javax.swing.JPanel; // JPanel sınıfını kullanmak için import ediyoruz
import javax.swing.Timer; // Oyun döngüsü için Swing Timer kullanıyoruz
import java.awt.Color; // Renkleri kullanmak için import
import java.awt.Dimension; // Panel boyutlarını ayarlamak için kullanıyoruz
import java.awt.Font; // Yazı tipi ayarı için import
import java.awt.Graphics; // Çizim işlemleri için Graphics sınıfı
import java.awt.Graphics2D; // Daha gelişmiş çizim işlemleri için Graphics2D
import java.awt.RenderingHints; // Anti-aliasing ayarları için
import java.awt.event.ActionEvent; // Timer olayları için
import java.awt.event.ActionListener; // Timer ile çalışmak için
import java.util.ArrayList; // Dinamik listeler için
import java.util.Iterator; // Liste üzerinde güvenli gezinme için
import java.util.List; // List arayüzü için
import java.util.Random; // Rastgele değer üretmek için
// Boş satır kural gereği açıklama içerir // Her satırın sonunda Türkçe yorum olmalı
public class GamePanel extends JPanel implements ActionListener { // Oyun paneli sınıfı, çizim ve güncellemeden sorumlu
    private final int width; // Panel genişliği saklanıyor
    private final int height; // Panel yüksekliği saklanıyor
    private final Timer timer; // 60 FPS hedefleyen Swing Timer
    private final Player player; // Oyuncu nesnesi
    private final InputManager inputManager; // Giriş yönetimi nesnesi
    private final List<Bullet> bullets; // Aktif mermiler listesi
    private final List<Enemy> enemies; // Aktif düşmanlar listesi
    private final List<Star> stars; // Arka plan yıldız listesi
    private final Random random; // Düşman ve yıldız üretimi için rastgele üreteç
    private final GameState gameState; // Skor ve can bilgisini tutan oyun durumu nesnesi
    private int enemySpawnCounter; // Düşman üretim süresini takip eden sayaç
    private int starSpawnCounter; // Yıldız üretim süresini takip eden sayaç
    // Yapıcı metod paneli kuruyor // Tek sorumluluk kurulumu tamamlamak
    public GamePanel(int width, int height) { // GamePanel yapıcısı
        this.width = width; // Genişlik alanına parametre ataması
        this.height = height; // Yükseklik alanına parametre ataması
        setPreferredSize(new Dimension(width, height)); // Panelin tercih edilen boyutunu ayarlıyoruz
        setBackground(Color.BLACK); // Arka plan rengini siyah yapıyoruz
        setFocusable(true); // Klavye odaklanması için paneli odaklanabilir yapıyoruz
        inputManager = new InputManager(); // Giriş yöneticisini oluşturuyoruz
        addKeyListener(inputManager); // Tuş dinleyicisini panele ekliyoruz
        player = new Player(width / 2 - 20, height - 100, 40, 40, 5); // Oyuncu gemisini ortada konumlandırıyoruz
        bullets = new ArrayList<>(); // Mermi listesini başlatıyoruz
        enemies = new ArrayList<>(); // Düşman listesini başlatıyoruz
        stars = new ArrayList<>(); // Yıldız listesini başlatıyoruz
        random = new Random(); // Rastgele üreteç oluşturuyoruz
        gameState = new GameState(3); // Oyuncuya 3 can vererek oyun durumunu başlatıyoruz
        enemySpawnCounter = 0; // Başlangıçta düşman üretim sayacını sıfırlıyoruz
        starSpawnCounter = 0; // Başlangıçta yıldız üretim sayacını sıfırlıyoruz
        timer = new Timer(16, this); // Yaklaşık 60 FPS için 16 ms aralıkla timer oluşturuyoruz
        timer.start(); // Oyun döngüsünü başlatıyoruz
    } // Yapıcı metod sonu
    // Timer'dan gelen olayları ele alıyoruz // Oyun döngüsü buradan tetikleniyor
    @Override // Üst sınıftan gelen metodun üzerine yazıldığını belirtiyoruz
    public void actionPerformed(ActionEvent e) { // Timer tetiklenince çalışacak metod
        if (gameState.isGameOver()) { // Eğer oyun bitmişse
            if (inputManager.isRestartPressed()) { // ENTER tuşuna basıldı mı kontrol ediyoruz
                gameState.setGameOver(false); // Oyun bitti bayrağını temizliyoruz
                restartGame(); // Tüm durumları sıfırlayıp oyunu yeniden başlatıyoruz
            } // if sonu
            repaint(); // Ekranı sadece yeniden çiziyoruz
            return; // Daha fazla güncelleme yapmadan metodu sonlandırıyoruz
        } // if sonu
        updateGame(); // Oyun mantığını güncelliyoruz
        repaint(); // Görüntüyü yeniden çiziyoruz
    } // actionPerformed sonu
    // Oyun mantığını adım adım güncelleyen metod // Hareket, üretim ve çarpışma kontrolünü içerir
    private void updateGame() { // updateGame başlangıcı
        handleInput(); // Kullanıcı girişlerini oyuncuya uygula
        updateEntities(); // Tüm varlıkların konumlarını güncelle
        spawnEnemies(); // Belirli aralıklarla düşman üret
        spawnStars(); // Arka plan yıldızlarını oluştur
        handleCollisions(); // Çarpışmaları kontrol et
        checkGameOver(); // Can bitti mi kontrol et
    } // updateGame sonu
    // Klavye girişlerini okuyan metod // InputManager'daki bayrakları kullanır
    private void handleInput() { // handleInput başlangıcı
        if (inputManager.isLeft()) { // Sol tuş basılı mı kontrol ediyoruz
            player.move(-player.getSpeed(), 0, width, height); // Oyuncuyu sola doğru hareket ettiriyoruz
        } // if sonu
        if (inputManager.isRight()) { // Sağ tuş basılı mı kontrol ediyoruz
            player.move(player.getSpeed(), 0, width, height); // Oyuncuyu sağa doğru hareket ettiriyoruz
        } // if sonu
        if (inputManager.isUp()) { // Yukarı tuş basılı mı kontrol ediyoruz
            player.move(0, -player.getSpeed(), width, height); // Oyuncuyu yukarı hareket ettiriyoruz
        } // if sonu
        if (inputManager.isDown()) { // Aşağı tuş basılı mı kontrol ediyoruz
            player.move(0, player.getSpeed(), width, height); // Oyuncuyu aşağı hareket ettiriyoruz
        } // if sonu
        if (inputManager.isShooting()) { // Ateş tuşu kontrolü
            Bullet bullet = player.shoot(); // Oyuncudan mermi üretmesini istiyoruz
            if (bullet != null) { // Mermi döndüyse
                bullets.add(bullet); // Mermiyi listeye ekliyoruz
            } // if sonu
        } // if sonu
    } // handleInput sonu
    // Varlıkları güncelleyen metod // Mermiler, düşmanlar ve yıldızlar hareket ediyor
    private void updateEntities() { // updateEntities başlangıcı
        Iterator<Bullet> bulletIterator = bullets.iterator(); // Güvenli kaldırma için iterator kullanıyoruz
        while (bulletIterator.hasNext()) { // Mermi listesi üzerinde gezinme
            Bullet bullet = bulletIterator.next(); // Güncel mermiyi alıyoruz
            bullet.update(); // Mermi konumunu güncelliyoruz
            if (!bullet.isOnScreen(height)) { // Mermi ekran dışına çıktı mı kontrol ediyoruz
                bulletIterator.remove(); // Ekran dışına çıkan mermiyi listeden kaldırıyoruz
            } // if sonu
        } // while sonu
        Iterator<Enemy> enemyIterator = enemies.iterator(); // Düşman listesi için iterator
        while (enemyIterator.hasNext()) { // Düşmanlar üzerinde gezinme
            Enemy enemy = enemyIterator.next(); // Güncel düşmanı alıyoruz
            enemy.update(); // Düşman konumunu güncelliyoruz
            if (enemy.getY() > height) { // Düşman ekranın altına geçti mi
                enemyIterator.remove(); // Düşmanı listeden kaldırıyoruz
                gameState.loseLife(); // Oyuncudan can düşürüyoruz
            } // if sonu
        } // while sonu
        Iterator<Star> starIterator = stars.iterator(); // Yıldız listesi için iterator
        while (starIterator.hasNext()) { // Yıldızlar üzerinde gezinme
            Star star = starIterator.next(); // Güncel yıldızı alıyoruz
            star.update(height); // Yıldız konumunu güncelliyoruz
            if (star.isOffScreen()) { // Yıldız ekran dışına çıktı mı kontrol ediyoruz
                starIterator.remove(); // Yıldızı listeden kaldırıyoruz
            } // if sonu
        } // while sonu
    } // updateEntities sonu
    // Düşman üretimini yöneten metod // Belirli aralıklarla yeni düşmanlar ekler
    private void spawnEnemies() { // spawnEnemies başlangıcı
        enemySpawnCounter++; // Sayaç artırılıyor
        if (enemySpawnCounter >= 45) { // Yaklaşık 0.75 saniyede bir üretim (60 FPS varsayımı)
            int enemyWidth = 40; // Düşman genişliği
            int enemyHeight = 40; // Düşman yüksekliği
            int x = random.nextInt(width - enemyWidth); // Düşmanı yatayda rastgele konumlandırıyoruz
            int speed = 2 + random.nextInt(3); // Hız aralığını rastgele belirliyoruz
            enemies.add(new Enemy(x, -enemyHeight, enemyWidth, enemyHeight, speed)); // Yeni düşmanı listeye ekliyoruz
            enemySpawnCounter = 0; // Sayaç sıfırlanıyor
        } // if sonu
    } // spawnEnemies sonu
    // Arka plan yıldızlarını oluşturan metod // Basit kayma efekti verir
    private void spawnStars() { // spawnStars başlangıcı
        starSpawnCounter++; // Sayaç artırılıyor
        if (starSpawnCounter >= 6) { // Daha sık üretim ile akıcı arka plan sağlanır
            int size = 1 + random.nextInt(3); // Yıldız boyutunu rastgele belirliyoruz
            int x = random.nextInt(width); // Yıldızın yatay konumu rastgele
            int speed = 1 + random.nextInt(3); // Yıldız düşüş hızını belirliyoruz
            stars.add(new Star(x, 0, size, speed)); // Yeni yıldızı listeye ekliyoruz
            starSpawnCounter = 0; // Sayaç sıfırlanıyor
        } // if sonu
    } // spawnStars sonu
    // Çarpışmaları yöneten metod // Mermi-düşman ve düşman-oyuncu etkileşimlerini kontrol eder
    private void handleCollisions() { // handleCollisions başlangıcı
        Iterator<Bullet> bulletIterator = bullets.iterator(); // Mermileri dolaşmak için iterator
        while (bulletIterator.hasNext()) { // Mermi listesi üzerinde gezinme
            Bullet bullet = bulletIterator.next(); // Güncel mermiyi alıyoruz
            Iterator<Enemy> enemyIterator = enemies.iterator(); // Her mermi için düşmanları geziyoruz
            while (enemyIterator.hasNext()) { // Düşman listesi üzerinde gezinme
                Enemy enemy = enemyIterator.next(); // Güncel düşmanı alıyoruz
                if (Collision.checkBulletEnemyCollision(bullet, enemy)) { // Çarpışma var mı kontrolü
                    bulletIterator.remove(); // Çarpışan mermiyi kaldırıyoruz
                    enemyIterator.remove(); // Çarpışan düşmanı kaldırıyoruz
                    gameState.addScore(10); // Skoru artırıyoruz
                    break; // Bu mermi başka düşmana çarpamaz, döngüden çıkıyoruz
                } // if sonu
            } // iç while sonu
        } // dış while sonu
        for (Enemy enemy : enemies) { // Kalan düşmanları dolaşıyoruz
            if (Collision.checkEnemyPlayerCollision(enemy, player)) { // Düşman oyuncuya çarptı mı
                gameState.loseLife(); // Oyuncunun canını düşürüyoruz
                enemy.setY(height + 1); // Düşmanı ekran dışına taşıyarak temizlenmesini sağlıyoruz
            } // if sonu
        } // for sonu
    } // handleCollisions sonu
    // Oyun bitişini kontrol eden metod // Can kalmadıysa oyunu durdurur
    private void checkGameOver() { // checkGameOver başlangıcı
        if (gameState.getLives() <= 0) { // Can 0 veya altına düştüyse
            gameState.setGameOver(true); // Oyun bitiş bayrağını açıyoruz
            timer.stop(); // Timer'ı durduruyoruz
        } // if sonu
    } // checkGameOver sonu
    // Panelin çizim metodunu override ediyoruz // Tüm varlıklar burada çizilir
    @Override // paintComponent üzerine yazılıyor
    protected void paintComponent(Graphics g) { // Swing çizim metodu
        super.paintComponent(g); // Panelin varsayılan boyama işlemini çağırıyoruz
        Graphics2D g2d = (Graphics2D) g; // Graphics2D'ye cast ediyoruz
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Kenar yumuşatma açılıyor
        drawStars(g2d); // Arka plan yıldızlarını çiziyoruz
        drawPlayer(g2d); // Oyuncu gemisini çiziyoruz
        drawBullets(g2d); // Mermileri çiziyoruz
        drawEnemies(g2d); // Düşmanları çiziyoruz
        drawHUD(g2d); // Skor ve can bilgilerini çiziyoruz
        if (gameState.isGameOver()) { // Oyun bittiyse
            drawGameOver(g2d); // GAME OVER mesajını çiziyoruz
        } // if sonu
    } // paintComponent sonu
    // Yıldızları çizen yardımcı metod // Basit beyaz noktalar halinde çiziliyor
    private void drawStars(Graphics2D g2d) { // drawStars başlangıcı
        g2d.setColor(Color.WHITE); // Rengi beyaza ayarlıyoruz
        for (Star star : stars) { // Tüm yıldızları dolaşıyoruz
            g2d.fillRect(star.getX(), star.getY(), star.getSize(), star.getSize()); // Yıldızı kare olarak çiziyoruz
        } // for sonu
    } // drawStars sonu
    // Oyuncuyu çizen metod // Oyuncu mavi bir dikdörtgen olarak temsil ediliyor
    private void drawPlayer(Graphics2D g2d) { // drawPlayer başlangıcı
        g2d.setColor(Color.CYAN); // Rengi cam göbeğine ayarlıyoruz
        g2d.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight()); // Oyuncu gemisini çiziyoruz
    } // drawPlayer sonu
    // Mermileri çizen metod // Kırmızı renkli dikdörtgenler olarak çiziliyor
    private void drawBullets(Graphics2D g2d) { // drawBullets başlangıcı
        g2d.setColor(Color.RED); // Rengi kırmızı yapıyoruz
        for (Bullet bullet : bullets) { // Tüm mermileri dolaşıyoruz
            g2d.fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight()); // Mermiyi dikdörtgen olarak çiziyoruz
        } // for sonu
    } // drawBullets sonu
    // Düşmanları çizen metod // Sarı dikdörtgen olarak çiziliyorlar
    private void drawEnemies(Graphics2D g2d) { // drawEnemies başlangıcı
        g2d.setColor(Color.YELLOW); // Rengi sarıya ayarlıyoruz
        for (Enemy enemy : enemies) { // Düşman listesi üzerinde dolaşıyoruz
            g2d.fillRect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight()); // Düşmanı dikdörtgen olarak çiziyoruz
        } // for sonu
    } // drawEnemies sonu
    // Skor ve can bilgisini gösteren HUD çizimi // Ekranın sol üst köşesine yerleştiriliyor
    private void drawHUD(Graphics2D g2d) { // drawHUD başlangıcı
        g2d.setColor(Color.WHITE); // Yazı rengini beyaza ayarlıyoruz
        g2d.setFont(new Font("Arial", Font.BOLD, 18)); // Fontu belirliyoruz
        g2d.drawString("Skor: " + gameState.getScore(), 10, 20); // Skoru ekrana yazdırıyoruz
        g2d.drawString("Can: " + gameState.getLives(), 10, 40); // Can bilgisini ekrana yazdırıyoruz
    } // drawHUD sonu
    // Oyun bitiş ekranını çizen metod // Kullanıcıya yeniden başlatma talimatı verir
    private void drawGameOver(Graphics2D g2d) { // drawGameOver başlangıcı
        g2d.setColor(Color.RED); // Yazı rengini kırmızı yapıyoruz
        g2d.setFont(new Font("Arial", Font.BOLD, 48)); // Büyük puntolu font ayarlıyoruz
        String text = "GAME OVER"; // Gösterilecek metin
        int textWidth = g2d.getFontMetrics().stringWidth(text); // Metin genişliğini ölçüyoruz
        g2d.drawString(text, (width - textWidth) / 2, height / 2); // Metni ekranın ortasına çiziyoruz
        g2d.setFont(new Font("Arial", Font.PLAIN, 24)); // Alt talimat için daha küçük font
        String restart = "Yeniden başlamak için ENTER"; // Kullanıcıya talimat veriyoruz
        int restartWidth = g2d.getFontMetrics().stringWidth(restart); // Talimat metni genişliğini ölçüyoruz
        g2d.drawString(restart, (width - restartWidth) / 2, height / 2 + 40); // Talimatı ortalayarak çiziyoruz
    } // drawGameOver sonu
    // Panel odaklandığında tuş dinleyicisini aktifleştiren metod // GameState'i de sıfırlamayı yönetir
    @Override // Üst sınıf metodunun üzerine yazma
    public void addNotify() { // Component eklendiğinde çağrılan metod
        super.addNotify(); // Üst sınıf davranışını koruyoruz
        requestFocusInWindow(); // Klavye odağını panelin almasını sağlıyoruz
    } // addNotify sonu
    // Kullanıcı ENTER'a basarak oyunu yeniden başlatmak istediğinde çağrılacak metod // Tüm durumları sıfırlar
    public void restartGame() { // restartGame başlangıcı
        bullets.clear(); // Mermileri temizliyoruz
        enemies.clear(); // Düşmanları temizliyoruz
        stars.clear(); // Yıldızları temizliyoruz
        player.resetPosition(width / 2 - player.getWidth() / 2, height - 100); // Oyuncuyu başlangıç konumuna getiriyoruz
        gameState.reset(); // Skor ve can bilgisini sıfırlıyoruz
        enemySpawnCounter = 0; // Sayaçları sıfırlıyoruz
        starSpawnCounter = 0; // Sayaçları sıfırlıyoruz
        timer.start(); // Timer'ı yeniden başlatıyoruz
    } // restartGame sonu
    // Giriş yöneticisine dışarıdan erişim sağlayan metod // GameFrame gerekirse kullanabilir
    public InputManager getInputManager() { // getInputManager başlangıcı
        return inputManager; // Giriş yöneticisini döndürüyoruz
    } // getInputManager sonu
} // GamePanel sınıfı sonu
