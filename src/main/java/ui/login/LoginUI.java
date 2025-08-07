package ui.login;

import configuration.ConfigDataRetriever;
import ui.main.MainUI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {

    /** frame width. */
    private static final int FRAME_WIDTH = 400;

    /** frame height. */
    private static final int FRAME_HEIGHT = 200;

    /** padding between each panel. */
    private static final int PANEL_PADDING = 20;

    /** vertical space between elements. */
    private static final int VERTICAL_SPACING = 10;

    /** if currently logged in (azure). */
    private boolean azureLoggedIn = false;

    /** if currently logged in (deepl). */
    private boolean deepLLoggedIn = false;

    /** the azure key. */
    private String azureKey = "none";

    /** the deepl key. */
    private String deepLKey = "none";

    /** the login button. */
    private final JButton continueButton = new JButton("Login");

    /** RGB value for dark button background color (R = G = B). */
    private static final int DARK_BUTTON_RGB = 100;

    /** RGB value for light button background color. */
    private static final int LIGHT_BUTTON_RGB = 230;

    /** Background color for dark mode. */
    private static final Color DARK_BG_COLOR = new Color(60, 63, 65);

    /** Background color for light mode. */
    private static final Color LIGHT_BG_COLOR = Color.WHITE;

    /** Hyperlink color for light mode. */
    private static final Color LIGHT_LINK_COLOR = new Color(0, 102, 204);

    /** creates a login UI for a user to be able to
     * log in using either azure or deepl. */
    public LoginUI() {

        boolean darkMode = false;
        try {
            String darkModeStr = ConfigDataRetriever.get("dark_mode");
            darkMode = darkModeStr != null && Boolean.parseBoolean(darkModeStr);
        } catch (Exception ignored) { }
        setTitle("Diglott - Select Login Method");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(
                PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));

        JButton azureLoginButton = new JButton("Azure API");
        JButton deepLLoginButton = new JButton("DeepL API");

        azureLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deepLLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setEnabled(false);

        if (darkMode) {
            azureLoginButton.setBackground(Color.DARK_GRAY);
            azureLoginButton.setForeground(Color.WHITE);

            deepLLoginButton.setBackground(Color.DARK_GRAY);
            deepLLoginButton.setForeground(Color.WHITE);

            continueButton.setBackground(Color.DARK_GRAY);
            continueButton.setForeground(Color.WHITE);

            panel.setBackground(DARK_BG_COLOR);
            getContentPane().setBackground(DARK_BG_COLOR);
        } else {
            azureLoginButton.setBackground(new Color(
                    LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB));
            azureLoginButton.setForeground(Color.BLACK);

            deepLLoginButton.setBackground(new Color(
                    LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB));
            deepLLoginButton.setForeground(Color.BLACK);

            continueButton.setBackground(new Color(
                    LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB, LIGHT_BUTTON_RGB));
            continueButton.setForeground(Color.BLACK);

            panel.setBackground(LIGHT_BG_COLOR);
            getContentPane().setBackground(LIGHT_BG_COLOR);
        }

        // Azure login action
        azureLoginButton.addActionListener((ActionEvent e) -> {
            new AzureLoginUI((key) -> {
                azureKey = key;
                azureLoggedIn = true;
                updateContinueButton();
            });
        });

        // DeepL login action
        deepLLoginButton.addActionListener((ActionEvent e) -> {
            new DeepLLoginUI((key) -> {
                deepLKey = key;
                deepLLoggedIn = true;
                updateContinueButton();
            });
        });

        // Continue to main UI if at least one is done
        continueButton.addActionListener((ActionEvent e) -> {
            dispose();
            MainUI.createInstance(deepLKey, azureKey).setVisible(true);
        });

        panel.add(azureLoginButton);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        panel.add(deepLLoginButton);
        panel.add(Box.createVerticalStrut(VERTICAL_SPACING * 2));
        panel.add(continueButton);

        add(panel);
        panel.revalidate();
        panel.repaint();
        getContentPane().revalidate();
        getContentPane().repaint();
        setVisible(true);
    }

    private void updateContinueButton() {
        continueButton.setEnabled(azureLoggedIn || deepLLoggedIn);
    }
}
