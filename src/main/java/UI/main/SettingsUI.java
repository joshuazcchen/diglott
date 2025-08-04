package UI.main;

import Configuration.ConfigDataRetriever;
import Configuration.FontList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class SettingsUI extends JFrame {

    public SettingsUI() {
        setTitle("Settings");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        boolean darkMode = Boolean.parseBoolean(ConfigDataRetriever.get("dark_mode"));

        // controls
        JButton pickCredsButton = new JButton("Select Google Credentials");
        JComboBox<Integer> speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JComboBox<String> fontBox = new JComboBox<>(FontList.FONTS.keySet().toArray(new String[0]));
        JCheckBox exponentialGrowthBox = new JCheckBox("Incremental Word Growth");
        JCheckBox originalScriptBox = new JCheckBox("Preserve Original Script");

        speedBox.setSelectedItem(ConfigDataRetriever.getSpeed());
        fontBox.setSelectedItem(ConfigDataRetriever.get("font"));
        exponentialGrowthBox.setSelected(ConfigDataRetriever.getBool("increment"));
        originalScriptBox.setSelected(ConfigDataRetriever.getBool("original_script"));

        // auto save listeners
        pickCredsButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Google Credentials JSON File");
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = fileChooser.getSelectedFile();
                ConfigDataRetriever.set("google_credentials_path", selected.getAbsolutePath());
                ConfigDataRetriever.saveConfig();
                JOptionPane.showMessageDialog(this, "Credentials file saved.");
            }
        });

        speedBox.addActionListener(e -> {
            ConfigDataRetriever.set("speed", String.valueOf(speedBox.getSelectedItem()));
            ConfigDataRetriever.saveConfig();
        });

        fontBox.addActionListener(e -> {
            ConfigDataRetriever.set("font", FontList.FONTS.get(fontBox.getSelectedItem()));
            ConfigDataRetriever.saveConfig();
        });

        exponentialGrowthBox.addActionListener(e -> {
            ConfigDataRetriever.set("increment", exponentialGrowthBox.isSelected());
            ConfigDataRetriever.saveConfig();
        });

        originalScriptBox.addActionListener(e -> {
            ConfigDataRetriever.set("original_script", originalScriptBox.isSelected());
            ConfigDataRetriever.saveConfig();
        });

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(6, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        content.add(pickCredsButton);
        content.add(wrapLabeled("Speed:", speedBox));
        content.add(wrapLabeled("Font:", fontBox));
        content.add(exponentialGrowthBox);
        content.add(originalScriptBox);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        content.add(closeButton);

        if (darkMode) applyDarkTheme(content);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel wrapLabeled(String label, JComponent comp) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(comp);
        return panel;
    }

    private void applyDarkTheme(JPanel panel) {
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
