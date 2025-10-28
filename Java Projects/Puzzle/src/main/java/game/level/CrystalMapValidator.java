package game.level;

import game.entity.Crystal;

import java.util.ArrayList;
import java.util.List;

public class CrystalMapValidator {

    private int checkCount;
    public CrystalMap currentMap;

    public CrystalMapValidator(CrystalMap currentMap) { this.currentMap = currentMap; }
    public void setCurrentMap(CrystalMap currentMap) { this.currentMap = currentMap; }

    private List<List<Crystal>> checkVerticals(int maxCount) {
        ArrayList<List<Crystal>> result = new ArrayList<List<Crystal>>();
        for (int x = 0; x < currentMap.w; x++) {
            Crystal c;
            int count = 0;
            int oldIcon = -1;
            for (int y = 0; y < currentMap.h; y++) {
                c = currentMap.get(x, y);
                if (c != null) {
                    count = oldIcon == c.icon ? ++count : 0;
                    oldIcon = c.icon;
                    if (count != maxCount) continue;

                    ArrayList<Crystal> combinations = new ArrayList<Crystal>();
                    for (int i = 0; i <= count; i++) {
                        combinations.add(currentMap.get(x, y - i));
                    }

                    result.add(combinations);
                    count = 0;
                }
            }
        }

        return result;
    }

    private List<List<Crystal>> checkHorizontals(int maxCount) {
        ArrayList<List<Crystal>> result = new ArrayList<List<Crystal>>();
        for (int y = 0; y < currentMap.h; y++) {
            Crystal c;
            int count = 0;
            int oldIcon = -1;
            for (int x = 0; x < currentMap.w && (c = currentMap.get(x, y)) != null; x++) {
                count = oldIcon == c.icon ? ++count : 0;
                oldIcon = c.icon;
                if (count != maxCount) continue;
                ArrayList<Crystal> combinations = new ArrayList<Crystal>();

                for (int i = 0; i <= count; i++) {
                    combinations.add(currentMap.get(x - i, y));
                }

                result.add(combinations);
                count = 0;
            }
        }
        
        return result;
    }

    private boolean hasAllEquals(List<Crystal> srcList, List<Crystal> valList) {
        int found = 0;
        search: for (Crystal src : srcList) {
            for (Crystal val: valList) {
                if (val != src) continue;
                found++;
                continue search;
            }
        }

        return found == valList.size();
    }

    private List<List<Crystal>> filterEquals(List<List<Crystal>> bingoCrystals) {
        ArrayList<List<Crystal>> result = new ArrayList<List<Crystal>>(bingoCrystals);
        for (List<Crystal> srcList : bingoCrystals) {
            for (List<Crystal> valList : bingoCrystals) {
                if (srcList.size() <= valList.size() || !hasAllEquals(srcList, valList)) continue;
                result.remove(valList);
            }
        }
        
        return result;
    }

    public List<List<Crystal>> crystalValidations() {
        int maxCombinations = Math.max(currentMap.w, currentMap.h);
        List<List<Crystal>> result = new ArrayList<List<Crystal>>();
        
        for (int i = maxCombinations; i >= 2; i--) {
            result.addAll(checkVerticals(i));
            result.addAll(checkHorizontals(i));
        }
        
        result = filterEquals(result);
        return result;
    }

    public int getFreeRow(int col) {
        if (col < 0 || col >= currentMap.w) return -2;

        for (int y = currentMap.h - 1; y >= 0; --y) {
            Crystal crystal = currentMap.get(col, y);
            if (crystal != null) continue;
            return y;
        }

        return -1;
    }

    public int getNextFreeCol() {
        int result = -1;
        for (int y = checkCount; y < checkCount + currentMap.w; y++) {
            int freeRow = getFreeRow(y % currentMap.w);
            if (freeRow < 0) continue;
            result = y % currentMap.w;
            break;
        }

        checkCount++;
        return result;
    }

    public List<List<Crystal>> findAllMoves() {
        ArrayList<List<Crystal>> result = new ArrayList<List<Crystal>>();
        for (int y = 0; y < currentMap.h; y++) {
            for (int x = 0; x < currentMap.w; x++) {
                Crystal c = currentMap.get(x, y);
                Crystal cd = currentMap.get(x, y + 1);
                Crystal cu = currentMap.get(x, y - 1);
                Crystal cr = currentMap.get(x + 1, y);
                Crystal cl = currentMap.get(x - 1, y);

                if (c == null) continue;

                if (cl != null) {
                    currentMap.swap(c, cl);
                    if (crystalValidations().size() > 0) {
                        ArrayList<Crystal> swappedList = new ArrayList<Crystal>();
                        swappedList.add(c);
                        swappedList.add(cl);
                        result.add(swappedList);
                    }
                    currentMap.swapBack(c, cl);
                }

                if (cr != null) {
                    currentMap.swap(c, cr);
                    if (crystalValidations().size() > 0) {
                        ArrayList<Crystal> swappedList = new ArrayList<Crystal>();
                        swappedList.add(c);
                        swappedList.add(cr);
                        result.add(swappedList);
                    }

                    currentMap.swapBack(c, cr);
                }

                if (cu != null) {
                    currentMap.swap(c, cu);
                    if (crystalValidations().size() > 0) {
                        ArrayList<Crystal> swappedList = new ArrayList<Crystal>();
                        swappedList.add(c);
                        swappedList.add(cu);
                        result.add(swappedList);
                    }

                    currentMap.swapBack(c, cu);
                }

                if (cd != null) {
                    currentMap.swap(c, cd);
                    if (crystalValidations().size() > 0) {
                        ArrayList<Crystal> swappedList = new ArrayList<Crystal>();
                        swappedList.add(c);
                        swappedList.add(cd);
                        result.add(swappedList);
                    }

                    currentMap.swapBack(c, cd);
                }
            }

        }

        return result;
    }

}