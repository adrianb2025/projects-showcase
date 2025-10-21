package game.screen;

import game.InputHandler;
import game.JumpComponent;
import game.gfx.Art;

import java.awt.*;

public class TitleScreen extends Screen {

    private int tickTime;
    private int scroll;

    public void tick(InputHandler input) {
        tickTime++;
        scroll = JumpComponent.HEIGHT - tickTime / 2;

        if (scroll < 0) scroll = 0;
        if (scroll == 0 && input.up.clicked) setScreen(new FadeScreen(new GameScreen(), 60, false, Color.BLACK));
    }

    public void render(Graphics2D g) {
        g.drawImage(Art.logo, 0, 0, JumpComponent.WIDTH, JumpComponent.HEIGHT, 0, scroll, JumpComponent.WIDTH, scroll + JumpComponent.HEIGHT, null);

        if (scroll == 0) {
            String s = "JUMP TO START";
            int yOffs = (int)Math.abs(Math.sin(tickTime * 0.1) * 10);
            Art.drawString(s, (JumpComponent.WIDTH - s.length() * 8) / 2, (JumpComponent.HEIGHT - 48) - yOffs, g);
        }
    }

}