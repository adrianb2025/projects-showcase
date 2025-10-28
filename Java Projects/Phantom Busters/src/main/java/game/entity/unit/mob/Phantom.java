package game.entity.unit.mob;

import game.PhantomBusters;
import game.Player;
import game.entity.unit.Team;
import game.entity.unit.mob.task.AttackTask;
import game.entity.unit.mob.task.IdleTask;
import game.weapon.PowerGlove;

public class Phantom extends Mob {

    public Phantom(Player player) {
        super(player);
        team = Team.PHANTOM;
        xr = 16.0;
        yr = 20.0;
        zh = 20.0;
        weapon = new PowerGlove(this);
        speed = 50;
        charClass = random.nextInt(4);
    }

    public void tick() {
        if (shootTime == 0 && task instanceof IdleTask) moveForward();

        if (task instanceof IdleTask && hurtTime == 0) {
            double weaponRange = weapon.maxRange * 3.0;
            Mob target = findTarget(weaponRange);
            if (target != null) setTask(new AttackTask(target, weaponRange));
        }

        super.tick();
    }

    public boolean isLegalTarget(Mob u) { return !(u instanceof Phantom); }

}
