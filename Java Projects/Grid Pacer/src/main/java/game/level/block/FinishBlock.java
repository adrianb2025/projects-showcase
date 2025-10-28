package game.level.block;

import game.gfx.Art;

import java.awt.*;

public class FinishBlock extends Block {

    public void tick() {}

    public void render(Graphics2D g, int x, int y, int xOffs, int yOffs) {
        int xx = (x << 4) + xOffs;
        int yy = (y << 4) + yOffs;
        g.drawImage(Art.sheet[2], xx, yy, null);
    }

    public boolean canPass() { return true; }
    public void steppedOn() { level.finish(); }
    public void steppedFrom() {}

}