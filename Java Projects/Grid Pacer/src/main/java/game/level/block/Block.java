package game.level.block;

import game.level.Level;

import java.awt.*;

public abstract class Block {

    public int steps;
    public Level level;

    public void init(Level level) { this.level = level; }

    public abstract void tick();
    public abstract void render(Graphics2D g, int x, int y, int xOffs, int yOffs);
    public abstract boolean canPass();
    public abstract void steppedOn();
    public abstract void steppedFrom();

}