package infrastructure.tts;

import configuration.ConfigDataRetriever;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import domain.gateway.Speaker;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class SpeechManager implements Speaker {

    private TextToSpeechClient ttsClient;

    public SpeechManager(String credentialsPath) {
        try {
            if (credentialsPath != null && !credentialsPath.isEmpty()) {
                GoogleCredentials credentials = GoogleCredentials
                        .fromStream(new FileInputStream(credentialsPath))
                        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

                TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

                this.ttsClient = TextToSpeechClient.create(settings);
            } else {
                System.out.println("No credentials provided. TTS will be disabled.");
                this.ttsClient = null;
            }
        } catch (IOException e) {
            System.out.println("Invalid credentials. TTS will be disabled. Reason: " + e.getMessage());
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

    // For original-translated pairs
    public void speakWordPair(String original, String translated) {
        String originalLangCode = ConfigDataRetriever.get("input_language");
        String targetLangCode = ConfigDataRetriever.get("target_language");

        speak(original, originalLangCode);
        speak(translated, targetLangCode);
    }

    // Internal helper
    public void speakWord(String word, String languageCode) {
        try {
            SynthesisInput input = SynthesisInput.newBuilder().setText(word).build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .build();

            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(audioContents.toByteArray()))) {

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            }

        } catch (Exception e) {
            System.err.println("Error speaking word: " + word + " in language " + languageCode);
            e.printStackTrace();
        }
    }
    public boolean isAvailable() {
        return ttsClient != null;
    }

}
