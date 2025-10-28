package game.entity;

import game.entity.mob.Player;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class Warper extends Entity {

    private boolean isUp;

    public Warper(int x, int y, boolean isUp) {
        this.x = x;
        this.y = y;
        this.isUp = isUp;
    }

    public void touchedBy(Entity e) {
        if (e instanceof Player) level.getGame().changeLevelByDir(isUp ? 1 : -1);
    }

    public void render(Screen screen) {
        int xo = x - 8;
        int yo = y - 9;

        int dirCol = isUp ? 214 : 222;
        int col = PaletteHelper.getColor(-1, 0, dirCol, 333);
        if (System.currentTimeMillis() / 100 % 2 == 0) col = PaletteHelper.getColor(-1, 111, dirCol, 533);
        screen.render(2, xo, yo, 0, 11 * 8, col, 0);
    }

}