package game.level.block;

import game.gfx.Art;

import java.awt.*;

public class EmptyBlock extends Block {

    public void tick() {}

    public void render(Graphics2D g, int x, int y, int xOffs, int yOffs) {
        int xx = (x << 4) + xOffs;
        int yy = (y << 4) + yOffs;
        g.drawImage(Art.sheet[0], xx, yy, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font(null, Font.PLAIN, 8));
        g.drawString("" + steps, xx + 8 - 3, yy + 8 + 3);
    }

    public boolean canPass() { return true; }
    public void steppedOn() {}
    public void steppedFrom() { steps++; }

}