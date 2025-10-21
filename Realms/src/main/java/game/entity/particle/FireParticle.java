package game.entity.particle;

public class FireParticle extends Particle {

    public void initParticle(int x, int y, double xa, double ya) {
        super.initParticle(x, y, xa, ya);
        time = random.nextInt(4) + 2;
    }

    public void tick() {
        super.tick();

        if (time < 2) col = 0xFF0000;
        else col = 0xFF7F00;

        x += xa;
        y += ya;
    }

}