package domain.gateway;

public interface ConfigGateway {
    /**
     * Returns the path to the Google Cloud credentials file (JSON).
     */
    String getGoogleCredentialsPath();
}
