package game.entity.mob;

import game.gfx.Art;

import java.awt.*;

public class Leaf extends Mob {

    public int dir;
    public double moveSpeed;

    public Leaf(double x, double y, int dir) {
        super(x, y);
        this.dir = dir;
        moveSpeed = 0.25;
        renderLayer = 0;
        bounce = 0.7;
    }

    public void tick() {
        xa += moveSpeed * dir;
        super.tick();

        if (dir < 0 && x < -16 || dir > 0 && x > (level.w + 1 << 4)) x += (((level.w << 4) + 32) * -dir);
    }

    public void render(Graphics2D g) {
        int x = (int)(this.x - 8);
        int y = (int)(this.y - 8);
        int t = 9;
        g.drawImage(Art.sprites[t], x, y, null);
    }
}