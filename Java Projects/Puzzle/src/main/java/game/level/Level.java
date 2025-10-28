package game.level;

import game.entity.Crystal;
import game.entity.mob.Mob;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {

    private List<Crystal> crystals = new ArrayList<Crystal>();
    public final int w;
    public final int h;
    public final int mw;
    public final int mh;
    private Random random = new Random();
    private List<Crystal>[] crystalMap;
    public CrystalHandler crystalHandler;
    public int tickTime;
    public final int xOffset;
    public final int yOffset;
    public GameScreen gameScreen;
    public Mob mob;

    public Level(int w, int h, GameScreen gameScreen, Mob mob) {
        this.w = w;
        this.h = h;
        this.mob = mob;
        this.gameScreen = gameScreen;
        mw = 8;
        mh = 8;
        xOffset = w - (mw << 4) >> 1;
        yOffset = h - (mh << 4) >> 1;
        int rate = random.nextInt(3) + 1;
        int batch = Math.min(mh, (random.nextInt(mh) + 2) / 2 * 2);
        int iconCount = gameScreen.levelNum + 5;
        crystalHandler = new CrystalHandler(mw, mh, batch, rate, this, iconCount);
        crystalMap = new ArrayList[mw * mh];

        for (int i = 0; i < mw * mh; i++) {
            crystalMap[i] = new ArrayList<Crystal>();
        }
    }

    public void tick() {
        crystalHandler.clear();
        boolean swappingCrystal = false;
        for (int i = 0; i < crystals.size(); i++) {
            Crystal c = crystals.get(i);

            int xt = c.x + c.xr >> 4;
            int yt = c.y + c.yr >> 4;

            c.tick();

            if (c.removed) {
                crystals.remove(i--);
                removeCrystal(c, xt, yt);
                continue;
            }

            int xtn = c.x + c.xr >> 4;
            int ytn = c.y + c.yr >> 4;

            if (xt != xtn || yt != ytn) {
                removeCrystal(c, xt, yt);
                insertCrystal(c, xtn, ytn);
            }

            if (c.stand && !c.removed) crystalHandler.put(c, xtn, ytn);

            if (!c.swap) continue;
            swappingCrystal = true;
        }

        crystalHandler.tick(swappingCrystal);
        gameScreen.player.tick();

        if (mob != null) mob.tick();

        tickTime++;
    }

    public void add(Crystal c) {
        List<Crystal> crystalsInCell = getCrystal(c.x + c.xr >> 4, c.y + c.yr >> 4);
        if (crystalsInCell == null || !crystalsInCell.isEmpty()) return;
        crystals.add(c);
        insertCrystal(c, c.x + c.xr >> 4, c.y + c.yr >> 4);
        c.init(this);
    }

    public void insertCrystal(Crystal c, int x, int y) {
        if (x < 0 || y < 0 || x >= mw || y >= mh) return;
        crystalMap[x + y * mw].add(c);
    }

    public void removeCrystal(Crystal c, int x, int y) {
        if (x < 0 || y < 0 || x >= mw || y >= mh) return;
        crystalMap[x + y * mw].remove(c);
    }

    public List<Crystal> getCrystal(int x, int y) {
        if (x < 0 || y < 0 || x >= mw || y >= mh) return null;
        return crystalMap[x + y * mw];
    }

    public void renderSprites(Bitmap screen) {
        gameScreen.player.render(screen);

        for (Crystal c : crystals) {
            c.render(screen, xOffset, yOffset);
        }

        if (crystalHandler.selectedCrystal != null && !crystalHandler.selectedCrystal.removed) screen.draw(Art.sprites[0][0], crystalHandler.selectedCrystal.x + xOffset, crystalHandler.selectedCrystal.y + yOffset);
        if (mob != null) mob.render(screen);
    }

}