package infrastructure.tts;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import configuration.ConfigDataRetriever;
import domain.gateway.Speaker;

/**
 * Handles text-to-speech functionality using the
 * Google Cloud Text-to-Speech API.
 */
public class SpeechManager implements Speaker {

    /**
     * Constant to convert microseconds to milliseconds.
     */
    private static final int MILLISECONDS_IN_MICROSECOND = 1000;

    /**
     * Google Cloud Text-to-Speech client.
     */
    private TextToSpeechClient ttsClient;

    /**
     * Constructs a SpeechManager instance using the given
     * credentials path. If credentials are valid,
     * initializes the TextToSpeechClient.
     *
     * @param credentialsPath the path to the Google Cloud
     *                        credentials JSON file
     */
    public SpeechManager(final String credentialsPath) {
        try {
            if (credentialsPath != null && !credentialsPath.isEmpty()) {
                final GoogleCredentials credentials =
                        GoogleCredentials.fromStream(
                                        new FileInputStream(credentialsPath))
                                .createScoped(List.of(
                                        "https://www.googleapis.com/"
                                                + "auth/cloud-platform"));

                final TextToSpeechSettings settings =
                        TextToSpeechSettings.newBuilder()
                                .setCredentialsProvider(
                                        FixedCredentialsProvider.
                                                create(credentials))
                                .build();

                this.ttsClient = TextToSpeechClient.create(settings);
            } else {
                System.out.println(
                        "No credentials provided. TTS will be disabled.");
                this.ttsClient = null;
            }
        } catch (final IOException exception) {
            System.out.println(
                    "Invalid credentials. TTS will be disabled. Reason: "
                            + exception.getMessage());
            this.ttsClient = null;
        }
    }

    /**
     * Speaks a list of words using the default language (English).
     *
     * @param words the list of words to speak
     */
    @Override
    public void speak(final List<String> words) {
        speak(words, "en-US");
    }

    /**
     * Speaks a single word using the default language (English).
     *
     * @param word the word to speak
     */
    @Override
    public void speak(final String word) {
        speak(List.of(word), "en-US");
    }

    /**
     * Speaks a list of words using the given language code.
     *
     * @param words        the list of words to speak
     * @param languageCode the language code (e.g., "en-US")
     */
    @Override
    public void speak(final List<String> words,
                      final String languageCode) {
        for (final String word : words) {
            speakWord(word, languageCode);
        }
    }

    /**
     * Speaks a single word using the given language code.
     *
     * @param word         the word to speak
     * @param languageCode the language code (e.g., "en-US")
     */
    @Override
    public void speak(final String word, final String languageCode) {
        speak(List.of(word), languageCode);
    }

    /**
     * Speaks a word in the original language followed by its translation.
     *
     * @param original   the original word to speak
     * @param translated the translated word to speak
     */
    public void speakWordPair(final String original,
                              final String translated) {
        final String originalLangCode =
                ConfigDataRetriever.get("input_language");
        final String targetLangCode =
                ConfigDataRetriever.get("target_language");

        speak(original, originalLangCode);
        speak(translated, targetLangCode);
    }

    /**
     * Internal method to synthesize and play audio for a single word.
     *
     * @param word         the word to speak
     * @param languageCode the language code for speech synthesis
     */
    public void speakWord(final String word, final String languageCode) {
        try {
            final SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(word)
                    .build();

            final VoiceSelectionParams voice = VoiceSelectionParams
                    .newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            final AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .build();

            final SynthesizeSpeechResponse response =
                    ttsClient.synthesizeSpeech(input, voice, audioConfig);
            final ByteString audioContents = response.getAudioContent();

            final AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(audioContents.toByteArray()));

            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength()
                    / MILLISECONDS_IN_MICROSECOND);
        } catch (IOException | InterruptedException
                 | UnsupportedAudioFileException
                 | LineUnavailableException ex) {
            System.err.println("Error speaking word: " + word
                    + " in language " + languageCode);
            ex.printStackTrace();
        }
    }

    /**
     * Checks if the TTS service is available.
     *
     * @return true if initialized with valid credentials,
     *         false otherwise
     */
    public boolean isAvailable() {
        return ttsClient != null;
    }
}
