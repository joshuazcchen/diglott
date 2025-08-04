package UI.main;

import domain.model.Page;
import Configuration.ConfigDataRetriever;
import application.usecase.TranslatePageUseCase;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PageUI extends JFrame {
    private int currentPage;
    private final List<Page> pages;
    private final JEditorPane content;
    private final boolean darkMode;
    private final TranslatePageUseCase translator;
    private final JLabel pageLabel;

    public PageUI(List<Page> pageSet, boolean darkMode, TranslatePageUseCase translator) {
        this.pages = pageSet;
        this.darkMode = darkMode;
        this.translator = translator;
        this.currentPage = 0;

        setTitle("Page View");
        setSize(450, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        content = new JEditorPane();
        content.setContentType("text/html");
        content.setEditable(false);
        setupStyle();

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.getViewport().setBackground(darkMode ? Color.DARK_GRAY : Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JButton nextPage = new JButton("Next Page");
        JButton previousPage = new JButton("Last Page");
        JButton backButton = new JButton("Back to Main Page");
        pageLabel = new JLabel();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(previousPage);
        buttonPanel.add(nextPage);
        buttonPanel.add(pageLabel);
        add(buttonPanel, BorderLayout.SOUTH);

        nextPage.setEnabled(pages.size() > 1);
        previousPage.setEnabled(false);

        backButton.addActionListener(e -> {
            dispose();
            new MainUI(ConfigDataRetriever.get("api_key"));
        });

        nextPage.addActionListener(e -> goToNextPage(previousPage, nextPage));
        previousPage.addActionListener(e -> goToPreviousPage(previousPage, nextPage));

        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    goToNextPage(previousPage, nextPage);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    goToPreviousPage(previousPage, nextPage);
                }
            }
        });

        if (darkMode) applyDarkTheme(buttonPanel, backButton, nextPage, previousPage);

        updateContent();
    }

    private void setupStyle() {
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.addRule("body { font-family: " + ConfigDataRetriever.get("font") +
                "; font-size: " + ConfigDataRetriever.get("font_size") + "; " +
                "color: " + (darkMode ? "white" : "black") + "; " +
                "background-color: " + (darkMode ? "#333333" : "white") + "; }");
        kit.setStyleSheet(styleSheet);
        content.setEditorKit(kit);
    }

    private void updateContent() {
        String htmlContent = "<html><body>" + pages.get(currentPage).getContent() + "</body></html>";
        content.setText(htmlContent);
        content.setCaretPosition(0);
        pageLabel.setText("Page " + (currentPage + 1) + " of " + pages.size());
    }

    private void goToNextPage(JButton previousPage, JButton nextPage) {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            if (!pages.get(currentPage).isTranslated()) {
                translator.execute(pages.get(currentPage));
            }
            updateContent();
            previousPage.setEnabled(true);
        }
        if (currentPage == pages.size() - 1) {
            nextPage.setEnabled(false);
        }
    }

    private void goToPreviousPage(JButton previousPage, JButton nextPage) {
        if (currentPage > 0) {
            currentPage--;
            updateContent();
            nextPage.setEnabled(true);
        }
        if (currentPage == 0) {
            previousPage.setEnabled(false);
        }
    }

    private void applyDarkTheme(JPanel panel, JButton... buttons) {
        Color bg = Color.DARK_GRAY;
        Color fg = Color.WHITE;

        panel.setBackground(bg);
        pageLabel.setForeground(fg);
        pageLabel.setBackground(bg);
        pageLabel.setOpaque(true);

        for (JButton button : buttons) {
            button.setBackground(bg);
            button.setForeground(fg);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
        }
    }
}
