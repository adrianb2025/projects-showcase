package game.gfx.sprite;

public class SpriteWrapper {

    private int xo;
    private int yo;
    private int w;
    private int h;
    private int cols;

    public SpriteWrapper(int xo, int yo, int w, int h, int cols) {
        this.xo = xo;
        this.yo = yo;
        this.w = w;
        this.h = h;
        this.cols = cols;
    }

    public int getXo() { return xo; }
    public void setXo(int xo) { this.xo = xo; }
    public int getYo() { return yo; }
    public void setYo(int yo) { this.yo = yo; }
    public int getWidth() { return w; }
    public void setWidth(int w) { this.w = w; }
    public int getHeight() { return h; }
    public void setHeight(int h) { this.h = h; }
    public int getColors() { return cols; }
    public void setColors(int cols) { this.cols = cols; }

}