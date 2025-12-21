package abstract_;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractFrame extends JFrame {

    protected AbstractFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void center() {
        setLocationRelativeTo(null);
    }

    protected abstract void buildLayout();

    protected abstract void bindEvents();
}
