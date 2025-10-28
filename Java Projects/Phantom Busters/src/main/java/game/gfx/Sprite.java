package game.gfx;

import game.PhantomBusters;
import game.level.Level;

import java.util.Random;

public class Sprite {

    public static final double SCALE_X = 0.75;
    public static final double SCALE_Y = 0.375;
    public boolean removed;
    public Level level;
    protected Random random = new Random();
    public double x;
    public double y;
    public double z;
    public double xr = 8.0;
    public double yr = 16.0;
    public double zh = 48.0;

    public void init(Level level) {
        this.level = level;
    }

    public void tick() {
    }

    public void remove() {
        this.removed = true;
    }

    public boolean isAlive() {
        return !this.removed;
    }

    public final void render(Bitmap screen) {
        int xp = (int)Math.floor((this.x - this.y) * 0.75);
        int yp = (int)Math.floor((this.y + this.x) * 0.375 - this.z);
        this.render(screen, xp, yp);
    }

    public void render(Bitmap screen, int xp, int yp) {
    }

    public final void renderShadow(Bitmap screen) {
        int xp = (int)Math.floor((this.x - this.y) * 0.75);
        int yp = (int)Math.floor((this.y + this.x) * 0.375 - this.z);
        this.renderShadow(screen, xp, yp);
    }

    public void renderShadow(Bitmap screen, int xp, int yp) {
    }

    public boolean intersectsScreenSpace(double x0, double y0, double x1, double y1) {
        double xx = (int)Math.floor((this.x - this.y) * 0.75);
        double yy = (int)Math.floor((this.y + this.x - 24.0) * 0.375);
        int ww = 640;
        int hh = 480;
        return !(x1 <= xx - (double)ww || x0 > xx + (double)ww || y1 <= yy - (double)hh) && !(y0 > yy + (double)hh);
    }

}