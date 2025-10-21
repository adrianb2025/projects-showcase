package game.entity.particle;

public class BloodParticle extends SmashParticle {

    public BloodParticle(int x, int y) {
        super(x, y, 0xCC1100);
        time = 100 + random.nextInt(100);
    }

    public void tick() {
        super.tick();

        if (time < 100 && time % 10 == 0) {
            int r = (col >> 16) & 0xFF;
            int g = (col >> 8) & 0xFF;
            int b = col & 0xFF;
            col = ((r >> 2) << 16) | ((g >> 2) << 8) | (b >> 2);
        }

    }

}