package game.entity.mob;

import game.gfx.Art;
import game.level.tile.Tile;
import game.snd.Sound;

import java.awt.*;

public class Cow extends Mob {

    public Cow(double x, double y) {
        super(x, y);
        gravity *= 2;
        bounce = 0.3;
    }

    public void render(Graphics2D g) {
        int x = (int)this.x;
        int y = (int)this.y - 20;
        int z = (int)this.z;
        int t = 0;

        if (z > 0) t = 1;

        g.drawImage(Art.sprites[54], x, y + 16, null);
        g.drawImage(Art.sprites[55], x + 16, y + 16, null);

        g.drawImage(Art.sprites[t * 2 + 0 + 40], x, y - z, null);
        g.drawImage(Art.sprites[t * 2 + 1 + 40], x + 16, y - z, null);
        g.drawImage(Art.sprites[t * 2 + 0 + 48], x, y + 16 - z, null);
        g.drawImage(Art.sprites[t * 2 + 1 + 48], x + 16, y + 16 - z, null);
    }

    public void steppedOn(int xta, int yta) {
        level.shake(15);
        level.setTile((int)x >> 4, (int)y >> 4, Tile.solidGrass.id);
        level.setTile(((int)x >> 4) + 1, (int)y >> 4, Tile.solidGrass.id);
    }

}