    package domain.gateway;

    import java.io.File;
    import java.io.IOException;

    /**
     * Gateway interface for importing book content from a file.
     */
    public interface BookImporter {

        /**
         * Reads and extracts text content from the specified file.
         *
         * @param file the file to import
         * @return the extracted text content as a string
         * @throws IOException if the file cannot be read or parsed
         */
        String importBook(File file) throws IOException;
    }
