package game;

import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.mob.Player;
import game.entity.tree.FirTree;
import game.entity.tree.PineTree;
import game.entity.tree.TreeStump;
import game.gfx.CreditsScreen;
import game.gfx.Font;
import game.gfx.Rasterizer;
import game.gfx.gui.GuiManager;
import game.gfx.gui.GuiPanel;
import game.gfx.gui.GuiSpeedIndicator;
import game.gfx.gui.GuiStatusPanel;
import game.gfx.util.PaletteHelper;
import game.level.Level;
import game.level.tile.Tile;
import game.snd.Sound;

import javax.swing.*;
import java.awt.*;

public class Game extends Rasterizer implements Runnable {

    public static final String TITLE = "Realms";
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;
    private boolean running = false;
    private Level level;
    private Level[] levels = new Level[6];

    private Player player;
    private int xScroll;
    private int yScroll;
    public static boolean soundOn;

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void init() {
        InputHandler.getInstance(this);

        player = new Player(this);

        Sound.backgroundMusic.loop();

        for (int i = 0; i < 6; i++) {
            levels[i] = new Level(i, this);
        }

        changeLevel(0);
    }

    public void tick() {
        if (!hasFocus()) InputHandler.getInstance(null).releaseAll();
        else {
            InputHandler.getInstance(null).tick();

            if (InputHandler.getInstance(null).sound.clicked) {
                soundOn = !soundOn;
                if (soundOn) Sound.backgroundMusic.loop();
                else Sound.backgroundMusic.stop();
            }

            if (CreditsScreen.getInstance().isVisible()) CreditsScreen.getInstance().tick();

            else {
                if (player.isRemoved()) {
                    if (InputHandler.getInstance(null).action.clicked) {
                        changeLevelByDir(0);
                    }
                }

                GuiManager.getInstance().tick();

                if (!GuiManager.pauseMenu) {
                    level.tick();
                    Tile.tickCount++;
                    weatherManager.tick(level);
                }
            }
        }
    }

    public void render() {
        initRaster();

        if (CreditsScreen.getInstance().isVisible()) CreditsScreen.getInstance().render(screen);
        else {
            xScroll = player.getX() - screen.getViewPort().getWidth() / 2;
            yScroll = player.getY() - (screen.getViewPort().getHeight() - 8) / 2;
            if (xScroll < 16) xScroll = 16;
            if (yScroll < 16) yScroll = 16;
            if (xScroll > level.getWidth() * 16 - screen.getViewPort().getWidth() - 16) xScroll = level.getWidth() * 16 - screen.getViewPort().getWidth() - 16;
            if (yScroll > level.getHeight() * 16 - screen.getViewPort().getHeight() - 16) yScroll = level.getHeight() * 16 - screen.getViewPort().getHeight() - 16;


            level.renderBackground(screen, xScroll, yScroll);
            level.renderSprites(screen, xScroll, yScroll);
            level.renderFog(screen, xScroll, yScroll);

            GuiPanel panel = GuiManager.getInstance().get("money");
            if (panel != null) {
                ResourceItem coin = player.getInventory().findResource(Resource.coin);
                ResourceItem bigCoin = player.getInventory().findResource(Resource.bigCoin);
                int score = ((coin != null ? coin.getCount() : 0) + (bigCoin != null ? (bigCoin.getCount() << 1) : 0));
                ((GuiStatusPanel) panel).setText(score);
            }

            panel = GuiManager.getInstance().get("health");
            if (panel != null) ((GuiStatusPanel) panel).setText2(player.getHealth() + "/" + player.getCurrentStats().getEndurance());

            panel = GuiManager.getInstance().get("speed");
            if (panel != null) ((GuiSpeedIndicator) panel).changeSpeed(player.getSlowPeriod());

            GuiManager.getInstance().render(screen);

            if (player.isRemoved()) {
                String text = "Game Over!";
                Font.draw(text, screen, (WIDTH - text.length() * 8) >> 1, 100, PaletteHelper.getColor(555, 111, 111, 115));
                text = "Press Space to Restart";
                Font.draw(text, screen, (WIDTH - text.length() * 8) >> 1, 120, PaletteHelper.getColor(555, 111, 111, 115));
            }
        }

        super.render(player.isRemoved());
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

            boolean shouldRender = true;
            while (delta >= 1) {
                delta--;
                tick();
                ticks++;
                shouldRender = false;
            }

            if (shouldRender) {
                render();
                frames++;
            }

            try {
                Thread.sleep(2L);
            } catch (InterruptedException e) {
                // ignore
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("frames: " + frames + " ticks: " + ticks);
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void changeLevel(int num) {
        if (level != null) level.remove(player);
        level = levels[num % 6];
        level.add(player);
        GuiManager.getInstance().initDefaultGUI(level);
        InputHandler.getInstance(null).releaseAll();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        Game game = new Game();
        frame.setTitle(TITLE);
        game.setMinimumSize(d);
        game.setMaximumSize(d);
        game.setPreferredSize(d);
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    public Level getLevel() { return level; }
    public int getXScroll() { return xScroll; }
    public int getYScroll() { return yScroll; }
    public void changeLevelByDir(int dir) { changeLevel(level.getNumber() + dir); }


}