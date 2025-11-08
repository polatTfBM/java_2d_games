import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * BinarySearchVisualizer is a complete Swing application that demonstrates how binary search works.
 * <p>
 * The program allows the user to configure the number of elements, type a target value and even edit the
 * underlying sorted array. A pair of navigation buttons steps forward and backward through each iteration of the
 * algorithm. The currently executed line of code is highlighted and the active low/mid/high indices are painted
 * using different colors.
 * </p>
 */
public class BinarySearchVisualizer extends JFrame {
    /** Colors for the active indices. */
    private static final Color COLOR_LOW = new Color(0x4E79A7);
    private static final Color COLOR_MID = new Color(0xF28E2B);
    private static final Color COLOR_HIGH = new Color(0xE15759);

    private final JSpinner sizeSpinner;
    private final JTextField targetField;
    private final JTextField arrayField;
    private final JButton generateButton;
    private final JButton startButton;
    private final JButton nextButton;
    private final JButton previousButton;
    private final JLabel statusLabel;
    private final CodePane codePane;
    private final ArrayPanel arrayPanel;

    /** Holds the sorted array currently displayed. */
    private int[] currentArray = new int[0];
    /** All recorded steps for the current run. */
    private final List<BinarySearchStep> steps = new ArrayList<>();
    /** Index of the step currently visible. */
    private int currentStepIndex = -1;

    /**
     * Creates the complete user interface and wires the event listeners.
     */
    public BinarySearchVisualizer() {
        super("Binary Search Visualizer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setMinimumSize(new Dimension(960, 600));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel sizeLabel = new JLabel("Number of elements:");
        sizeSpinner = new JSpinner(new SpinnerNumberModel(10, 3, 30, 1));
        sizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                generateSortedArray();
            }
        });

        JLabel targetLabel = new JLabel("Target value:");
        targetField = new JTextField("42", 8);

        JLabel arrayLabel = new JLabel("Sorted array (editable):");
        arrayField = new JTextField(40);

        generateButton = new JButton("Generate Array");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSortedArray();
            }
        });

        startButton = new JButton("Prepare Search");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startVisualization();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(sizeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(sizeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(targetLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(targetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(arrayLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(arrayField, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(generateButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(startButton, gbc);

        add(inputPanel, BorderLayout.NORTH);

        arrayPanel = new ArrayPanel();
        arrayPanel.setPreferredSize(new Dimension(800, 150));
        add(arrayPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        codePane = new CodePane();
        codePane.setPreferredSize(new Dimension(420, 260));
        JScrollPane codeScroll = new JScrollPane(codePane);
        codeScroll.setBorder(BorderFactory.createTitledBorder("Binary Search Code"));
        bottomPanel.add(codeScroll, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        previousButton = new JButton("Previous Step");
        nextButton = new JButton("Next Step");
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveStep(-1);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveStep(1);
            }
        });
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);

        statusLabel = new JLabel("Configure the array and press 'Prepare Search' to begin.");

        controlPanel.add(previousButton);
        controlPanel.add(nextButton);
        controlPanel.add(statusLabel);

        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        generateSortedArray();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Generates a sorted array with the requested size and fills the text field.
     * Random numbers are produced, sorted and displayed so that the user can keep or edit them.
     */
    private void generateSortedArray() {
        int size = ((Number) sizeSpinner.getValue()).intValue();
        Random random = new Random();
        int[] values = new int[size];
        int current = random.nextInt(10);
        for (int i = 0; i < size; i++) {
            current += random.nextInt(10) + 1; // ensure strictly increasing values
            values[i] = current;
        }
        currentArray = values;
        arrayField.setText(arrayToString(values));
        resetVisualization("New array generated. Press 'Prepare Search'.");
    }

    /**
     * Converts an integer array to a string representation separated by spaces.
     */
    private String arrayToString(int[] values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(values[i]);
        }
        return builder.toString();
    }

    /**
     * Parses the text inside the array field and stores a sorted version in {@link #currentArray}.
     * If parsing fails a message dialog is displayed and the method returns false.
     */
    private boolean parseArrayFromField() {
        String[] tokens = arrayField.getText().trim().split("\\s+");
        if (tokens.length == 1 && tokens[0].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least one number in the array field.",
                    "Invalid Array", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int[] values = new int[tokens.length];
        try {
            for (int i = 0; i < tokens.length; i++) {
                values[i] = Integer.parseInt(tokens[i]);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Array values must be integers separated by spaces.",
                    "Invalid Array", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        Arrays.sort(values);
        currentArray = values;
        arrayField.setText(arrayToString(values));
        return true;
    }

    /**
     * Parses the target value, records all algorithm steps and updates the interface to the first step.
     */
    private void startVisualization() {
        if (!parseArrayFromField()) {
            return;
        }
        int targetValue;
        try {
            targetValue = Integer.parseInt(targetField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Target value must be an integer.",
                    "Invalid Target", JOptionPane.WARNING_MESSAGE);
            return;
        }

        steps.clear();
        BinarySearchRecorder recorder = new BinarySearchRecorder(currentArray, targetValue);
        steps.addAll(recorder.createSteps());
        if (steps.isEmpty()) {
            statusLabel.setText("Unable to create steps. Check the input values.");
            return;
        }
        currentStepIndex = 0;
        updateForCurrentStep();
        previousButton.setEnabled(true);
        nextButton.setEnabled(true);
    }

    /**
     * Moves the current step forward or backward depending on the supplied delta.
     */
    private void moveStep(int delta) {
        if (steps.isEmpty()) {
            return;
        }
        int newIndex = currentStepIndex + delta;
        newIndex = Math.max(0, Math.min(steps.size() - 1, newIndex));
        currentStepIndex = newIndex;
        updateForCurrentStep();
    }

    /**
     * Updates the array drawing, code highlighting and status text according to the current step.
     */
    private void updateForCurrentStep() {
        if (currentStepIndex < 0 || currentStepIndex >= steps.size()) {
            return;
        }
        BinarySearchStep step = steps.get(currentStepIndex);
        arrayPanel.setArray(currentArray, step.low, step.mid, step.high);
        codePane.highlightLine(step.codeLine);
        statusLabel.setText(step.message);
        previousButton.setEnabled(currentStepIndex > 0);
        nextButton.setEnabled(currentStepIndex < steps.size() - 1);
    }

    /**
     * Resets the state so that no step is selected and the controls are disabled.
     */
    private void resetVisualization(String message) {
        steps.clear();
        currentStepIndex = -1;
        arrayPanel.setArray(currentArray, -1, -1, -1);
        codePane.highlightLine(-1);
        statusLabel.setText(message);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
    }

    /**
     * Entry point of the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BinarySearchVisualizer().setVisible(true);
            }
        });
    }

    /**
     * Represents a single recorded step of the binary search algorithm.
     */
    private static class BinarySearchStep {
        final int low;
        final int mid;
        final int high;
        final int codeLine;
        final String message;

        BinarySearchStep(int low, int mid, int high, int codeLine, String message) {
            this.low = low;
            this.mid = mid;
            this.high = high;
            this.codeLine = codeLine;
            this.message = message;
        }
    }

    /**
     * Custom panel responsible for drawing the array and highlighting the active indices.
     */
    private static class ArrayPanel extends JPanel {
        private int[] array = new int[0];
        private int low = -1;
        private int mid = -1;
        private int high = -1;

        ArrayPanel() {
            setPreferredSize(new Dimension(800, 180));
            setBorder(BorderFactory.createTitledBorder("Array View"));
        }

        void setArray(int[] array, int low, int mid, int high) {
            this.array = array != null ? array.clone() : new int[0];
            this.low = low;
            this.mid = mid;
            this.high = high;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (array == null || array.length == 0) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight() - 40; // Leave space for border title and indices
            int elementWidth = Math.max(40, width / array.length - 10);
            int startX = (width - ((elementWidth + 10) * array.length - 10)) / 2;
            int y = height / 2;

            for (int i = 0; i < array.length; i++) {
                int x = startX + i * (elementWidth + 10);
                Color fillColor = new Color(0xF0F0F0);
                if (i == low) {
                    fillColor = COLOR_LOW;
                }
                if (i == high) {
                    fillColor = COLOR_HIGH;
                }
                if (i == mid) {
                    fillColor = COLOR_MID;
                }
                g2.setColor(fillColor);
                g2.fillRoundRect(x, y, elementWidth, 60, 12, 12);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRoundRect(x, y, elementWidth, 60, 12, 12);

                g2.setColor(Color.BLACK);
                String value = String.valueOf(array[i]);
                FontMetrics metrics = g2.getFontMetrics();
                int textX = x + (elementWidth - metrics.stringWidth(value)) / 2;
                int textY = y + (60 + metrics.getAscent()) / 2 - 4;
                g2.drawString(value, textX, textY);

                g2.setColor(Color.DARK_GRAY);
                String indexText = "" + i;
                int indexY = y + 80;
                g2.drawString(indexText, x + elementWidth / 2 - metrics.stringWidth(indexText) / 2, indexY);
            }

            drawLegend(g2);
            g2.dispose();
        }

        private void drawLegend(Graphics2D g2) {
            int legendY = getHeight() - 30;
            int legendX = 20;
            drawLegendItem(g2, legendX, legendY, COLOR_LOW, "low");
            drawLegendItem(g2, legendX + 120, legendY, COLOR_MID, "mid");
            drawLegendItem(g2, legendX + 240, legendY, COLOR_HIGH, "high");
        }

        private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String label) {
            g2.setColor(color);
            g2.fillRect(x, y, 18, 18);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, y, 18, 18);
            g2.drawString(label, x + 26, y + 14);
        }
    }

    /**
     * Panel that shows the binary search source code and highlights the active line.
     */
    private static class CodePane extends JTextPane {
        private final String[] codeLines = {
                "1  int low = 0;",
                "2  int high = array.length - 1;",
                "3  while (low <= high) {",
                "4      int mid = low + (high - low) / 2;",
                "5      if (array[mid] == target) {",
                "6          return mid;",
                "7      } else if (array[mid] < target) {",
                "8          low = mid + 1;",
                "9      } else {",
                "10         high = mid - 1;",
                "11     }",
                "12 }",
                "13 return -1;"
        };

        private final Style defaultStyle;
        private final Style highlightStyle;

        CodePane() {
            setEditable(false);
            setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            StyledDocument doc = getStyledDocument();
            defaultStyle = doc.addStyle("default", null);
            StyleConstants.setForeground(defaultStyle, Color.DARK_GRAY);

            highlightStyle = doc.addStyle("highlight", null);
            StyleConstants.setForeground(highlightStyle, Color.BLACK);
            StyleConstants.setBackground(highlightStyle, new Color(0xFFF3B0));

            refreshDocument(-1);
        }

        void highlightLine(int codeLine) {
            refreshDocument(codeLine);
        }

        private void refreshDocument(int highlightedLine) {
            StyledDocument doc = getStyledDocument();
            try {
                doc.remove(0, doc.getLength());
                for (int i = 0; i < codeLines.length; i++) {
                    String line = codeLines[i] + "\n";
                    Style style = (i + 1 == highlightedLine) ? highlightStyle : defaultStyle;
                    doc.insertString(doc.getLength(), line, style);
                }
            } catch (BadLocationException ignored) {
                // In case of an unexpected exception we simply ignore it as there is nothing useful to do.
            }
        }
    }

    /**
     * Responsible for executing the binary search logic while recording every intermediate step.
     */
    private static class BinarySearchRecorder {
        private final int[] array;
        private final int target;

        BinarySearchRecorder(int[] array, int target) {
            this.array = array;
            this.target = target;
        }

        List<BinarySearchStep> createSteps() {
            List<BinarySearchStep> recordedSteps = new ArrayList<>();
            int low = 0;
            int high = array.length - 1;

            recordedSteps.add(new BinarySearchStep(low, -1, high, 1, "Set low to 0."));
            recordedSteps.add(new BinarySearchStep(low, -1, high, 2, "Set high to array.length - 1."));

            boolean found = false;
            while (true) {
                recordedSteps.add(new BinarySearchStep(low, -1, high, 3,
                        String.format("Check loop condition: low=%d, high=%d.", low, high)));
                if (low > high) {
                    recordedSteps.add(new BinarySearchStep(low, -1, high, 13,
                            "low > high, target not found. Return -1."));
                    break;
                }

                int mid = low + (high - low) / 2;
                recordedSteps.add(new BinarySearchStep(low, mid, high, 4,
                        String.format("Compute mid = %d.", mid)));

                recordedSteps.add(new BinarySearchStep(low, mid, high, 5,
                        String.format("Compare array[%d]=%d with target %d.", mid, array[mid], target)));

                if (array[mid] == target) {
                    recordedSteps.add(new BinarySearchStep(low, mid, high, 6,
                            String.format("Found target at index %d.", mid)));
                    found = true;
                    break;
                } else if (array[mid] < target) {
                    recordedSteps.add(new BinarySearchStep(low, mid, high, 7,
                            String.format("%d is less than target %d.", array[mid], target)));
                    low = mid + 1;
                    recordedSteps.add(new BinarySearchStep(low, -1, high, 8,
                            String.format("Move low to %d.", low)));
                } else {
                    recordedSteps.add(new BinarySearchStep(low, mid, high, 9,
                            String.format("%d is greater than target %d.", array[mid], target)));
                    high = mid - 1;
                    recordedSteps.add(new BinarySearchStep(low, -1, high, 10,
                            String.format("Move high to %d.", high)));
                }
            }

            if (found) {
                recordedSteps.add(new BinarySearchStep(0, -1, array.length - 1, 12,
                        "Loop exits after returning the index."));
            }

            return recordedSteps;
        }
    }
}
