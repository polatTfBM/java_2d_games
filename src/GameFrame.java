import javax.swing.JFrame; // JFrame sınıfını kullanmak için import ediyoruz
// Bu satır kural gereği açıklama içeriyor // Boş satır yerine yorum ekleniyor
public class GameFrame extends JFrame { // Oyun penceresini temsil eden sınıf
    private static final int WIDTH = 480; // Pencere genişliği sabiti
    private static final int HEIGHT = 720; // Pencere yüksekliği sabiti
    // Yapıcı metod penceremizi ayarlıyor // Tek sorumluluk pencere kurulumudur
    public GameFrame() { // GameFrame yapıcısı
        setTitle("Space Shooter"); // Pencere başlığını ayarlıyoruz
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Kapatma butonuna basıldığında uygulamayı sonlandırıyoruz
        setResizable(false); // Kullanıcının pencere boyutunu değiştirmesini engelliyoruz
        GamePanel panel = new GamePanel(WIDTH, HEIGHT); // Oyun panelini belirtilen boyutlarda oluşturuyoruz
        add(panel); // Paneli pencereye ekliyoruz
        pack(); // Panelin preferred size'ına göre pencereyi boyutlandırıyoruz
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına konumluyoruz
        setVisible(true); // Pencereyi görünür hale getiriyoruz
    } // Yapıcı metod sonu
} // GameFrame sınıfı sonu
