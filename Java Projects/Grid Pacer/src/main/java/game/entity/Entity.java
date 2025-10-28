package game.entity;

import game.level.Level;

import java.awt.*;
import java.util.Random;

public class Entity {

    protected static final Random random = new Random();
    public int x;
    public int y;
    public int xa;
    public int ya;
    public Level level;
    public boolean removed;

    public void init(Level level) { this.level = level; }

    public void tick() {}
    public void render(Graphics2D g, int xOffs, int yOffs) {}

}