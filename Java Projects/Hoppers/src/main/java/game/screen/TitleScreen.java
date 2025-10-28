package game.screen;

import game.Hoppers;
import game.InputHandler;
import game.gfx.Art;

import java.awt.*;

public class TitleScreen extends Screen {

    private int tickTime;

    public void tick(InputHandler input) {
        tickTime++;
        if (tickTime > 120) {
            if (input.jump.clicked) game.newGame();
        }
    }

    public void render(Graphics2D g, double delta) {
        int yOffs = Hoppers.HEIGHT - tickTime * 2;
        if (yOffs < 0) yOffs = 0;
        g.drawImage(Art.title[0], 0, -yOffs, null);

        if (tickTime > 120) {
            String text = "PRESS 'SPACE' TO START";
            g.setColor(Color.WHITE);
            g.drawString(text, (Hoppers.WIDTH / 2) - text.length() * 4, (Hoppers.HEIGHT - 40) - 3 - (int)Math.abs(Math.sin(tickTime * 0.1) * 10));
        }

        if (tickTime >= 0) {
            String text = "chinchilla";
            g.drawString(text, 2, Hoppers.HEIGHT - 6 - 2);
        }
    }

}