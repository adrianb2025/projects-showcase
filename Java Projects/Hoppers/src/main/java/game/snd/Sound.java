package game.snd;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    public static final Sound hurt = load("/sfx/hurt.wav");
    public static final Sound select = load("/sfx/select.wav");
    public static final Sound jump = load("/sfx/jump.wav");
    public static final Sound portaled = load("/sfx/portaled.wav");
    public static final Sound die = load("/sfx/die.wav");
    public static final Sound gameOver = load("/sfx/gameover.wav");
    public static final Sound startGame = load("/sfx/startgame.wav");
    public static final Sound lava = load("/sfx/lava.wav");
    public static final Sound bubbleDie = load("/sfx/bubbledie.wav");
    public static final Sound win = load("/sfx/win.wav");
    private Clip clip;

    public static Sound load(String path) {
        Sound sound = new Sound();
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            sound.clip = clip;
        } catch (Exception e) {
            // ignore
        }

        return sound;
    }

    public void play() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        clip.stop();
                        clip.setFramePosition(0);
                        clip.start();
                    }
                }.start();
            }
        } catch (Exception e) {
            // ignore
        }
    }

}