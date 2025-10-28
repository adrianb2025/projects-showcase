package game.level.tile;

import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class WaterTile extends Tile {

    public WaterTile(int id) { super(id); }

    public void render(Level level, Graphics2D g, int x, int y, int yScroll) {
        random.setSeed(1231234597L + (x * 16 * (y * 16) * (level.tickTime / 30)) ^ 0x24D3629L);
        int t = random.nextInt(4) + 4;
        t += 24;
        g.drawImage(Art.sprites[t], x * 16, y * 16 - yScroll, null);
    }

}