package game.level;

import game.entity.Crystal;
import game.entity.Entity;

import java.util.List;
import java.util.Random;

public class CrystalHandler {

    private Random random = new Random();
    private CrystalMap currentMap;
    private CrystalMap oldMap;
    private CrystalMapValidator validator;
    private boolean updated;
    private boolean needToSpawn = true;
    private int tickTime;
    private int batch;
    private int rate;
    private Level level;
    private final int iconCount;
    public Crystal selectedCrystal;

    public CrystalHandler(int w, int h, int batch, int rate, Level level, int iconCount) {
        currentMap = new CrystalMap(w, h);
        oldMap = new CrystalMap(w, h);
        this.level = level;
        this.batch = batch;
        this.rate = rate;
        this.iconCount = iconCount;
        validator = new CrystalMapValidator(currentMap);
    }

    public void clear() {
        oldMap = new CrystalMap(currentMap.w, currentMap.h, currentMap.crystals);
        needToSpawn = currentMap.clear() != currentMap.w * currentMap.h;
    }

    public void put(Crystal c, int x, int y) {
        if (oldMap.get(x, y) != c) updated = true;
        currentMap.put(c, x, y);
    }

    private Crystal getSelectedNeighbor(int x, int y) {
        if (selectedCrystal == null) return null;

        Crystal cu = currentMap.get(x, y - 1);
        Crystal cd = currentMap.get(x, y + 1);
        Crystal cl = currentMap.get(x - 1, y);
        Crystal cr = currentMap.get(x + 1, y);

        if (cu == selectedCrystal) return cu;
        if (cd == selectedCrystal) return cd;
        if (cl == selectedCrystal) return cl;
        if (cr == selectedCrystal) return cr;

        return null;
    }

    public void selectCrystal(int x, int y) {
        if (needToSpawn) return;

        if (selectedCrystal != null) {
            int xx = selectedCrystal.x + selectedCrystal.xr >> 4;
            int yy = selectedCrystal.y + selectedCrystal.yr >> 4;

            if (x == xx && y == yy) {
                selectedCrystal = null;
                return;
            }

        }

        System.out.println("Selected: " + (selectedCrystal != null));

        Crystal c = currentMap.get(x, y);
        if (c == null || c.removed) return;

        Crystal neighbor = getSelectedNeighbor(x, y);
        if (neighbor != null && !neighbor.removed) {
            currentMap.swap(c, neighbor);
            List<List<Crystal>> bingoCrystals = validator.crystalValidations();
            if (bingoCrystals.isEmpty()) selectedCrystal = null;
            else c.swap(neighbor);
            currentMap.swapBack(c, neighbor);
        } else selectedCrystal = c;
    }

    public int calcScore(List<List<Crystal>> bingoCrystals) {
        int result = 0;
        int coefficent = bingoCrystals.size();
        for (List<Crystal> crystals : bingoCrystals) {
            int count = crystals.size();
            int bonus = count > 3 ? count * (count - 2) * (level.gameScreen.player.prevScore > 100 ? 2 : 1) : 0;
            result += coefficent * count + bonus;
            for (Crystal c : crystals) {
                if (c.die) continue;
                c.die();
            }
        }

        return result;
    }

    public void tick(boolean crystalSwapping) {
        if (!needToSpawn) {
            if (updated) {
                int newScore = calcScore(validator.crystalValidations());
                if (newScore > 0) {
                    newScore = newScore * 10 + random.nextInt(10);
                    level.gameScreen.player.addScore(newScore);
                }

                updated = false;
            } else if (tickTime % 120 == 0) {
                List<List<Crystal>> moves = validator.findAllMoves();
                System.out.println("moves left: " + moves.size() / 2);
                if (moves.size() == 0) {
                    level.gameScreen.player.subScore(500);
                    level.gameScreen.restartLevel();
                }
            }
        } else if (!crystalSwapping) {
            int freeCol = validator.getNextFreeCol();
            selectedCrystal = null;
            if (tickTime / batch % rate == 0 && freeCol >= 0) level.add(new Crystal(freeCol << 4, 0, random.nextInt(iconCount)));
        }
        
        tickTime++;
    }

}