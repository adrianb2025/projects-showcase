package game.entity;

import game.entity.projectile.Bullet;
import game.gfx.Sprite;

import java.util.List;

public class Entity extends Sprite {

    public int tickTime;
    public boolean isCollideable = true;
    public int xBlockCell;
    public int yBlockCell;
    public double xa;
    public double ya;
    public double za;

    public void attemptMove() {
        int steps = (int)(Math.sqrt(xa * xa + ya * ya + za * za) + 1);
        for (int i = 0; i < steps; i++) {
            move(xa / steps, 0, 0);
            move(0, ya / steps, 0);
            move(0, 0, za / steps);
        }
    }

    private void move(double xxa, double yya, double zza) {
        if (removed) return;

        double xn = x + xxa;
        double yn = y + yya;
        double zn = z + zza;

        if (xn < 0 || yn < 0 || zn < 0 || xn >= level.w * 16 * 4 || yn >= level.h * 16 * 4 || zn >= level.maxHeight) {
            if (zn < 0) z = 0;
            collide(null, xxa, yya, zza);
            return;
        }

        List<Entity> isInside = level.getEntities(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh);
        for (Entity e : isInside) {
            if (e == this) continue;
            e.touchedBy(this, xxa, yya, zza);
        }

        List<Entity> wasInside = level.getEntities(x - xr, y - yr, z, x + xr, y + yr, z + zh);
        isInside.removeAll(wasInside);
        for (Entity e : isInside) {
            if (e == this || !e.blocks(this) || !blocks(e)) continue;
            collide(e, xxa, yya, zza);
            return;
        }

        if (level.blocks(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh)) {
            collide(null, xxa, yya, zza);
            return;
        }

        x = xn;
        y = yn;
        z = zn;
    }

    public void tick() { tickTime++; }

    public void touchedBy(Entity e, double xxa, double yya, double zza) {}
    public boolean blocks(Entity e) { return true; }

    public boolean intersects(double x0, double y0, double z0, double x1, double y1, double z1) {
        return !(x0 > x + xr || y0 > y + yr || z0 > z + zh || x1 <= x - xr || y1 <= y - yr || z1 <= z);
    }

    public void collide(Entity e, double xxa, double yya, double zza) {
        if (xxa != 0) xa = 0;
        if (yya != 0) ya = 0;
        if (zza != 0) za = 0;
    }

    public double distanceToSqr(Entity e) {
        double xd = x - e.x;
        double yd = y - e.y;
        double zd = z - e.z;
        return xd * xd + yd * yd + zd * zd;
    }

    public double distanceTo(double xt, double yt) {
        double xd = xt - x;
        double yd = yt - y;
        return Math.sqrt(xd * xd + yd * yd);
    }

    public void hitBy(Bullet b) {}
    public double angleTo(double xt, double yt) { return Math.atan2(yt - y, xt - x); }

}
