package game.entity;

import game.gfx.Art;
import game.level.Level;
import game.level.tile.Tile;

import java.awt.*;

public class Chair extends Entity {

    public Entity owner;
    public int lifeTime;
    public int maxLifeTime;
    int[] spinAnim = { 0, 2, 1, 3 };
    private int movements = random.nextInt(100);

    public Chair(Entity owner) {
        super(0, 0);
        this.owner = owner;
        lifeTime = maxLifeTime = 180;
        bounce = 0.3;
        renderLayer--;
    }

    public void tick() {
        super.tick();
        if (isFree(xa, 0)) {
            x += xa;
            if (Math.abs(xa) > 0.01) movements++;
        } else xa *= -bounce;

        if (isFree(0, ya)) {
            y += ya;
            if (Math.abs(ya) > 0.01) movements++;
        } else {
            if (Math.abs(ya) > 0.1) xa += random.nextDouble() * random.nextDouble() * 2.0 - 1.0;
            ya *= -bounce;
        }

        z += za;
        xa *= friction;
        ya *= friction;
        za *= friction;

        if (z < 0) {
            z = 0;
            za *= -bounce;
            xa *= 0.77;
            ya *= 0.77;
        }

        za += gravity;

        if (lifeTime-- <= 0) die();
    }

    public void render(Graphics2D g) {
        if (lifeTime < maxLifeTime / 3 && lifeTime / 10 % 2 == 0) return;

        int x = (int)this.x - 8;
        int y = (int)this.y - 8;
        int t = 2 + this.spinAnim[this.movements / 10 % this.spinAnim.length] + 8;
        g.drawImage(Art.sprites[8], x, y + 4, null);
        g.drawImage(Art.sprites[t], x, y - (int)this.z, null);
    }


    private boolean isFree(double xxa, double yya) {
        int x0 = (int)(x - 8 + xxa);
        int y0 = (int)(y - 8 + yya);
        int x1 = (int)(x + 8 + xxa);
        int y1 = (int)(y + 8 + yya);

        if (Tile.tiles[level.getTile(x0 >> 4, y0 >> 4)].blocks(this)) return false;
        if (Tile.tiles[level.getTile(x0 >> 4, y1 >> 4)].blocks(this)) return false;
        if (Tile.tiles[level.getTile(x1 >> 4, y0 >> 4)].blocks(this)) return false;
        if (Tile.tiles[level.getTile(x1 >> 4, y1 >> 4)].blocks(this)) return false;

        Entity e = level.getEntity(this, x0, y0, x1, y1, new Level.EntityFilter() {
            public boolean accept(Entity e) {
                return e != owner;
            }
        });

        if (e != null) {
            e.touchedBy(this, xxa, yya);
            return !e.blocks(this);
        }

        return true;
    }

    protected boolean blocks(Entity e) { return false; }

}
