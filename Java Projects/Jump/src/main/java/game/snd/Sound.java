package game.snd;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

    public static boolean mute;

    public static final Sound hurt = new Sound("/sfx/hurt.wav");
    public static final Sound jump = new Sound("/sfx/jump0.wav");
    public static final Sound pickup = new Sound("/sfx/pickup.wav");
    public static final Sound cow = new Sound("/sfx/cow.wav");
    public static final Sound water = new Sound("/sfx/water.wav");
    public static final Sound shoot = new Sound("/sfx/shoot.wav");
    public static final Sound trrrrr = new Sound("/sfx/trrrrr.wav");
    private AudioClip clip;

    private Sound(String path) {
        try {
            clip = Applet.newAudioClip(Sound.class.getResource(path));
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to load " + path);
        }
    }

    public void play() {
        new Thread(new Runnable() {
            public void run() {
                clip.play();
            }
        }).start();
    }

}