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
 * <p>
 * This class dynamically creates a grid of buttons, one per translated word,
 * allowing the user to click a word to have it spoken aloud.
 */
public class SpeakUI extends JFrame {

    /**
     * Constructs the SpeakUI window and populates it with word buttons.
     *
     * @param page            the page containing translated (or untranslated) words
     * @param speakController the controller responsible for handling speech playback
     * @param darkMode        whether to render the UI in dark mode
     */
    public SpeakUI(final Page page, final SpeakController speakController, final boolean darkMode) {
        setTitle("Speak Words"); // Set window title
        setSize(600, 400); // Set window size
        setLocationRelativeTo(null); // Center the window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window when X is clicked

        // Whether the original script should be preserved alongside translations
        final boolean preserveOriginal = ConfigDataRetriever.getBool("original_script");

        // Get the correct list of words to display depending on settings
        // If preserveOriginal is true, words will contain original + translation in parentheses
        final List<String> pageWords = page.getWords();
        final String targetLangCode = ConfigDataRetriever.get("target_language");

        // Main grid panel for word buttons: 3 columns, auto-adjusting rows
        final JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(darkMode ? Color.DARK_GRAY : Color.WHITE);

        // Track translations we have already added to avoid duplicate buttons
        final Set<String> seenTranslations = new HashSet<>();

        // Loop through each word from the page
        for (String raw : pageWords) {
            // Remove HTML tags and non-breaking spaces
            final String clean = stripHtml(raw);
            String translatedWord;

            if (preserveOriginal) {
                // If preserving original script, only process words with parentheses
                if (!clean.contains("(") || !clean.contains(")")) {
                    continue; // Skip words without translations
                }
                translatedWord = extractInsideParentheses(clean);
            } else {
                // Without original script, the word is already the translated form
                translatedWord = clean;
            }

            // Remove punctuation (.,!? etc.) from the translated word
            translatedWord = translatedWord.replaceAll("[\\p{Punct}]", "").trim();

            // Skip empty translations after cleaning
            if (translatedWord.isEmpty()) {
                continue;
            }

            // Avoid adding the same translated word twice
            if (!seenTranslations.add(translatedWord)) {
                continue;
            }

            // Create and style the button for the word
            final JButton wordButton = createWordButton(
                    translatedWord, // Displayed label
                    translatedWord, // Spoken text
                    targetLangCode, // Language code for TTS
                    speakController,
                    darkMode
            );
            gridPanel.add(wordButton); // Add to grid panel
        }

        // Make the grid scrollable in case there are many words
        final JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scroll speed
        scrollPane.setBorder(null);

        add(scrollPane); // Add to main frame
        setVisible(true); // Show the UI
    }

    /**
     * Removes HTML tags and replaces non-breaking spaces with normal spaces.
     *
     * @param input the raw word string (may contain HTML formatting)
     * @return cleaned string with HTML removed
     */
    private String stripHtml(final String input) {
        return input.replaceAll("<[^>]*>", "")
                .replace("&nbsp;", " ")
                .trim();
    }

    /**
     * Extracts the text inside parentheses.
     *
     * @param text the text containing parentheses
     * @return the string inside the first pair of parentheses, or empty if not found
     */
    private String extractInsideParentheses(final String text) {
        int start = text.indexOf('(');
        int end = text.indexOf(')');
        if (start != -1 && end != -1 && start < end) {
            return text.substring(start + 1, end).trim();
        }
        return "";
    }

    /**
     * Creates a styled button for a given word and attaches a click listener to speak it.
     *
     * @param label           the text displayed on the button
     * @param spokenText      the text to speak when clicked
     * @param langCode        the language code for speech synthesis
     * @param speakController controller to handle speech playback
     * @param darkMode        whether the UI is in dark mode
     * @return the fully styled and functional JButton
     */
    private JButton createWordButton(
            final String label,
            final String spokenText,
            final String langCode,
            final SpeakController speakController,
            final boolean darkMode
    ) {
        JButton wordButton = new JButton(label);
        wordButton.setFocusPainted(false);
        wordButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        wordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        wordButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Apply dark or light mode styling
        if (darkMode) {
            styleDarkButton(wordButton);
        } else {
            styleLightButton(wordButton);
        }

        // Speak the word when clicked
        wordButton.addActionListener(
                (ActionEvent e) -> speakController.speakWord(spokenText, langCode)
        );
        return wordButton;
    }

    /**
     * Applies dark mode styling and hover effects to a button.
     *
     * @param button the button to style
     */
    private void styleDarkButton(final JButton button) {
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(new Color(100, 100, 100));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setBackground(Color.GRAY);
            }
        });
    }

    /**
     * Applies light mode styling and hover effects to a button.
     *
     * @param button the button to style
     */
    private void styleLightButton(final JButton button) {
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(Color.BLACK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
        });
    }
}
