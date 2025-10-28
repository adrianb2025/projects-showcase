package game.level.event;

import game.JumpComponent;
import game.entity.Entity;
import game.entity.mob.Player;
import game.level.Level;
import game.level.tile.Tile;

public class LavaEvent extends Event {

    public double yScroll;
    public int yScrollD;
    public double yScrollA;
    public double scrollSpeed = -0.005;

    public LavaEvent(int distance) { super(distance); }

    public void init(Level level, Player player) {
        super.init(level, player);
        yScroll = player.y - 120;
        level.generate(player.distanceAchieved + 16, distance);
        System.err.println("------------------------------------------ LAVA EVENT ------------------------------------------");
    }

    public void tick() {
        int yto = yScrollD;
        yScrollA += scrollSpeed;
        yScroll += yScrollA;
        yScrollA *= 0.97;
        yScrollD = ((int)(yScroll + JumpComponent.HEIGHT) >> 4) - 2;
        if (yto != yScrollD) {
            for (int x = 0; x < level.w; x++) {
                for (Entity e : level.entities) {
                    if (e.yt != yto) continue;
                    e.die();
                }

                level.setTile(x, yto, Tile.damaged.id);
                level.setTile(x, yto + 1, Tile.lava.id);
            }
        }

        scrollSpeed = -(0.005 + (player.distanceAchieved - startDistance) / (double) distance * 0.01);
    }

}