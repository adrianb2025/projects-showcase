package game.entity;

import game.gfx.Art;
import game.screen.GameScreen;

import java.awt.*;

public class Block {

    public double x;
    public double y;
    public final int value;
    public int xt;
    public int yt;
    public GameScreen gameScreen;
    public boolean isEmpty;

    public Block(int value, int xt, int yt, boolean isEmpty) {
        this.value = value;
        this.xt = xt;
        this.yt = yt;
        this.isEmpty = isEmpty;
        x = xt << 5;
        y = yt << 5;
    }

    public void init(GameScreen game) { this.gameScreen = game; }

    public void render(Graphics2D g) {
        if (isEmpty) return;
        g.drawImage(Art.images[value], (int) x, (int) y, null);
    }

}