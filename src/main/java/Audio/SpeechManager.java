package Audio;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import javax.sound.sampled.*;
import java.io.*;

public class SpeechManager {

    private TextToSpeechClient ttsClient;

    public SpeechManager(String credentialsPath) throws IOException {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", credentialsPath);
        ttsClient = TextToSpeechClient.create();
    }

    public void speak(String text, String languageCode) {
        try {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16) // WAV format
                    .build();

            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            // Play audio directly
            playAudio(audioContents.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAudio(byte[] audioData) {
        try (InputStream byteStream = new ByteArrayInputStream(audioData);
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteStream)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (ttsClient != null) {
            ttsClient.close();
        }
    }
}
