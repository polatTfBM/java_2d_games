import ui.MazeFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MazeFrame frame = new MazeFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
