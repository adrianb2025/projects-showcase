package game.entity.unit;

import game.entity.unit.task.AttackTask;
import game.entity.unit.task.MoveTask;
import game.entity.unit.task.Task;
import game.weapon.FistWeapon;

public class Knight extends Unit {

    public Knight(double x, double y, int r) {
        super(x, y, r);
        sprite = 2;
        setWeapon(new FistWeapon());
    }

    public Task getNextTask() {
        Task result = super.getNextTask();
        Player player = (Player) level.getNearbyEntity(x, y, r * 100, Player.class, null);

        if (random.nextDouble() < 0.01) result = new MoveTask(random.nextDouble() * ((level.w << 4) - 64), random.nextDouble() * ((level.h << 4) - 64));
        else if (random.nextDouble() < 0.03 && player != null) result = new AttackTask(player, 1000, weapon.minDist);

        return result;
    }

    public void die() {
        super.die();
        level.addScore(random.nextInt(100) + 10);
    }

}