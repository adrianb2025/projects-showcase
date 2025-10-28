package game.entity.mob;

import game.entity.Entity;
import game.entity.Team;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

public class NPC extends Mob {

    protected int xStart;
    protected int yStart;
    protected int randomWalkTime;
    protected int xa;
    protected int ya;
    protected int col;
    protected int xts;
    protected int yts;

    public NPC(int x, int y) {
        xStart = this.x = x;
        yStart = this.y = y;
        team = Team.PLAYER;
        col = PaletteHelper.getColor(-1, 100, 522, 555);
        xts = 0;
        yts = 14;
    }

    public void render(Screen screen) {
        int xt = xts;
        int yt = yts;

        int flip1 = (walkDist >> 3) & 1;
        int flip2 = (walkDist >> 3) & 1;

        if (dir == 1) xt += 2;

        if (dir > 1) {
            flip1 = 0;
            flip2 = ((walkDist >> 4) & 1);

            if (dir == 2) flip1 = 1;

            xt += 4 + ((walkDist >> 3) & 1) * 2;
        }

        int xo = x - 8;
        int yo = y - 11;

        if (isSwimming()) {
            yo += 4;
            int waterCol = PaletteHelper.getColor(-1, -1, -1, 444);
            if (tickTime / 8 % 2 == 0) waterCol = PaletteHelper.getColor(-1, 444, -1, -1);
            screen.render(xo + 0, yo + 3, 5 * 8, 13 * 8, waterCol, 0);
            screen.render(xo + 8, yo + 3, 5 * 8, 13 * 8, waterCol, 1);
        }

        int col = this.col;
        if (hurtTime > 0) col = PaletteHelper.getColor(-1, 555, 555, 555);

        screen.render(xo + 8 * flip1, yo + 0, xt * 8, yt * 8, col, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, (xt + 1) * 8, yt * 8, col, flip1);

        if (!isSwimming()) {
            screen.render(xo + 8 * flip2, yo + 8, xt * 8, (yt + 1) * 8, col, flip2);
            screen.render(xo + 8 - 8 * flip2, yo + 8, (xt + 1) * 8, (yt + 1) * 8, col, flip2);
        }
    }

    public boolean canSwim() { return true; }
    public boolean canRegenerate() { return true; }
    public boolean canAttack() { return true; }
    public boolean blocks(Entity e) { return true; }

}