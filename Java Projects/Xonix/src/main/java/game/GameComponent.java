package game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class GameComponent extends Canvas implements Runnable {

    public static final String TITLE = "Xonix Remake";
    public static final int WIDTH = 320;
    public static final int HEIGHT = WIDTH * 9 / 12;
    public static final int SCALE = 3;
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
        double nsPerTick = 1000000000.0 / 30.0;
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

    private void init() {
        screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        game = new Game();
        input = new InputHandler(this);
    }

    private void tick() {
        input.tick();
        game.tick(input);
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            requestFocus();
            return;
        }

        Graphics2D g2 = screenImage.createGraphics();

        g2.setColor(Color.GRAY);
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
        GameComponent game = new GameComponent();
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