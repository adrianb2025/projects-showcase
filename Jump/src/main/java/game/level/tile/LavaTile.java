package game.level.tile;

import game.entity.particle.LavaParticle;
import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class LavaTile extends Tile {
    protected boolean sparks = true;

    public LavaTile(int id) { super(id); }

    public void render(Level level, Graphics2D g, int x, int y, int yScroll) {
        random.setSeed(1231234597L + (x * 16 * (y * 16) * (level.tickTime / 30)) ^ 0x24D3629L);
        int t = random.nextInt(2) + 2;
        t += 24;

        if (sparks && random.nextDouble() < 0.01) {
            double xo = (x * 16 + 8) + (random.nextDouble() * 2.0 - 1.0) * 8;
            double yo = (y * 16 + 8) + (random.nextDouble() * 2.0 - 1.0) * 8;
            for (int i = 0; i < 2; i++) {
                level.addParticle(new LavaParticle(xo, yo, 0));
            }
        }

        g.drawImage(Art.sprites[t], x * 16, y * 16 - yScroll, null);
    }

}