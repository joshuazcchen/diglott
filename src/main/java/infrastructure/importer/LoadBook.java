package infrastructure.importer;

import configuration.ConfigDataRetriever;
import domain.model.Book;
import domain.model.Page;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * .dig file importer.
 */
public class LoadBook {

    /**
     * Reads the .dig file, parses JSON array, and returns list of pages.
     *
     * @param file the .dig file to import
     * @return list of Page objects built from the file
     * @throws IOException if file reading fails
     */
    public static Book importBook(final File file) throws IOException {
        String content = Files.readString(file.toPath(),
                StandardCharsets.UTF_8);

        JSONArray pagesJson = new JSONArray(content);
        List<Page> pages = new ArrayList<>();

        for (int i = 0; i < pagesJson.length(); i++) {
            JSONObject pageObj = pagesJson.getJSONObject(i);

            int pageNumber = pageObj.getInt("pageNumber");
            String pageContent = pageObj.getString("content");
            boolean translated = pageObj.getBoolean("translated");
            System.out.println(translated);
            System.out.println("asdfasdfasdfasdfasdfasdfsadf:"+translated);
            List<String> words = List.of(pageContent.split("\\s+"));
            int maxWords = ConfigDataRetriever.getInt("page_length")
                    * ConfigDataRetriever.getInt("page_length");
            Page page = new Page(words, pageNumber, maxWords);
            if (translated) {
                page.translated();
            }
            System.out.println("as'dfadsfasdfasdf"+page.isTranslated());
            pages.add(page);
        }

        return new Book(file.getName(), pages);
    }
}
