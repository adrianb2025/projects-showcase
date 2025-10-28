package game.gfx;

import game.Hoppers;
import game.Player;
import game.entity.hopper.Hopper;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameGui {

    private Player player;
    private int tickTime;

    private Font defaultFont;
    private Font smallFont;

    public GameGui(Player player) {
        this.player = player;
        smallFont = new Font(null, Font.PLAIN, 10);
    }

    public void tick() { tickTime++; }

    public void render(Graphics2D g) {
        int xOffset = (Hoppers.WIDTH - player.hoppers.size() * 20) / 2;
        int yOffset = Hoppers.HEIGHT - 20;

        g.setColor(Color.BLACK);
        g.fillRect(0, yOffset - 10, Hoppers.WIDTH, Hoppers.HEIGHT);

        for (int i = 0; i < player.hoppers.size(); i++) {
            Hopper hopper = player.hoppers.get(i);

            int xx = xOffset + i * 20;
            int yy = yOffset;

            double scale = 0.5;

            AffineTransform at = g.getTransform();

            g.translate(xx, yy);
            g.scale(scale, scale);
            g.translate(-xx, -yy);

            g.drawImage(Art.sprites[hopper.sprite], xx, yy, null);

            g.setTransform(at);

            if (player.index == i) {
                g.drawImage(Art.gui[0], xx + 4, (int)(yy - 8 - Math.abs(Math.sin(tickTime * 0.15) * 2.2)), null);
            }
        }

        if (defaultFont == null) defaultFont = g.getFont();

        g.setColor(Color.WHITE);
        g.setFont(smallFont);

        g.drawString("restart level 'R'", 10, Hoppers.HEIGHT - 7);
        g.drawString("select hopper 'Q-E'", 10, Hoppers.HEIGHT - 17);

        String text = "use skill 'ENTER/F'";
        g.drawString(text, Hoppers.WIDTH - text.length() * 5, Hoppers.HEIGHT - 17);

        text = "move 'W/A/S/D'";
        g.drawString(text, Hoppers.WIDTH - text.length() * 5 - 10, Hoppers.HEIGHT - 7);

        g.setFont(defaultFont);
    }

}