package ui.main;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import application.controller.SpeakController;
import application.usecase.TranslatePageUseCase;
import domain.model.Book;
import domain.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;

public class PageUITest {

    private TranslatePageUseCase translatorMock;
    private Book book;
    private PageUI pageUI;

    private Page page1;
    private Page page2;
    private Page page3;

    @BeforeEach
    public void setup() {
        translatorMock = mock(TranslatePageUseCase.class);

        page1 = mock(Page.class);
        when(page1.isTranslated()).thenReturn(true);
        when(page1.getContent()).thenReturn("Page 1 content");

        page2 = mock(Page.class);
        when(page2.isTranslated()).thenReturn(false);
        when(page2.getContent()).thenReturn("Page 2 content");

        page3 = mock(Page.class);
        when(page3.isTranslated()).thenReturn(false);
        when(page3.getContent()).thenReturn("Page 3 content");

        book = mock(Book.class);
        when(book.getTitle()).thenReturn("Test Book");
        when(book.getTotalPages()).thenReturn(3);
        when(book.getCurrentPageNumber()).thenReturn(1);
        when(book.getCurrentPage()).thenReturn(page1);

        doAnswer(invocation -> {
            when(book.getCurrentPageNumber()).thenReturn(2);
            when(book.getCurrentPage()).thenReturn(page2);
            return null;
        }).when(book).nextPage();

        pageUI = new PageUI(book, false, translatorMock, mock(SpeakController.class));
    }

    /*
     Tests that clicking "Next Page" triggers async translation if the next page
     is not already translated.
     */
    @Test
    public void testGoToNextPage_triggersTranslationAsync() throws Exception {
        JButton nextBtn = findButtonByText(pageUI.getContentPane(), "Next Page");
        assertNotNull(nextBtn, "Next Page button not found in PageUI");
        SwingUtilities.invokeAndWait(nextBtn::doClick);
        verify(translatorMock, timeout(1000)).execute(page2);
    }

    // Helper: recursively search container for a JButton with given text
    private JButton findButtonByText(Container container, String text) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && text.equals(((JButton) c).getText())) {
                return (JButton) c;
            }
            if (c instanceof Container) {
                JButton found = findButtonByText((Container) c, text);
                if (found != null) return found;
            }
        }
        return null;
    }
}
