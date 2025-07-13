package UI;

import Book.BookImporter;
import Book.BookImporterFactory;
import Translation.TranslationHandler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainUI extends JFrame {
    private final JButton fromLangButton;
    private final JButton toLangButton;
    private final JButton pickFileButton;
    private final JButton startButton;

    private File selectedFile;

    public MainUI() {
        setTitle("Diglott Translator");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fromLangButton = new JButton("From Language (Coming soon)");
        fromLangButton.setEnabled(false);

        toLangButton = new JButton("To Language (Coming soon)");
        toLangButton.setEnabled(false);

        pickFileButton = new JButton("Pick File");
        startButton = new JButton("Start");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(fromLangButton);
        panel.add(toLangButton);
        panel.add(pickFileButton);
        panel.add(startButton);

        add(panel);
        setVisible(true);

        addListeners();
    }

    private void addListeners() {
        pickFileButton.addActionListener(e -> {
            FileSelector selector = new FileSelector();
            File file = selector.selectBookFile();

            if (file != null) {
                selectedFile = file;
                JOptionPane.showMessageDialog(this, "Selected file: " + file.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "No valid file selected");
            }
        });

        startButton.addActionListener(e -> {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Please select a file first.");
                return;
            }

            try {
                BookImporter importer = BookImporterFactory.getImporter(selectedFile);
                String content = importer.importBook(selectedFile);

                TranslationHandler translationHandler = new TranslationHandler();
                translationHandler.addWord(content);

                JOptionPane.showMessageDialog(this, "Successfully imported and translated: " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to import file: " + selectedFile.getAbsolutePath());
                ex.printStackTrace();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Unsupported file format.");
            }
        });
    }
}
