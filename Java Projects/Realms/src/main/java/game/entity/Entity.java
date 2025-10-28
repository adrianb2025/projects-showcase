package game.entity;

import game.entity.item.Item;
import game.entity.item.ItemEntity;
import game.entity.mob.Mob;
import game.entity.mob.Player;
import game.gfx.Bitmap;
import game.gfx.Screen;
import game.gfx.util.BitmapHelper;
import game.level.Level;
import game.level.tile.Tile;

import java.util.List;
import java.util.Random;

public class Entity {

    protected final Random random = new Random();
    protected int x;
    protected int y;
    protected int xr = 4;
    protected int yr = 4;
    protected Level level;
    protected Team team = Team.NEUTRAL;
    protected Bitmap sprite;
    protected boolean removed;

    public void init(Level level) { this.level = level; }

    public void tick() {}
    public void render(Screen screen) {}

    public boolean intersects(int x0, int y0, int x1, int y1) { return !(x + xr < x0 || y + yr < y0 || x - xr > x1 || y - yr > y1); }

    public boolean move(int xa, int ya) {
        if (xa != 0 || ya != 0) {
            boolean stopped = true;

            if (xa != 0 && move2(xa, 0)) stopped = false;
            if (ya != 0 && move2(0, ya)) stopped = false;

            if (!stopped) {
                int xt = x >> 4;
                int yt = y >> 4;
                level.getTile(xt, yt).steppedOn(level, xt, yt, this);
            }

            return !stopped;

        }

        return true;
    }

    public boolean move2(int xa, int ya) {
        if (xa != 0 && ya != 0) throw new RuntimeException("You can only move along one axis at a time!");

        int xto0 = (x - xr) >> 4;
        int yto0 = (y - yr) >> 4;
        int xto1 = (x + xr) >> 4;
        int yto1 = (y + yr) >> 4;

        int xt0 = ((x + xa) - xr) >> 4;
        int yt0 = ((y + ya) - yr) >> 4;
        int xt1 = ((x + xa) + xr) >> 4;
        int yt1 = ((y + ya) + yr) >> 4;

        for (int yt = yt0; yt <= yt1; yt++) {
            for (int xt = xt0; xt <= xt1; xt++) {
                if (xt >= xto0 && yt >= yto0 && xt <= xto1 && yt <= yto1) continue;
                level.getTile(xt, yt).bumpInto(level, xt, yt, this);
                if (ignoreBlocks() || level.getTile(xt, yt).mayPass(level, xt, yt, this)) continue;
                return false;
            }
        }

        List<Entity> wasInside = level.getEntities(x - xr, y - yr, x + xr, y + yr, null);
        List<Entity> isInside = level.getEntities(x + xa - xr, y + ya - yr, x + xa + xr, y + ya + yr, null);
        for (Entity e : isInside) {
            if (e == this) continue;
            e.touchedBy(this);
        }

        if (!ignoreBlocks()) {
            isInside.removeAll(wasInside);
            for (Entity e : isInside) {
                if (e == this || !e.blocks(this)) continue;
                return false;
            }
        }

        x += xa;
        y += ya;

        return true;
    }

    protected boolean isSwimming() {
        Tile tile = level.getTile(x >> 4, y >> 4);
        return tile.isLiquid();
    }

    public void touchedBy(Entity e) {}
    public boolean blocks(Entity e) { return false; }
    public boolean ignoreBlocks() { return false; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getXr() { return xr; }
    public int getYr() { return yr; }
    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) { this.removed = removed; }
    public Team getTeam() { return team; }
    public Level getLevel() { return level; }
    public void hurt(Mob mob, int dmg, int attackDir) {}
    public void touchItem(ItemEntity e) {}
    public boolean interact(Item item, Player player, int dir) { return false; }
    public boolean canSwim() { return false; }

}