package game.entity.mob.hostile.boss.snake;

import game.entity.CharacterStats;
import game.entity.Team;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class SnakeNeck extends SnakePart {

    protected SnakePart child;
    protected double baseRot = Math.PI * 1.25;
    protected double rot;

    public SnakeNeck(int x, int y, SnakePart child) {
        this.x = x;
        this.y = y;
        xr = 6;
        yr = 6;
        this.child = child;
        health = 100;
        currentStats = new CharacterStats(0, 100, 0, 0, 0);
        team = Team.HOSTILE;
    }

    public void tick() {
        super.tick();

        rot = Math.sin(time / 40.0) * Math.cos(time / 13.0) * 0.5;
        rot *= 0.9;
        double rr = baseRot + rot;
        double xa = Math.cos(rr) * 7;
        double ya = Math.sin(rr) * 7;
        if (!child.isRemoved()) child.setMovement(x + xa, y + ya, rr);
    }

    public void render(Screen screen) {
        int xo = x - 8;
        int yo = y - 9;

        int col = PaletteHelper.getColor(-1, 40 + level.getNumber() * 100, 51 + level.getNumber() * 100, 150);
        if (hurtTime > 0) col = PaletteHelper.getColor(-1, 555, 555, 555);

        screen.render(2, xo + 1, yo + 1, 2 * 8, 3 * 8, PaletteHelper.getColor(-1, 0, 0, 0), 0);
        screen.render(2, xo, yo, 2 * 8, 3 * 8, col, 0);
    }

    public void setMovement(double xa, double ya, double rot) {
        this.xa = xa;
        this.ya = ya;
        baseRot = rot;
    }
}