package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Diglott Login");
        setSize(320, 200);  // taller for better spacing
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Enter API Key:");
        JTextField keyField = new JTextField();
        keyField.setMaximumSize(new Dimension(250, 25));
        keyField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linkLabel = new JLabel(
                "<html><span style='font-size:10pt'>To obtain your own API, click <a href='#'>here</a>.</span></html>"
        );
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.deepl.com/en/pro-api?utm_term=deepl%20voice%20translator&utm_campaign=CA%7CSearch%7CC%7CBrand%7CT%7CEnglish&utm_source=google&utm_medium=paid&hsa_acc=1083354268&hsa_cam=20413744485&hsa_grp=170892657056&hsa_ad=721386176117&hsa_src=g&hsa_tgt=kwd-1410011109754&hsa_kw=deepl%20voice%20translator&hsa_mt=b&hsa_net=adwords&hsa_ver=3&gad_source=1&gad_campaignid=20413744485&gbraid=0AAAAABbqoWArNOUE6zqaiU8WXkBzHxusF&gclid=EAIaIQobChMI-_Wpy7nDjgMVsUb_AR2vlyqoEAAYASACEgLkMfD_BwE#api-pricing"));
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
