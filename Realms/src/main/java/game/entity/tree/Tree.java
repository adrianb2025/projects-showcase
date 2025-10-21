package game.entity.tree;

import game.entity.Entity;
import game.gfx.Screen;
import game.gfx.util.BitmapHelper;

public abstract class Tree extends Entity {

    public Tree(int x, int y, int xr, int yr) {
        this.x = x;
        this.y = y;
        this.xr = xr;
        this.yr = yr;
    }

    public void render(Screen screen) {
        int xt = (x - (xr << 1)) - screen.getXOffset();
        int yt = (y - (yr << 1) * 3 - yr) - screen.getYOffset();
        BitmapHelper.drawNormal(sprite, xt, yt, screen.getViewPort(), 0xFF00FF);
    }

    public boolean blocks(Entity e) { return true; }

}