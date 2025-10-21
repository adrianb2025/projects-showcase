package game.gfx.gui.minimap;

import game.gfx.Bitmap;
import game.gfx.gui.GuiPanel;
import game.gfx.util.BitmapHelper;

public class Mark extends GuiPanel {

    private long tick;
    private int interval = 1000;
    private Bitmap parent;

    public Mark(Bitmap parent) {
        this.parent = parent;
        image = new Bitmap(3, 3);
        BitmapHelper.fill(image, 0xFF00FF);
        image.getPixels()[1] = 0xFF0000;
        image.getPixels()[3] = 0xFF0000;
        image.getPixels()[4] = 0xFF0000;
        image.getPixels()[5] = 0xFF0000;
        image.getPixels()[7] = 0xFF0000;
        visible = false;
        changed = false;
    }

    public void put(int x, int y) {
        this.x = Math.max(x - 1, 0);
        this.y = Math.max(y - 1, 0);

        visible = true;
        changed = true;
    }

    public void tick() {
        if (changed && tick < System.currentTimeMillis()) {
            visible = !visible;
            tick = System.currentTimeMillis() + interval;
        }
    }

    public void render() {
        if (!visible) return;
        BitmapHelper.copy(image, 0, 0, x, y, 3, 3, parent, 0xFF00FF);
    }

    public void hide() {
        visible = false;
        changed = false;
    }

}