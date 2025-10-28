package game.level;

import game.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityListCache {

    public static final List<List<Entity>> cacheMap = new ArrayList<List<Entity>>();
    public static int c = 0;

    public static List<Entity> get() {
        List<Entity> result = null;
        if (cacheMap.size() <= c) {
            result = new ArrayList<Entity>();
            cacheMap.add(result);
        } else {
            result = cacheMap.get(c++);
            result.clear();
        }

        c++;
        return result;
    }

    public static void reset() { c = 0; }

}