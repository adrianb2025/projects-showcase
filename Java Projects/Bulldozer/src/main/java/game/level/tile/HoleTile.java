package game.level.tile;

import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class HoleTile extends GrassTile {

    public HoleTile(int id) { super(id); }

    public void render(Graphics2D g, Level level, int xt, int yt) {
        super.render(g, level, xt, yt);
        g.drawImage(Art.sheet[4], xt << 4, yt << 4, null);
    }

}