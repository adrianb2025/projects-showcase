package game.entity.particle;

import game.entity.Entity;
import game.gfx.Screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParticleSystem extends Entity {

    private final double[] COS = new double[360];
    private final double[] SIN = new double[360];

    private List<Particle> particles = new ArrayList<Particle>();
    private double wind;
    private double gravity;
    private long lastTime;
    private int delay;

    private Comparator<Particle> particleComparator = new Comparator<Particle>() {
        public int compare(Particle e0, Particle e1) {
            if (e1.getTime() < e0.getTime()) return 1;
            if (e1.getTime() > e0.getTime()) return -1;
            return 0;
        }
    };

    public ParticleSystem(Class<? extends  Particle> clazz, int count, double wind, double gravity, int delay) throws IllegalAccessException, InstantiationException {
        this.wind = wind;
        this.gravity = gravity;
        this.delay = delay;
        initSinCos();
        for (int i = 0; i < count; i++) {
            Particle p = clazz.newInstance();
            p.setRemoved(true);
            particles.add(p);
        }
    }

    public void createExplosion(int x, int y, double xa, double ya, int count) {
        count = Math.min(particles.size(), count);
        while (count > 0) {
            count--;
            int angle = random.nextInt(360);
            updateDeadParticle(x + random.nextInt(9) - 4, y + random.nextInt(5) - 2, xa + COS[angle], ya + SIN[angle]);
        }
    }

    public void tick() {
        if (System.currentTimeMillis() - lastTime > delay) {
            for (Particle p : particles) {
                if (p.isRemoved()) continue;
                p.tick();
                p.addXaYa(wind, gravity);
            }

            Collections.sort(particles, particleComparator);
            lastTime = System.currentTimeMillis();
        }
    }

    public void render(Screen screen) {
        for (Particle p : particles) {
            if (p.isRemoved()) continue;
            p.render(screen);
        }
    }

    private void updateDeadParticle(int x, int y, double xa, double ya) {
        for (Particle p : particles) {
            if (p.isRemoved()) {
                p.setRemoved(false);
                p.initParticle(x, y, xa, ya);
                break;
            }
        }
    }

    private void initSinCos() {
        for (int i = 0; i < 360; i++) {
            COS[i] = Math.cos(Math.toRadians(i));
            SIN[i] = Math.sin(Math.toRadians(i));
        }
    }

}