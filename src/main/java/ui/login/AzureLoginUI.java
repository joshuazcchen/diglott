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

    /** Callback function. */
    private final LoginCallback callback;

    // === Constants for layout and styling ===

    /** Frame width in pixels. */
    private static final int FRAME_WIDTH = 350;

    /** Frame height in pixels. */
    private static final int FRAME_HEIGHT = 300;

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
     * @param loginCallback is a callback function which allows for a return.
     */
    public AzureLoginUI(final LoginCallback loginCallback) {
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

        JLabel label = createLabel("Enter API Key:", darkMode);
        JTextField keyField = createTextField(darkMode);
        JLabel regionLabel = createLabel("Enter Azure Region:", darkMode);
        JTextField regionField = createTextField(darkMode);

        JButton loginButton = createButton("Login", darkMode);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton cancelButton = createButton("Cancel", darkMode);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linkLabel = createLinkLabel(darkMode);

        // Clicking "Login" should store the API key and launch MainUI
        loginButton.addActionListener(e -> handleLogin(keyField, regionField));

        // Clicking cancel should return you to the original page.
        cancelButton.addActionListener(e -> {
            dispose();
        });

        // Clicking the link should open Microsoft Azure’s API page
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                openAzureLink();
            }
        });

        UIComponents components = new UIComponents(label, keyField, regionLabel,
                regionField, linkLabel, loginButton, cancelButton);
        JPanel panel = createMainPanel(darkMode, components);
        setLayout(new GridBagLayout());
        add(panel);
        setVisible(true);
    }

    /** Creates a JLabel with appropriate color and alignment.
     * @param text the text associated with the label.
     * @param darkMode whether its in dark mode.
     * @return a label it made.
     * */
    private JLabel createLabel(final String text,
                               final boolean darkMode) {
        JLabel label = new JLabel(text);
        label.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /** Creates a JTextField styled according to the darkMode flag.
     * @param darkMode whether it's in dark mode
     * @return a text field it made
     * */
    private JTextField createTextField(final boolean darkMode) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(TEXT_FIELD_WIDTH,
                TEXT_FIELD_HEIGHT));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (darkMode) {
            field.setBackground(new Color(DARK_INPUT_RGB,
                    DARK_INPUT_RGB, DARK_INPUT_RGB));
            field.setForeground(Color.WHITE);
            field.setCaretColor(Color.WHITE);
        } else {
            field.setBackground(Color.WHITE);
            field.setForeground(Color.BLACK);
            field.setCaretColor(Color.BLACK);
        }
        return field;
    }

    /** Creates a JButton styled according to the darkMode flag.
     * @param darkMode whether thine function ist be in dark mode.
     * @param text the text associated with the button being created.
     * @return a button, since this is a button creator.
     * */
    private JButton createButton(final String text,
                                 final boolean darkMode) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (darkMode) {
            button.setBackground(new Color(DARK_BUTTON_RGB,
                    DARK_BUTTON_RGB, DARK_BUTTON_RGB));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(LIGHT_BUTTON_RGB,
                    LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB));
            button.setForeground(Color.BLACK);
        }
        return button;
    }

    /** Creates the clickable link label with proper styling.
     * @param darkMode whether the UI is in dark mode
     * @return a link label.
     * */
    private JLabel createLinkLabel(final boolean darkMode) {
        JLabel linkLabel = new JLabel(String.format(
                "<html><span style='font-size:10pt;"
                        + "color:%s'>To get an API key, "
                        + "click <a style='color:%s;'"
                        + "href=''>here</a>.</span></html>",
                darkMode ? "white" : "black",
                darkMode ? "rgb(90,156,255)" : "rgb(0,102,204)"));
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkLabel.setForeground(darkMode ? DARK_LINK_COLOR : LIGHT_LINK_COLOR);
        return linkLabel;
    }

    /**
     * Creates and lays out the main JPanel with all components.
     *
     * @param darkMode whether dark mode styling is enabled
     * @param components the container of UI components
     * @return the fully constructed JPanel
     */
    private JPanel createMainPanel(final boolean darkMode,
                                   final UIComponents components) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(
                PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
        panel.setBackground(darkMode ? DARK_BG_COLOR : LIGHT_BG_COLOR);

        components.label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(components.label);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(components.keyField);
        panel.add(components.regionLabel);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(components.regionField);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(components.linkLabel);
        panel.add(Box.createVerticalStrut(BUTTON_SPACING));
        panel.add(components.loginButton);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(components.cancelButton);

        return panel;
    }

        /**
         * Container class for grouping UI components.
         * @param regionLabel
         * @param label
         * @param regionField
         * @param loginButton
         * @param linkLabel
         * @param keyField
         * @param cancelButton
         */
        private record UIComponents(JLabel label, JTextField keyField,
                                    JLabel regionLabel,
                                    JTextField regionField,
                                    JLabel linkLabel, JButton loginButton,
                                    JButton cancelButton) {
        /**
         * Constructs UIComponents to group components together.
         *
         * @param label        the API key label
         * @param keyField     the API key input field
         * @param regionLabel  the Azure region label
         * @param regionField  the Azure region input field
         * @param linkLabel    the hyperlink label
         * @param loginButton  the login button
         * @param cancelButton the cancel button
         */
        private UIComponents {
        }
        }

    /**
     * Handles login button logic, testing API key validity,
     * saving configuration, and launching the appropriate UI.
     * @param keyField is an API key field.
     * @param regionField is the region field.
     */
    private void handleLogin(final JTextField keyField,
                             final JTextField regionField) {
        final String azureApiKey = keyField.getText().trim();
        final String azureRegion = regionField.getText().trim();

        if (azureApiKey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "API key is required.");
            return;
        }

        int responseCode = 0;

        try {
            responseCode = testAzureApiKey(azureApiKey, azureRegion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (responseCode == HttpURLConnection.HTTP_OK) {
            ConfigDataRetriever.set("azure_api_key", azureApiKey);
            ConfigDataRetriever.saveConfig();
            dispose();
            callback.onSuccess(azureApiKey);
        } else {
            ConfigDataRetriever.set("azure_api_key", azureApiKey);
            ConfigDataRetriever.saveConfig();
            dispose();
            MainUI.createInstance(ConfigDataRetriever.get("deepl_api_key"),
                    azureApiKey).setVisible(true);
        }
    }

    /**
     * Sends a test request to Azure Translator API to validate the API key.
     * @return HTTP response code of the request, or -1 if failed.
     * @param apiKey is the API key to be tested.
     * @param region is the region for testing.
     */
    private int testAzureApiKey(final String apiKey,
                                final String region) {
        try {
            final URL url = new URL("https://api.cognitive."
                    + "microsofttranslator.com"
                    + "/translate?api-version=3.0&to=fr");
            final HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", apiKey);
            connection.setRequestProperty("Ocp-Apim-Subscription-Region",
                    region);
            connection.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            // Dummy JSON payload for test request
            String jsonPayload = "[{\"Text\": \"test\"}]";
            byte[] postData = jsonPayload.getBytes(StandardCharsets.UTF_8);
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(postData);
            }
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /** Opens the Azure API key page in the user's default browser. */
    private void openAzureLink() {
        try {
            Desktop.getDesktop().browse(new URI(
                    "https://azure.microsoft.com/en-us/"
                    + "services/cognitive-services/translator/"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Could not open the link.");
        }
    }
}
