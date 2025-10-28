package game.particle;

import game.entity.unit.mob.Mob;
import game.gfx.Bitmap;

public class SmoothRay extends Particle {

    public Mob from;
    public Mob to;
    public int life = 1;
    public double pos = 0.0;
    public double speed;
    public int br = random.nextInt(200);
    public double xo = (random.nextDouble() - 0.5) * 9.0 * 4.0;
    public double yo = (random.nextDouble() - 0.5) * 9.0 * 4.0;
    public double zo = (random.nextDouble() - 0.5) * 9.0 * 4.0;
    public int halfSize;
    public int color;

    public SmoothRay(int color, Mob from, Mob to) {
        this.from = from;
        this.to = to;
        this.color = color;
        pos = random.nextDouble() * 0.8 + 0.3;
        halfSize = (int)Math.round(1.0 + pos);
        life = random.nextInt(10) + 20;
        speed = (random.nextDouble() + 0.4) * 0.02;
    }

    public void tick() {
        if (life-- < 0 || pos < -1.0) {
            remove();
            return;
        }

        double xs = from.x + Math.cos(from.dir) * 64.0;
        double ys = from.y + Math.sin(from.dir) * 64.0;
        double xm = from.x + Math.cos(from.dir) * 128.0;
        double ym = from.y + Math.sin(from.dir) * 128.0;
        double x0 = xs + (xm - from.x) * pos;
        double y0 = ys + (ym - from.y) * pos;
        double x1 = xm + (to.x - xm) * pos;
        double y1 = ym + (to.y - ym) * pos;
        x = x0 + (x1 - x0) * pos + xo * pos;
        y = y0 + (y1 - y0) * pos + yo * pos;
        z = from.z + (to.z - from.z) * pos + 5.0 + zo * pos;
        pos -= speed;
        halfSize = (int)Math.round(1.0 + pos);
    }

    public void render(Bitmap b, int xp, int yp) { b.fill(xp - halfSize, yp - halfSize - 24, xp + halfSize, yp + halfSize - 24, color | 65793 * br); }
    public void renderShadow(Bitmap b, int xp, int yp) { b.fill(xp - halfSize, yp + 12 - (int)z - halfSize, xp + halfSize, yp + 12 - (int)z + halfSize, 1); }
    
}
