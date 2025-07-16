package UI;

import Book.Book;
import Book.Page;

import javax.swing.*;
import java.awt.*;

public class BookViewerUI extends JFrame {
    private final JTextArea textArea;
    private final JButton nextButton;
    private final JButton prevButton;
    private final JLabel pageLabel;

    private final Book book;

    public BookViewerUI(Book book) {
        this.book = book;

        setTitle("Translated Book");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        nextButton = new JButton("Next");
        prevButton = new JButton("Back");
        pageLabel = new JLabel();  // NEW: Page number label
        pageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel navButtons = new JPanel();
        navButtons.add(prevButton);
        navButtons.add(nextButton);

        buttonPanel.add(pageLabel, BorderLayout.NORTH);
        buttonPanel.add(navButtons, BorderLayout.SOUTH);

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        updateText();
        addListeners();

        setVisible(true);
    }

    private void updateText() {
        Page currentPage = book.getCurrentPage();
        textArea.setText(currentPage.getContent());

        int current = book.getCurrentPageIndex() + 1;
        int total = book.getPageCount();
        pageLabel.setText("Page " + current + " of " + total);
    }

    private void addListeners() {
        nextButton.addActionListener(e -> {
            book.nextPage();
            updateText();
        });

        prevButton.addActionListener(e -> {
            book.previousPage();
            updateText();
        });
    }
}
