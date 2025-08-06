package ui.main;

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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import application.controller.SpeakController;
import configuration.ConfigDataRetriever;
import domain.model.Page;

/**
 * UI window for speaking translated words from a page.
 *
 * <p>
 * This class dynamically creates a grid of buttons, one per translated word,
 * allowing the user to click a word to have it spoken aloud.
 */
public class SpeakUi extends JFrame {

    /**
     * Default window width.
     */
    private static final int WINDOW_WIDTH = 600;

    /**
     * Default window height.
     */
    private static final int WINDOW_HEIGHT = 400;

    /**
     * Default padding for UI elements.
     */
    private static final int PADDING = 20;

    /**
     * Default font size for buttons.
     */
    private static final int BUTTON_FONT_SIZE = 14;

    /**
     * Scroll increment value.
     */
    private static final int SCROLL_INCREMENT = 16;

    /**
     * Dark hover color value.
     */
    private static final int DARK_HOVER_COLOR = 100;

    /**
     * Light background color value.
     */
    private static final int LIGHT_BG_COLOR = 240;

    /**
     * Light hover color value.
     */
    private static final int LIGHT_HOVER_COLOR = 220;

    /**
     * Constructs the SpeakUi window and populates it with word buttons.
     *
     * @param page
     * the page containing translated (or untranslated) words
     * @param speakController
     * the controller responsible for handling speech playback
     * @param darkMode
     * whether to render the UI in dark mode
     */
    public SpeakUi(final Page page, final SpeakController speakController,
                   final boolean darkMode) {
        // Set window title
        setTitle("Speak Words");
        // Set window size
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        // Center the window on screen
        setLocationRelativeTo(null);
        // Close only this window when X is clicked
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Whether the original script should
        // be preserved alongside translations
        final boolean preserveOriginal =
                ConfigDataRetriever.getBool("original_script");

        // Get the correct list of words to display depending on settings
        // If preserveOriginal is true, words will contain original +
        // translation in parentheses
        final List<String> pageWords = page.getWords();
        final String targetLangCode = ConfigDataRetriever.get(
                "target_language");

        // Create the UI components
        final JPanel gridPanel = createGridPanel(darkMode);
        populateGridPanel(gridPanel, pageWords,
                preserveOriginal, targetLangCode, speakController, darkMode);

        // Make the grid scrollable in case there are many words
        final JScrollPane scrollPane = new JScrollPane(gridPanel);
        // Smoother scroll speed
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        scrollPane.setBorder(null);

        // Add to main frame
        add(scrollPane);
        // Show the UI
        setVisible(true);
    }

    /**
     * Creates the grid panel for the word buttons.
     *
     * @param darkMode whether dark mode is enabled
     * @return the configured grid panel
     */
    private JPanel createGridPanel(final boolean darkMode) {
        final JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING, PADDING, PADDING, PADDING));

        if (darkMode) {
            gridPanel.setBackground(Color.DARK_GRAY);
        } else {
            gridPanel.setBackground(Color.WHITE);
        }

        return gridPanel;
    }

    /**
     * Populates the grid panel with word buttons.
     *
     * @param gridPanel the panel to populate
     * @param pageWords the list of words from the page
     * @param preserveOriginal whether to preserve original script
     * @param targetLangCode the target language code
     * @param speakController the controller for speech
     * @param darkMode whether dark mode is enabled
     */
    private void populateGridPanel(final JPanel gridPanel,
                                   final List<String> pageWords,
                                   final boolean preserveOriginal,
                                   final String targetLangCode,
                                   final SpeakController speakController,
                                   final boolean darkMode) {
        // Track translations we have already added to avoid duplicate buttons
        final Set<String> seenTranslations = new HashSet<>();

        // Loop through each word from the page
        for (String raw : pageWords) {
            // Remove HTML tags and non-breaking spaces
            final String clean = stripHtml(raw);
            String translatedWord;

            if (preserveOriginal) {
                // If preserving original script,
                // only process words with parentheses
                if (!clean.contains("(") || !clean.contains(")")) {
                    // Skip words without translations
                    continue;
                }
                translatedWord = extractInsideParentheses(clean);
            } else {
                // Without original script,
                // the word is already the translated form
                translatedWord = clean;
            }

            // Remove punctuation (.,!? etc.) from the translated word
            translatedWord = translatedWord.replaceAll(
                    "[\\p{Punct}]", "").trim();

            // Skip empty translations after cleaning
            if (translatedWord.isEmpty()) {
                continue;
            }

            // Avoid adding the same translated word twice
            if (!seenTranslations.add(translatedWord)) {
                continue;
            }

            // The text displayed on the label
            final String displayLabel = translatedWord;
            // The text to be spoken
            final String spokenText = translatedWord;
            // Language code for TTS

            // Create and style the button for the word
            final JButton wordButton = createWordButton(
                    displayLabel,
                    spokenText,
                    targetLangCode,
                    speakController,
                    darkMode
            );
            // Add to grid panel
            gridPanel.add(wordButton);
        }
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
     * @return the string inside the first pair of parentheses,
     * or empty if not found
     */
    private String extractInsideParentheses(final String text) {
        final int start = text.indexOf('(');
        final int end = text.indexOf(')');
        String result = "";

        if (start != -1 && end != -1 && start < end) {
            result = text.substring(start + 1, end).trim();
        }

        return result;
    }

    /**
     * Creates a styled button for a given word
     * and attaches a click listener to speak it.
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
        final JButton wordButton = new JButton(label);
        wordButton.setFocusPainted(false);
        wordButton.setFont(new Font("SansSerif", Font.BOLD, BUTTON_FONT_SIZE));
        wordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        wordButton.setBorder(BorderFactory.createLineBorder(
                Color.LIGHT_GRAY, 1));

        // Apply dark or light mode styling
        if (darkMode) {
            styleDarkButton(wordButton);
        } else {
            styleLightButton(wordButton);
        }

        // Speak the word when clicked
        wordButton.addActionListener(
                (final ActionEvent actionEvent) ->
                        speakController.speakWord(spokenText, langCode)
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
                button.setBackground(new Color(
                        DARK_HOVER_COLOR, DARK_HOVER_COLOR, DARK_HOVER_COLOR));
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
        button.setBackground(new Color(
                LIGHT_BG_COLOR,
                LIGHT_BG_COLOR,
                LIGHT_BG_COLOR));
        button.setForeground(Color.BLACK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(new Color(
                        LIGHT_HOVER_COLOR,
                        LIGHT_HOVER_COLOR,
                        LIGHT_HOVER_COLOR));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setBackground(new Color(
                        LIGHT_BG_COLOR,
                        LIGHT_BG_COLOR,
                        LIGHT_BG_COLOR));
            }
        });
    }
}
