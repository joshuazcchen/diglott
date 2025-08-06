package diglott;

/**
 * Application entry point.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        // Prevent instantiation
    }

    /**
     * Launches the Diglott application.
     *
     * @param args the command-line arguments (unused)
     */
    public static void main(final String[] args) {
        new DiglottLauncher().start();
    }
}
