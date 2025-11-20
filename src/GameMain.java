import javax.swing.SwingUtilities; // SwingUtilities sınıfını kullanmak için import ediyoruz
// Bu satır boşluk yerine açıklama içererek her satırı yorumlamış oluyor // Boş satırlar bile açıklanmalı kuralına uyuyoruz
public class GameMain { // Uygulamanın ana giriş sınıfı
    public static void main(String[] args) { // Programın başlangıç noktası
        SwingUtilities.invokeLater(() -> new GameFrame()); // GUI işlemlerini Event Dispatch Thread üzerinde başlatıyoruz
    } // main metodu sonu
} // GameMain sınıfı sonu
