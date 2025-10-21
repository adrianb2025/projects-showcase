package game.gfx;

import game.gfx.util.BitmapHelper;

public class Screen {

    private Bitmap viewPort;
    private Bitmap sprites;
    private int xOffset;
    private int yOffset;

    public Screen(int w, int h, String spritesPath) {
        viewPort = new Bitmap(w, h);
        sprites = BitmapHelper.loadTextureFromResources(spritesPath);
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void render(int xOffs, int yOffs, int xo, int yo, int w, int h, int cols, int bits) {
        viewPort.draw(sprites, xOffs - xOffset, yOffs - yOffset, xo, yo, w, h, cols, bits);
    }

    public void render(int xOffs, int yOffs, int xo, int yo, int cols, int bits) {
        viewPort.draw(sprites, xOffs - xOffset, yOffs - yOffset, xo, yo, 8, 8, cols, bits);
    }

    public void render(int scale, int xOffs, int yOffs, int xo, int yo, int w, int h, int cols, int bits) {
        viewPort.draw(sprites, scale, xOffs - xOffset, yOffs - yOffset, xo, yo, w, h, cols, bits);
    }

    public void render(int scale, int xOffs, int yOffs, int xo, int yo, int cols, int bits) {
        viewPort.draw(sprites, scale, xOffs - xOffset, yOffs - yOffset, xo, yo, 8, 8, cols, bits);
    }

    public void render(double angle, int xOffs, int yOffs, int xo, int yo, int w, int h, int cols, int bits) {
        viewPort.draw(1, angle, sprites, xOffs - xOffset, yOffs - yOffset, xo, yo, w, h, cols, bits);
    }

    public void render(double angle, int xOffs, int yOffs, int xo, int yo, int cols, int bits) {
        viewPort.draw(1, angle, sprites, xOffs - xOffset, yOffs - yOffset, xo, yo, 8, 8, cols, bits);
    }

    public void render(int scale, double angle, int xOffs, int yOffs, int xo, int yo, int w, int h, int cols, int bits) {
        viewPort.draw(scale, angle, sprites, xOffs - xOffset, yOffs - yOffset, xo, yo, w, h, cols, bits);
    }

    public void render(int scale, double angle, int xOffs, int yOffs, int xo, int yo, int cols, int bits) {
        viewPort.draw(scale, angle, sprites, xOffs - xOffset, yOffs - yOffset, xo, yo, 8, 8, cols, bits);
    }

    public Bitmap getViewPort() { return viewPort; }
    public Bitmap getSprites() { return sprites; }
    public int getXOffset() { return xOffset; }
    public int getYOffset() { return yOffset; }

}