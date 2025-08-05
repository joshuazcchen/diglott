package infrastructure.translation;

import javax.swing.SwingWorker;

import application.usecase.TranslatePageUseCase;
import domain.model.Page;

public class PageTranslationTask extends SwingWorker<Void, Void> {

    private final TranslatePageUseCase translator;
    private final Page page;
    private final Runnable onTranslationDone;

    public PageTranslationTask(TranslatePageUseCase translator, Page page,
                               Runnable onTranslationDone) {
        this.translator = translator;
        this.page = page;
        this.onTranslationDone = onTranslationDone;
    }

    @Override
    protected Void doInBackground() throws Exception {
        translator.execute(page);
        return null;
    }

    @Override
    protected void done() {
        if (onTranslationDone != null) {
            onTranslationDone.run();
        }
    }
}

