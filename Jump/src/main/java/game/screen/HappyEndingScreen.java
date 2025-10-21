package game.screen;

import game.InputHandler;
import game.JumpComponent;
import game.gfx.Art;

import java.awt.*;

public class HappyEndingScreen extends Screen {

    private int tickTime;
    private final Screen screen;

    public HappyEndingScreen(Screen screen) { this.screen = screen; }

    public void tick(InputHandler input) {
        if (tickTime++ > 120 && input.up.clicked) setScreen(new FadeScreen(new GameScreen(), 60, false, Color.BLACK));
    }

    public void render(Graphics2D g) {
        screen.render(g);
        g.setColor(new Color(120, 70, 70, 170));
        g.fillRect(0, 0, JumpComponent.WIDTH, JumpComponent.HEIGHT);

        String text = "THANKS FOR PLAYING";
        Art.drawString(text, (JumpComponent.WIDTH - text.length() * 8) / 2, 60, g);

        if (tickTime > 120) {
            text = "JUMP TO RESTART";
            int yOffs = (int)Math.abs(Math.sin(tickTime * 0.1) * 10);
            Art.drawString(text, (JumpComponent.WIDTH - text.length() * 8) / 2, (JumpComponent.HEIGHT - 80) - yOffs, g);
        }
    }

}