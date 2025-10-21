package game.level.event;

import game.entity.mob.Player;
import game.level.Level;

public class IdleEvent extends Event {

    public IdleEvent(int distance) { super(distance); }

    public void init(Level level, Player player) {
        super.init(level, player);
        System.err.println("------------------------------------------ IDLE EVENT ------------------------------------------");
    }

    public void tick() {}

}