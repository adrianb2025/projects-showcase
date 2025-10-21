package game.snd;

import game.Game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    public static final Sound backgroundMusic = loadSound("/sfx/back.mid");
    public static final Sound pickup = loadSound("/sfx/pickup.wav");
    public static final Sound playerHurt = loadSound("/sfx/playerhurt.wav");
    public static final Sound monsterHurt = loadSound("/sfx/monsterhurt.wav");
    private Clip clip;

    private Sound(URL url) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new IllegalStateException("Failed to load audio: " + e.getMessage());
        }
    }

    public static Sound loadSound(String path) {
        URL url = Sound.class.getResource(path);
        if (url == null) {
            throw new IllegalArgumentException("Sound file not found: " + path);
        }
        return new Sound(url);
    }

    public void play() {
        if (!Game.soundOn) return;

        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (!Game.soundOn) return;

        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) clip.stop();
    }

}