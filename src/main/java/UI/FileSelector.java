package UI;

import javax.swing.*;
import java.io.File;

public class FileSelector {

    public File selectBookFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a book file (.txt or .pdf)");

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String name = file.getName().toLowerCase();
            if (name.endsWith(".txt") || name.endsWith(".pdf")) {
                return file;
            }
        }
        return null;
    }
}
