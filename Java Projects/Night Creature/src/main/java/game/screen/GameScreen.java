package game.screen;

import game.InputHandler;
import game.NightCreature;
import game.entity.unit.Knight;
import game.entity.unit.Player;
import game.entity.unit.Unit;
import game.entity.unit.task.MoveTask;
import game.gfx.Bitmap;
import game.level.Level;
import game.util.MathUtil;

import java.awt.*;
import java.util.Random;

public class GameScreen extends Screen {

    private static final Random random = new Random();
    public Level level;
    private Unit player;
    private int tickTime = 2200;
    private double xScroll;
    private double yScroll;
    private double xScrollA;
    private double yScrollA;
    private double xScrollT;
    private double yScrollT;
    private int dayFactor;
    private int[] postData = new int[NightCreature.WIDTH * NightCreature.HEIGHT];
    public double scrollSpeed = 0.3;

    private int score;

    public GameScreen() {
        level = Level.loadLevel(this, 0, 0);
        player = new Player(100, 100, 4);
        level.addEntity(player);

        for (int i = 0; i < 2; i++) {
            Knight knight = new Knight(random.nextDouble() * (level.w << 4), random.nextDouble() * (level.h << 4), 8);
            level.addEntity(knight);
        }
    }

    void translateLevel(int xa, int ya) {
        int nx = level.x + xa;
        int ny = level.y + ya;

        if (nx < 0 || ny < 0 || nx > 7 || ny > 7) return;

        Level oldLevel = level;
        oldLevel.setOffset((int) xScroll, (int) yScroll);
        player.x -= xa * ((level.w << 4) - 64);
        player.y -= ya * ((level.h << 4) - 64);

        xScroll -= xa * ((level.w << 4) - 64);
        yScroll -= ya * ((level.h << 4) - 64);

        Level newLevel = Level.loadLevel(this, level.x + xa, level.y + ya);
        newLevel.addEntity(player);
        newLevel.setOffset((int) xScroll, (int) yScroll);

        level = newLevel;

        setScreen(new TranslateScreen(this, oldLevel, newLevel, xa, ya));
    }

    public void tick(InputHandler input) {
        dayFactor = (int)(145 * Math.abs(Math.sin(tickTime++ * 0.0001 * Math.PI * 2)) + 110);
        level.tick();
        level.updateTime(dayFactor);
        if (input.leftClicked) player.setTask(new MoveTask(input.x + level.xOffset, input.y + level.yOffset));

        if (tickTime % (60 - ((int)((score % 20000) / 20000.0)) * 59) == 0) {
            Knight knight = new Knight(random.nextDouble() * (level.w << 4), random.nextDouble() * (level.h << 4), 8);
            level.addEntity(knight);
        }

        if (player.x > ((level.w << 4) - 8 - 16)) translateLevel(1, 0);
        if (player.y > ((level.h << 4) - 8 - 16)) translateLevel(0, 1);
        if (player.x < 8) translateLevel(-1, 0);
        if (player.y < 8) translateLevel(0, -1);

        double xd = player.x - (xScroll + NightCreature.WIDTH / 2);
        double yd = player.y - (yScroll + NightCreature.HEIGHT / 2);
        double dd = NightCreature.HEIGHT / 3;

        if (xd * xd + yd * yd > dd * dd) {
            xScrollT = player.x;
            yScrollT = player.y;
        }

        xd = xScrollT - (xScroll + NightCreature.WIDTH / 2);
        yd = yScrollT - (yScroll + NightCreature.HEIGHT / 2);
        dd = 2;

        double l = MathUtil.length(xd, yd);
        if (l > dd) {
            xd /= l;
            yd /= l;

            xScrollA += xd * scrollSpeed;
            yScrollA += yd * scrollSpeed;
        }

        xScroll += xScrollA;
        yScroll += yScrollA;

        xScrollA *= 0.77;
        yScrollA *= 0.77;

        if (xScroll < 0) xScroll = 0;
        if (yScroll < 0) yScroll = 0;
        if (xScroll > ((level.w << 4) - NightCreature.WIDTH - 16)) xScroll = (level.w << 4) - NightCreature.WIDTH - 16;
        if (yScroll > ((level.h << 4) - NightCreature.HEIGHT - 16)) yScroll = (level.h << 4) - NightCreature.HEIGHT - 16;


        level.setOffset((int)xScroll, (int)yScroll);

        if (player.removed) setScreen(new GameOverScreen(this));
    }

    public void render(Graphics2D g) { level.render(g); }

    public void postRender(Bitmap screenBitmap, Graphics2D g2) {
        int[] pixels = screenBitmap.pixels;

        for (int i = 0; i < pixels.length; i++) {
            int x = i % screenBitmap.w - NightCreature.WIDTH / 2;
            int y = i / screenBitmap.w - NightCreature.HEIGHT / 2;

            double angle = x / (double) screenBitmap.h * y / (double)screenBitmap.h * Math.PI * 2.0 * (double)(255 - dayFactor) / 255.0;
            int xx = (int)(x * Math.cos(angle) - y * Math.sin(angle)) + NightCreature.WIDTH / 2;
            int yy = (int)(y * Math.cos(angle) + x * Math.sin(angle)) + NightCreature.HEIGHT / 2;

            if (xx >= 0 && yy >= 0 && xx < screenBitmap.w && yy < screenBitmap.h) {
                int c = pixels[xx + yy * screenBitmap.w];
                int r = (c >> 16) & 0xFF;
                int g = (c >> 8) & 0xFF;
                int b = c & 0xFF;

                int m = (r * 30 + g * 59 + b * 11) / 100;

                r = (r + m) / 2 * dayFactor / 255;
                g = (g + m) / 2 * dayFactor / 255;
                b = (b + m) / 2 * dayFactor / 255;

                postData[i] = 0xFF000000 | (r << 16) | (g << 8) | b;
                continue;
            }

            int rnd = (int)(random.nextDouble() * 16);
            postData[i] = 0xFF000000 | (76 + rnd << 16) | (rnd << 8) | rnd;
        }

        for (int i = 0; i < postData.length; i++) {
            pixels[i] = postData[i];
        }

        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + score, 10, NightCreature.HEIGHT - 10);
    }

    public void addScore(int amount) { score += amount; }

}