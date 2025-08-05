package UI.main;

import configuration.ConfigDataRetriever;
import configuration.FontList;

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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

/**
 * UI window for adjusting Diglott application settings.
 * <p>
 * This allows the user to change font, reading speed,
 * toggle incremental growth, preserve original script,
 * and select Google API credentials.
 */
public class SettingsUI extends JFrame {

    /**
     * Creates and displays the Settings UI.
     */
    public SettingsUI() {
        setTitle("Settings");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        final boolean darkMode = Boolean.parseBoolean(ConfigDataRetriever.get("dark_mode"));

        // UI controls
        JButton pickCredsButton = new JButton("Select Google Credentials");
        JComboBox<Integer> speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JComboBox<Integer> pagesTranslated = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 10, 20});
        pagesTranslated.setEditable(true);
        JComboBox<String> fontBox =
                new JComboBox<>(FontList.FONTS.keySet().toArray(new String[0]));
        JCheckBox exponentialGrowthBox = new JCheckBox("Incremental Word Growth");
        JCheckBox originalScriptBox = new JCheckBox("Preserve Original Script");

        // Initialize controls with current config values
        speedBox.setSelectedItem(ConfigDataRetriever.getSpeed());
        pagesTranslated.setSelectedItem(ConfigDataRetriever.getInt("pages_translated"));
        fontBox.setSelectedItem(ConfigDataRetriever.get("font"));
        exponentialGrowthBox.setSelected(ConfigDataRetriever.getBool("increment"));
        originalScriptBox.setSelected(ConfigDataRetriever.getBool("original_script"));

        // Button: pick Google credentials JSON
        pickCredsButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Google Credentials JSON File");
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = fileChooser.getSelectedFile();
                ConfigDataRetriever.set(
                        "credentials_path",
                        selected.getAbsolutePath()
                );
                ConfigDataRetriever.saveConfig();
                JOptionPane.showMessageDialog(this, "Credentials file saved.");
            }
        });

        // Save changes when speed selection changes
        speedBox.addActionListener(e -> {
            ConfigDataRetriever.set("speed", String.valueOf(speedBox.getSelectedItem()));
            ConfigDataRetriever.saveConfig();
        });

        // Save changes when pages translated changes
        pagesTranslated.addActionListener(e -> {
            ConfigDataRetriever.set("pages_translated",
                    String.valueOf(pagesTranslated.getSelectedItem()));
            ConfigDataRetriever.saveConfig();
        });

        // Save changes when font selection changes
        fontBox.addActionListener(e -> {
            ConfigDataRetriever.set("font", FontList.FONTS.get(fontBox.getSelectedItem()));
            ConfigDataRetriever.saveConfig();
        });

        // Save changes when incremental growth checkbox is toggled
        exponentialGrowthBox.addActionListener(e -> {
            ConfigDataRetriever.set("increment", exponentialGrowthBox.isSelected());
            ConfigDataRetriever.saveConfig();
        });

        // Save changes when preserve original script checkbox is toggled
        originalScriptBox.addActionListener(e -> {
            ConfigDataRetriever.set("original_script", originalScriptBox.isSelected());
            ConfigDataRetriever.saveConfig();
        });

        // Main content panel
        JPanel content = new JPanel();
        content.setLayout(new GridLayout(7, 1, 10, 10));
        content.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add all settings controls
        content.add(pickCredsButton);
        content.add(wrapLabeled("Speed:", speedBox));
        content.add(wrapLabeled("Pages to Translate:", pagesTranslated));
        content.add(wrapLabeled("Font:", fontBox));
        content.add(exponentialGrowthBox);
        content.add(originalScriptBox);

        // Close button at bottom
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        content.add(closeButton);

        // Apply dark theme if enabled
        if (darkMode) {
            applyDarkTheme(content);
        }

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Wraps a label and component together in a horizontal layout.
     *
     * @param label the label text
     * @param comp  the component to place beside the label
     * @return a panel containing the label and component
     */
    private JPanel wrapLabeled(final String label, final JComponent comp) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(comp);
        return panel;
    }

    /**
     * Applies dark theme colors to a panel and all nested components.
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
