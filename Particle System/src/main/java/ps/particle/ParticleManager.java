package ps.particle;

import ps.InputHandler;
import ps.ParticleComponent;
import ps.gfx.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleManager {


    public static final Random random = new Random();
    public List<Particle> particles = new ArrayList<Particle>();
    public int color = 0xFF7F00;

    public int x = ParticleComponent.WIDTH / 2;
    public int y = ParticleComponent.HEIGHT / 2;

    public ParticleManager(int count, int col) {
        this(count);
        color = col;
    }

    public ParticleManager(int count) {
        for (int i = 0; i < count; i++) {
            add(new Particle());
        }
    }

    public void add(Particle p) {
        particles.add(p);
        p.init(this);
    }

    public void tick(InputHandler input) {
        x = input.mx;
        y = input.my;

        for (Particle p : particles) {
            p.tick();
        }
    }

    public void render(Bitmap screen) {
        for (Particle p : particles) {
            screen.setPixel(p.x, p.y, color);
        }
    }

    public void setColor(int color) { this.color = color; }

}