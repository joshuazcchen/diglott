package infrastructure.tts;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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

public class SpeechManager implements Speaker {

    private static final int MILLISECONDS_IN_MICROSECOND = 1000;
    private TextToSpeechClient ttsClient;

    public SpeechManager(String credentialsPath) {
        try {
            if (credentialsPath != null && !credentialsPath.isEmpty()) {
                final GoogleCredentials credentials = GoogleCredentials
                        .fromStream(new FileInputStream(credentialsPath))
                        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

                final TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

                this.ttsClient = TextToSpeechClient.create(settings);
            }
            else {
                System.out.println("No credentials provided. TTS will be disabled.");
                this.ttsClient = null;
            }
        }
        catch (IOException exception) {
            System.out.println("Invalid credentials. TTS will be disabled. Reason: " + exception.getMessage());
            this.ttsClient = null;
        }
    }

    // Default: speak in English
    @Override
    public void speak(List<String> words) {
        speak(words, "en-US");
    }

    @Override
    public void speak(String word) {
        speak(List.of(word), "en-US");
    }

    // Overloaded: speak with language code
    @Override
    public void speak(List<String> words, String languageCode) {
        for (String word : words) {
            speakWord(word, languageCode);
        }
    }

    @Override
    public void speak(String word, String languageCode) {
        speak(List.of(word), languageCode);
    }

    /**
     * Speaks the original word followed by its translation.
     *
     * @param original   the original word to speak
     * @param translated the translated word to speak
     */
    public void speakWordPair(String original, String translated) {
        final String originalLangCode = ConfigDataRetriever.get("input_language");
        final String targetLangCode = ConfigDataRetriever.get("target_language");

        speak(original, originalLangCode);
        speak(translated, targetLangCode);
    }

    /**
     * Internal helper method to speak a single word using TTS.
     *
     * @param word         the word to speak
     * @param languageCode the language code for speech synthesis
     */
    public void speakWord(String word, String languageCode) {
        try {
            final SynthesisInput input = SynthesisInput.newBuilder().setText(word).build();

            final VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            final AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .build();

            final SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);
            final ByteString audioContents = response.getAudioContent();

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(audioContents.toByteArray()))) {

                final Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / MILLISECONDS_IN_MICROSECOND);
            }
        }
        catch (IOException | InterruptedException | javax.sound.sampled.UnsupportedAudioFileException
                 | javax.sound.sampled.LineUnavailableException exception) {
            System.err.println("Error speaking word: " + word + " in language " + languageCode);
            exception.printStackTrace();
        }
    }

    public boolean isAvailable() {
        return ttsClient != null;
    }
}
