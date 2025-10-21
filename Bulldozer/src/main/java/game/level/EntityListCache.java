package game.level;

import game.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityListCache {

    private static final List<List<Entity>> cache = new ArrayList<List<Entity>>();
    private static int c;

    public static List<Entity> get() {
        if (c == cache.size()) cache.add(new ArrayList<>());
        List<Entity> result = cache.get(c++);
        result.clear();
        return result;
    }

    public static void reset() { c = 0; }

}