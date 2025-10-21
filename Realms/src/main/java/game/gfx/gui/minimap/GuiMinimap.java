package game.gfx.gui.minimap;

import game.InputHandler;
import game.entity.Entity;
import game.entity.Team;
import game.entity.mob.Mob;
import game.entity.tree.Tree;
import game.gfx.Bitmap;
import game.gfx.Screen;
import game.gfx.gui.GuiPanel;
import game.gfx.util.BitmapHelper;
import game.gfx.util.PaletteHelper;
import game.level.Level;
import game.level.tile.GrassTile;
import game.level.tile.Tile;

import java.awt.*;

public class GuiMinimap extends GuiPanel {

    private Level level;
    private Bitmap minimap;
    private Bitmap resizedMinimap;
    private int tick = 10;
    private Mark mark;

    public GuiMinimap(int posX, int posY, Level level) {
        super(posX, posY, (level.getWidth() >> 1) >> 3, (level.getHeight() >> 1) >> 3, PaletteHelper.getColor(-1, 530, 0, 111));
        this.level = level;
        minimap = new Bitmap(level.getWidth(), level.getHeight());
        resizedMinimap = new Bitmap(level.getWidth() >> 1, level.getHeight() >> 1);
        mark = new Mark(resizedMinimap);
    }

    public void markObject(int x, int y, int col) {
        int xx = Math.min(1 + (x >> 4), level.getWidth() - 1);
        int yy = Math.min(0 + (y >> 4), level.getHeight() - 1);

        minimap.getPixels()[xx + yy * minimap.getWidth()] = col;

        xx = Math.min(1 + (x >> 4), level.getWidth() - 1);
        yy = Math.min(1 + (y >> 4), level.getHeight() - 1);

        minimap.getPixels()[xx + yy * minimap.getWidth()] = col;

        xx = Math.min(0 + (x >> 4), level.getWidth() - 1);
        yy = Math.min(1 + (y >> 4), level.getHeight() - 1);

        minimap.getPixels()[xx + yy * minimap.getWidth()] = col;

        xx = Math.min(0 + (x >> 4), level.getWidth() - 1);
        yy = Math.min(0 + (y >> 4), level.getHeight() - 1);

        minimap.getPixels()[xx + yy * minimap.getWidth()] = col;
    }

    public void tick() {
        tick++;
        if (InputHandler.getInstance(null).minimap.clicked) setVisible(!visible);
        mark.tick();
        if (tick < 45 || !visible) return;
        tick = 0;
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                if (level.getFog() != null && level.getFog().getFog(x, y)) minimap.getPixels()[x + y * minimap.getWidth()] = 0;
                else {
                    switch (level.getTile(x, y).getID()) {
                        case Tile.GRASS_TILE:
                            minimap.getPixels()[x + y * minimap.getWidth()] = 0xCC00;
                            break;
                        case Tile.WATER_TILE:
                            minimap.getPixels()[x + y * minimap.getWidth()] = 0x1FF;
                            break;
                        case Tile.DEEP_WATER_TILE:
                            minimap.getPixels()[x + y * minimap.getWidth()] = 0x1CC;
                            break;
                        case Tile.SAND_TILE:
                            minimap.getPixels()[x + y * minimap.getWidth()] = 0xFFFF00;
                            break;
                        case Tile.ROAD_TILE:
                            minimap.getPixels()[x + y * minimap.getWidth()] = 0x6B6B6B;
                            break;
                        default:
                            minimap.getPixels()[x + y * minimap.getWidth()] = 0xCC00;
                    }

                    for (Entity e : level.getEntities((x - 1) << 4, (y - 1) << 4, (x + 1) << 4, (y + 1) << 4, null)) {
                        if (!(e instanceof Tree) && !(e instanceof Mob)) continue;

                        int col = 0xAABBCC;
                        if (e instanceof Mob) col = e.getTeam() == Team.HOSTILE ? 0xFF0000 : 0xFFCC00;
                        if (e instanceof Tree) col = 0x009900;
                        markObject(e.getX(), e.getY(), col);
                    }
                }

                if (level.getPlayer() != null) markObject(level.getPlayer().getX(), level.getPlayer().getY(), 0x1CC);
            }
        }

        Graphics2D g = resizedMinimap.getImage().createGraphics();
        g.drawImage(minimap.getImage(), 0, 0, resizedMinimap.getWidth(), resizedMinimap.getHeight(), null);
        g.dispose();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void render(Screen screen) {
        if (!visible) return;
        super.render(screen);
        mark.render();
        BitmapHelper.copy(resizedMinimap, 0, 0, x + 8, y + 8, resizedMinimap.getWidth(), resizedMinimap.getHeight(), screen.getViewPort());
    }

    public void showMark(int x, int y) { mark.put((x >> 4) >> 1, (y >> 4) >> 1); }
    public void hideMark() { mark.hide(); }
}