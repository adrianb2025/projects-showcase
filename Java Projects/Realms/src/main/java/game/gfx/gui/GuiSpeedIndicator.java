package game.gfx.gui;

import game.gfx.Bitmap;
import game.gfx.Screen;
import game.gfx.sprite.SpriteCollector;
import game.gfx.sprite.SpriteWrapper;

public class GuiSpeedIndicator extends GuiPanel {

    private Bitmap slow;
    private Bitmap walk;
    private Bitmap run;
    private Bitmap sprint;
    private int col;
    private int speed;

    public GuiSpeedIndicator(int x, int y, int col, SpriteCollector spriteCollector) {
        this.x = x;
        this.y = y;
        this.col = col;
        drawIcons(9, 3, spriteCollector);
        visible = true;
    }

    private void drawIcons(int xOffs, int yOffs, SpriteCollector spriteCollector) {
        slow = new Bitmap(16, 18);
        walk = new Bitmap(16, 18);
        run = new Bitmap(16, 18);
        sprint = new Bitmap(16, 18);

        xOffs = xOffs * 8;
        yOffs = yOffs * 8;

        spriteCollector.resetWrappers();
        spriteCollector.addWrapper(new SpriteWrapper(xOffs, yOffs, 16, 16, col));
        slow = spriteCollector.mergeWrappers("speed_slow", 1, 0, 0x01444444);

        spriteCollector.resetWrappers();
        spriteCollector.addWrapper(new SpriteWrapper(xOffs + 16, yOffs, 16, 16, col));
        walk = spriteCollector.mergeWrappers("speed_walk", 1, 0, 0x01444444);

        spriteCollector.resetWrappers();
        spriteCollector.addWrapper(new SpriteWrapper(xOffs + 32, yOffs, 16, 16, col));
        run = spriteCollector.mergeWrappers("speed_run", 1, 0, 0x01444444);

        spriteCollector.resetWrappers();
        spriteCollector.addWrapper(new SpriteWrapper(xOffs + 48, yOffs, 16, 16, col));
        sprint = spriteCollector.mergeWrappers("speed_sprint", 1, 0, 0x01444444);
    }

    public void changeSpeed(int s) {
        if (speed == s) return;
        else speed = s;
        changed = true;
    }

    protected void paintF(Screen screen) {
        if (speed < 3) image = slow;
        else if (speed < 10) image = walk;
        else if (speed < 40) image = run;
        else image = sprint;
    }
}