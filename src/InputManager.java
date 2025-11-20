import java.awt.event.KeyEvent; // KeyEvent sabitlerini kullanmak için import ediyoruz
import java.awt.event.KeyListener; // Klavye dinleyicisi için arayüzü import ediyoruz
// Boş satır açıklaması // Tüm satırların yorumla bittiğinden emin oluyoruz
public class InputManager implements KeyListener { // Klavye girişlerini yöneten sınıf
    private boolean left; // Sol yön tuşu veya A durumu
    private boolean right; // Sağ yön tuşu veya D durumu
    private boolean up; // Yukarı yön tuşu veya W durumu
    private boolean down; // Aşağı yön tuşu veya S durumu
    private boolean shooting; // Ateş etme tuşu durumu
    private boolean restartPressed; // Yeniden başlatma için ENTER durumu
    // Yapıcı metod başlangıcı // Başlangıç değerleri false
    public InputManager() { // InputManager yapıcısı
        left = false; // Sol basılı değil
        right = false; // Sağ basılı değil
        up = false; // Yukarı basılı değil
        down = false; // Aşağı basılı değil
        shooting = false; // Ateş basılı değil
        restartPressed = false; // Restart basılı değil
    } // Yapıcı metod sonu
    // KeyListener arayüzünden gelen metod // Kullanılmayan parametre için açıklama ekliyoruz
    @Override // keyTyped üzerine yazılıyor
    public void keyTyped(KeyEvent e) { // Tuş basılıp bırakıldığında tetiklenen ama kullanılmayan metod
        // Bu metod kullanılmıyor çünkü basılı tutma durumlarına ihtiyacımız var // Boş gövdeli ama açıklamalı
    } // keyTyped sonu
    // Tuş basıldığında çalışan metod // Hareket ve ateş bayraklarını set eder
    @Override // keyPressed üzerine yazılıyor
    public void keyPressed(KeyEvent e) { // Tuş basılma olayı
        int key = e.getKeyCode(); // Basılan tuşun kodunu alıyoruz
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) { // A veya sol ok basıldıysa
            left = true; // Sol bayrağını açıyoruz
        } // if sonu
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) { // D veya sağ ok basıldıysa
            right = true; // Sağ bayrağını açıyoruz
        } // if sonu
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) { // W veya yukarı ok basıldıysa
            up = true; // Yukarı bayrağını açıyoruz
        } // if sonu
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) { // S veya aşağı ok basıldıysa
            down = true; // Aşağı bayrağını açıyoruz
        } // if sonu
        if (key == KeyEvent.VK_SPACE) { // SPACE basıldıysa
            shooting = true; // Ateş bayrağını açıyoruz
        } // if sonu
        if (key == KeyEvent.VK_ENTER) { // ENTER basıldıysa
            restartPressed = true; // Yeniden başlat bayrağını açıyoruz
        } // if sonu
    } // keyPressed sonu
    // Tuş bırakıldığında çalışan metod // Basılı durumları false yapar
    @Override // keyReleased üzerine yazılıyor
    public void keyReleased(KeyEvent e) { // Tuş bırakma olayı
        int key = e.getKeyCode(); // Bırakılan tuşun kodunu alıyoruz
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) { // A veya sol ok bırakıldıysa
            left = false; // Sol bayrağını kapatıyoruz
        } // if sonu
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) { // D veya sağ ok bırakıldıysa
            right = false; // Sağ bayrağını kapatıyoruz
        } // if sonu
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) { // W veya yukarı ok bırakıldıysa
            up = false; // Yukarı bayrağını kapatıyoruz
        } // if sonu
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) { // S veya aşağı ok bırakıldıysa
            down = false; // Aşağı bayrağını kapatıyoruz
        } // if sonu
        if (key == KeyEvent.VK_SPACE) { // SPACE bırakıldıysa
            shooting = false; // Ateş bayrağını kapatıyoruz
        } // if sonu
        if (key == KeyEvent.VK_ENTER) { // ENTER bırakıldıysa
            restartPressed = false; // Yeniden başlat bayrağını kapatıyoruz
        } // if sonu
    } // keyReleased sonu
    // Aşağıdaki getter metodlar panelin ihtiyaç duyduğu bayrak bilgilerini döndürür // Temiz arayüz sağlar
    public boolean isLeft() { // Sol basılı mı
        return left; // Sol bayrak değeri dönüyor
    } // isLeft sonu
    public boolean isRight() { // Sağ basılı mı
        return right; // Sağ bayrak değeri dönüyor
    } // isRight sonu
    public boolean isUp() { // Yukarı basılı mı
        return up; // Yukarı bayrak değeri dönüyor
    } // isUp sonu
    public boolean isDown() { // Aşağı basılı mı
        return down; // Aşağı bayrak değeri dönüyor
    } // isDown sonu
    public boolean isShooting() { // Ateş tuşu basılı mı
        return shooting; // Ateş bayrak değeri dönüyor
    } // isShooting sonu
    public boolean isRestartPressed() { // Enter basıldı mı
        return restartPressed; // Restart bayrak değeri dönüyor
    } // isRestartPressed sonu
} // InputManager sınıfı sonu
