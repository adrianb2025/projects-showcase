package game.gfx.gui;

import game.gfx.Bitmap;
import game.gfx.Font;
import game.gfx.Screen;
import game.gfx.util.BitmapHelper;
import game.gfx.util.PaletteHelper;

public class GuiStatusPanel extends GuiPanel {

    private static final int NEGATIVE = PaletteHelper.getColor(-1, -1, -1, 500);
    private static final int POSITIVE = PaletteHelper.getColor(-1, -1, -1, 50);
    private static final int NEUTRAL = PaletteHelper.getColor(-1, -1, -1, 555);

    private String text;
    private int val;
    private int pal;
    private int xOffs;
    private int yOffs;
    private int currentCol = NEUTRAL;
    private long time;

    public GuiStatusPanel(int x, int y, int xOffs, int yOffs, int val, int col) {
        this.x = x;
        this.y = y;
        setText(val);
        sizeY = 2;
        pal = col;
        this.xOffs = xOffs * 8;
        this.yOffs = yOffs * 8;
        visible = true;
    }

    public void setText(int t) {
        if (t == val) return;
        time = System.currentTimeMillis() + 1000;
        if (t > val) currentCol = POSITIVE;
        else currentCol = NEGATIVE;

        val = t;
        setText2("" + t);
    }

    public void setText2(String t) {
        text = t;
        sizeX = 2 + 1 + text.length();
        changed = true;
    }

    public void tick() {
        if (currentCol == NEUTRAL) return;

        if (System.currentTimeMillis() > time) currentCol = NEUTRAL;

        changed = true;
    }

    protected void paintF(Screen screen) {
        Bitmap temp = new Bitmap(sizeX * 8, sizeY * 8);

        BitmapHelper.fill(temp, 0xFF00FF);
        BitmapHelper.scaleDraw(screen.getSprites(), 1, 0, 0, xOffs, yOffs, 8 << 1, 8 << 1, pal, 0, temp);

        Font.drawToBitmap(text, screen, 17, 4, currentCol, temp);
        BitmapHelper.drawShadow(temp, 0xFF00FF, 0x555555);

        image = null;
        image = temp;
        changed = false;
    }

}
