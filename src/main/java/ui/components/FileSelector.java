package UI.components;

import javax.swing.JFileChooser;
import java.io.File;

/**
 * Utility component for selecting supported book files.
 */
public class FileSelector {

    /**
     * Opens a file chooser dialog and returns a valid book file.
     *
     * @return a File with .txt, .pdf, or .epub extension, or null if cancelled/invalid
     */
    public static File selectBookFile() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a book file (.txt or .pdf or .epub)");

        final int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            final String name = file.getName().toLowerCase();
            if (name.endsWith(".txt")
                    || name.endsWith(".pdf")
                    || name.endsWith(".epub")) {
                return file;
            }
        }
        return null;
    }
}
