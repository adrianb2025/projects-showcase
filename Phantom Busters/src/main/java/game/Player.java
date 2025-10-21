package game;

import game.entity.unit.Unit;
import game.level.Level;
import game.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public int[] visMap;
    public Level level;
    public List<Unit> units = new ArrayList<Unit>();

    public Player(Level level) {
        this.level = level;
        visMap = new int[level.w * level.h];
    }

    public void add(Unit unit) { units.add(unit); }

    public void tick() {
        int i = 0;

        while (i < visMap.length) {
            int n = i++;
            visMap[n] = visMap[n] & 1;
        }

        for (Unit u : units) { reveal(u); }
    }

    private void reveal(Unit u) {
        int r = u.visRange;
        int xx = (int)(u.x / 64);
        int yy = (int)(u.y / 64);

        for (int i = 0; i <= r * 2 + 1; i++) {
            revealRay(xx, yy, xx - r, yy - r + i, r);
            revealRay(xx, yy, xx + r, yy - r + i, r);
            revealRay(xx, yy, xx - r + i, yy - r, r);
            revealRay(xx, yy, xx - r + i, yy + r, r);
        }

    }

    private void revealRay(double x0, double y0, double x1, double y1, int r) {
        int steps = r + 1;
        double xd = x1 - x0;
        double yd = y1 - y0;

        for (int i = 0; i < steps; i++) {
            double xa = xd * i / steps;
            double ya = yd * i / steps;

            if (xa * xa + ya * ya > (r * r - 3)) return;

            int x = (int)(x0 + xa + 0.5);
            int y = (int)(y0 + ya + 0.5);

            if (x < 0 || y < 0 || x >= level.w || y >= level.h) continue;

            if (level.getTile(x, y) == Tile.wall) {
                visMap[x + y * level.w] = 3;
                return;
            }

            visMap[x + y * level.w] = 3;
        }
    }

    public boolean canSee(int x, int y) {
        if (x < 1 || y < 1 || x >= level.w - 1 || y >= level.h - 1) return false;
        int pp = x - 1 + (y - 1) * level.w;
        if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2]) > 0) return true;
        pp += level.w;
        if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2]) > 0) return true;
        pp += level.w;
        return (visMap[pp] | visMap[pp + 1] | visMap[pp + 2]) > 0;
    }

}