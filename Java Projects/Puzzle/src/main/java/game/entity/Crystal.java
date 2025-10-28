package game.entity;

import game.gfx.Art;
import game.gfx.Bitmap;

import java.util.List;

public class Crystal extends Entity {

    public final int icon;
    public double gravity;
    public boolean stand;
    public int checkCount;
    public int yt = 1;
    public int xTarget;
    public int yTarget;
    public boolean swap;
    public boolean die;

    public Crystal(int x, int y, int icon) {
        this.x = x;
        this.y = y;
        this.icon = icon;
        gravity = 0.21;
    }

    public void swap(Crystal c) {
        c.xTarget = x;
        c.yTarget = y;
        xTarget = c.x;
        yTarget = c.y;
        swap = true;
        c.swap = true;
    }

    public void tick() {
        super.tick();
        int xo = x;
        int yo = y;
        if (swap) {
            double speed = 2;
            int xd = xTarget - x;
            int yd = yTarget - y;
            double m = Math.sqrt(xd * xd + yd * yd);

            if (m < 2) {
                swap = false;
                stop();
                return;
            }

            double xxa = xd / m;
            double yya = yd / m;
            xa = xxa * speed;
            xa *= 0.99;
            ya = yya * speed;
            ya *= 0.99;
        } else {
            ya *= 0.99;
            ya += gravity;

            if (isRested()) {
                collide();
                if (Math.abs(ya) < 0.5) stop();
            }
        }

        if (die) {
            if (tickTime % 7 == 0) yt++;

            if (yt > 9) {
                remove();
                return;
            }
        } else {
            if (tickTime % 15 == 0) yt++;
            if (yt > 4) yt = 1;
        }

        x = (int)(x + xa);
        y = (int)(y + ya);
        checkCount = y - yo < 1 && x - xo < 1 ? ++checkCount : --checkCount;
        stand = checkCount > 3;
    }

    public void stop() {
        x = x + xr >> 4 << 4;
        y = y + yr >> 4 << 4;
        xa = 0;
        ya = 0;
    }

    public void die() {
        yt = 7;
        die = true;
    }

    public boolean isRested() {
        boolean result = false;
        if ((int)((y + yr) + Math.abs(ya) + 8) >> 4 >= level.mh) result = true;
        else {
            List<Crystal> crystals = level.getCrystal((int)((x + xr) + xa) >> 4, (int)((y + yr) + Math.abs(ya) + 8) >> 4);
            for (Crystal c : crystals) {
                if (c == null || c == this) continue;
                result = true;
                break;
            }
        }

        return result;
    }

    public void render(Bitmap screen, int xOffs, int yOffs) { screen.draw(Art.sprites[icon][yt], x + xOffs, y + yOffs); }

}