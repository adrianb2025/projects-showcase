package game.screen;

import game.Hoppers;
import game.InputHandler;

import java.awt.*;

public class GameWinScreen extends Screen {

    public Font defaultFont;
    public Font bigFont = new Font(null, Font.BOLD, 30);
    private Font smallFont = new Font(null, Font.BOLD, 15);

    public GameScreen gameScreen;
    private int tickTime;

    public GameWinScreen(GameScreen gameScreen) { this.gameScreen = gameScreen; }

    public void tick(InputHandler input) { tickTime++; }

    public void render(Graphics2D g, double delta) {
        gameScreen.render(g, delta);
        if (defaultFont == null) defaultFont = g.getFont();

        g.setFont(bigFont);
        String text = "YOU WIN";
        g.setColor(new Color(125, 2, 0));
        g.drawString(text, (Hoppers.WIDTH - text.length() * 20) / 2, (Hoppers.HEIGHT - 10) / 2 - (int)(Math.abs(Math.sin(tickTime * 0.1)) * 15));

        g.setFont(smallFont);
        g.setColor(Color.WHITE);
        text = "Press 'R' to replay the game";
        g.drawString(text, (Hoppers.WIDTH - text.length() * 7) / 2, (Hoppers.HEIGHT + 120) / 2);

        g.setFont(defaultFont);
    }

}