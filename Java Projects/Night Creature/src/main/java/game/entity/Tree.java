package game.entity;

import game.gfx.Art;

import java.awt.*;

public class Tree extends Entity {

    public Tree(double x, double y, int r) { super(x, y, r); }

    public void tick() {}

    public void render(Graphics2D g) {
        int xx = (int)(x - 16);
        int yy = (int)(y - 16);
        g.drawImage(Art.sprites[(level.night ? 1 : 0) + 48], xx, yy - 16, null);
        g.drawImage(Art.sprites[(level.night ? 1 : 0) + 64], xx, yy, null);
    }

}