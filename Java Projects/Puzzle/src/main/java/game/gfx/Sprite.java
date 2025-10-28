package game.gfx;

public class Sprite {

    public int xp;
    public int yp;
    public Bitmap b;

    public void tick() {}
    public void render(Bitmap screen) { screen.draw(b, xp, yp); }
    public void render(Bitmap screen, int xOffs, int yOffs) {}

}