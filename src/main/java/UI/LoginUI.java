package UI;

import Configuration.ConfigDataRetriever;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.io.File;

public class LoginUI extends JFrame {

    private static final Color DARK_BG_COLOR = new Color(60, 63, 65);
    private static final Color DARK_LINK_COLOR = new Color(90, 156, 255);
    private static final Color LIGHT_BG_COLOR = Color.WHITE;
    private static final Color LIGHT_LINK_COLOR = new Color(0, 102, 204);

    public LoginUI() {
        setTitle("Diglott Login");
        setSize(320, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boolean darkMode = false;
        try {
            String darkModeStr = ConfigDataRetriever.get("dark_mode");
            darkMode = darkModeStr != null && Boolean.parseBoolean(darkModeStr);
        } catch (Exception ignored) {}

        JLabel label = new JLabel("Enter API Key:");
        JTextField keyField = new JTextField();
        keyField.setMaximumSize(new Dimension(250, 25));
        keyField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton uploadGoogleKey = new JButton("Upload Google TTS Key");
        uploadGoogleKey.setAlignmentX(Component.CENTER_ALIGNMENT);

        uploadGoogleKey.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();

                ConfigDataRetriever.set("gcloud_credentials_path", path);
                JOptionPane.showMessageDialog(null, "Google TTS key uploaded successfully!");
            }
        });

        String linkHtml = String.format(
                "<html><span style='font-size:10pt; color:%s'>To obtain your own API key, click <a style='color:%s;' href=''>here</a>.</span></html>",
                darkMode ? "white" : "black",
                darkMode ? "rgb(90,156,255)" : "rgb(0,102,204)"
        );
        JLabel linkLabel = new JLabel(linkHtml);
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (darkMode) {
            label.setForeground(Color.WHITE);
            keyField.setBackground(new Color(77, 77, 77));
            keyField.setForeground(Color.WHITE);
            keyField.setCaretColor(Color.WHITE);
            loginButton.setBackground(new Color(100, 100, 100));
            loginButton.setForeground(Color.WHITE);
            linkLabel.setForeground(DARK_LINK_COLOR); // Just in case
            getContentPane().setBackground(DARK_BG_COLOR);
        } else {
            label.setForeground(Color.BLACK);
            keyField.setBackground(Color.WHITE);
            keyField.setForeground(Color.BLACK);
            keyField.setCaretColor(Color.BLACK);
            loginButton.setBackground(new Color(230, 230, 230));
            loginButton.setForeground(Color.BLACK);
            linkLabel.setForeground(LIGHT_LINK_COLOR);
            getContentPane().setBackground(LIGHT_BG_COLOR);
        }

        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.deepl.com/en/pro-api"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not open the link.");
                }
            }
        });

        loginButton.addActionListener(e -> {
            String enteredKey = keyField.getText().trim();
            if (enteredKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, "API key is required.");
            } else {
                MainUI.createInstance(enteredKey).setVisible(true);
                dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(darkMode ? DARK_BG_COLOR : LIGHT_BG_COLOR);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(keyField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(linkLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(uploadGoogleKey);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loginButton);

        setLayout(new GridBagLayout());
        add(panel);
    }
}
