package game.particle;

import game.gfx.Bitmap;

public class BloodDebris extends Debris {

    public int br = random.nextInt(20);

    public BloodDebris(double x, double y, double z) {
        super(x, y, z);
        drag = 0.96;
        bounce = 0.1;
    }

    public void renderShadow(Bitmap screen, int xp, int yp) { screen.fill(xp - 2, yp - 2 + (int)z, xp + 2, yp + 2 + (int)z, 1); }
    public void render(Bitmap screen, int xp, int yp) { screen.fill(xp - 1, yp - 1, xp + 1, yp + 1, 0xFF700000 | 65793 * br); }

}
