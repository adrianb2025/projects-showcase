package game.screen;

import game.InputHandler;
import game.NightCreature;
import game.gfx.Bitmap;

import java.awt.*;

public class GameOverScreen extends Screen {

    private GameScreen gameScreen;

    public GameOverScreen(GameScreen gameScreen) { this.gameScreen = gameScreen; }

    public void tick(InputHandler input) {}

    public void render(Graphics2D g) {
        gameScreen.render(g);

        g.setColor(new Color(0x7F000AFF, true));
        g.fillRect(0, 0, NightCreature.WIDTH, NightCreature.HEIGHT);

        g.setColor(Color.RED);
        String text = "Game Over";
        g.drawString(text, (NightCreature.WIDTH - text.length() * 5) / 2, 56);
    }

    public void postRender(Bitmap screenBitmap, Graphics2D g) {}

}