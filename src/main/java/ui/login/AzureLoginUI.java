package ui.login;

import configuration.ConfigDataRetriever;
import ui.main.MainUI;

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
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.nio.charset.StandardCharsets;

public class AzureLoginUI extends JFrame {

    // === Constants for layout and styling ===

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
     */
    @SuppressWarnings("checkstyle:FinalLocalVariable")
    public AzureLoginUI() {
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

        JLabel regionLabel = new JLabel("Enter Azure Region:");
        JTextField regionField = new JTextField();
        regionField.setMaximumSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        regionField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

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
            regionLabel.setForeground(Color.WHITE);
            regionField.setBackground(new Color(
                    DARK_INPUT_RGB, DARK_INPUT_RGB, DARK_INPUT_RGB));
            regionField.setForeground(Color.WHITE);
            regionField.setCaretColor(Color.WHITE);
            loginButton.setBackground(new Color(
                    DARK_BUTTON_RGB, DARK_BUTTON_RGB, DARK_BUTTON_RGB));
            loginButton.setForeground(Color.WHITE);
            linkLabel.setForeground(DARK_LINK_COLOR);
            getContentPane().setBackground(DARK_BG_COLOR);
        } else {
            label.setForeground(Color.BLACK);
            keyField.setBackground(Color.WHITE);
            keyField.setForeground(Color.BLACK);
            keyField.setCaretColor(Color.BLACK);
            loginButton.setBackground(new Color(
                    LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB));
            loginButton.setForeground(Color.BLACK);
            linkLabel.setForeground(LIGHT_LINK_COLOR);
            getContentPane().setBackground(LIGHT_BG_COLOR);
        }

        // Clicking "Login" should store the API key and launch MainUI
        loginButton.addActionListener(e -> {
            int responseCode = 0;
            final String azureApiKey = keyField.getText().trim();
            final String azureRegion = regionField.getText().trim();
            if (azureApiKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, "API key is required.");
                return;
            }

            try {
                final URL url = new URL("https://api.cognitive.microsoft"
                        + "translator.com/translate?api-version=3.0&to=fr");
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Ocp-Apim-Subscription-Key", azureApiKey);
                connection.setRequestProperty("Ocp-Apim-Subscription-Region", azureRegion);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                // Dummy JSON payload for test request
                String jsonPayload = "[{\"Text\": \"test\"}]";
                byte[] postData = jsonPayload.getBytes(StandardCharsets.UTF_8);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(postData);
                }
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
                ConfigDataRetriever.set("azure_api_key", azureApiKey);
                ConfigDataRetriever.saveConfig();
                dispose();
                MainUI.createInstance(azureApiKey).setVisible(true);
            }
        });

        // Clicking the link should open Microsoft Azure’s API page
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(
                            "https://azure.microsoft.com/en-us/services/"
                                    + "cognitive-services/translator/"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Could not open the link.");
                }
            }
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
        panel.add(regionLabel);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(regionField);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(linkLabel);
        panel.add(Box.createVerticalStrut(BUTTON_SPACING));
        panel.add(loginButton);

        setLayout(new GridBagLayout());
        add(panel);
        setVisible(true);
    }
}
