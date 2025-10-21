package game.level.tile;

import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class GrassTile extends Tile {

    public GrassTile(int id) { super(id); }

    public void render(Graphics2D g, Level level, int xt, int yt) {
        random.setSeed(xt * 1258457473315L + yt * 34328817171L + 151543823L);
        g.drawImage(Art.sheet[random.nextInt(4) + 12], xt << 4, yt << 4, null);
    }

}