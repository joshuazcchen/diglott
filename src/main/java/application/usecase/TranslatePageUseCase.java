package application.usecase;

import domain.model.Page;

/**
 * Use case interface for translating a page.
 */
public interface TranslatePageUseCase {

    /**
     * Translates and formats the specified page's content.
     *
     * @param page the page to be translated
     */
    void execute(Page page);
}
