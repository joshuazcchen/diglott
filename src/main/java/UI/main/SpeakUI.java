package UI.main;

import Configuration.ConfigDataRetriever;
import application.controller.SpeakController;
import domain.model.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SpeakUI extends JFrame {

    public SpeakUI(Page page, SpeakController speakController, boolean darkMode) {
        setTitle("Speak Words");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<String> translatedWords = page.getWords();
        List<String> originalWords = page.getOriginalWords();

        String inputLangCode = ConfigDataRetriever.get("input_language");
        String targetLangCode = ConfigDataRetriever.get("target_language");

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(darkMode ? Color.DARK_GRAY : Color.WHITE);

        for (int i = 0; i < translatedWords.size(); i++) {
            String trans = stripHtml(translatedWords.get(i));
            String orig = (i < originalWords.size()) ? stripHtml(originalWords.get(i)) : "";

            String langCode = trans.equals(orig) ? inputLangCode : targetLangCode;

            JButton wordButton = new JButton(trans);
            wordButton.setFocusPainted(false);
            wordButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            wordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            wordButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

            if (darkMode) {
                wordButton.setBackground(Color.GRAY);
                wordButton.setForeground(Color.WHITE);
                wordButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

                wordButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        wordButton.setBackground(new Color(100, 100, 100));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        wordButton.setBackground(Color.GRAY);
                    }
                });
            } else {
                wordButton.setBackground(new Color(240, 240, 240));
                wordButton.setForeground(Color.BLACK);
                wordButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        wordButton.setBackground(new Color(220, 220, 220));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        wordButton.setBackground(new Color(240, 240, 240));
                    }
                });
            }

            wordButton.addActionListener((ActionEvent e) -> speakController.speakWord(trans, langCode));
            gridPanel.add(wordButton);
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane);
        setVisible(true);
    }

    private String stripHtml(String input) {
        return input.replaceAll("<[^>]*>", "").replace("&nbsp;", " ").trim();
    }
}
