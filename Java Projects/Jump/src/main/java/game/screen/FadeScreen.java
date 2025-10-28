package game.screen;

import game.InputHandler;
import game.JumpComponent;

import java.awt.*;

public class FadeScreen extends Screen {

    private int tickTime;
    private final Screen screen;
    private final int duration;
    private final boolean out;
    private final Color color;

    public FadeScreen(Screen screen, int duration, boolean out, Color color) {
        this.screen = screen;
        this.duration = duration;
        this.out = out;
        this.color = color;
    }

    public void tick(InputHandler input) {
        screen.tick(input);
        if (tickTime++ == duration) setScreen(screen);
    }

    public void render(Graphics2D g) {
        double p = tickTime / (double) duration;

        if (p > 1) p = 1;
        if (!out) p = 1 - p;

        int a = (int)(p * 255) & 0xFF;
        screen.render(g);

        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), a));
        g.fillRect(0, 0, JumpComponent.WIDTH, JumpComponent.HEIGHT);
    }

}