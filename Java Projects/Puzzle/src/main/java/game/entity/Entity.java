package game.entity;

import game.gfx.Sprite;
import game.level.Level;

import java.util.Random;

public class Entity extends Sprite {

    public Random random = new Random();
    public int x;
    public int y;
    public double xa;
    public double ya;
    public int xr = 8;
    public int yr = 8;
    public int tickTime;
    public Level level;
    public boolean removed;

    public void init(Level level) { this.level = level;}

    public void tick() { tickTime++; }
    public void collide() { ya *= -0.1; }
    public void remove() { removed = true; }

}