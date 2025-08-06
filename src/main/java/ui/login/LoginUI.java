package ui.login;

import configuration.ConfigDataRetriever;
import ui.main.MainUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI extends JFrame {

    private static final Color DARK_BG_COLOR = new Color(60, 63, 65);
    private static final Color DARK_LINK_COLOR = new Color(90, 156, 255);
    private static final Color LIGHT_BG_COLOR = Color.WHITE;
    private static final Color LIGHT_LINK_COLOR = new Color(0, 102, 204);

    @SuppressWarnings("checkstyle:FinalLocalVariable")
    public LoginUI() {
        setTitle("Diglott Login");
        setSize(350, 220);
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

        JLabel linkLabel = new JLabel(String.format(
                "<html><span style='font-size:10pt; color:%s'>To get an API key, click <a style='color:%s;' href=''>here</a>.</span></html>",
                darkMode ? "white" : "black",
                darkMode ? "rgb(90,156,255)" : "rgb(0,102,204)"
        ));
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Apply dark/light styles
        if (darkMode) {
            label.setForeground(Color.WHITE);
            keyField.setBackground(new Color(77, 77, 77));
            keyField.setForeground(Color.WHITE);
            keyField.setCaretColor(Color.WHITE);
            loginButton.setBackground(new Color(100, 100, 100));
            loginButton.setForeground(Color.WHITE);
            linkLabel.setForeground(DARK_LINK_COLOR);
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

        // Clicking "Login" should store the API key and launch MainUI
        loginButton.addActionListener(e -> {
            int responseCode = 0;
            final String apiKey = keyField.getText().trim();
            if (apiKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, "API key is required.");
                return;
            }

            try {
                final URL url = new URL("https://api-free.deepl.com/v2/usage?auth_key=" + apiKey);

                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                responseCode = connection.getResponseCode();

                connection.disconnect();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            if (responseCode != HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please verify your key is correctly inputted.",
                        "Could not connect to the API",
                        JOptionPane.WARNING_MESSAGE
                );
                System.out.println(responseCode);
            } else {
                ConfigDataRetriever.set("api_key", apiKey);
                ConfigDataRetriever.saveConfig();

                dispose();
                MainUI.createInstance(apiKey).setVisible(true);
            }
        });

        // Clicking the link should open DeepL’s API page
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.deepl.com/en/pro-api"));
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Could not open the link.");
                }
            }
        });

        // UI Layout
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
        panel.add(loginButton);

        setLayout(new GridBagLayout());
        add(panel);
    }
}
