package com.example.binarysearch;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple Swing application that visualizes how the binary search algorithm works.
 * The animation highlights the low, mid, and high indices for each step until the
 * target is found or the search space is exhausted.
 */
public class BinarySearchAnimation extends JPanel {

    private static final int PADDING = 40;
    private static final int BAR_WIDTH = 50;
    private static final int BAR_HEIGHT = 60;
    private static final int TIMER_DELAY_MS = 1000;

    private final int[] numbers;
    private final int target;
    private final List<SearchStep> steps;
    private int currentStep = -1;
    private final Timer timer;

    private static class SearchStep {
        final int low;
        final int mid;
        final int high;
        final int comparison;

        SearchStep(int low, int mid, int high, int comparison) {
            this.low = low;
            this.mid = mid;
            this.high = high;
            this.comparison = comparison;
        }
    }

    private BinarySearchAnimation(int[] numbers, int target) {
        this.numbers = numbers;
        this.target = target;
        this.steps = createSteps(numbers, target);

        setBackground(Color.decode("#0f172a"));
        setPreferredSize(new Dimension(PADDING * 2 + numbers.length * BAR_WIDTH, 350));

        Timer animationTimer = new Timer(TIMER_DELAY_MS, null);
        animationTimer.addActionListener(e -> {
            currentStep++;
            if (currentStep >= steps.size()) {
                animationTimer.stop();
            }
            repaint();
        });
        animationTimer.setInitialDelay(500);
        animationTimer.start();
        this.timer = animationTimer;
    }

    private static List<SearchStep> createSteps(int[] numbers, int target) {
        List<SearchStep> steps = new ArrayList<>();
        int low = 0;
        int high = numbers.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = Integer.compare(numbers[mid], target);
            steps.add(new SearchStep(low, mid, high, comparison));

            if (comparison == 0) {
                break;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        // Add a final state to show the search ended without a match.
        if (steps.isEmpty() || steps.get(steps.size() - 1).comparison != 0) {
            steps.add(new SearchStep(low, -1, high, Integer.MAX_VALUE));
        }

        return steps;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTitle(g2d);
        drawArray(g2d);
        drawStatus(g2d);

        g2d.dispose();
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(getFont().deriveFont(Font.BOLD, 24f));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Binary Search Animasyonu", PADDING, 40);
        g2d.setFont(getFont().deriveFont(Font.PLAIN, 18f));
        g2d.drawString("Hedef: " + target, PADDING, 70);
    }

    private void drawArray(Graphics2D g2d) {
        g2d.setFont(getFont().deriveFont(Font.BOLD, 18f));
        int y = 120;
        SearchStep step = currentStep >= 0 && currentStep < steps.size() ? steps.get(currentStep) : null;

        for (int i = 0; i < numbers.length; i++) {
            int x = PADDING + i * BAR_WIDTH;
            Rectangle rectangle = new Rectangle(x, y, BAR_WIDTH - 10, BAR_HEIGHT);
            Color fill = Color.decode("#1e293b");

            if (step != null) {
                if (step.mid == i) {
                    fill = Color.decode("#22c55e"); // mid value
                } else if (i >= step.low && i <= step.high) {
                    fill = Color.decode("#38bdf8"); // current search window
                } else {
                    fill = Color.decode("#334155");
                }
            }

            g2d.setColor(fill);
            g2d.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 16, 16);

            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 16, 16);

            String text = numbers[i] + "";
            FontMetrics fm = g2d.getFontMetrics();
            int textX = rectangle.x + (rectangle.width - fm.stringWidth(text)) / 2;
            int textY = rectangle.y + (rectangle.height + fm.getAscent()) / 2 - 4;
            g2d.drawString(text, textX, textY);

            g2d.setFont(getFont().deriveFont(Font.PLAIN, 14f));
            g2d.drawString("i=" + i, rectangle.x + 6, rectangle.y + rectangle.height + 20);
            g2d.setFont(getFont().deriveFont(Font.BOLD, 18f));
        }

        drawMarkers(g2d, step, y);
    }

    private void drawMarkers(Graphics2D g2d, SearchStep step, int top) {
        if (step == null) {
            return;
        }

        g2d.setFont(getFont().deriveFont(Font.BOLD, 14f));
        int arrowHeight = 16;
        int arrowWidth = 12;

        if (step.low >= 0 && step.low < numbers.length) {
            drawArrow(g2d, indexCenterX(step.low), top - 10, arrowWidth, arrowHeight, Color.decode("#f97316"), "low");
        }

        if (step.high >= 0 && step.high < numbers.length) {
            drawArrow(g2d, indexCenterX(step.high), top - 10, arrowWidth, arrowHeight, Color.decode("#facc15"), "high");
        }

        if (step.mid >= 0 && step.mid < numbers.length && step.comparison != Integer.MAX_VALUE) {
            drawArrow(g2d, indexCenterX(step.mid), top - 30, arrowWidth, arrowHeight, Color.decode("#22c55e"), "mid");
        }
    }

    private void drawArrow(Graphics2D g2d, int centerX, int baseY, int width, int height, Color color, String label) {
        int half = width / 2;
        Polygon triangle = new Polygon();
        triangle.addPoint(centerX, baseY - height);
        triangle.addPoint(centerX - half, baseY);
        triangle.addPoint(centerX + half, baseY);

        g2d.setColor(color);
        g2d.fillPolygon(triangle);

        g2d.setColor(Color.WHITE);
        g2d.drawPolygon(triangle);

        FontMetrics fm = g2d.getFontMetrics();
        int textX = centerX - fm.stringWidth(label) / 2;
        int textY = baseY - height - 6;
        g2d.drawString(label, textX, textY);
    }

    private int indexCenterX(int index) {
        return PADDING + index * BAR_WIDTH + (BAR_WIDTH - 10) / 2;
    }

    private void drawStatus(Graphics2D g2d) {
        g2d.setFont(getFont().deriveFont(Font.PLAIN, 16f));
        g2d.setColor(Color.WHITE);

        if (currentStep < 0) {
            g2d.drawString("Hazırlanıyor...", PADDING, 230);
            return;
        }

        SearchStep step = steps.get(currentStep);
        if (step.comparison == Integer.MAX_VALUE) {
            g2d.drawString(String.format("Arama tamamlandı: low=%d, high=%d", step.low, step.high),
                    PADDING, 230);
            g2d.drawString("Hedef bulunamadı.", PADDING, 255);
            return;
        }

        String comparisonText = switch (Integer.signum(step.comparison)) {
            case -1 -> "Hedef mid değerinden daha büyük, sağ yarıya geç.";
            case 0 -> "Hedef bulundu!";
            case 1 -> "Hedef mid değerinden daha küçük, sol yarıya geç.";
            default -> "";
        };

        g2d.drawString(String.format("Adım %d: low=%d, mid=%d, high=%d", currentStep + 1,
                step.low, step.mid, step.high), PADDING, 230);
        g2d.drawString(comparisonText, PADDING, 255);
    }

    private static void createAndShowUI() {
        int[] numbers = {3, 7, 11, 19, 23, 29, 31, 37, 41};
        int target = 29;

        JFrame frame = new JFrame("Binary Search Animation");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new BinarySearchAnimation(numbers, target));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BinarySearchAnimation::createAndShowUI);
    }
}
