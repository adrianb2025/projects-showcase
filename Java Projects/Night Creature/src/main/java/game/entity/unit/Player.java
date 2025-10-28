package game.entity.unit;

import game.entity.Entity;
import game.entity.unit.task.AttackTask;
import game.entity.unit.task.Task;
import game.level.Level;
import game.particle.DustParticle;
import game.weapon.RayWeapon;

public class Player extends Monster {

    public Player(double x, double y, int r) {
        super(x, y, r);
        setWeapon(new RayWeapon());
        moveSpeed *= 4.0;
    }

    public void tick() {
        super.tick();

        if (random.nextDouble() < 0.001) jump();

        int s = sprite;
        sprite = level.night ? 0 : 1;
        moveSpeed = level.night ? 10.4 : 2.0;

        if (s != sprite) {
            for (int i = 0; i < 100; i++) {
                level.addParticle(new DustParticle(x, y, z));
            }
        }

    }

    public Task getNextTask() {
        Task result = super.getNextTask();
        Knight t = (Knight) level.getNearbyEntity((int) x, (int) y, 1000.0, Knight.class, new Level.EntityFilter<Knight>() {
            public boolean accept(Entity e) {
                return !e.removed;
            }
        });

        if (level.night && random.nextDouble() > 0.01 && t != null) result = new AttackTask(t, 3000, weapon.minDist * 0.5);

        return result;
    }

    public void jump() {
        super.jump();
        for (int i = 0; i < 10; i++) {
            level.addParticle(new DustParticle(x, y, z));
        }
    }

}