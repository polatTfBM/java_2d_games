package ui;

import algo.BFS;
import algo.DFS;
import algo.Dijkstra;
import algo.PathFinder;
import model.Maze;
import model.PathResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

public class MazeFrame extends JFrame {
    private final Maze maze;
    private final MazePanel mazePanel;
    private final JLabel resultLabel;
    private final JButton dfsButton;
    private final JButton bfsButton;
    private final JButton dijkstraButton;

    public MazeFrame() throws Exception {
        this.maze = new Maze(new File("maze.txt"));
        this.mazePanel = new MazePanel(maze);
        this.resultLabel = new JLabel("Choose an algorithm to solve the maze.");
        this.dfsButton = new JButton("DFS");
        this.bfsButton = new JButton("BFS");
        this.dijkstraButton = new JButton("Dijkstra");
        initLayout();
        initActions();
    }

    private void initLayout() {
        setTitle("Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(mazePanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.add(dfsButton);
        controlPanel.add(bfsButton);
        controlPanel.add(dijkstraButton);
        add(controlPanel, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        resultPanel.add(resultLabel);
        add(resultPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initActions() {
        dfsButton.addActionListener(e -> runAlgorithm(new DFS()));
        bfsButton.addActionListener(e -> runAlgorithm(new BFS()));
        dijkstraButton.addActionListener(e -> runAlgorithm(new Dijkstra()));
    }

    private void runAlgorithm(PathFinder finder) {
        disableButtons();
        resultLabel.setText("Running " + finder.getName() + " ...");
        mazePanel.resetAnimation();

        SwingWorker<PathResult, Void> worker = new SwingWorker<>() {
            @Override
            protected PathResult doInBackground() throws Exception {
                return finder.findPath(maze);
            }

            @Override
            protected void done() {
                try {
                    PathResult result = get();
                    mazePanel.animate(result, () -> onAnimationFinished(finder, result));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MazeFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    enableButtons();
                }
            }
        };
        worker.execute();
    }

    private void onAnimationFinished(PathFinder finder, PathResult result) {
        SwingUtilities.invokeLater(() -> {
            String text = String.format("%s - Time: %d ms | Cost: %d | Visited: %d",
                    finder.getName(),
                    result.getElapsedMillis(),
                    result.getCost(),
                    result.getVisitedOrder().size());
            resultLabel.setText(text);
            enableButtons();
        });
    }

    private void disableButtons() {
        dfsButton.setEnabled(false);
        bfsButton.setEnabled(false);
        dijkstraButton.setEnabled(false);
    }

    private void enableButtons() {
        dfsButton.setEnabled(true);
        bfsButton.setEnabled(true);
        dijkstraButton.setEnabled(true);
    }
}
