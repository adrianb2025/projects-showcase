package ps;

import ps.gfx.Bitmap;
import ps.particle.ParticleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class ParticleComponent extends Canvas implements Runnable {

    public static final String TITLE = "Particle System";
    public static final int WIDTH = 240;
    public static final int HEIGHT = 180;
    public static final int SCALE = 3;
    public static final Random random = new Random();
    private boolean running;
    private Bitmap screenBitmap;
    public ParticleManager pm;
    private InputHandler input;

    public synchronized void start() {
        if (running) return;
        running = true;
        new Thread(this).start();
    }

    public void run() {
        init();
        long lastTime = System.nanoTime();
        double delta = 0.0;
        double nsPerTick = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                delta--;
                tick();
                ticks++;
            }


            render();
            swap();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("frames: " + frames + " ticks: " + ticks);
                frames = 0;
                ticks = 0;
            }

            try {
                Thread.sleep(2L);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    public void init() {
        screenBitmap = new Bitmap(WIDTH, HEIGHT);
        pm = new ParticleManager(400, 0xFF7F00);
        input = new InputHandler(this);

    }

    public void tick() {
        input.tick();
        pm.tick(input);
        if (input.mouseClicked) pm.setColor(random.nextInt());
    }

    public void swap() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            requestFocus();
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(screenBitmap.getImage(), 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        g.dispose();
        bs.show();
    }

    public void render() {
        pm.render(screenBitmap);
        screenBitmap.smooth(2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        ParticleComponent ps = new ParticleComponent();
        frame.setTitle(TITLE);
        ps.setMinimumSize(d);
        ps.setMaximumSize(d);
        ps.setPreferredSize(d);
        frame.setLayout(new BorderLayout());
        frame.add(ps, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        ps.start();
    }

}