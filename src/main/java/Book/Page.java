package Book;

public class Page {
    private final int pageNumber;
    private final int maxWords;
    private final String content;

    public Page(String content, int pageNumber, int maxWords) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty.");
        }

        if (maxWords <= 0) {
            throw new IllegalArgumentException("Max words must be positive.");
        }

        int wordCount = content.trim().split("\\s+").length;
        if (wordCount > maxWords) {
            throw new IllegalArgumentException(
                    "Content exceeds the maximum allowed words per page: " + wordCount + " > " + maxWords
            );
        }

        this.content = content;
        this.pageNumber = pageNumber;
        this.maxWords = maxWords;
    }

    public String getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public int getWordCount() {
        return content.trim().split("\\s+").length;
    }
}
