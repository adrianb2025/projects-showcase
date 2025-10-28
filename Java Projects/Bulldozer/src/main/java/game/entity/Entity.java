package game.entity;

import game.level.Level;
import game.level.tile.Tile;

import java.awt.*;
import java.util.List;

public abstract class Entity {

    public int x;
    public int y;
    public int xa;
    public int ya;
    public Level level;
    public boolean removed;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void init(Level level) { this.level = level; }

    public boolean attemptMove(int xa, int ya) {
        if (xa == 0 && ya == 0) return false;
        if (xa != 0 && ya != 0) throw new IllegalStateException("You can only move along one axis at a time");

        int xo = x;
        int yo = y;

        boolean result = isFree(xa, ya);

        if (result) {
            x += xa;
            y += ya;

            if (xo != x || yo != y) {
                level.removeEntity(this, xo, yo);
                level.insertEntity(this, x, y);
            }
        }

        return result;
    }

    public boolean isFree(int xa, int ya) {
        int xx = x + xa;
        int yy = y + ya;

        if (level.getTile(xx, yy).blocks()) return false;

        steppedOn(level.getTile(xx, yy), xx, yy);

        List<Entity> entities = level.getEntities(xx, yy);
        for (Entity e : entities) {
            if (e == this) continue;
            if (e.blocks(this)) {
                if (!(this instanceof Bunny)) return false;
                // if (!(this instanceof Bulldozer)) return false;
                if (!e.attemptMove(xa, ya)) return false;
            }
        }


        return true;

    }

    public void steppedOn(Tile tile, int xxa, int yya) {}

    public void tick() {
        xa = 0;
        ya = 0;
    }


    public abstract void render(Graphics2D g);

    public boolean blocks(Entity e) { return false; }

}