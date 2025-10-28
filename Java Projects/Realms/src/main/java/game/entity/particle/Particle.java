package game.entity.particle;

import game.entity.Entity;
import game.gfx.Bitmap;
import game.gfx.Screen;
import game.gfx.util.BitmapHelper;

public class Particle extends Entity {

    protected int time;
    protected double xa;
    protected double ya;
    protected double za;
    protected double xx;
    protected double yy;
    protected double zz;
    protected int col;
    protected int speed = 1;

    public Particle(int x, int y, int col) {
        this.x = x;
        this.y = y;
        this.col = col;
        xx = x;
        yy = y;
        zz = 2;
        xa = random.nextGaussian() * 0.3;
        ya = random.nextGaussian() * 0.2;
        za = random.nextFloat() * 0.7 + 2;
        time = 60;
    }

    public Particle() {}

    public void initParticle(int x, int y, double xa, double ya) {
        this.x = x;
        this.y = y;
        this.xa = xa;
        this.ya = ya;
    }

    public void tick() {
        time--;
        if (time < 0) removed = true;
    }

    public void render(Screen screen) {
        int xo = x - screen.getXOffset();
        int yo = y - screen.getYOffset();

        if (zz > 0) BitmapHelper.drawPixel(xo, yo, 0, screen.getViewPort());

        BitmapHelper.drawPixel(xo, yo - (int) zz + 1, 0, screen.getViewPort());
        BitmapHelper.drawPixel(xo, yo - (int) zz, col, screen.getViewPort());
    }

    public void addXaYa(double xa, double ya) {
        this.xa += xa;
        this.ya += ya;
    }

    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public int getColor() { return col; }
    public void setColor(int col) { this.col = col; }
    public double getXa() { return xa; }
    public void setXa(double xa) { this.xa = xa; }
    public double getYa() { return ya; }
    public void setYa(double ya) { this.ya = ya; }
    public double getZa() { return za; }
    public void setZa(double za) { this.za = za; }
    public double getXx() { return xx; }
    public void setXx(double xx) { this.xx = xx; }
    public double getYy() { return yy; }
    public void setYy(double yy) { this.yy = yy; }
    public double getZz() { return zz; }
    public void setZz(double zz) { this.zz = zz; }

}