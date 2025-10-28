package game.screen;

import game.InputHandler;
import game.NightCreature;
import game.gfx.Bitmap;
import game.level.Level;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TranslateScreen extends Screen {

    private int tickTime;
    private GameScreen gameScreen;
    private int xa;
    private int ya;
    private Level oldLevel;
    private Level newLevel;
    private int lifeTime;
    private int speed = 4;

    public TranslateScreen(GameScreen gameScreen, Level oldLevel, Level newLevel, int xa, int ya) {
        this.gameScreen = gameScreen;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.xa = xa;
        this.ya = ya;

        lifeTime = xa != 0 ? NightCreature.WIDTH / speed : NightCreature.HEIGHT / speed;
    }

    public void tick(InputHandler input) {
        tickTime++;
        if (tickTime >= lifeTime) setScreen(gameScreen);
    }

    public void render(Graphics2D g) {
        AffineTransform at = g.getTransform();

        g.translate(-xa * tickTime * speed, -ya * tickTime * speed);
        oldLevel.render(g);
        g.setTransform(at);

        at = g.getTransform();

        g.translate(-(xa * tickTime * speed - xa * NightCreature.WIDTH), -(ya * tickTime * speed - ya * NightCreature.HEIGHT));
        newLevel.render(g);
        g.setTransform(at);
    }

    public void postRender(Bitmap screenBitmap, Graphics2D g) { gameScreen.postRender(screenBitmap, g); }

}