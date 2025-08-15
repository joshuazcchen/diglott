package diglott;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

/**
 * Tests for {@link Main}.
 */
class MainTest {

    /**
     * Verifies that main() constructs a launcher and calls start().
     */
    @Test
    @DisplayName("main(): constructs DiglottLauncher and starts")
    void main_ConstructsAndStarts() {
        try (MockedConstruction<DiglottLauncher> ctor =
                     mockConstruction(DiglottLauncher.class)) {

            Main.main(new String[] {});

            // Exactly one launcher should be constructed.
            assert ctor.constructed().size() == 1;

            final DiglottLauncher instance = ctor.constructed()
                    .get(0);
            verify(instance).start();
        }
    }
}
