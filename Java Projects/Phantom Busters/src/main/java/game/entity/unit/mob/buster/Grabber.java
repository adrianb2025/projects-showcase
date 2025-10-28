package game.entity.unit.mob.buster;

import game.Player;
import game.entity.unit.Team;
import game.entity.unit.mob.Mob;
import game.entity.unit.mob.Phantom;
import game.entity.unit.mob.task.AttackTask;
import game.entity.unit.mob.task.IdleTask;
import game.entity.unit.mob.task.MoveTask;
import game.particle.SmoothRay;
import game.weapon.GrabberWeapon;

public class Grabber extends Mob {

    public static final int[] colors = new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFF00FF, 0xFF00FFFF, 0xFFFFFF00 };
    Hunter hunter;
    int rayCol;

    public Grabber(Player player, Hunter hunter) {
        super(player);
        rayCol = random.nextInt(colors.length);
        this.hunter = hunter;
        speed = 120;
        charClass = random.nextInt(2) + 5;
        weapon = new GrabberWeapon(this);
        team = Team.HUNTERS;
    }

    public void shootAt(Mob target) {
        for (int i = 0; i < 5; i++) {
            level.add(new SmoothRay(colors[rayCol], this, target));
        }

        dir = Math.atan2(target.y - y, target.x - x);
        shootTime = 10;
        target.attract(this, 200);
    }

    public void tick() {
        if (!(task instanceof AttackTask) && hurtTime == 0) {
            double weaponRange = weapon.maxRange * 2.0;
            Mob target = findTarget(weaponRange);
            if (target != null) setTask(new AttackTask(target, weaponRange));
        }

        double distToSqr = distanceToSqr(hunter);
        if (task instanceof IdleTask && distToSqr > 22500.0 || distToSqr > 40000.0) {
            int xt = (int) Math.floor(hunter.x / 64.0);
            int yt = (int) Math.floor(hunter.y / 64.0);

            int xx = xt;
            int yy = yt;

            while (xt == (xx += random.nextInt(2) * 2 - 1) && yt == (yy += random.nextInt(2) * 2 - 1) || level.getTile(xx, yy).blocks()) {}

            setTask(new MoveTask(xx * 16 * 4, yy * 16 * 4));
        }

        super.tick();
    }

    public void findPos() {
        int xd = (int) Math.floor(hunter.x / 64.0);
        int yd = (int) Math.floor(hunter.y / 64.0);
        int xx = xd;
        int yy = yd;
        double r = 10.0;
        do {
            this.x = (xx += random.nextInt(2) * 2 - 1) * 16 * 4 + 32;
            this.y = (yy += random.nextInt(2) * 2 - 1) * 16 * 4 + 32;
        } while (xd == xx && yd == yy || level.getTile(xx, yy).blocks() || level.getEntities(x - r, y - r, 0.0, x + r, y + r, 0.0).size() > 1);
    }


    public boolean isLegalTarget(Mob u) { return u instanceof Phantom; }


}