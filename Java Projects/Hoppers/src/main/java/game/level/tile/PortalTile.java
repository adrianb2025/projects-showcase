package game.level.tile;

import game.entity.Entity;
import game.entity.hopper.Hopper;
import game.level.Level;
import game.particle.BlueParticle;

public class PortalTile extends Tile {

    protected PortalTile(int tile) { super(tile); }

    public void tick(Level level, int xt, int yt) {
        if (yt + 1 < level.h && !level.getTile(xt, yt + 1).blocks()) {
            level.setTile(Tile.empty, xt, yt);
            level.setTile(this, xt, yt + 1);
        }

        for (int i = 0; i < 3; i++) {
            level.addParticle(new BlueParticle((xt << 5) + 16 + random.nextInt(16) - 8, (yt << 5) + 32));
        }
    }

    public void bumpInto(Level level, int xt, int yt, double xa, double ya, Entity e) {
        if (e instanceof Hopper && ((Hopper) e).idle()) ((Hopper) e).portaling(e);
    }

}