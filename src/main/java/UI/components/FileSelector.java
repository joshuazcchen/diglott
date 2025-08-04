package UI.components;

import javax.swing.*;
import java.io.File;

public class FileSelector {

    public static File selectBookFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a book file (.txt or .pdf or .epub)");

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String name = file.getName().toLowerCase();
            if (name.endsWith(".txt") || name.endsWith(".pdf") || name.endsWith(".epub")) {
                return file;
            }
        }
        return null;
    }
}
