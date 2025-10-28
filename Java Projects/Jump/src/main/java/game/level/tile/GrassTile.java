package game.level.tile;

import game.entity.Entity;
import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class GrassTile extends Tile {

    public GrassTile(int id) { super(id); }

    public void render(Level level, Graphics2D g, int x, int y, int yScroll) {
        random.setSeed(1231234597L + x * 12871523L + y * 8762232177L);
        boolean d = level.getTile(x, y + 1) == Tile.water.id;
        int t = d ? 7 : random.nextInt(7);
        t += 16;
        g.drawImage(Art.sprites[t], x * 16, y * 16 - yScroll, null);
    }

    public boolean canSpawn(Entity e) { return true; }

}