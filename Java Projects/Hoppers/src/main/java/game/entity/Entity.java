package game.entity;

import game.gfx.Art;
import game.level.Level;
import game.snd.Sound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Random;

public abstract class Entity {

    protected static final Random random = new Random();
    public double xo;
    public double yo;
    public double x;
    public double y;
    public double xa;
    public double ya;
    public double xr = 15;
    public double yr = 15;
    public int tickTime;
    public int ysOffs = 4;
    public int sprite = -1;
    protected double bounce = 0.05;
    public int dir = 0;
    public double bobFactor;
    public int xSlot;
    public int ySlot;
    public int zSort;
    public double scale = 1;
    public Entity entityUnder;
    public int hurtTime;
    public int health;
    public Level level;
    public boolean ignoreBlocks;
    public boolean onGround;
    public boolean removed;

    public Entity(double x, double y) {
        this.x = xo = x;
        this.y = yo = y;
        zSort = 0;
    }

    public void init(Level level) { this.level = level; }

    public void tick() {
        xo = x;
        yo = y;
        tickTime++;

        if (hurtTime > 0) hurtTime--;

        attemptMove();

        xa *= 0.7;
        ya *= level.friction;
        ya += level.gravity;
    }

    public void render(Graphics2D g, double delta) {
        if (hurtTime > 0 && hurtTime / 4 % 2 == 0) return;

        if (sprite >= 0) {
            double xx = (int)(xo + (x - xo) * delta);
            double yy = (int)(yo + (y - yo) * delta);

            boolean xFlip = dir == 0;

            AffineTransform at = g.getTransform();

            double breath = Math.sin(tickTime * 0.1) * 0.05;

            g.translate(xx, yy);
            g.scale((xFlip ? -1 : 1) * scale, scale);
            g.rotate(bobFactor);
            g.translate(-xx, -yy);

            g.drawImage(Art.sprites[sprite], (int)(xx - xr), (int)(yy - yr + breath), null);

            g.setTransform(at);
        }
    }

    public void attemptMove() {
        bobFactor += (Math.abs(xa)) * 0.5;
        bobFactor *= 0.3;


        onGround = false;
        entityUnder = null;

        int xStep = (int)(Math.abs(xa * 1000) + 1);
        for (int i = xStep; i >= 0; i--) {
            if (isFree(xa * i / xStep, 0)) {
                x += xa * i / xStep;
                break;
            } else xa *= -bounce;

        }

        int yStep = (int)(Math.abs(ya * 1000) + 1);
        for (int i = yStep; i >= 0; i--) {
            if (isFree(0, ya * i / yStep)) {
                y += ya * i / yStep;
                break;
            } else {
                if (ya > 0) onGround = true;
                ya *= -bounce;
            }
        }
    }

    private boolean isFree(double xa, double ya) {
        if (ignoreBlocks) return true;

        int xto0 = (int)(x - xr) >> 5;
        int yto0 = (int)(y - yr) >> 5;
        int xto1 = (int)(x + xr) >> 5;
        int yto1 = (int)(y + yr) >> 5;

        int xt0 = (int)((x + xa) - xr) >> 5;
        int yt0 = (int)((y + ya) - yr) >> 5;
        int xt1 = (int)((x + xa) + xr) >> 5;
        int yt1 = (int)((y + ya) + yr) >> 5;

        for (int yt = yt0; yt <= yt1; yt++) {
            for (int xt = xt0; xt <= xt1; xt++) {
                if (xt >= xto0 && yt >= yto0 && xt <= xto1 && yt <= yto1) continue;
                level.getTile(xt, yt).bumpInto(level, xt, yt, xa, ya, this);
                if (level.getTile(xt, yt).blocks()) return false;
            }
        }

        List<Entity> wasInside = level.getEntities((int)(x - xr), (int)(y - yr), (int)(x + xr), (int)(y + yr));
        List<Entity> isInside = level.getEntities((int)(x + xa - xr), (int)(y + ya - yr), (int)(x + xa + xr), (int)(y + ya + yr));
        for (int i = 0; i < isInside.size(); i++) {
            Entity e = isInside.get(i);
            if (e == this) continue;

            if (ya > 0) {
                touchUnder(e);
                return false;
            }

            e.touchedBy(this);
        }

        isInside.removeAll(wasInside);
        for (int i = 0; i < isInside.size(); i++) {
            Entity e = isInside.get(i);
            if (e == this) continue;
            if (e.blocks(this)) return false;
        }

        return true;
    }

    protected void touchUnder(Entity e) { entityUnder = e; }
    protected boolean blocks(Entity e) { return true; }

    protected void touchedBy(Entity e) {
        xa += e.xa * 0.007;
        ya += e.ya * 0.007;
    }

    public void hurt(double xa, double ya, int dmg) {
        if (hurtTime <= 0) {
            this.xa *= xa;
            this.ya *= ya;
            health -= dmg;
            if (health <= 0) die();
            else {
                hurtTime = 60;
                Sound.hurt.play();
            }
        }
    }

    public boolean intersects(int x0, int y0, int x1, int y1) { return !(x + xr < x0 || y + yr < y0 || x - xr > x1 || y - yr > y1); }
    public void outOfBounds() { die(); }
    public void remove() { removed = true; }
    public void die() { remove(); }

}