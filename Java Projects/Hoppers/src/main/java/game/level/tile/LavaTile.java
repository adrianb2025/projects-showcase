package game.level.tile;

import game.gfx.Art;
import game.level.Level;
import game.particle.FireParticle;
import game.snd.Sound;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class LavaTile extends Tile {

    private int tickTime;

    protected LavaTile(int tile) { super(tile); }

    public void tick(Level level, int xt, int yt) {
        tickTime++;

        if (yt + 1 < level.h && !level.getTile(xt, yt + 1).blocks() && tickTime % 120 == 0) {
            level.setTile(Tile.empty, xt, yt);
            level.setTile(this, xt, yt + 1);
        }

        for (int i = 0; i < 3; i++) {
            level.addParticle(new FireParticle((xt << 5) + 17 + random.nextInt(32) - 16, (yt << 5) + 8));
        }

        if (Math.random() < 0.01) {
            for (int i = 0; i < 3; i++) {
                level.addParticle(new FireParticle((xt << 5) + 16 + random.nextInt(32) - 16, (yt << 5) + 8, random.nextInt(30) + 30));
            }

            Sound.lava.play();
        }
    }

    public void render(Graphics2D g, int x, int y) {
        random.setSeed(x * 1230 + y * 2134232234);

        if (tile >= 0) {
            AffineTransform at = g.getTransform();

            g.drawImage(Art.tiles[tile], x, y, null);

            g.setTransform(at);
        }
    }

    public boolean blocks() { return true; }

}
