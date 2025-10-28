package game.entity;

import game.gfx.Renderable;
import game.level.Level;

import java.awt.*;
import java.util.Random;

public abstract class Entity implements Renderable {

    protected static final Random random = new Random();
    public double x;
    public double y;
    public double z;
    public double xa;
    public double ya;
    public double za;
    public double rot;
    public int r;
    public Level level;
    public boolean removed;

    public Entity(double x, double y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void init(Level level) { this.level = level; }

    public abstract void tick();
    public abstract void render(Graphics2D g);

    public boolean intersectsRing(int xx, int yy, int rr) {
        double xt = xx - x;
        double yt = yy - y;
        double rs = rr + r;
        return xt * xt + yt * yt < rs * rs;
    }

    public double distanceToSqr(double xx, double yy) {
        double xd = xx - x;
        double yd = yy - y;
        return xd * xd + yd * yd;
    }

    public double angleTo(double xt, double yt) { return Math.atan2(yt - y, xt - x); }
    public void die() { removed = true; }
    public double angleTo(Entity e) { return angleTo(e.x, e.y); }
    public double distanceToSqr(Entity e) { return distanceToSqr(e.x, e.y); }

}