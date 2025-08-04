package UI;

import Audio.SpeechManager;
import Translation.StoredWords;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class SpeakUI extends JFrame {

    private SpeechManager speechManager;
    private final List<String> words;
    private final StoredWords storedWords;

    public SpeakUI(List<String> words, String credentialsPath, StoredWords storedWords) {
        this.words = words;
        this.storedWords = storedWords;

        setTitle("Speak Words");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            speechManager = new SpeechManager(credentialsPath);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to initialize SpeechManager.");
            dispose();
            return;
        }

        // Grid layout: dynamic rows, 3 columns
        JPanel wordPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        wordPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wordPanel.setBackground(Color.WHITE);

        String targetLang = Configuration.ConfigDataRetriever.get("target_language");
        Map<String, String> translations = storedWords.getTranslations();

        for (String rawWord : words) {
            String plainWord = stripHtmlTags(rawWord);
            JLabel wordLabel = new JLabel(plainWord, SwingConstants.CENTER);
            wordLabel.setOpaque(true);
            wordLabel.setBackground(new Color(240, 240, 240));
            wordLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            wordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            wordLabel.setPreferredSize(new Dimension(150, 40));
            wordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            // Highlight translated words
            if (translations.containsValue(plainWord)) {
                wordLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                wordLabel.setForeground(new Color(30, 90, 200)); // blue
            } else {
                wordLabel.setForeground(Color.DARK_GRAY);
            }

            wordLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new Thread(() -> {
                        try {
                            // If it's a translated word, use targetLang — else use inputLang
                            String lang = translations.containsValue(plainWord)
                                    ? targetLang
                                    : Configuration.ConfigDataRetriever.get("input_language");

                            speechManager.speak(plainWord, lang);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(SpeakUI.this, "Failed to speak word: " + plainWord);
                        }
                    }).start();
                }
            });

            wordPanel.add(wordLabel);
        }

        JScrollPane scrollPane = new JScrollPane(wordPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            speechManager.close();
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Remove HTML tags from word content like <b>, <u>, etc.
     */
    private String stripHtmlTags(String text) {
        return text.replaceAll("<[^>]*>", "").trim();
    }
}