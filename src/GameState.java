// Boş satır açıklaması // Oyun durumunu tutan sınıf
public class GameState { // Skor ve can gibi değerleri yöneten sınıf
    private int score; // Oyuncunun skoru
    private int lives; // Oyuncunun kalan canı
    private boolean gameOver; // Oyun bitiş durumu
    private final int initialLives; // Başlangıç can değeri
    // Yapıcı metod başlangıç canını alır // Skoru sıfırlar ve oyunu başlatır
    public GameState(int initialLives) { // GameState yapıcısı
        this.initialLives = initialLives; // Başlangıç canını saklıyoruz
        reset(); // Skor ve canı başlangıç değerlerine çekiyoruz
    } // Yapıcı metod sonu
    // Skoru artıran metod // Pozitif değer bekleniyor
    public void addScore(int amount) { // addScore başlangıcı
        score += amount; // Skoru belirtilen miktarda artırıyoruz
    } // addScore sonu
    // Can kaybını yöneten metod // Can 0'a düşerse gameOver true olur
    public void loseLife() { // loseLife başlangıcı
        lives--; // Canı bir azaltıyoruz
        if (lives <= 0) { // Can bitti mi kontrol
            gameOver = true; // Oyunu bitiriyoruz
        } // if sonu
    } // loseLife sonu
    // Oyun durumunu başa saran metod // Skor ve canı yeniliyor
    public void reset() { // reset başlangıcı
        score = 0; // Skoru sıfırlıyoruz
        lives = initialLives; // Canı başlangıç değerine getiriyoruz
        gameOver = false; // Oyun devam ediyor olarak işaretliyoruz
    } // reset sonu
    // Getter ve setter metodlar // Dış dünya ile kontrollü paylaşım
    public int getScore() { // Skor getter
        return score; // Skor değerini döndürüyoruz
    } // getScore sonu
    public int getLives() { // Can getter
        return lives; // Can değerini döndürüyoruz
    } // getLives sonu
    public boolean isGameOver() { // Oyun bitti mi
        return gameOver; // Game over bayrağını döndürüyoruz
    } // isGameOver sonu
    public void setGameOver(boolean gameOver) { // Game over setter
        this.gameOver = gameOver; // Bayrağı güncelliyoruz
    } // setGameOver sonu
} // GameState sınıfı sonu
