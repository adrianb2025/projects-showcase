package game;

import game.gfx.Bitmap;
import game.level.EntityListCache;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class NightCreature extends Canvas implements Runnable {

    public static final String TITLE = "Night Creature";
    public static final int WIDTH = 200;
    public static final int HEIGHT = 120;
    public static final int SCALE = 5;
    private boolean running;
    private Bitmap screenBitmap;
    private Game game;
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
                EntityListCache.reset();
            }

            render();
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
        game = new Game();
        input = new InputHandler(this);
    }

    public void tick() {
        input.tick();
        game.tick(input);
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        Graphics2D g2 = screenBitmap.image.createGraphics();

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        game.render(g2);
        game.postRender(screenBitmap, g2);

        g2.dispose();

        Graphics g = bs.getDrawGraphics();

        g.drawImage(screenBitmap.image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        NightCreature game = new NightCreature();
        frame.setTitle(TITLE);
        game.setMinimumSize(d);
        game.setMaximumSize(d);
        game.setPreferredSize(d);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.start();
    }

}