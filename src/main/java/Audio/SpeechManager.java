package Audio;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.util.List;

public class SpeechManager {

    private final TextToSpeechClient ttsClient;

    public SpeechManager(String credentialsPath) {
        try {
            System.out.println("Using credentials file: " + credentialsPath);

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(credentialsPath))
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            ttsClient = TextToSpeechClient.create(settings);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not read text aloud.");
            throw new RuntimeException("Failed to initialize TextToSpeechClient", e);
        }
    }

    public void speak(String text, String languageCode) {
        try {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .build();

            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            playAudio(audioContents.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error during speech synthesis.");
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
            JOptionPane.showMessageDialog(null, "Error playing audio.");
        }
    }

    public void close() {
        if (ttsClient != null) {
            ttsClient.close();
        }
    }
}
