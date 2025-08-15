package ui.components;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FileSelector}.
 *
 * <p>Uses Mockito's constructor mocking to stub {@code new JFileChooser()}
 * so the dialog is never shown during tests.</p>
 */
class FileSelectorTest {

    /**
     * Verifies that a valid approved selection returns the chosen file.
     */
    @Test
    void selectBookFile_ReturnsFile_WhenApprovedAndValid() {
        try (MockedConstruction<JFileChooser> mocked =
                     mockConstruction(
                             JFileChooser.class,
                             (chooser, context) -> {
                                 when(chooser.showOpenDialog(null))
                                         .thenReturn(
                                                 JFileChooser.APPROVE_OPTION);
                                 when(chooser.getSelectedFile())
                                         .thenReturn(new File("book.txt"));
                             })) {

            final File file = FileSelector.selectBookFile();
            assertNotNull(file, "Expected a non-null file on approve.");
        }
    }

    /**
     * Verifies that canceling the dialog returns null.
     */
    @Test
    void selectBookFile_ReturnsNull_WhenCancelled() {
        try (MockedConstruction<JFileChooser> mocked =
                     mockConstruction(
                             JFileChooser.class,
                             (chooser, context) -> when(
                                     chooser.showOpenDialog(null))
                                     .thenReturn(JFileChooser.CANCEL_OPTION))) {

            final File file = FileSelector.selectBookFile();
            assertNull(file, "Expected null when dialog is cancelled.");
        }
    }

    /**
     * Verifies that an invalid extension yields null even if approved.
     */
    @Test
    void selectBookFile_ReturnsNull_WhenExtensionInvalid() {
        try (MockedConstruction<JFileChooser> mocked =
                     mockConstruction(
                             JFileChooser.class,
                             (chooser, context) -> {
                                 when(chooser.showOpenDialog(null))
                                         .thenReturn(
                                                 JFileChooser.APPROVE_OPTION);
                                 when(chooser.getSelectedFile())
                                         .thenReturn(new File("image.png"));
                             })) {

            final File file = FileSelector.selectBookFile();
            assertNull(file, "Expected null for unsupported extension.");
        }
    }

    /**
     * Verifies the private constructor throws to prevent instantiation.
     */
    @Test
    void constructor_IsPrivate_AndThrows() throws Exception {
        final Constructor<FileSelector> ctor =
                FileSelector.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        final InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                ctor::newInstance,
                "Expected constructor to throw.");

        // Ensure the cause is the documented exception type.
        if (!(ex.getCause() instanceof UnsupportedOperationException)) {
            throw ex;
        }
    }
}
