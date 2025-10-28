package game.level.tile;

import game.gfx.Screen;
import game.gfx.util.PaletteHelper;
import game.level.Level;

public class SandTile extends Tile {
    public SandTile(int id) {
        super(id);
        connectsToSand = true;
        slowPeriod = 6;
    }

    public void render(Screen screen, Level level, int x, int y) {
        int tc = PaletteHelper.getColor(sandMainColor - 110, sandMainColor, sandMainColor - 110, dirtMainColor);

        boolean u = !level.getTile(x, y - 1).connectsToSand;
        boolean d = !level.getTile(x, y + 1).connectsToSand;
        boolean l = !level.getTile(x - 1, y).connectsToSand;
        boolean r = !level.getTile(x + 1, y).connectsToSand;

        if (!u && !l) screen.render(x * 16 + 0, y * 16 + 0, 3 * 8, 0 * 8, 8, 8, sandCol, 0);
        else screen.render(x * 16 + 0, y * 16 + 0, (l ? 11 : 12) * 8, (u ? 0 : 1) * 8, 8, 8, tc, 0);


        if (!u && !r) screen.render(x * 16 + 8, y * 16 + 0, 0 * 8, 0 * 8, 8, 8, sandCol, 0);
        else screen.render(x * 16 + 8, y * 16 + 0, (r ? 13 : 12) * 8, (u ? 0 : 1) * 8, 8, 8, tc, 0);


        if (!d && !l) screen.render(x * 16 + 0, y * 16 + 8, 2 * 8, 0 * 8, 8, 8, sandCol, 0);
        else screen.render(x * 16 + 0, y * 16 + 8, (l ? 11 : 12) * 8, (d ? 2 : 1) * 8, 8, 8, tc, 0);


        if (!d && !r) screen.render(x * 16 + 8, y * 16 + 8, 3 * 8, 0 * 8, 8, 8, sandCol, 0);
        else screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) * 8, (d ? 2 : 1) * 8, 8, 8, tc, 0);
    }
}
