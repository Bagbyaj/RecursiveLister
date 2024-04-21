import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveListerUI extends JFrame {

    private JTextArea textArea;

    public RecursiveListerUI() {
        setTitle("Recursive Lister");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeUI();
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton startButton = new JButton("Select Directory");
        startButton.addActionListener(this::handleStart);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(this::handleReset);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        JLabel titleLabel = new JLabel("Directory Lister");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        textArea = new JTextArea(15, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        controlPanel.add(startButton);
        controlPanel.add(resetButton);
        controlPanel.add(quitButton);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);

        this.setLayout(new BorderLayout());
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(controlPanel, BorderLayout.SOUTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void handleStart(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textArea.setText("");
            Path startPath = chooser.getSelectedFile().toPath();
            try {
                Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        textArea.append(file.toString() + "\n");
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        textArea.append(dir.toString() + "\n");
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading directory");
            }
        }
    }

    private void handleReset(ActionEvent event) {
        textArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RecursiveListerUI().setVisible(true);
        });
    }
}
