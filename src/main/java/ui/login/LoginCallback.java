package ui.login;

@FunctionalInterface
public interface LoginCallback {
    /** will return the api key.
     * @param apiKey will return whichever key it receives.
    **/
    void onSuccess(String apiKey);
}
