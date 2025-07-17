package UI;

import Book.BookImporter;
import Book.BookImporterFactory;
import Book.PageFactory;
import Configuration.ConfigDataRetriever;
import Translation.StoredWords;
import Translation.TranslatePage;
import Book.Page;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainUI extends JFrame {
    private JButton pickFileButton;
    private JButton startButton;
    private JButton closeButton;
    private JToggleButton darkModeToggle;

    private File selectedFile;
    private String bookText;
    private boolean darkMode;
    private List<Page> pages;

    private JComboBox<String> inputLangBox;
    private JComboBox<String> targetLangBox;
    private JComboBox<Integer> speedBox;

    private final StoredWords storedWords = new StoredWords();

    public MainUI(String apiKey) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        ConfigDataRetriever.set("api_key", apiKey);
        setupUI();
    }

    private void setupUI() {
        setTitle("Diglott Translator");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dropdowns
        inputLangBox = new JComboBox<>(new String[]{"en"});
        targetLangBox = new JComboBox<>(new String[]{"fr", "en", "es", "de", "zh"});
        speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        inputLangBox.setSelectedItem("en");
        targetLangBox.setSelectedItem(ConfigDataRetriever.get("target_language"));
        speedBox.setSelectedItem(ConfigDataRetriever.getSpeed());

        inputLangBox.setPreferredSize(new Dimension(80, 25));
        targetLangBox.setPreferredSize(new Dimension(80, 25));
        speedBox.setPreferredSize(new Dimension(50, 25));

        // Buttons
        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");
        closeButton = new JButton("Close App");
        darkModeToggle = new JToggleButton("Dark Mode");

        // Layout setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Compact language/speed panel
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        langPanel.add(new JLabel("From:"));
        langPanel.add(inputLangBox);
        langPanel.add(new JLabel("To:"));
        langPanel.add(targetLangBox);
        langPanel.add(new JLabel("Speed:"));
        langPanel.add(speedBox);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setMaximumSize(new Dimension(400, 100));
        buttonPanel.add(pickFileButton);
        buttonPanel.add(startButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(darkModeToggle);

        mainPanel.add(langPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
        addListeners();
        applyTheme();
    }

    private void addListeners() {
        pickFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a text or EPUB file");

            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("EPUB Files", "epub"));
            fileChooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("All Supported", "txt", "epub"));

            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                try {
                    BookImporter importer = BookImporterFactory.getImporter(selectedFile);
                    bookText = importer.importBook(selectedFile);
                    List<String> words = new ArrayList<>(Arrays.asList(bookText.split(" ")));
                    this.pages = PageFactory.paginate(words,
                            ConfigDataRetriever.getInt("page_length"));
                    JOptionPane.showMessageDialog(this, "Book loaded successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to load book.");
                }
            }
        });

        startButton.addActionListener(e -> {
            if (bookText == null) {
                JOptionPane.showMessageDialog(this, "Please load a book first.");
                return;
            }

            String inputLang = (String) inputLangBox.getSelectedItem();
            String targetLang = (String) targetLangBox.getSelectedItem();
            int speed = (int) speedBox.getSelectedItem();

            ConfigDataRetriever.set("target_language", targetLang);
            ConfigDataRetriever.set("speed", String.valueOf(speed));

            TranslatePage translatePage = new TranslatePage(storedWords);
            for (Page page : pages) {
                translatePage.translatePage(page);
            }

            startButton.setEnabled(false);
            pickFileButton.setEnabled(false);
            closeButton.setEnabled(false);

            new PageUI(pages).setVisible(true);
        });

        closeButton.addActionListener(e -> dispose());

        darkModeToggle.addActionListener(e -> {
            darkMode = darkModeToggle.isSelected();
            applyTheme();
        });
    }

    private void applyTheme() {
        Color bg = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        applyThemeRecursive(getContentPane(), bg, fg);
        repaint();
    }

    private void applyThemeRecursive(Component component, Color bg, Color fg) {
        if (component instanceof JPanel || component instanceof JFrame || component instanceof JScrollPane) {
            component.setBackground(bg);
        }

        if (component instanceof AbstractButton button) {
            button.setBackground(bg);
            button.setForeground(fg);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
        }

        if (component instanceof JTextArea area) {
            area.setBackground(bg);
            area.setForeground(fg);
            area.setCaretColor(fg);
        }

        if (component instanceof JTextComponent field) {
            field.setBackground(bg);
            field.setForeground(fg);
            field.setCaretColor(fg);
        }

        if (component instanceof JLabel label) {
            label.setForeground(fg);
        }

        if (component instanceof JProgressBar bar) {
            bar.setBackground(bg);
            bar.setForeground(fg);
            bar.setBorderPainted(true);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyThemeRecursive(child, bg, fg);
            }
        }
    }
}
