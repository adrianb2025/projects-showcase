package game.level.tile;

import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class GrassTile extends Tile {

    public GrassTile(int id) { super(id); }

    public void render(Graphics2D g, Level level, int xt, int yt, int xScroll, int yScroll) {
        int xx = xt >> 4;
        int yy = yt >> 4;
        int t = 0;

        if (level.getTile(xx, yy) != water) t++;
        if (level.getTile(xx + 1, yy) != water) t += 2;
        if (level.getTile(xx, yy + 1) != water) t += 4;
        if (level.getTile(xx + 1, yy + 1) != water) t += 8;

        g.drawImage(Art.waterToGrass[t], xt - xScroll, yt - yScroll, null);

        if (t == 15) {
            t = 0;
            if (level.getTile(xx, yy) != sand) t++;
            if (level.getTile(xx + 1, yy) != sand) t += 2;
            if (level.getTile(xx, yy + 1) != sand) t += 4;
            if (level.getTile(xx + 1, yy + 1) != sand) t += 8;

            g.drawImage(Art.sandToGrass[t], xt - xScroll, yt - yScroll, null);
        }
    }

}