package infrastructure.translation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import application.usecase.TranslatePageUseCase;
import domain.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PageTranslationTask}.
 */
class PageTranslationTaskTest {

    /** Mocked translation use case. */
    private TranslatePageUseCase translator;

    /** Mocked page. */
    private Page page;

    /** Mocked completion callback. */
    private Runnable callback;

    @BeforeEach
    void setUp() {
        translator = mock(TranslatePageUseCase.class);
        page = mock(Page.class);
        callback = mock(Runnable.class);
    }

    /**
     * Verifies that the translator is invoked when the page
     * is not yet translated, and that completion actions run.
     */
    @Test
    void doInBackground_InvokesUseCase_WhenPageNotTranslated()
            throws Exception {
        when(page.isTranslated()).thenReturn(false);

        final PageTranslationTask task =
                new PageTranslationTask(translator, page, callback);

        // Run background work and completion synchronously in test.
        assertDoesNotThrow(task::doInBackground);
        task.done();

        verify(translator, times(1)).execute(page);
        verify(page, times(1)).translated();
        verify(callback, times(1)).run();
    }

    /**
     * Verifies that the translator is NOT invoked when the page
     * is already translated, but completion actions still run.
     */
    @Test
    void doInBackground_SkipsUseCase_WhenPageAlreadyTranslated()
            throws Exception {
        when(page.isTranslated()).thenReturn(true);

        final PageTranslationTask task =
                new PageTranslationTask(translator, page, callback);

        assertDoesNotThrow(task::doInBackground);
        task.done();

        verify(translator, never()).execute(page);
        verify(page, times(1)).translated();
        verify(callback, times(1)).run();
    }
}
