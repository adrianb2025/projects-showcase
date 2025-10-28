package game.level.block;

import game.gfx.Art;

import java.awt.*;

public class SolidBlock extends Block {

    public void tick() {}

    public void render(Graphics2D g, int x, int y, int xOffs, int yOffs) {
        int xx = (x << 4) + xOffs;
        int yy = (y << 4) + yOffs;
        g.drawImage(Art.sheet[1], xx, yy, null);
    }

    public boolean canPass() { return false; }
    public void steppedOn() {}
    public void steppedFrom() {}

}