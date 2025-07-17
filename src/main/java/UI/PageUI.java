package UI;

import Book.Page;
import Configuration.ConfigDataRetriever;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.util.List;

public class PageUI extends JFrame {
    private int currentPage;
    private List<Page> pages;
    private JEditorPane content;
    private JButton backButton;

    public PageUI(List<Page> pageSet) {
        this.pages = pageSet;
        this.currentPage = 0;

        setTitle("Page View");
        setSize(450, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set up styled editor
        content = new JEditorPane();
        content.setContentType("text/html");
        content.setEditable(false);

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = new StyleSheet();
        styleSheet.addRule("body { font-family: " + ConfigDataRetriever.get("font") +
                "; font-size: "+ ConfigDataRetriever.get("font_size") + "; }");
        kit.setStyleSheet(styleSheet);
        content.setEditorKit(kit);

        updateContent();

        JScrollPane scrollPane = new JScrollPane(content);
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
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose();
            new MainUI(ConfigDataRetriever.get("api_key"));
        });

        nextPage.addActionListener(e -> {
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
                updateContent();
                nextPage.setEnabled(true);
            }
            if (currentPage == 0) {
                previousPage.setEnabled(false);
            }
        });
    }

    private void updateContent() {
        String htmlContent = "<html><body>" + pages.get(currentPage).getContent() + "</body></html>";
        content.setText(htmlContent);
        content.setCaretPosition(0);
    }
}
