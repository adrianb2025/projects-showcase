package game.entity.rain;

import game.Game;
import game.entity.Entity;
import game.entity.mob.Player;
import game.gfx.Screen;
import game.gfx.util.BitmapHelper;
import game.level.tile.Tile;

public class Rain extends Entity {

    private double xa;
    private double ya;
    private int lifeTime;
    public Rain(Player player, double xa, double ya, int speed) {
        if (random.nextInt(2) == 0) {
            x = (player.getX() - Game.WIDTH + random.nextInt(Game.WIDTH << 1));
            y = (player.getY() - (Game.HEIGHT >> 1) - 10);
        } else {
            x = (player.getX() + (Game.WIDTH >> 1) - 10);
            y = (player.getY() - Game.HEIGHT + random.nextInt(Game.HEIGHT << 1));
        }

        this.xa = xa * speed;
        this.ya = ya * speed;

        lifeTime = random.nextInt(150);
    }

    public void tick() {
        lifeTime--;
        if (lifeTime < 0) {
            removed = true;
            level.add(new Puddle(x, y));
            int xx = x >> 4;
            int yy = y >> 4;
            if (level.getTile(xx, yy) == Tile.hole) level.setTile(x, y, Tile.water);
            return;
        }

        x += xa;
        y += ya;
    }
    public void render(Screen screen) {
        int xo = x - screen.getXOffset();
        int yo = y - screen.getYOffset();

        BitmapHelper.drawLine(xo, yo, xo + (int) xa, yo + (int) ya, 0xFFFFFF, screen.getViewPort());
    }

}
