package game.entity;

import game.gfx.Art;
import game.level.block.Block;

import java.awt.*;

public class Pacer extends Entity {

    private int[] dirs = new int[] { 2, 1, 0, 3 };
    private int index;
    private int tickTime;

    public void tick() {
        tickTime++;

        if (tickTime % (10 - level.pacerSpeed) == 0) giveDir();

        if (isFree()) {
            x += xa;
            y += ya;
        }

        xa = 0;
        ya = 0;
    }

    public void render(Graphics2D g, int xOffs, int yOffs) {
        int xx = (x << 4) + xOffs;
        int yy = (y << 4) + yOffs;
        g.drawImage(Art.sheet[dirs[index] + 4], xx, yy, null);
    }

    public void giveDir() {
        int dir = dirs[index % dirs.length];
        xa = dir == 3 ? -1 : (dir == 1 ? 1 : 0);
        ya = dir == 0 ? -1 : (dir == 2 ? 1 : 0);
        Block currentBlock = level.getBlock(x, y);
        Block nextBlock = level.getBlock(x + xa, y + ya);
        if (!nextBlock.canPass() || nextBlock.steps > 0 || nextBlock.steps > currentBlock.steps + 1) {
            int c = 99999999;
            for (int i = 0; i < dirs.length; ++i) {
                int nextDir = dirs[i];
                int xxa = nextDir == 3 ? -1 : (nextDir == 1 ? 1 : 0);
                int yya = nextDir == 0 ? -1 : (nextDir == 2 ? 1 : 0);
                Block block = level.getBlock(x + xxa, y + yya);
                if (!block.canPass() || c <= block.steps) continue;
                c = block.steps;
                xa = xxa;
                ya = yya;
                index = i;
            }
        }
    }

    public boolean isFree() {
        if (xa == 0 && ya == 0) return false;

        int xx = x + xa;
        int yy = y + ya;

        if (xx < 0 || yy < 0 || xx >= level.w || yy >= level.h) return false;

        steppedFrom(x, y);
        steppedOn(xx, yy);
        return true;
    }

    protected void steppedOn(int x, int y) { level.steppedOn(x, y); }
    protected void steppedFrom(int x, int y) { level.steppedFrom(x, y); }

}