package game.level;

import game.particle.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleListCache {

    private static final List<List<Particle>> cacheMap = new ArrayList<List<Particle>>();
    private static int c;

    public static List<Particle> get() {
        if (c == cacheMap.size()) cacheMap.add(new ArrayList<>());

        List<Particle> result = cacheMap.get(c++);
        result.clear();
        return result;
    }

    public static void reset() { c = 0; }

}