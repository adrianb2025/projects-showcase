package game.screen;

import game.InputHandler;
import game.JumpComponent;

import java.awt.*;

public class TransitionScreen extends Screen {

    private final Screen from;
    private final Screen to;
    private final int iterations;
    private final Color color;
    private int tickTime;
    private boolean rendered;

    public TransitionScreen(Screen from, Screen to, int iterations, Color color) {
        this.from = from;
        this.to = to;
        this.iterations = iterations;
        this.color = color;
    }

    public void tick(InputHandler input) {
        for (int i = 0; i < 6; i++) {
            if (tickTime++ != iterations) continue;
            setScreen(to);
        }
    }

    public void render(Graphics2D g) {
        if (!rendered) from.render(g);

        rendered = true;
        double p = tickTime / (double) iterations;
        p = p * p * p;

        double rw = p * JumpComponent.HEIGHT / 2.0;
        double rh = p * JumpComponent.HEIGHT / 2.0;
        double d = 100 * Math.max(0.6, p);
        double x = Math.cos(p * Math.PI * 10) * rw + JumpComponent.WIDTH / 2.0 - d * 0.5;
        double y = Math.sin(p * Math.PI * 10) * rh + JumpComponent.HEIGHT / 2.0 - d * 0.5;
        g.setColor(new Color(0, 0, 0, 0));
        g.fillOval((int)x, (int)y, (int)d, (int)d);
    }

    public boolean clearScreen() { return false; }

}