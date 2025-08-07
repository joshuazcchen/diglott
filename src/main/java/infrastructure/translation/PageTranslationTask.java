package infrastructure.translation;

import javax.swing.SwingWorker;

import application.usecase.TranslatePageUseCase;
import domain.model.Page;

/**
 * A background SwingWorker task that performs page translation
 * asynchronously using a TranslatePageUseCase.
 */
public class PageTranslationTask extends SwingWorker<Void, Void> {

    /**
     * The translation use case to execute.
     */
    private final TranslatePageUseCase translator;

    /**
     * The page to translate.
     */
    private final Page page;

    /**
     * The callback to run when translation is complete.
     */
    private final Runnable onTranslationDone;

    /**
     * Constructs a PageTranslationTask.
     *
     * @param useCase        the TranslatePageUseCase to perform the translation
     * @param pageToTranslate the Page to translate
     * @param callback        a Runnable to execute when translation is complete
     */
    public PageTranslationTask(final TranslatePageUseCase useCase,
                               final Page pageToTranslate,
                               final Runnable callback) {
        this.translator = useCase;
        this.page = pageToTranslate;
        this.onTranslationDone = callback;
    }

    /**
     * Executes the translation logic in the background.
     *
     * @return {@code null} on completion
     * @throws Exception if translation fails
     */
    @Override
    protected Void doInBackground() throws Exception {
        if (!page.isTranslated()) {
            translator.execute(page);
        }
        return null;
    }

    /**
     * Called on the UI thread after the background task completes.
     */
    @Override
    protected void done() {
        if (onTranslationDone != null) {
            page.translated();
            onTranslationDone.run();
        }
    }
}
