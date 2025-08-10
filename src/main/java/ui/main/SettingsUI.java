package ui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import configuration.ConfigDataRetriever;
import configuration.FontList;
import ui.login.AzureLoginUI;
import ui.login.DeepLLoginUI;

/**
 * UI window for adjusting Diglott application settings.
 *
 * <p>
 * This allows the user to change font, reading speed,
 * toggle incremental growth, preserve original script,
 * and select Google API credentials.
 */
public class SettingsUI extends JFrame {

    /** Default width for the settings window. */
    private static final int DEFAULT_WIDTH = 500;

    /** Default height for the settings window. */
    private static final int DEFAULT_HEIGHT = 350;

    /** Default padding for the content panel. */
    private static final int PANEL_PADDING = 15;

    /** Default spacing between grid elements. */
    private static final int GRID_SPACING = 10;

    /** Number of rows in the grid layout. */
    private static final int GRID_ROWS = 10;

    /** Number of columns in the grid layout. */
    private static final int GRID_COLUMNS = 2;

    /** Speed option values. */
    private static final Integer[] SPEED_OPTIONS = {1, 2, 3, 4, 5};

    /** Page translation option values. */
    private static final Integer[] PAGE_OPTIONS = {1, 2, 3, 4, 5, 10, 20};

    /**
     * Creates and displays the Settings UI.
     */
    public SettingsUI() {
        initializeFrame();

        final boolean darkMode = Boolean.
                parseBoolean(ConfigDataRetriever.get("dark_mode"));

        // Create UI controls
        final JButton addAzureButton = createAzureButton();
        final JButton pickCredsButton = createCredentialsButton();
        final JButton addDeepLButton = createDeepLButton();

        final JComboBox<Integer> speedBox = createSpeedBox();
        final JComboBox<Integer> pagesTranslated = createPagesBox();
        final JComboBox<String> fontBox = createFontBox();

        final JCheckBox exponentialGrowthBox = createGrowthBox();
        final JCheckBox originalScriptBox = createScriptBox();

        // Group
        final JButton[] buttons = {addAzureButton,
                addDeepLButton, pickCredsButton};
        final JComboBox<?>[] comboBoxes =
                {speedBox, pagesTranslated, fontBox};
        final JCheckBox[] checkBoxes =
                {exponentialGrowthBox, originalScriptBox};

        // Build panel
        final JPanel content = createContentPanel(buttons,
                comboBoxes, checkBoxes, darkMode);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Initializes the frame properties.
     */
    private void initializeFrame() {
        setTitle("Settings");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * Creates the azure button with its action listener.
     *
     * @return the configured button
     */
    private JButton createAzureButton() {
        final JButton button = new JButton("Update Azure Key");
        button.addActionListener(actionEvent -> handleAzureLogin());
        return button;
    }

    private JButton createDeepLButton() {
        final JButton button = new JButton("Update DeepL Key");
        button.addActionListener(actionEvent -> handleDeepLLogin());
        return button;
    }

    /**
     * Handles the credential file selection process.
     */
    private void handleAzureLogin() {
        new AzureLoginUI((key, region) -> {
            ConfigDataRetriever.set("azure_api_key", key);
            ConfigDataRetriever.set("azure_region", region);
            ConfigDataRetriever.saveConfig();
            JOptionPane.showMessageDialog(this,
                    "Key and region saved!");
        }).setVisible(true);
    }

    /**
     * Handles the credential file selection process for DeepL.
     */
    private void handleDeepLLogin() {
        new DeepLLoginUI(key -> {
            ConfigDataRetriever.set("deepl_api_key", key);
            ConfigDataRetriever.saveConfig();
            JOptionPane.showMessageDialog(this, "Key saved!");
        }).setVisible(true);
    }


    /**
     * Creates the credentials button with its action listener.
     *
     * @return the configured button
     */
    private JButton createCredentialsButton() {
        final JButton button = new JButton("Select Google Credentials");
        button.addActionListener(actionEvent -> handleCredentialsSelection());
        return button;
    }

    /**
     * Handles the credential file selection process.
     */
    private void handleCredentialsSelection() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Google Credentials JSON File");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "JSON files", "json"));

        final int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            final File selected = fileChooser.getSelectedFile();
            ConfigDataRetriever.set(
                    "credentials_path",
                    selected.getAbsolutePath()
            );
            ConfigDataRetriever.saveConfig();
            JOptionPane.showMessageDialog(this, "Credentials file saved.");
        }
    }

    /**
     * Creates the speed selection box with its action listener.
     *
     * @return the configured combo box
     */
    private JComboBox<Integer> createSpeedBox() {
        final JComboBox<Integer> box = new JComboBox<>(SPEED_OPTIONS);
        box.setSelectedItem(ConfigDataRetriever.getSpeed());
        box.addActionListener(actionEvent -> {
            ConfigDataRetriever.set("speed",
                    String.valueOf(box.getSelectedItem()));
            ConfigDataRetriever.saveConfig();
        });
        return box;
    }

    /**
     * Creates the pages translated box with its action listener.
     *
     * @return the configured combo box
     */
    private JComboBox<Integer> createPagesBox() {
        final JComboBox<Integer> box = new JComboBox<>(PAGE_OPTIONS);
        box.setEditable(true);
        box.setSelectedItem(ConfigDataRetriever.getInt("pages_translated"));
        box.addActionListener(actionEvent -> {
            ConfigDataRetriever.set("pages_translated",
                    String.valueOf(box.getSelectedItem()));
            ConfigDataRetriever.saveConfig();
        });
        return box;
    }

    /**
     * Creates the font selection box with its action listener.
     *
     * @return the configured combo box
     */
    private JComboBox<String> createFontBox() {
        final JComboBox<String> box =
                new JComboBox<>(FontList.FONTS.keySet().toArray(new String[0]));
        box.setSelectedItem(ConfigDataRetriever.get("font"));
        box.addActionListener(actionEvent -> {
            String selectedKey = (String) box.getSelectedItem();
            String fontValue = FontList.FONTS.get(selectedKey);
            if (fontValue != null) {
                ConfigDataRetriever.set("font", fontValue);
                ConfigDataRetriever.saveConfig();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Selected font not found.",
                        "Font Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        return box;
    }

    /**
     * Creates the incremental growth checkbox with its action listener.
     *
     * @return the configured checkbox
     */
    private JCheckBox createGrowthBox() {
        final JCheckBox box = new JCheckBox("Incremental Word Growth");
        box.setSelected(ConfigDataRetriever.getBool("increment"));
        box.addActionListener(actionEvent -> {
            ConfigDataRetriever.set("increment", box.isSelected());
            ConfigDataRetriever.saveConfig();
        });
        return box;
    }

    /**
     * Creates the original script checkbox with its action listener.
     *
     * @return the configured checkbox
     */
    private JCheckBox createScriptBox() {
        final JCheckBox box = new JCheckBox("Preserve Original Script");
        box.setSelected(ConfigDataRetriever.getBool("original_script"));
        box.addActionListener(actionEvent -> {
            ConfigDataRetriever.set("original_script", box.isSelected());
            ConfigDataRetriever.saveConfig();
        });
        return box;
    }

    /**
     * Creates the main content panel with all UI controls.
     *
     * @param buttons , all the buttons.
     * @param comboBoxes , all the combo boxes.
     * @param checkBoxes , all the checkboxes.
     * @param darkMode whether to apply dark mode
     * @return the configured content panel
     */
    private JPanel createContentPanel(
            final JButton[] buttons,
            final JComboBox<?>[] comboBoxes,
            final JCheckBox[] checkBoxes,
            final boolean darkMode) {

        final JPanel content = new JPanel();
        content.setLayout(new GridLayout(GRID_ROWS, GRID_COLUMNS,
                GRID_SPACING, GRID_SPACING));
        content.setBorder(BorderFactory.createEmptyBorder(PANEL_PADDING,
                PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));

        // add buttons
        for (JButton button : buttons) {
            content.add(button);
        }

        // match labels
        final String[] labels = {"Speed:", "Pages to Translate:", "Font:"};
        for (int i = 0; i < comboBoxes.length; i++) {
            content.add(wrapLabeled(labels[i], comboBoxes[i]));
        }

        // add checkboxes
        for (JCheckBox checkBox : checkBoxes) {
            content.add(checkBox);
        }

        // close button
        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        content.add(closeButton);

        if (darkMode) {
            applyDarkTheme(content);
        }

        return content;
    }

    /**
     * Wraps a label and component together in a horizontal layout.
     *
     * @param label the label text
     * @param comp  the component to place beside the label
     * @return a panel containing the label and component
     */
    private JPanel wrapLabeled(final String label, final JComponent comp) {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(comp);
        return panel;
    }

    /**
     * Applies theme colors to a panel and all nested components.
     *
     * @param panel the panel to apply the theme to
     */
    private void applyDarkTheme(final JPanel panel) {
        panel.setBackground(Color.DARK_GRAY);
        for (Component c : panel.getComponents()) {
            if (c instanceof JLabel || c instanceof JCheckBox) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.DARK_GRAY);
            }
            if (c instanceof JPanel) {
                c.setBackground(Color.DARK_GRAY);
                for (Component sub : ((JPanel) c).getComponents()) {
                    sub.setForeground(Color.WHITE);
                    sub.setBackground(Color.DARK_GRAY);
                }
            }
        }
    }
}
