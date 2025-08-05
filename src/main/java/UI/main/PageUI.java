package UI.main;

import configuration.ConfigDataRetriever;
import application.controller.SpeakController;
import application.usecase.TranslatePageUseCase;
import domain.model.Page;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * UI window for displaying book pages and navigating between them.
 */
public class PageUI extends JFrame {

    /** Dark mode background color. */
    private static final Color DARK_BG = Color.DARK_GRAY;

    /** Dark mode foreground (text) color. */
    private static final Color DARK_FG = Color.WHITE;

    /** Speak controller. */
    private final SpeakController speakController;

    /** Index of the currently displayed page in the list. */
    private int currentPage;

    /** List of all pages in the book. */
    private final List<Page> pages;

    /** Editor pane used for displaying HTML-formatted page content. */
    private final JEditorPane content;

    /** Whether dark mode is currently enabled. */
    private final boolean darkMode;

    /** Translator use case for translating the displayed pages. */
    private final TranslatePageUseCase translator;

    /** Label for showing the current page number and total page count. */
    private final JLabel pageLabel;

    /**
     * Creates a PageUI instance for viewing and navigating pages.
     *
     * @param pageSet         the list of pages to display
     * @param darkModeEnabled whether dark mode is enabled
     * @param translatorUseCase the translator use case for translating pages
     * @param speakController the controller for the speaker
     */
    public PageUI(final List<Page> pageSet,
                  final boolean darkModeEnabled,
                  final TranslatePageUseCase translatorUseCase,
                  final SpeakController speakController) {

        this.pages = pageSet;
        this.darkMode = darkModeEnabled;
        this.translator = translatorUseCase;
        this.speakController = speakController;
        this.currentPage = 0;

        setTitle("Page View");
        setSize(600, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        content = new JEditorPane();
        content.setContentType("text/html");
        content.setEditable(false);
        setupStyle();

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.getViewport().setBackground(darkMode ? DARK_BG : Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons
        JButton backButton = new JButton("Back to Main Page");
        JButton previousPageButton = new JButton("Last Page");
        JButton nextPageButton = new JButton("Next Page");
        JButton speakButton = new JButton("Speak");

        pageLabel = new JLabel("", SwingConstants.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(previousPageButton);
        buttonPanel.add(nextPageButton);
        buttonPanel.add(speakButton);
        buttonPanel.add(pageLabel);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial button states
        nextPageButton.setEnabled(pages.size() > 1);
        previousPageButton.setEnabled(false);

        // Actions
        backButton.addActionListener(e -> {
            dispose();
            new MainUI(ConfigDataRetriever.get("api_key"));
        });

        nextPageButton.addActionListener(e -> goToNextPage(previousPageButton, nextPageButton));
        previousPageButton.addActionListener(e -> goToPreviousPage(previousPageButton, nextPageButton));

        speakButton.addActionListener(e -> {
            if (!speakController.isTTSAvailable()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Google Cloud credentials not configured. Please upload a valid file to use speech.",
                        "Text-to-Speech Unavailable",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                new SpeakUI(pages.get(currentPage), speakController, darkMode);
            }
        });

        // Mouse navigation
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    goToNextPage(previousPageButton, nextPageButton);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    goToPreviousPage(previousPageButton, nextPageButton);
                }
            }
        });

        // Apply dark mode if enabled
        if (darkMode) {
            applyDarkTheme(buttonPanel, backButton, previousPageButton, nextPageButton, speakButton);
        }

        updateContent();
    }

    /** Sets up HTML/CSS styling for the page content. */
    private void setupStyle() {
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.addRule(
                "body { font-family: " + ConfigDataRetriever.get("font") + "; font-size: "
                        + ConfigDataRetriever.get("font_size") + "; color: "
                        + (darkMode ? "white" : "black") + "; background-color: "
                        + (darkMode ? "#333333" : "white") + "; }"
        );
        kit.setStyleSheet(styleSheet);
        content.setEditorKit(kit);
    }

    /** Updates the displayed page content and the page label. */
    private void updateContent() {
        String htmlContent = "<html><body>" + pages.get(currentPage).getContent() + "</body></html>";
        content.setText(htmlContent);
        content.setCaretPosition(0);
        pageLabel.setText("Page " + (currentPage + 1) + " of " + pages.size());
    }

    /**
     * Moves to the next page if available and triggers translation if needed.
     *
     * @param previousPageButton the previous page button
     * @param nextPageButton     the next page button
     */
    private void goToNextPage(final JButton previousPageButton, final JButton nextPageButton) {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            if (!pages.get(currentPage).isTranslated()) {
                translator.execute(pages.get(currentPage));
            }
            updateContent();
            previousPageButton.setEnabled(true);
        }
        if (currentPage == pages.size() - 1) {
            nextPageButton.setEnabled(false);
        }
    }

    /**
     * Moves to the previous page if available.
     *
     * @param previousPageButton the previous page button
     * @param nextPageButton     the next page button
     */
    private void goToPreviousPage(final JButton previousPageButton, final JButton nextPageButton) {
        if (currentPage > 0) {
            currentPage--;
            updateContent();
            nextPageButton.setEnabled(true);
        }
        if (currentPage == 0) {
            previousPageButton.setEnabled(false);
        }
    }

    /**
     * Applies dark mode styling to the panel and given buttons.
     *
     * @param panel   the panel containing the buttons
     * @param buttons the buttons to style
     */
    private void applyDarkTheme(final JPanel panel, final JButton... buttons) {
        panel.setBackground(DARK_BG);
        pageLabel.setForeground(DARK_FG);
        pageLabel.setBackground(DARK_BG);
        pageLabel.setOpaque(true);

        for (JButton button : buttons) {
            button.setBackground(DARK_BG);
            button.setForeground(DARK_FG);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
        }
    }
}
