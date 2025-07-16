package Book;

public class Page {
    private final int pageNumber;
    private final String content;
    private final int numWords;

    public Page(int pageNumber, String content) {
        this.pageNumber = pageNumber;
        this.content = content;
        this.numWords = countWords(content);
    }

    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) return 0;
        return text.trim().split("\\s+").length;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getContent() {
        return content;
    }

    public int getNumWords() {
        return numWords;
    }
}
