package UI;

import Book.Page;
import Configuration.ConfigDataRetriever;
import Translation.TranslateAndTransliteratePage;
import Audio.SpeechManager;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PageUI extends JFrame {
    private int currentPage;
    private List<Page> pages;
    private JEditorPane content;
    private JButton backButton;
    private boolean darkMode;
    private Translation.StoredWords storedWords;

    public PageUI(List<Page> pageSet, boolean darkMode, TranslateAndTransliteratePage translatePage) {
        this.pages = pageSet;
        this.currentPage = 0;
        this.darkMode = darkMode;
        translatePage.translatePage(pages.get(currentPage)); // ensures first page is translated
        storedWords = translatePage.getStoredWords();        // saves the words to use later

        setTitle("Page View");
        setSize(600, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set up styled editor
        content = new JEditorPane();
        content.setContentType("text/html");
        content.setEditable(false);

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = new StyleSheet();

        // Apply font family and size from config
        styleSheet.addRule("body { font-family: " + ConfigDataRetriever.get("font") +
                "; font-size: " + ConfigDataRetriever.get("font_size") + "; " +
                // Text color depends on darkMode
                "color: " + (darkMode ? "white" : "black") + "; " +
                // Background color also set for completeness
                "background-color: " + (darkMode ? "#333333" : "white") + "; }");

        kit.setStyleSheet(styleSheet);
        content.setEditorKit(kit);

        updateContent();

        JScrollPane scrollPane = new JScrollPane(content);
        // ScrollPane background matches theme
        scrollPane.getViewport().setBackground(darkMode ? Color.DARK_GRAY : Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);

        JButton nextPage = new JButton("Next Page");
        JButton previousPage = new JButton("Last Page");
        nextPage.setEnabled(pages.size() > 1);
        previousPage.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        backButton = new JButton("Back to Main Page");
        buttonPanel.add(backButton);
        buttonPanel.add(previousPage);
        buttonPanel.add(nextPage);
        JButton speakWordsButton = new JButton("Speak Words");
        buttonPanel.add(speakWordsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose();
            new MainUI(ConfigDataRetriever.get("api_key"));
        });
        // Apply dark mode theme to buttons and panel
        if (darkMode) {
            applyDarkTheme(buttonPanel, backButton, nextPage, previousPage, speakWordsButton);
        }

        nextPage.addActionListener(e -> {
            if (!pages.get(currentPage+1).isTranslated()) {
                translatePage.translatePage(pages.get(currentPage+1));
                storedWords = translatePage.getStoredWords();
            }
            if (currentPage < pages.size() - 1) {
                currentPage++;
                updateContent();
                previousPage.setEnabled(true);
            }
            if (currentPage == pages.size() - 1) {
                nextPage.setEnabled(false);
            }
        });

        previousPage.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                storedWords = translatePage.getStoredWords(); // refresh for previous page
                updateContent();
                nextPage.setEnabled(true);
            }
            if (currentPage == 0) {
                previousPage.setEnabled(false);
            }
        });
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!pages.get(currentPage+1).isTranslated()) {
                        translatePage.translatePage(pages.get(currentPage+1));
                    }
                    if (currentPage < pages.size() - 1) {
                        currentPage++;
                        updateContent();
                        previousPage.setEnabled(true);
                    }
                    if (currentPage == pages.size() - 1) {
                        nextPage.setEnabled(false);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (currentPage > 0) {
                        currentPage--;
                        updateContent();
                        nextPage.setEnabled(true);
                    }
                    if (currentPage == 0) {
                        previousPage.setEnabled(false);
                    }
                }
            }
        });
        speakWordsButton.addActionListener(e -> {
            Page current = pages.get(currentPage);
            List<String> translatedWords = current.getWords();

            String creds = ConfigDataRetriever.get("gcp_credentials");
            if (creds == null || creds.equals("none")) {
                JOptionPane.showMessageDialog(this, "Google TTS credentials not set.");
                return;
            }

            new SpeakUI(translatedWords, creds, translatePage.getStoredWords());
        });
    }

    private void updateContent() {
        Page page = pages.get(currentPage);
        List<String> originalWords = page.getOriginalWords();
        List<String> translatedWords = page.getWords();

        String inputLang = ConfigDataRetriever.get("input_language");
        String targetLang = ConfigDataRetriever.get("target_language");

        String htmlContent = wrapTranslatedWithOriginals(translatedWords, originalWords, inputLang, targetLang);
        content.setText(htmlContent);
        content.setCaretPosition(0);
    }


    private void applyDarkTheme(JPanel panel, JButton... buttons) {
        Color bg = Color.DARK_GRAY;
        Color fg = Color.WHITE;

        panel.setBackground(bg);
        for (JButton button : buttons) {
            button.setBackground(bg);
            button.setForeground(fg);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
        }
    }
    private String wrapTranslatedWithOriginals(List<String> translatedWords, List<String> originalWords,
                                               String inputLang, String targetLang) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");

        int len = Math.min(translatedWords.size(), originalWords.size());
        for (int i = 0; i < len; i++) {
            String translated = translatedWords.get(i);
            String original = originalWords.get(i);

            builder.append("<span class='word' lang='")
                    .append(targetLang)
                    .append("' style='font-weight:bold; margin-right:4px;'>")
                    .append(translated);

            if (!translated.equalsIgnoreCase(original)) {
                builder.append(" <span class='word original' lang='")
                        .append(inputLang)
                        .append("' style='color:gray;'>(")
                        .append(original)
                        .append(")</span>");
            }

            builder.append("</span> ");
        }

        builder.append("</body></html>");
        return builder.toString();
    }

}