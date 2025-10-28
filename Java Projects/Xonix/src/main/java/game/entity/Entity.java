package game.entity;

import game.level.Level;

import java.awt.*;
import java.util.Random;

import static game.Constants.*;

public abstract class Entity {
    public static final Random random = new Random();
    public int xt;
    public int yt;
    public int xa;
    public int ya;
    public boolean removed;
    public Level level;
    public Color color = Color.MAGENTA;

    public Entity(int xt, int yt) {
        this.xt = xt;
        this.yt = yt;
    }

    public void init(Level level) {
        this.level = level;
    }

    public void tick() {

    }

    public void render(Graphics2D g) {
        int d = level.getTile(xt, yt);

        int sz = (CELL_SIZE - ENTITY_SIZE) / 2;
        int xOffs = d > 0 ? CELL_SIZE / 4 : 0;
        int yOffs = d > 0 ? CELL_SIZE / 2 : 0;

        int x = xt * CELL_SIZE + sz + xOffs;
        int y = yt * CELL_SIZE + sz - yOffs;

        g.setColor(color);
        g.fillRect(x, y, ENTITY_SIZE, ENTITY_SIZE);
    }

}