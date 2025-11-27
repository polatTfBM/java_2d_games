public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "gui".equalsIgnoreCase(args[0])) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                OyunArayuzu arayuz = new OyunArayuzu();
                arayuz.setVisible(true);
            });
        } else {
            OyunYoneticisi yonetici = new OyunYoneticisi();
            yonetici.baslat();
        }
    }
}
