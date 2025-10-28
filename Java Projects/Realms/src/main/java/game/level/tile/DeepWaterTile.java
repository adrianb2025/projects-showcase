package game.level.tile;

import game.entity.Entity;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;
import game.level.Level;

public class DeepWaterTile extends Tile {
    public DeepWaterTile(int id) {
        super(id);
        connectsToWater = true;
        liquid = true;
        slowPeriod = 2;
    }

    public void render(Screen screen, Level level, int x, int y) {
        random.setSeed(((tickCount + (x / 2 - y) * 4311) / 10) * 54687121L + x * 3271612L + y * 3412987161L);

        int tc1 = PaletteHelper.getColor(2, deepWaterMainColor, deepWaterMainColor + 3, deepWaterMainColor + 3);
        int tc2 = PaletteHelper.getColor(2, deepWaterMainColor, deepWaterMainColor + 3, deepWaterMainColor + 3);

        boolean u = level.getTile(x, y - 1).getID() != id;
        boolean d = level.getTile(x, y + 1).getID() != id;
        boolean l = level.getTile(x - 1, y).getID() != id;
        boolean r = level.getTile(x + 1, y).getID() != id;

        boolean su = u && level.getTile(x, y - 1).connectsToWater;
        boolean sd = d && level.getTile(x, y + 1).connectsToWater;
        boolean sl = l && level.getTile(x - 1, y).connectsToWater;
        boolean sr = r && level.getTile(x + 1, y).connectsToWater;

        if (!u && !l) screen.render(x * 16 + 0, y * 16 + 0, random.nextInt(4) * 8, 0, 8, 8, deepWaterCol, random.nextInt(4));
        else screen.render(x * 16 + 0, y * 16 + 0, (l ? 14 : 15) * 8, (u ? 0 : 1) * 8, 8, 8, su || sl ? tc2 : tc1, 0);

        if (!u && !r) screen.render(x * 16 + 8, y * 16 + 0, random.nextInt(4) * 8, 0, 8, 8, deepWaterCol, random.nextInt(4));
        else screen.render(x * 16 + 8, y * 16 + 0, (r ? 16 : 15) * 8, (u ? 0 : 1) * 8, 8, 8, su || sr ? tc2 : tc1, 0);

        if (!d && !l) screen.render(x * 16 + 0, y * 16 + 8, random.nextInt(4) * 8, 0, 8, 8, deepWaterCol, random.nextInt(4));
        else screen.render(x * 16 + 0, y * 16 + 8, (l ? 14 : 15) * 8, (d ? 2 : 1) * 8, 8, 8, sd || sl ? tc2 : tc1, 0);

        if (!d && !r) screen.render(x * 16 + 8, y * 16 + 8, random.nextInt(4) * 8, 0, 8, 8, deepWaterCol, random.nextInt(4));
        else screen.render(x * 16 + 8, y * 16 + 8, (r ? 16 : 15) * 8, (d ? 2 : 1) * 8, 8, 8, sd || sr ? tc2 : tc1, 0);
    }

    public void tick(Level level, int xt, int yt) {
        int xn = xt;
        int yn = yt;

        if (random.nextBoolean()) xn += random.nextInt(2) * 2 - 1;
        else yn += random.nextInt(2) * 2 - 1;

        if (level.getTile(xn, yn) == hole) level.setTile(xn, yn, this);
    }

    public boolean mayPass(Level level, int xt, int yt, Entity e) { return e.canSwim(); }

}