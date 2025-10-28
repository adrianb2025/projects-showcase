package game.entity;

import game.level.Level;

import java.awt.*;
import java.util.Random;

public abstract class Entity implements Comparable<Entity> {

    public static final Random random = new Random();
    public int xt = -1;
    public int yt = -1;
    public double x;
    public double y;
    public double z;
    public double xa;
    public double ya;
    public double za;
    public int r = 6;
    public double gravity = -0.064;
    public double friction = 0.96;
    public double bounce = 0.1;
    public int tickTime;
    public Color color = Color.WHITE;
    public Level level;
    public int renderLayer = 10;
    public boolean removed;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
        xt = (int)x >> 4;
        yt = (int)y >> 4;
    }

    public void init(Level level) { this.level = level; }

    public void tick() { tickTime++; }
    public void render(Graphics2D g) {}

    public int compareTo(Entity o) {
        Entity e = (Entity) o;
        int result = Double.compare(renderLayer, e.renderLayer);
        if (result == 0) result = Double.compare(y, e.y);
        return result;
    }

    public boolean intersects(int x0, int y0, int x1, int y1) { return !(x + r < x0 || y + r < y0 || x - r > x1 || y - r > y1); }

    public void die() { removed = true; }
    protected void touchedBy(Entity e, double xxa, double yya) {}
    protected boolean blocks(Entity e) { return true; }

}