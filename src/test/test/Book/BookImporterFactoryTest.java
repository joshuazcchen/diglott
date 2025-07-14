package Book;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BookImporterFactoryTest {
//
//    @Test
//    void testTxtFileReturnsTxtBookImporter() {
//        File txtFile = new File("test.txt");
//        BookImporter importer = BookImporterFactory.getImporter(txtFile);
//        assertTrue(importer instanceof TxtBookImporter);
//    }
//
//    @Test
//    void testUnsupportedExtensionThrows() {
//        File unknownFile = new File("test.docx");
//        assertThrows(IllegalArgumentException.class, () -> {
//            BookImporterFactory.getImporter(unknownFile);
//        });
//    }
//}
