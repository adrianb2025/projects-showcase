package game.level.event;

import game.entity.mob.Player;
import game.level.Level;

import java.util.Random;

public abstract class Event {

    public static final Random random = new Random();
    protected Player player;
    protected Level level;
    protected int distance;
    protected int startDistance;

    public Event(int distance) { this.distance = distance; }

    public void init(Level level, Player player) {
        this.level = level;
        this.player = player;
        startDistance = player.distanceAchieved;
    }

    public abstract void tick();

    public boolean finished() { return player.distanceAchieved - startDistance == distance; }

}