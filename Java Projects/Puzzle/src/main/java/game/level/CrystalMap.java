package game.level;

import game.entity.Crystal;

import java.util.Arrays;

public class CrystalMap {

    public final int w;
    public final int h;
    protected Crystal[] crystals;

    public CrystalMap(int w, int h) {
        this.w = w;
        this.h = h;
        crystals = new Crystal[w * h];
    }

    public CrystalMap(int w, int h, Crystal[] crystals) {
        this.w = w;
        this.h = h;
        this.crystals = Arrays.copyOf(crystals, crystals.length);
    }

    public int clear() {
        int count = 0;
        for (int i = 0; i < w * h; i++) {
            Crystal c = crystals[i];
            if (c != null) count++;
            crystals[i] = null;
        }

        return count;
    }

    public void put(Crystal c, int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        crystals[x + y * w] = c;
    }

    public void remove(Crystal c, int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        crystals[x + y * w] = null;
    }

    public Crystal get(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return null;
        return crystals[x + y * w];
    }

    public void swap(Crystal c0, Crystal c1) {
        put(c0, c1.x + c1.xr >> 4, c1.y + c1.yr >> 4);
        put(c1, c0.x + c0.xr >> 4, c0.y + c0.yr >> 4);
    }

    public void swapBack(Crystal c0, Crystal c1) {
        put(c0, c0.x + c0.xr >> 4, c0.y + c0.yr >> 4);
        put(c1, c1.x + c1.xr >> 4, c1.y + c1.yr >> 4);
    }

    public void killAll() {
        for (Crystal c : crystals) {
            c.die();
        }
    }

}