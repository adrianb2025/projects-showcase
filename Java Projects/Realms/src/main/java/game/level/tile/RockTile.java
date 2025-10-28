package game.level.tile;

import game.entity.Entity;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;
import game.level.Level;

public class RockTile extends Tile {

    public RockTile(int id) { super(id); }

    public void render(Screen screen, Level level, int x, int y) {
        int col = PaletteHelper.getColor(444, 444, 333, 333);
        int tc = PaletteHelper.getColor(111, 444, 555, grassMainColor);

        boolean u = level.getTile(x, y - 1) != this;
        boolean d = level.getTile(x, y + 1) != this;
        boolean l = level.getTile(x - 1, y) != this;
        boolean r = level.getTile(x + 1, y) != this;

        boolean ul = level.getTile(x - 1, y - 1) != this;
        boolean dl = level.getTile(x - 1, y + 1) != this;
        boolean ur = level.getTile(x + 1, y - 1) != this;
        boolean dr = level.getTile(x + 1, y + 1) != this;

        x <<= 4;
        y <<= 4;

        if (!u && !l) {
            if (!ul) screen.render(x, y, 0, 0, col, 0);
            else screen.render(x, y, 5 * 8, 0 * 8, tc, 3);
        } else screen.render(x, y, (l ? 9 : 8) * 8, (u ? 2 : 1) * 8, tc, 3);


        if (!u && !r) {
            if (!ur) screen.render(x + 8, y, 0, 0, col, 0);
            else screen.render(x + 8, y, 6 * 8, 0 * 8, tc, 3);
        } else screen.render(x + 8, y, (r ? 7 : 8) * 8, (u ? 2 : 1) * 8, tc, 3);


        if (!d && !l) {
            if (!dl) screen.render(x, y + 8, 0, 0, col, 0);
            else screen.render(x, y + 8, 5 * 8, 1 * 8, tc, 3);
        } else
            screen.render(x, y + 8, (l ? 9 : 8) * 8, (d ? 0 : 1) * 8, tc, 3);
        if (!d && !r) {
            if (!dr) screen.render(x + 8, y + 8, 0, 0, col, 0);
            else screen.render(x + 8, y + 8, 6 * 8, 1 * 8, tc, 3);
        } else screen.render(x + 8, y + 8, (r ? 7 : 8) * 8, (d ? 0 : 1) * 8, tc, 3);
    }

    public boolean mayPass(Level level, int xt, int yt, Entity e) { return false; }

}
