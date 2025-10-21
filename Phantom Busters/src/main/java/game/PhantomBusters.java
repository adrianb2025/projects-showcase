package game;

import game.gfx.Bitmap;
import game.level.EntityListCache;
import game.level.ParticleListCache;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class PhantomBusters extends Canvas implements Runnable {

    public static final String TITLE = "Phantom Busters";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    private boolean running;
    private BufferedImage screenImage;
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
                ParticleListCache.reset();
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
        screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        screenBitmap = new Bitmap(screenImage);
        input = new InputHandler(this);
        game = new Game(input);
    }

    public void tick() {
        if (!hasFocus()) input.releaseAll();
        else  {
            input.tick();
            game.tick();
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(2);
            requestFocus();
            return;
        }

        screenBitmap.clear(0xFF000000);
        game.render(screenBitmap);

        Graphics g = bs.getDrawGraphics();

        g.drawImage(screenImage, 0, 0, WIDTH, HEIGHT, null);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Dimension d = new Dimension(WIDTH, HEIGHT);
        PhantomBusters game = new PhantomBusters();
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

        frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "invisible"));

        game.start();
    }

}