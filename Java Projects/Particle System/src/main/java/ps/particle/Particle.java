package ps.particle;

import ps.ParticleComponent;

import java.util.Random;

public class Particle {

    public static final Random random = new Random();
    public double lifeTime;
    public double age;
    public int x;
    public int y;
    public double angle;
    public double angleA;
    public int speed;
    protected ParticleManager pm;

    public void init(ParticleManager pm) {
        this.pm = pm;
        reset();
    }

    public void reset() {
        lifeTime = random.nextDouble();
        age = random.nextDouble() * 0.05;
        angle = random.nextDouble() * Math.PI * 2;
        angleA = random.nextDouble() * 0.05;
        speed = random.nextInt(2) + 1;
        x = pm.x;
        y = pm.y;
    }

    public void tick() {
        angle += angleA;
        if (angle > 2 * Math.PI) angle = 0;

        lifeTime -= age;
        if (lifeTime < 0) reset();

        if (x < 3 || y < 3 || x >= ParticleComponent.WIDTH - 4 || y >= ParticleComponent.HEIGHT - 4) reset();

        x += Math.round(Math.cos(angle) * speed);
        y += Math.round(Math.sin(angle) * speed);
    }

}