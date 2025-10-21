package game.gfx.gui;

import game.gfx.Bitmap;
import game.gfx.Screen;
import game.gfx.util.BitmapHelper;

public class GuiPanel {

    protected boolean visible;
    protected int x;
    protected int y;
    protected int sizeX = 2;
    protected int sizeY = 2;
    protected Bitmap image;
    protected boolean changed;
    protected boolean closed;
    protected int panelCol;

    protected GuiPanel() {}

    public GuiPanel(int posX, int posY, int sizeX, int sizeY) { init(posX, posY, sizeX, sizeY, GuiManager.PANEL_COL); }
    public GuiPanel(int posX, int posY, int sizeX, int sizeY, int panelCol) { init(posX, posY, sizeX, sizeY, panelCol); }
    public GuiPanel(int posX, int posY) { init(posX, posY, 0, 0, GuiManager.PANEL_COL); }
    public GuiPanel(int posX, int posY, int panelCol) { init(posX, posY, 0, 0, panelCol); }

    private void init(int posX, int posY, int sizeX, int sizeY, int panelCol) {
        x = posX;
        y = posY;
        this.sizeX = sizeX + 2;
        this.sizeY = sizeY + 2;
        changed = true;
        visible = true;
        this.panelCol = panelCol;
        image = new Bitmap(this.sizeX * 8, this.sizeY * 8);
    }

    public void setVisible(boolean v) {
        if (visible != v) {
            visible = v;
            if (visible) changed = true;
        }
    }

    public void put(int x, int y) {
        this.x = x;
        this.y = y;
        changed = true;
    }

    public void setPanelColor(int col) {
        panelCol = col;
        changed = true;
    }

    public void setSize(int newX, int newY) {
        x = (newX < 1) ? 1 : newX;
        y = (newY < 1) ? 1 : newY;
        changed = true;
    }

    protected void change() {
        if (!changed) changed = true;
    }

    public void close() {
        closed = true;
        visible = false;
        changed = true;
    }

    public void paint(Screen screen) {
        if (!changed) return;
        paintF(screen);
    }

    protected void paintF(Screen screen) {
        Bitmap temp = new Bitmap(sizeX * 8, sizeY * 8);

        int xt = (sizeX - 1) * 8;
        int yt = (sizeY - 1) * 8;

        BitmapHelper.fill(temp, 0xFF00FF);
        BitmapHelper.drawHalfTile(screen.getSprites(), 0, 0, 0, 13 * 8, panelCol, 0, temp);
        BitmapHelper.drawHalfTile(screen.getSprites(), xt, 0, 0, 13 * 8, panelCol, 1, temp);
        BitmapHelper.drawHalfTile(screen.getSprites(), 0, yt, 0, 13 * 8, panelCol, 2, temp);
        BitmapHelper.drawHalfTile(screen.getSprites(), xt, yt, 0, 13 * 8, panelCol, 3, temp);

        for (int x = 1; x < sizeX - 1; x++) {
            BitmapHelper.drawHalfTile(screen.getSprites(), x * 8, 0, 8, 13 * 8, panelCol, 0, temp);
            BitmapHelper.drawHalfTile(screen.getSprites(), x * 8, yt, 8, 13 * 8, panelCol, 2, temp);
        }

        for (int y = 1; y < sizeY - 1; y++) {
            BitmapHelper.drawHalfTile(screen.getSprites(), 0, y * 8, 2 * 8, 13 * 8, panelCol, 0, temp);
            BitmapHelper.drawHalfTile(screen.getSprites(), xt, y * 8, 2 * 8, 13 * 8, panelCol, 1, temp);
        }

        for (int x = 1; x < sizeX - 1; x++) {
            for (int y = 1; y < sizeY - 1; y++) {
                BitmapHelper.drawHalfTile(screen.getSprites(), x * 8, y * 8, 3 * 8, 13 * 8, panelCol, 0, temp);
            }
        }

        image = null;
        image = temp;
        changed = false;
    }

    public void render(Screen screen) {
        if (!visible) return;

        paint(screen);
        BitmapHelper.copy(image, 0, 0, x, y, sizeX * 8, sizeY * 8, screen.getViewPort(), 0xFF00FF);
    }

    public void tick() {}
    public boolean isClosed() { return closed; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Bitmap getImage() { return image; }
    public void show() { setVisible(true); }
    public void hide() { setVisible(false); }
    public boolean isVisible() { return visible; }

}