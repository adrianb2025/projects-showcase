package game.screen;

import game.Hoppers;
import game.InputHandler;

import java.awt.*;

public class GameOverScreen extends Screen {

    public Font defaultFont;
    public Font bigFont = new Font(null, Font.PLAIN, 30);

    public GameScreen gameScreen;
    private int tickTime;

    public GameOverScreen(GameScreen gameScreen) { this.gameScreen = gameScreen; }

    public void tick(InputHandler input) { tickTime++; }

    public void render(Graphics2D g, double delta) {
        gameScreen.render(g, delta);
        if (defaultFont == null) defaultFont = g.getFont();

        g.setFont(bigFont);
        String text = "GAME OVER";
        g.setColor(Color.BLACK);
        g.drawString(text, (Hoppers.WIDTH - text.length() * 20) / 2, (Hoppers.HEIGHT - 10) / 2);

        g.setFont(defaultFont);
    }

}