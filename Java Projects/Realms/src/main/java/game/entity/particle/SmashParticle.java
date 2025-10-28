package game.entity.particle;

public class SmashParticle extends Particle {

    public SmashParticle(int x, int y, int col) { super(x, y, col); }

    public void tick() {
        super.tick();

        if (zz != 0) {
            xx += xa;
            yy += ya;
            zz += za;
            if (zz < 0) {
                zz = 0;
                xa *= 0.6;
                ya *= 0.6;
                za *= -0.5;
            }

            za -= 0.15;
            x = (int) xx;
            y = (int) yy;
        }
    }

}
