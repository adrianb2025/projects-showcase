package game.entity.rain;

import game.entity.Entity;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class Puddle extends Entity {

    private long lifeTime;
    public Puddle(int x, int y) {
        this.x = x;
        this.y = y;
        lifeTime = 20;
    }
    public void tick() {
        lifeTime--;
        if (lifeTime < 0) removed = true;
    }

    public void render(Screen screen) {
        int xo = x - 8;
        int yo = y - 5;

        int waterCol = PaletteHelper.getColor(-1, -1, -1, 444);
        if (lifeTime / 8 % 2 == 0) waterCol = PaletteHelper.getColor(-1, 444, -1, -1);

        screen.render(xo + 0, yo + 3, 5 * 8, 13 * 8, waterCol, 0);
        screen.render(xo + 8, yo + 3, 5 * 8, 13 * 8, waterCol, 1);
    }

}
