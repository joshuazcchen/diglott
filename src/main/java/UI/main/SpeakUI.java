package UI.main;

import Configuration.ConfigDataRetriever;
import application.controller.SpeakController;
import domain.model.Page;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UI window for speaking translated words from a page.
 */
public class SpeakUI extends JFrame {

    /**
     * Constructs the SpeakUI window.
     *
     * @param page            the page containing words
     * @param speakController the controller for speaking words
     * @param darkMode        whether dark mode is enabled
     */
    public SpeakUI(Page page, SpeakController speakController, boolean darkMode) {
        setTitle("Speak Words");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<String> translatedWords = page.getWords();
        List<String> originalWords = page.getOriginalWords();
        String targetLangCode = ConfigDataRetriever.get("target_language");

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(darkMode ? Color.DARK_GRAY : Color.WHITE);

        Set<String> seenTranslations = new HashSet<>();

        for (int i = 0; i < translatedWords.size(); i++) {
            String trans = stripHtml(translatedWords.get(i));
            String orig = (i < originalWords.size())
                    ? stripHtml(originalWords.get(i)) : "";

            // Skip if untranslated or empty
            if (trans.equals(orig) || trans.isEmpty()) {
                continue;
            }

            // Extract part inside parentheses
            String spokenText = extractInsideParentheses(trans);
            // Fallback: if no parentheses found, use the full word
            if (spokenText.equals(trans)) {
                spokenText = trans;
            }


            // Skip duplicates
            if (!seenTranslations.add(spokenText)) {
                continue;
            }

            JButton wordButton = createWordButton(
                    trans, spokenText, targetLangCode, speakController, darkMode
            );
            gridPanel.add(wordButton);
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane);
        setVisible(true);
    }

    /**
     * Removes HTML tags and non-breaking spaces.
     *
     * @param input the string to clean
     * @return cleaned string
     */
    private String stripHtml(String input) {
        return input.replaceAll("<[^>]*>", "")
                .replace("&nbsp;", " ")
                .trim();
    }

    /**
     * Extracts the text inside parentheses.
     *
     * @param text the full word string
     * @return the text inside parentheses, or original text if not found
     */
    private String extractInsideParentheses(String text) {
        int start = text.indexOf('(');
        int end = text.indexOf(')');
        if (start != -1 && end != -1 && start < end) {
            return text.substring(start + 1, end).trim();
        }
        return text;
    }

    /**
     * Creates a styled JButton for a word.
     *
     * @param label          the button label
     * @param spokenText     the text to speak
     * @param langCode       the language code for speaking
     * @param speakController the controller to handle speech
     * @param darkMode       whether dark mode is enabled
     * @return the configured JButton
     */
    private JButton createWordButton(
            String label,
            String spokenText,
            String langCode,
            SpeakController speakController,
            boolean darkMode
    ) {
        JButton wordButton = new JButton(label);
        wordButton.setFocusPainted(false);
        wordButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        wordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        wordButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        if (darkMode) {
            styleDarkButton(wordButton);
        } else {
            styleLightButton(wordButton);
        }

        wordButton.addActionListener(
                (ActionEvent e) -> speakController.speakWord(spokenText, langCode)
        );
        return wordButton;
    }

    /**
     * Applies dark mode styling to a JButton.
     *
     * @param button the button to style
     */
    private void styleDarkButton(JButton button) {
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }
        });
    }

    /**
     * Applies light mode styling to a JButton.
     *
     * @param button the button to style
     */
    private void styleLightButton(JButton button) {
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(Color.BLACK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
        });
    }
}
