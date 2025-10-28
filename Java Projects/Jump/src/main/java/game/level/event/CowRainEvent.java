package game.level.event;

import game.entity.mob.Player;
import game.level.Level;

public class CowRainEvent extends Event {

    private int prevDistance;

    public CowRainEvent(int distance) { super(distance); }

    public void init(Level level, Player player) {
        super.init(level, player);
        prevDistance = startDistance;
        System.err.println("------------------------------------------ COW EVENT ------------------------------------------");
    }

    public void tick() {
        if (prevDistance == player.distanceAchieved) return;

        prevDistance = player.distanceAchieved;
        if (random.nextInt(10) <= (player.distanceAchieved - startDistance) / (double) distance * 20) level.spawnCow(300);
    }

}