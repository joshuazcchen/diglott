package UI.main;

import static org.mockito.Mockito.*;

import application.controller.SpeakController;
import application.usecase.TranslatePageUseCase;
import domain.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import java.util.Arrays;
import java.util.List;

public class PageUITest {

    private TranslatePageUseCase translatorMock;
    private List<Page> pages;
    private PageUI pageUI;

    private JButton previousPageButton;
    private JButton nextPageButton;

    @BeforeEach
    public void setup() {
        translatorMock = mock(TranslatePageUseCase.class);

        Page page1 = mock(Page.class);
        when(page1.isTranslated()).thenReturn(true);
        when(page1.getContent()).thenReturn("Page 1 content");

        Page page2 = mock(Page.class);
        when(page2.isTranslated()).thenReturn(false);
        when(page2.getContent()).thenReturn("Page 2 content");

        Page page3 = mock(Page.class);
        when(page3.isTranslated()).thenReturn(false);
        when(page3.getContent()).thenReturn("Page 3 content");

        pages = Arrays.asList(page1, page2, page3);

        pageUI = new PageUI(pages, false, translatorMock, null);

        previousPageButton = new JButton();
        nextPageButton = new JButton();
    }

    /*
    Tests that clicking next page triggers async translation if it is not already translated.
     */
    @Test
    public void testGoToNextPage_triggersTranslationAsync() {
        List<Page> pages = mock(List.class);
        Page page0 = mock(Page.class);
        Page page1 = mock(Page.class);
        when(pages.size()).thenReturn(2);
        when(pages.get(0)).thenReturn(page0);
        when(pages.get(1)).thenReturn(page1);
        when(page0.isTranslated()).thenReturn(true);
        when(page1.isTranslated()).thenReturn(false);
        TranslatePageUseCase translatorMock = mock(TranslatePageUseCase.class);
        PageUI pageUI = new PageUI(pages, false, translatorMock, mock(SpeakController.class));
        pageUI.goToNextPage(mock(JButton.class), mock(JButton.class));
        verify(translatorMock).execute(page1);
    }

}

