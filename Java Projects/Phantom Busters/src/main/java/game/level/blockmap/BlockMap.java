package game.level.blockmap;

import game.entity.Entity;
import game.level.EntityListCache;

import java.util.ArrayList;
import java.util.List;

public class BlockMap {

    private final int w;
    private final int h;
    private final int divide;
    private BlockCell[] blockCells;

    public BlockMap(int w, int h, int divide) {
        this.divide = divide;
        this.w = w;
        this.h = h;
        blockCells = new BlockCell[w * h];
        for (int i = 0; i < w * h; i++) {
            blockCells[i] = new BlockCell();
        }
    }

    public void add(Entity e) {
        if (!e.isCollideable) return;

        e.xBlockCell = (int)(e.x / (double)divide);
        e.yBlockCell = (int)(e.y / (double)divide);
        blockCells[e.xBlockCell + e.yBlockCell * w].entities.add(e);
    }

    public void update(Entity e) {
        if (!e.isCollideable) return;

        int xOld = e.xBlockCell;
        int yOld = e.yBlockCell;
        e.xBlockCell = (int)(e.x / (double)divide);
        e.yBlockCell = (int)(e.y / (double)divide);

        if (xOld != e.xBlockCell || yOld != e.yBlockCell) {
            blockCells[xOld + yOld * w].entities.remove(e);
            blockCells[e.xBlockCell + e.yBlockCell * w].entities.add(e);
        }

    }

    public void remove(Entity e) {
        if (!e.isCollideable) return;

        blockCells[e.xBlockCell + e.yBlockCell * w].entities.remove(e);
    }

    public List<Entity> getEntities(double x0, double y0, double z0, double x1, double y1, double z1) {
        double r = 10.0;
        int xc0 = (int)((x0 - r) / (double)divide);
        int xc1 = (int)((x1 + r) / (double)divide);
        int yc0 = (int)((y0 - r) / (double)divide);
        int yc1 = (int)((y1 + r) / (double)divide);

        if (xc0 < 0) xc0 = 0;
        if (yc0 < 0) yc0 = 0;
        if (xc1 >= w) xc1 = w - 1;
        if (yc1 >= h) yc1 = h - 1;

        List<Entity> result = EntityListCache.get();
        for (int y = yc0; y <= yc1; y++) {
            for (int x = xc0; x <= xc1; x++) {
                blockCells[x + y * w].getEntities(result, x0, y0, z0, x1, y1, z1);
            }
        }

        return result;
    }

    public List<Entity> getEntities(double x, double y) { return getEntities(x, y, 0.0, x, y, 0.0); }

    private class BlockCell {
        public List<Entity> entities = new ArrayList<Entity>();

        private BlockCell() {}

        public void getEntities(List<Entity> result, double x0, double y0, double z0, double x1, double y1, double z1) {
            for (Entity entity : entities) {
                if (!entity.isAlive() || !entity.intersects(x0, y0, z0, x1, y1, z1)) continue;
                result.add(entity);
            }
        }
    }

}