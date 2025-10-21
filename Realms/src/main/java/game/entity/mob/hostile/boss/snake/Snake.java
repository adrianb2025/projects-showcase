package game.entity.mob.hostile.boss.snake;

import game.entity.CharacterStats;
import game.entity.Team;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class Snake extends SnakePart {

    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
        xr = 6;
        yr = 6;
        health = 200;
        currentStats = new CharacterStats(0, 100, 0, 0, 0);
        team = Team.HOSTILE;
    }

    public void render(Screen screen) {
        int xo = x - 8;
        int yo = y - 7;

        int col = PaletteHelper.getColor(-1, 500 - (level.getNumber() % 5) * 100, 411 - (level.getNumber() % 4) * 100, 322);
        if (hurtTime > 0) col = PaletteHelper.getColor(-1, 555, 555, 555);

        screen.render(2, xo + 1, yo + 1, 2 * 8, 3 * 8, PaletteHelper.getColor(-1, 0, 0, 0), 0);
        screen.render(2, xo, yo, 2 * 8, 3 * 8, col, 0);
    }

    public void setMovement(double xa, double ya, double rot) {
        this.xa = xa;
        this.ya = ya;
    }

}