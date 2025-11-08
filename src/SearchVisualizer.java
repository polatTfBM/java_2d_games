import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Swing application that visualizes multiple search algorithms (Linear, Binary, and Interpolation search)
 * step-by-step. The UI provides controls to configure the array size and target, choose an algorithm, and
 * navigate through the algorithm steps. A visualization panel displays the array and highlights the relevant
 * indices (current, low, mid, high), while a code panel shows the pseudocode with the current line highlighted.
 */
public class SearchVisualizer extends JFrame {
    private final JTextField arraySizeField = new JTextField("15", 5);
    private final JTextField targetField = new JTextField("25", 5);
    private final JComboBox<String> algorithmCombo = new JComboBox<>(
            new String[]{"Linear Search", "Binary Search", "Interpolation Search"});
    private final JButton startButton = new JButton("Start");
    private final JButton nextButton = new JButton("Next Step");
    private final JButton previousButton = new JButton("Previous Step");
    private final JButton resetButton = new JButton("Reset");
    private final JLabel statusLabel = new JLabel("Configure the search and press Start.");
    private final ArrayPanel arrayPanel = new ArrayPanel();
    private final CodePanel codePanel = new CodePanel();

    private SearchAlgorithm currentAlgorithm;
    private int currentStepIndex = 0;
    private int[] currentArray;

    public SearchVisualizer() {
        super("Search Algorithm Visualizer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(1000, 650));

        add(buildControlPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildStatusPanel(), BorderLayout.SOUTH);

        hookListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel buildControlPanel() {
        JPanel controls = new JPanel(new GridBagLayout());
        controls.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;

        gbc.gridx = 0;
        controls.add(new JLabel("Array Size:"), gbc);
        gbc.gridx = 1;
        controls.add(arraySizeField, gbc);

        gbc.gridx = 2;
        controls.add(new JLabel("Target:"), gbc);
        gbc.gridx = 3;
        controls.add(targetField, gbc);

        gbc.gridx = 4;
        controls.add(new JLabel("Algorithm:"), gbc);
        gbc.gridx = 5;
        controls.add(algorithmCombo, gbc);

        gbc.gridx = 6;
        controls.add(startButton, gbc);

        gbc.gridx = 7;
        controls.add(previousButton, gbc);

        gbc.gridx = 8;
        controls.add(nextButton, gbc);

        gbc.gridx = 9;
        controls.add(resetButton, gbc);

        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        resetButton.setEnabled(false);

        return controls;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane arrayScroll = new JScrollPane(arrayPanel);
        arrayScroll.setBorder(BorderFactory.createTitledBorder("Array Visualization"));
        center.add(arrayScroll);

        JScrollPane codeScroll = new JScrollPane(codePanel);
        codeScroll.setBorder(BorderFactory.createTitledBorder("Pseudocode"));
        center.add(codeScroll);

        return center;
    }

    private JPanel buildStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 14f));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        return statusPanel;
    }

    private void hookListeners() {
        startButton.addActionListener(this::onStart);
        nextButton.addActionListener(e -> moveStep(1));
        previousButton.addActionListener(e -> moveStep(-1));
        resetButton.addActionListener(e -> resetVisualization());
    }

    private void onStart(ActionEvent event) {
        try {
            int size = Math.max(2, Integer.parseInt(arraySizeField.getText().trim()));
            int target = Integer.parseInt(targetField.getText().trim());
            currentArray = generateSortedArray(size);
            currentAlgorithm = createAlgorithm((String) algorithmCombo.getSelectedItem(), currentArray, target);
            currentAlgorithm.generateSteps();
            currentStepIndex = 0;
            updateStep();
            startButton.setEnabled(false);
            nextButton.setEnabled(true);
            resetButton.setEnabled(true);
            previousButton.setEnabled(false);
            statusLabel.setText("Array generated. Use Next/Previous to step through the algorithm.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid integer values.", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moveStep(int delta) {
        if (currentAlgorithm == null) {
            return;
        }
        int newIndex = currentStepIndex + delta;
        if (newIndex < 0 || newIndex >= currentAlgorithm.getSteps().size()) {
            return;
        }
        currentStepIndex = newIndex;
        updateStep();
        previousButton.setEnabled(currentStepIndex > 0);
        nextButton.setEnabled(currentStepIndex < currentAlgorithm.getSteps().size() - 1);
    }

    private void resetVisualization() {
        currentAlgorithm = null;
        currentArray = null;
        currentStepIndex = 0;
        arrayPanel.setData(null, null);
        codePanel.setPseudocode(new String[]{""}, -1);
        statusLabel.setText("Visualization reset. Adjust parameters and press Start.");
        startButton.setEnabled(true);
        nextButton.setEnabled(false);
        previousButton.setEnabled(false);
        resetButton.setEnabled(false);
    }

    private void updateStep() {
        if (currentAlgorithm == null) {
            return;
        }
        SearchStep step = currentAlgorithm.getSteps().get(currentStepIndex);
        arrayPanel.setData(currentArray, step);
        codePanel.setPseudocode(currentAlgorithm.getPseudocode(), step.getHighlightedLine());
        statusLabel.setText(step.getDescription());
        previousButton.setEnabled(currentStepIndex > 0);
        nextButton.setEnabled(currentStepIndex < currentAlgorithm.getSteps().size() - 1);
    }

    private SearchAlgorithm createAlgorithm(String name, int[] array, int target) {
        if ("Binary Search".equals(name)) {
            return new BinarySearchAlgorithm(array, target);
        } else if ("Interpolation Search".equals(name)) {
            return new InterpolationSearchAlgorithm(array, target);
        }
        return new LinearSearchAlgorithm(array, target);
    }

    private int[] generateSortedArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        int value = random.nextInt(5);
        for (int i = 0; i < size; i++) {
            value += random.nextInt(10) + 1;
            array[i] = value;
        }
        return array;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SearchVisualizer::new);
    }
}

/**
 * Represents a single visualization step with index highlights and descriptive text.
 */
class SearchStep {
    private final int lowIndex;
    private final int midIndex;
    private final int highIndex;
    private final int currentIndex;
    private final boolean found;
    private final String description;
    private final int highlightedLine;

    public SearchStep(int lowIndex, int midIndex, int highIndex, int currentIndex,
                      boolean found, String description, int highlightedLine) {
        this.lowIndex = lowIndex;
        this.midIndex = midIndex;
        this.highIndex = highIndex;
        this.currentIndex = currentIndex;
        this.found = found;
        this.description = description;
        this.highlightedLine = highlightedLine;
    }

    public int getLowIndex() {
        return lowIndex;
    }

    public int getMidIndex() {
        return midIndex;
    }

    public int getHighIndex() {
        return highIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isFound() {
        return found;
    }

    public String getDescription() {
        return description;
    }

    public int getHighlightedLine() {
        return highlightedLine;
    }
}

/**
 * Base class for all search algorithms. Stores the array, target, steps, and pseudocode.
 */
abstract class SearchAlgorithm {
    protected final int[] array;
    protected final int target;
    protected final List<SearchStep> steps = new ArrayList<>();
    protected String[] pseudocode;

    protected SearchAlgorithm(int[] array, int target) {
        this.array = array;
        this.target = target;
    }

    public abstract void generateSteps();

    public List<SearchStep> getSteps() {
        return steps;
    }

    public String[] getPseudocode() {
        return pseudocode;
    }
}

class LinearSearchAlgorithm extends SearchAlgorithm {
    public LinearSearchAlgorithm(int[] array, int target) {
        super(array, target);
        pseudocode = new String[]{
                "for i from 0 to n - 1",
                "    if array[i] == target",
                "        return i",
                "return -1"
        };
    }

    @Override
    public void generateSteps() {
        steps.clear();
        steps.add(new SearchStep(-1, -1, -1, -1, false,
                "Start linear search.", 0));
        for (int i = 0; i < array.length; i++) {
            steps.add(new SearchStep(-1, -1, -1, i, false,
                    "Checking index " + i + " (value=" + array[i] + ").", 0));
            if (array[i] == target) {
                steps.add(new SearchStep(-1, -1, -1, i, true,
                        "Target found at index " + i + ".", 1));
                steps.add(new SearchStep(-1, -1, -1, i, true,
                        "Returning index " + i + ".", 2));
                return;
            }
        }
        steps.add(new SearchStep(-1, -1, -1, -1, false,
                "Target not found after scanning entire array.", 3));
    }
}

class BinarySearchAlgorithm extends SearchAlgorithm {
    public BinarySearchAlgorithm(int[] array, int target) {
        super(array, target);
        pseudocode = new String[]{
                "low = 0",
                "high = n - 1",
                "while low <= high",
                "    mid = (low + high) / 2",
                "    if array[mid] == target",
                "        return mid",
                "    else if array[mid] < target",
                "        low = mid + 1",
                "    else",
                "        high = mid - 1",
                "return -1"
        };
    }

    @Override
    public void generateSteps() {
        steps.clear();
        int low = 0;
        int high = array.length - 1;
        steps.add(new SearchStep(low, -1, high, -1, false,
                "Set low = 0.", 0));
        steps.add(new SearchStep(low, -1, high, -1, false,
                "Set high = n - 1 = " + high + ".", 1));
        while (low <= high) {
            steps.add(new SearchStep(low, -1, high, -1, false,
                    "Check loop condition low <= high (" + low + " <= " + high + ").", 2));
            int mid = (low + high) / 2;
            steps.add(new SearchStep(low, mid, high, -1, false,
                    "Compute mid = (" + low + " + " + high + ") / 2 = " + mid + ".", 3));
            if (array[mid] == target) {
                steps.add(new SearchStep(low, mid, high, -1, true,
                        "array[mid] == target (" + array[mid] + ").", 4));
                steps.add(new SearchStep(low, mid, high, -1, true,
                        "Return mid = " + mid + ".", 5));
                return;
            } else if (array[mid] < target) {
                steps.add(new SearchStep(low, mid, high, -1, false,
                        "array[mid] < target (" + array[mid] + " < " + target + ").", 6));
                low = mid + 1;
                steps.add(new SearchStep(low, mid, high, -1, false,
                        "Set low = mid + 1 -> " + low + ".", 7));
            } else {
                steps.add(new SearchStep(low, mid, high, -1, false,
                        "array[mid] > target (" + array[mid] + " > " + target + ").", 8));
                high = mid - 1;
                steps.add(new SearchStep(low, mid, high, -1, false,
                        "Set high = mid - 1 -> " + high + ".", 9));
            }
        }
        steps.add(new SearchStep(-1, -1, -1, -1, false,
                "low exceeded high. Target not present.", 10));
    }
}

class InterpolationSearchAlgorithm extends SearchAlgorithm {
    public InterpolationSearchAlgorithm(int[] array, int target) {
        super(array, target);
        pseudocode = new String[]{
                "low = 0",
                "high = n - 1",
                "while low <= high and target between array[low] and array[high]",
                "    pos = low + ((target - array[low]) * (high - low)) / (array[high] - array[low])",
                "    if array[pos] == target",
                "        return pos",
                "    else if array[pos] < target",
                "        low = pos + 1",
                "    else",
                "        high = pos - 1",
                "return -1"
        };
    }

    @Override
    public void generateSteps() {
        steps.clear();
        int low = 0;
        int high = array.length - 1;
        steps.add(new SearchStep(low, -1, high, -1, false,
                "Set low = 0.", 0));
        steps.add(new SearchStep(low, -1, high, -1, false,
                "Set high = n - 1 = " + high + ".", 1));
        while (low <= high && target >= array[low] && target <= array[high]) {
            steps.add(new SearchStep(low, -1, high, -1, false,
                    "Check bounds and range conditions.", 2));
            if (array[high] == array[low]) {
                int pos = low;
                boolean found = array[pos] == target;
                steps.add(new SearchStep(low, pos, high, -1, found,
                        "All values equal. Checking position " + pos + ".", 3));
                if (found) {
                    steps.add(new SearchStep(low, pos, high, -1, true,
                            "Target found at pos=" + pos + ".", 4));
                    steps.add(new SearchStep(low, pos, high, -1, true,
                            "Return pos = " + pos + ".", 5));
                    return;
                }
                break;
            }
            int pos = low + (int) (((long) (target - array[low]) * (high - low)) /
                    (array[high] - array[low]));
            pos = Math.max(low, Math.min(pos, high));
            steps.add(new SearchStep(low, pos, high, -1, false,
                    "Estimate position pos=" + pos + ".", 3));
            if (array[pos] == target) {
                steps.add(new SearchStep(low, pos, high, -1, true,
                        "array[pos] == target (" + array[pos] + ").", 4));
                steps.add(new SearchStep(low, pos, high, -1, true,
                        "Return pos = " + pos + ".", 5));
                return;
            } else if (array[pos] < target) {
                steps.add(new SearchStep(low, pos, high, -1, false,
                        "array[pos] < target (" + array[pos] + " < " + target + ").", 6));
                low = pos + 1;
                steps.add(new SearchStep(low, pos, high, -1, false,
                        "Set low = pos + 1 -> " + low + ".", 7));
            } else {
                steps.add(new SearchStep(low, pos, high, -1, false,
                        "array[pos] > target (" + array[pos] + " > " + target + ").", 8));
                high = pos - 1;
                steps.add(new SearchStep(low, pos, high, -1, false,
                        "Set high = pos - 1 -> " + high + ".", 9));
            }
        }
        steps.add(new SearchStep(low, -1, high, -1, false,
                "Search finished. Target not present in range.", 10));
    }
}

/**
 * Panel responsible for drawing the array and highlighting the relevant indices.
 */
class ArrayPanel extends JPanel {
    private static final Color COLOR_DEFAULT = new Color(0xCFD8DC);
    private static final Color COLOR_LOW = new Color(0xFFA726);
    private static final Color COLOR_MID = new Color(0x29B6F6);
    private static final Color COLOR_HIGH = new Color(0xEF5350);
    private static final Color COLOR_CURRENT = new Color(0x66BB6A);
    private static final Color COLOR_FOUND = new Color(0x7E57C2);

    private int[] array;
    private SearchStep step;

    public ArrayPanel() {
        setPreferredSize(new Dimension(500, 400));
        setBackground(Color.WHITE);
        setToolTipText("Visualization of the search process.");
    }

    public void setData(int[] array, SearchStep step) {
        this.array = array;
        this.step = step;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null || array.length == 0) {
            g.setColor(Color.DARK_GRAY);
            g.drawString("No data to display.", 20, 20);
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int boxWidth = Math.max(40, width / array.length);
        int boxHeight = 80;
        int startX = Math.max(10, (width - boxWidth * array.length) / 2);
        int y = height / 2 - boxHeight / 2;

        for (int i = 0; i < array.length; i++) {
            int x = startX + i * boxWidth;
            Color color = COLOR_DEFAULT;
            if (step != null) {
                if (i == step.getCurrentIndex()) {
                    color = step.isFound() ? COLOR_FOUND : COLOR_CURRENT;
                } else if (i == step.getLowIndex()) {
                    color = COLOR_LOW;
                } else if (i == step.getMidIndex()) {
                    color = COLOR_MID;
                } else if (i == step.getHighIndex()) {
                    color = COLOR_HIGH;
                }
            }

            g.setColor(color);
            g.fillRoundRect(x, y, boxWidth - 10, boxHeight, 15, 15);
            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(x, y, boxWidth - 10, boxHeight, 15, 15);

            String value = String.valueOf(array[i]);
            String index = "[" + i + "]";
            FontMetrics fm = g.getFontMetrics();
            int valueX = x + (boxWidth - 10 - fm.stringWidth(value)) / 2;
            int valueY = y + boxHeight / 2;
            g.drawString(value, valueX, valueY);
            g.drawString(index, x + (boxWidth - 10 - fm.stringWidth(index)) / 2, valueY + 20);
        }
    }
}

/**
 * Panel showing pseudocode lines and highlighting the current line of execution.
 */
class CodePanel extends JList<String> {
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private int highlightedLine = -1;

    public CodePanel() {
        setModel(model);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        setCellRenderer(new CodeRenderer());
        setPseudocode(new String[]{""}, -1);
    }

    public void setPseudocode(String[] lines, int highlightedLine) {
        model.clear();
        for (String line : lines) {
            model.addElement(line);
        }
        this.highlightedLine = highlightedLine;
        repaint();
    }

    private class CodeRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, false, false);
            if (index == highlightedLine) {
                label.setBackground(new Color(0xFFF59D));
            } else {
                label.setBackground(Color.WHITE);
            }
            label.setOpaque(true);
            label.setBorder(new EmptyBorder(4, 8, 4, 8));
            return label;
        }
    }
}
