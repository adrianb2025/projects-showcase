package game.particle;

import game.gfx.Art;
import game.gfx.Bitmap;

import java.util.Random;

public class Debris extends Particle {

    public static final Random random = new Random();
    public double xa;
    public double ya;
    public double za;
    public int lifeTime;
    public double drag = 0.998;
    public double bounce = 0.6;
    public double gravity = 0.08;
    public int icon = 0;

    public Debris(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        lifeTime = random.nextInt(20) + 100;
        do {
            xa = random.nextDouble() * 2.0 - 1.0;
            ya = random.nextDouble() * 2.0 - 1.0;
            za = random.nextDouble() * 2.0 - 1.0;
        } while (xa * xa + ya * ya + za * za < 1.0);
        double dd = Math.sqrt(xa * xa + ya * ya + za * za);
        double speed = random.nextInt(2) + 2;
        xa = xa / dd * speed;
        ya = ya / dd * speed;
        za = (za / dd - 1.0) * speed;
    }

    public void render(Bitmap screen, int xp, int yp) { screen.draw(getBitmap(), xp - 16, yp - 16); }
    public void renderShadow(Bitmap b, int xp, int yp) { b.fill(xp - 3, yp + 2 + (int)z, xp + 3, yp + 4 + (int)z, 1); }

    public Bitmap getBitmap() { return Art.particles[icon % 8][icon / 8]; }

    public void tick() {
        super.tick();
        if (lifeTime-- < 0) {
            remove();
            return;
        }

        boolean onGround = z <= 1.0;

        xa *= onGround ? 0.85 : drag;
        ya *= onGround ? 0.85 : drag;
        za -= gravity;

        attemptMove();
    }

    public void attemptMove() {
        int steps = (int)(Math.sqrt(xa * xa + ya * ya + za * za) + 1.0);
        for (int i = 0; i < steps; i++) {
            _move(xa / steps, 0.0, 0.0);
            _move(0.0, ya / steps, 0.0);
            _move(0.0, 0.0, za / steps);
        }
    }

    private void _move(double xxa, double yya, double zza) {
        if (removed) return;

        double xn = x + xxa;
        double yn = y + yya;
        double zn = z + zza;

        if (xn < 0.0 || yn < 0.0 || zn < 0.0 || xn >= (level.w * 16 * 4) || yn >= (level.h * 16 * 4) || zn >= level.maxHeight) {
            if (zn < 0.0) z = 0.0;
            collide(xxa, yya, zza);
            return;
        }

        if (level.blocks(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh)) {
            collide(xxa, yya, zza);
            return;
        }

        x = xn;
        y = yn;
        z = zn;
    }

    public void collide(double xxa, double yya, double zza) {
        if (xxa != 0.0) xa *= -bounce;
        if (yya != 0.0) ya *= -bounce;
        if (zza != 0.0) za *= -bounce;
    }
    
}