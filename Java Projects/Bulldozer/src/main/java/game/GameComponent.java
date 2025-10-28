package game;

import game.level.EntityListCache;
import game.screen.GameScreen;
import game.screen.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class GameComponent extends Canvas implements Runnable {

    public static final String TITLE = "Bulldozer";
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;
    private boolean running;
    private BufferedImage screenImage;
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
        screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
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
            createBufferStrategy(2);
            requestFocus();
            return;
        }

        Graphics2D g2 = screenImage.createGraphics();

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        game.render(g2);

        g2.dispose();

        Graphics g = bs.getDrawGraphics();

        g.drawImage(screenImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        g.dispose();
        bs.show();

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        GameComponent gc = new GameComponent();
        frame.setTitle(TITLE);
        gc.setMinimumSize(d);
        gc.setMaximumSize(d);
        gc.setPreferredSize(d);
        frame.setLayout(new BorderLayout());
        frame.add(gc, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        gc.start();
    }
}