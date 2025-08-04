package domain.gateway;

import java.io.File;
import java.io.IOException;

public interface BookImporter {
    String importBook(File file) throws IOException;
}
