package UI;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Diglott Login");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Enter API Key:");
        JTextField keyField = new JTextField();
        keyField.setPreferredSize(new Dimension(250, 25));  // adjust as needed
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String enteredKey = keyField.getText().trim();

            if (enteredKey.isEmpty()) {
                JOptionPane.showMessageDialog(this, "API key is required.");
            } else {
                new MainUI(enteredKey).setVisible(true);
                dispose();  // close login window
            }
        });

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(keyField);
        panel.add(loginButton);
        add(panel);
    }
}
