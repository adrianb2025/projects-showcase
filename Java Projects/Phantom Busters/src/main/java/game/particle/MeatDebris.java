package game.particle;

import game.gfx.Bitmap;

public class MeatDebris extends Debris {

    public int halfSize = 4;

    public MeatDebris(double x, double y, double z) { super(x, y, z); }

    public void tick() {
        super.tick();
        if (random.nextInt(3) != 0) return;

        BloodDebris blood = new BloodDebris(x, y, z);
        blood.xa *= 0.05;
        blood.ya *= 0.05;
        blood.za *= 0.05;
        blood.xa += xa * 0.5;
        blood.ya += ya * 0.5;
        blood.za += za * 0.5;
        level.add(blood);
    }

    public void collide(double xxa, double yya, double zza) {
        if (za < -0.5) {
            for (int i = 0; i < 20; i++) {
                BloodDebris blood = new BloodDebris(x, y, 0.0);
                blood.xa *= 0.4;
                blood.ya *= 0.4;
                blood.za *= 0.2;
                blood.xa += xa * 0.5;
                blood.ya += ya * 0.5;
                blood.za = -za * 0.5;
                level.add(blood);
            }
            
            if (halfSize > 0) halfSize--;
            
        }
        super.collide(xxa, yya, zza);
    }

    public void renderShadow(Bitmap screen, int xp, int yp) { screen.fill(xp - 4, yp + 4 + (int)z, xp + 4, yp + 6 + (int)z, 1); }
    public void render(Bitmap screen, int xp, int yp) { screen.fill(xp - halfSize, yp - halfSize, xp + halfSize, yp + halfSize, 0xFFFFAF80); }

}