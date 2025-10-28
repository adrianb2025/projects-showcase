package game.level.event;

import game.JumpComponent;
import game.entity.mob.Boss;
import game.entity.mob.Player;
import game.level.Level;

public class BossEvent extends Event {

    private final Boss boss;
    private final String[] intro = new String[] { "?", "HEY", "?", "YOU HAVE THE FOOD", "?", "DON'T YOU?", "?", "GIVE IT TO ME", "?", "WHERE IS THE FOOD?" };
    private final String[] middle = new String[] { "ME", "PLEASE, BRO", "ME", "DON'T", "BROTHER", "I AM HUNGRY!" };
    private final String[] end = new String[] { "BROTHER", "GIVE", "BROTHER", "ME", "BROTHER", "FOOD!", "ME", "STOP!" };
    private final String[] lastLife = new String[] { "BROTHER", "I'M SORRY", "BROTHER", "I HAD A BAD DAY", "ME", "I UNDERSTAND", "BROTHER", "NEVER AGAIN", "BROTHER", "I PROMISE" };
    private int line;
    private String[] current;

    public BossEvent(int distance, Boss boss) {
        super(distance);
        this.boss = boss;
    }

    public void init(Level level, Player player) {
        super.init(level, player);
        boss.x = JumpComponent.WIDTH / 2;
        boss.y = player.y - 176;
        level.addEntity(boss);
        level.showTiles = false;
        level.canMoveForward = false;
        System.err.println("------------------------------------------ BOSS EVENT ------------------------------------------");
    }

    public void tick() {
        String[] dialogue = current;

        if (boss.health == boss.maxHealth) dialogue = intro;
        else if (boss.health == boss.maxHealth / 2) dialogue = middle;
        else if (boss.health == 3) dialogue = end;
        else if (boss.health == 1) dialogue = lastLife;

        if (current != dialogue) {
            line = 0;
            boss.bossDialogue = true;
            current = dialogue;
        }

        if (line < current.length && level.canStartDialogue()) {
            level.startDialogue(current[line], current[line + 1], 60);
            line += 2;
        }

        if (line == current.length && level.canStartDialogue()) boss.bossDialogue = false;

    }

}