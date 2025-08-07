package ui.login;

import configuration.ConfigDataRetriever;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class DeepLLoginUI extends JFrame {

    /** Callback function. */
    private final LoginCallback callback;

    /** Frame width in pixels. */
    private static final int FRAME_WIDTH = 350;

    /** Frame height in pixels. */
    private static final int FRAME_HEIGHT = 220;

    /** Text field width in pixels. */
    private static final int TEXT_FIELD_WIDTH = 250;

    /** Text field height in pixels. */
    private static final int TEXT_FIELD_HEIGHT = 25;

    /** RGB value for dark input background color (R = G = B). */
    private static final int DARK_INPUT_RGB = 77;

    /** RGB value for dark button background color (R = G = B). */
    private static final int DARK_BUTTON_RGB = 100;

    /** RGB value for light button background color. */
    private static final int LIGHT_BUTTON_RGB = 230;

    /** Vertical spacing between components. */
    private static final int VERTICAL_SPACING = 5;

    /** Larger vertical spacing used before button. */
    private static final int BUTTON_SPACING = 10;

    /** Padding for the main panel border (top, left, bottom, right). */
    private static final int PANEL_PADDING = 20;

    // === Color constants ===

    /** Background color for dark mode. */
    private static final Color DARK_BG_COLOR = new Color(60, 63, 65);

    /** Hyperlink color for dark mode. */
    private static final Color DARK_LINK_COLOR = new Color(90, 156, 255);

    /** Background color for light mode. */
    private static final Color LIGHT_BG_COLOR = Color.WHITE;

    /** Hyperlink color for light mode. */
    private static final Color LIGHT_LINK_COLOR = new Color(0, 102, 204);

    /**
     * Suppresses Checkstyle warning for non-final local variables.
     * This constructor uses non-final variables (e.g., darkMode) intentionally
     * for clarity and to allow conditional reassignment.
     * @param loginCallback is a callback.
     */
    public DeepLLoginUI(final LoginCallback loginCallback) {
        this.callback = loginCallback;
        setTitle("Diglott Login");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boolean darkMode = false;
        try {
            String darkModeStr = ConfigDataRetriever.get("dark_mode");
            darkMode = darkModeStr != null && Boolean.parseBoolean(darkModeStr);
        } catch (Exception ignored) { }

        JLabel label = new JLabel("Enter API Key:");
        JTextField keyField = new JTextField();
        keyField.setMaximumSize(new Dimension(
                TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        keyField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linkLabel = new JLabel(String.format(
                "<html><span style='font-size:10pt; "
                        + "color:%s'>To get an API key, "
                        + "click <a style='color:%s;' "
                        + "href=''>here</a>.</span></html>",
                darkMode ? "white" : "black",
                darkMode ? "rgb(90,156,255)" : "rgb(0,102,204)"
        ));
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Apply dark/light styles
        if (darkMode) {
            label.setForeground(Color.WHITE);
            keyField.setBackground(new Color(
                    DARK_INPUT_RGB, DARK_INPUT_RGB, DARK_INPUT_RGB));
            keyField.setForeground(Color.WHITE);
            keyField.setCaretColor(Color.WHITE);
            loginButton.setBackground(new Color(
                    DARK_BUTTON_RGB, DARK_BUTTON_RGB, DARK_BUTTON_RGB));
            loginButton.setForeground(Color.WHITE);
            cancelButton.setBackground(new Color(
                    DARK_BUTTON_RGB, DARK_BUTTON_RGB, DARK_BUTTON_RGB));
            cancelButton.setForeground(Color.WHITE);
            linkLabel.setForeground(DARK_LINK_COLOR);
            getContentPane().setBackground(DARK_BG_COLOR);
        } else {
            label.setForeground(Color.BLACK);
            keyField.setBackground(Color.WHITE);
            keyField.setForeground(Color.BLACK);
            keyField.setCaretColor(Color.BLACK);
            cancelButton.setBackground(new Color(
                    LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB));
            cancelButton.setForeground(Color.BLACK);
            loginButton.setBackground(new Color(
                    LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB));
            loginButton.setForeground(Color.BLACK);
            linkLabel.setForeground(LIGHT_LINK_COLOR);
            getContentPane().setBackground(LIGHT_BG_COLOR);
        }

        // Clicking "Login" should store the API key and launch MainUI
        loginButton.addActionListener(e -> {
            int responseCode = 0;
            final String deepLApiKey = keyField.getText().trim();
            if (deepLApiKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, "API key is required.");
                return;
            }

            try {
                final URL url = new URL(
                        "https://api-free.deepl.com/v2/usage?auth_key="
                                + deepLApiKey);
                final HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                responseCode = connection.getResponseCode();
                connection.disconnect();
            } catch (IOException ex) {
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
                ConfigDataRetriever.set("deepl_api_key", deepLApiKey);
                ConfigDataRetriever.saveConfig();
                dispose();
                callback.onSuccess(deepLApiKey);
            }
        });

        // Clicking the link should open DeepL’s API page
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(
                            "https://www.deepl.com/en/pro-api"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Could not open the link.");
                }
            }
        });

        // Clicking cancel should return you to the original page.
        cancelButton.addActionListener(e -> {
            dispose();
        });

        // UI Layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(
                PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
        panel.setBackground(darkMode ? DARK_BG_COLOR : LIGHT_BG_COLOR);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(keyField);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(linkLabel);
        panel.add(Box.createVerticalStrut(BUTTON_SPACING));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(cancelButton);

        setLayout(new GridBagLayout());
        add(panel);
        setVisible(true);
    }
}
